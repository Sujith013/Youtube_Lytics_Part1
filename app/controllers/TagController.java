package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class TagController extends Controller {

    private static final String API_KEY = "";

    public Result tagIndex(String videoId) {
        String apiUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=" + videoId + "&key=" + API_KEY;

        try {
            // Create an HTTP Client
            HttpClient client = HttpClient.newHttpClient();

            // Build a GET Request to YouTube API
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Parse the response JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());

            JsonNode tagsNode = jsonResponse.path("items").get(0).path("snippet").path("tags");
            String videoTitle = jsonResponse.path("items").get(0).path("snippet").path("title").asText();
            String channelTitle = jsonResponse.path("items").get(0).path("snippet").path("channelTitle").asText();
            String description = jsonResponse.path("items").get(0).path("snippet").path("description").asText();
            String thumbnail = jsonResponse.path("items").get(0).path("snippet").path("thumbnails").path("high").path("url").asText();

            List<String> tagsResponseList = new ArrayList<>();

            for (JsonNode tag : tagsNode) {
                tagsResponseList.add(tag.asText()); // Convert JsonNode to String
            }

            String tagsResponse = String.join("+", tagsResponseList);

            // Send the data to the view (HTML template)
            return ok(views.html.tags.render(videoId,videoTitle,channelTitle,description,thumbnail,tagsResponse));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return internalServerError("Error fetching YouTube data");
        }
    }


    public Result tagResultIndex(Http.Request request) {
        String sT = request.getQueryString("searchTerm");

        try {
            sT = URLEncoder.encode(sT, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        String apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + sT + "&maxResults=20&key=" + API_KEY;

        try {
            // Create an HTTP Client
            HttpClient client = HttpClient.newHttpClient();

            // Build a GET Request to YouTube API
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Parse the response JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.body());

            List<List<String>> videos = StreamSupport.stream(jsonResponse.get("items").spliterator(), false)
                    .filter(video -> "youtube#video".equals(video.get("id").get("kind").asText()))
                    .map(video -> Arrays.asList(video.get("snippet").get("title").asText(),
                            "https://www.youtube.com/watch?v=" + video.get("id").get("videoId").asText(),
                            "https://www.youtube.com/@"+video.get("snippet").get("channelTitle").asText(),
                            video.get("snippet").get("description").asText(),
                            video.get("snippet").get("thumbnails").get("high").get("url").asText(),
                            "https://www.youtube.com/channel/"+video.get("snippet").get("channelId").asText()))
                    .limit(10)
                    .collect(Collectors.toList());

            // Send the data to the view (HTML template)
            return ok(objectMapper.writeValueAsString(videos));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return internalServerError("Error fetching YouTube data");
        }
    }
}

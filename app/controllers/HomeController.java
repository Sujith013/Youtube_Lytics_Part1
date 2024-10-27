package controllers;

import play.api.libs.json.Json;
import play.mvc.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private static final String API_KEY = "";

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result submitInput(Http.Request request) {
        String sT = request.getQueryString("searchTerms");

        try {
            sT = URLEncoder.encode(sT, "UTF-8");
        }catch (UnsupportedEncodingException e) {
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

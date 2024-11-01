package Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sujith Manikandan
 * The java class containing the attributes and methods for the video data to be obtained and stored using the YouTube API
 * */
public class VideoData {
    public String videoTitle,channelTitle,description,thumbnail,tagsResponse;
    public JsonNode tagsNode,jsonResponse;
    public List<String> tagsResponseList;

    /**
     * @author Sujith Manikandan
     * @param apiUrl The url to be passed into the api object created to search for the information using the parameter video id.
     * @throws InterruptedException API request is interrupted before completion
     * @throws IOException due to network or I/O issues such as connectivity issues
     * */
    public VideoData (String apiUrl) throws InterruptedException, IOException
    {
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
        this.jsonResponse = objectMapper.readTree(response.body());

        //all the paths are traversed to obtain the specific values.
        this.tagsNode = jsonResponse.path("items").get(0).path("snippet").path("tags");
        this.videoTitle = jsonResponse.path("items").get(0).path("snippet").path("title").asText();
        this.channelTitle = jsonResponse.path("items").get(0).path("snippet").path("channelTitle").asText();
        this.description = jsonResponse.path("items").get(0).path("snippet").path("description").asText();
        this.thumbnail = jsonResponse.path("items").get(0).path("snippet").path("thumbnails").path("high").path("url").asText();

        this.tagsResponseList = new ArrayList<>();

        //loop through the list of nodes and add it as a string to the list.
        for (JsonNode tag : tagsNode) {
            tagsResponseList.add(tag.asText()); // Convert JsonNode to String
        }

        //join the list using a delimiter such that it becomes easy to separate and read the values in the
        //client side JS
        this.tagsResponse = String.join("+", tagsResponseList);
    }
}

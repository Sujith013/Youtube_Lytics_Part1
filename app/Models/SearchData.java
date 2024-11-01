package Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Sujith Manikandan
 * The java class containing the attributes and methods for the search data to be obtained and stored using the YouTube API
 * */
public class SearchData {
    public List<List<String>> videos;

    /**
     * @author Sujith Manikandan
     * @param apiUrl The url to be passed into the api object created to search for the information using the parameter video id.
     * @throws InterruptedException API request is interrupted before completion
     * @throws IOException due to network or I/O issues such as connectivity issues
     * */
    public SearchData(String apiUrl) throws InterruptedException, IOException
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
        JsonNode jsonResponse = objectMapper.readTree(response.body());

        //Streams are used to filter only the videos from the search results and leave out all the other
        //information such as channel and shorts. Then map the values to only get a list of strings of
        //video and channel id and title, description and the url for the thumbnail.
        this.videos = StreamSupport.stream(jsonResponse.get("items").spliterator(), false)
                .filter(video -> "youtube#video".equals(video.get("id").get("kind").asText()))
                .map(video -> Arrays.asList(video.get("snippet").get("title").asText(),
                        "https://www.youtube.com/watch?v=" + video.get("id").get("videoId").asText(),
                        "https://www.youtube.com/@"+video.get("snippet").get("channelTitle").asText(),
                        video.get("snippet").get("description").asText(),
                        video.get("snippet").get("thumbnails").get("high").get("url").asText(),
                        "https://www.youtube.com/channel/"+video.get("snippet").get("channelId").asText()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
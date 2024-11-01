/**
 * @author Sujith Manikandan
 * @author Tharun Balaji
 * @author Thansil Mohamed Syed Hamdulla
 * @author Prakash Yuvaraj
 * @version 1.0
 * @since 01-11-2024
 * */
package controllers;

import Models.SearchData;
import Models.VideoData;

import play.mvc.*;

import java.net.URLEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

/**
 * This is the one and only main controller for the project that handles the HTTP requests and renders
 * pages for the entire application.
 */
public class HomeController extends Controller {

    //The api key
    private static final String API_KEY = "AIzaSyAugi0_hJ_OgciWZoKLnYybGcZlq4CLJiw";

    /**
     * @author Sujith Manikandan
     * @return returns the view of the index page containing the html content for the main page of our application
     * */
    public Result index() {
        return ok(views.html.index.render());
    }

    /**
     * @author Sujith Manikandan
     * @param request The http request sent through the fetch API from the client side using javascript for the submission of the search field
     * @return A wrapped object containing all the data about the search results such as videoID,videoTitle,ChannelTitle,ChannelId,description and url of the thumbnail
     * */
    public CompletionStage<Result> submitInput(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
        //String containing the search query
        String sT = request.getQueryString("searchTerms");

        try {
            sT = URLEncoder.encode(sT, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        //final url to be passed onto the object
        String apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + sT + "&maxResults=20&key=" + API_KEY;

        try
        {
            //Maps the list of list of string and returns it back to the client side JS.
            ObjectMapper objectMapper = new ObjectMapper();
            SearchData videos = new SearchData(apiUrl);
            return ok(objectMapper.writeValueAsString(videos.videos));
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
            return internalServerError("Error fetching YouTube data");
        }
        });
    }

    /**
     * @author Sujith Manikandan
     * @param videoId the id of the video for which the content is required
     * @return A wrapped object containing all the data about the video such as title,channel,description,thumbnails and tags of the video.
     * */
    public CompletionStage<Result>tagIndex(String videoId) {
        return CompletableFuture.supplyAsync(() -> {
        //The final search url to be passed onto the api object/client
        String apiUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=" + videoId + "&key=" + API_KEY;

        try {
            //Returns all the information obtained about the video using the id back to the client side JS.
            VideoData videos = new VideoData(apiUrl);
            return ok(views.html.tags.render(videoId,videos.videoTitle,videos.channelTitle,videos.description,videos.thumbnail,videos.tagsResponse));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return internalServerError("Error fetching YouTube data");
        }
        });
    }

    /**
     * @author Sujith Manikandan
     * @param request The http request sent through the fetch API from the client side using javascript for the submission of the search field
     * @return A wrapped object containing all the data about the search results such as videoID,videoTitle,ChannelTitle,ChannelId,description and url of the thumbnail
     * */
    public CompletionStage<Result> tagResultIndex(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
        String sT = request.getQueryString("searchTerm");

        try {
            sT = URLEncoder.encode(sT, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        //The final search url to be passed onto the api object/client
        String apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + sT + "&maxResults=20&key=" + API_KEY;

        //Maps all the search data about an object and returns everything back to the client side JS.
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SearchData videos = new SearchData(apiUrl);
            return ok(objectMapper.writeValueAsString(videos.videos));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return internalServerError("Error fetching YouTube data");
        }
    });
    }
}

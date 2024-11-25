/**
 * @author Sujith Manikandan
 * @author Tharun Balaji
 * @author Thansil Mohamed Syed Hamdulla
 * @author Prakash Yuvaraj
 * @version 1.0
 * @since 01-11-2024
 * */
package controllers;

import Models.TagsData;
import Models.SearchData;
import Models.YoutubeService;
import Models.ChannelData;

import play.mvc.*;

import java.net.URLEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.security.GeneralSecurityException;

import com.google.api.services.youtube.YouTube;

/**
 * This is the one and only main controller for the project that handles the HTTP requests and renders
 * pages for the entire application.
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    //The api key
    private static final String API_KEY = "AIzaSyAW0T6vizZ9wEgix9jH8WzVIaw_TVe1mak";
    private static YouTube youtube;
    private static SearchData videos;

    /**
     * @author Sujith Manikandan
     * @author Tharun Balaji
     * @author Thansil Mohamed Syed Hamdulla
     * @author Prakash Yuvaraj
     * @return returns the view of the index page containing the html content for the main page of our application
     * */
    public Result index() {
        return ok(views.html.index.render());
    }




    /**
     * @author Sujith Manikandan
     * @author Tharun Balaji
     * @author Thansil Mohamed Syed Hamdulla
     * @author Prakash Yuvaraj
     * @param request The http request sent through the fetch API from the client side using javascript for the submission of the search field
     * @return A wrapped object containing all the data about the search results such as videoID,videoTitle,ChannelTitle,ChannelId,description and url of the thumbnail
     * */
    public CompletionStage<Result> submitInput(Http.Request request) {
     return CompletableFuture.supplyAsync(() -> {
         try
         {
             videos = new SearchData(YoutubeService.getService(),request.getQueryString("searchTerms"),API_KEY);
             return ok(new ObjectMapper().writeValueAsString(videos.getResponse()));
         }
         catch (IOException | GeneralSecurityException | IllegalStateException e)
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

         try{
             TagsData videos = new TagsData(YoutubeService.getService(),videoId,API_KEY);
             return ok(views.html.tags.render(videoId,videos.getVideoTitle(),videos.getChannelTitle(),videos.getDescription(),videos.getThumbnail(),videos.getTagsResponse(),videos.getChannelId()));
         }
         catch (IOException | GeneralSecurityException e)
         {
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
         try
         {
             return ok(new ObjectMapper().writeValueAsString(new SearchData(YoutubeService.getService(), request.getQueryString("searchTerm"),API_KEY,true).getVideos()));
         }
         catch (IOException | GeneralSecurityException e)
         {
             e.printStackTrace();
             return internalServerError("Error fetching YouTube data");
         }
       });
     }




    /**
     * @author Thansil Mohammed Syed Hamdulla
     * Method to handle requests for displaying the channel profile page.
     * @param channelId The ID of the YouTube channel to display.
     * @return A Result containing the channel profile page.
     */
    public CompletionStage<Result> channelProfile(String channelId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ChannelData channelData = new ChannelData(YoutubeService.getService(), channelId, API_KEY);

                return ok(views.html.channel.render(
                        channelData.getChannelTitle(),
                        channelData.getDescription(),
                        channelData.getSubscriberCount(),
                        channelData.getThumbnailUrl(),
                        channelData.getRecentVideos() // List of recent videos
                ));
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
                return internalServerError("Error fetching channel data");
            }
        });
    }




    /**
     * @author Tharun Balaji
     * @param searchNumber the search query number
     * @return the rendering of the word stats web page*/
    public CompletionStage<Result> wordStats(String searchNumber) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Render the channel profile page with channel details and recent videos
                return ok(views.html.stats.render(
                    videos.getWordStats()
                ));
            } catch (Exception e) {
                e.printStackTrace();
                return internalServerError("Error fetching channel data");
            }
        });
    }


}
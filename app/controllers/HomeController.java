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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final String API_KEY = "AIzaSyDycQNgX1KuZSCibwXdsyxpDdRfb0rr6FI";
    private static YouTube youtube;
    private static SearchData videos;

    /**
     * @author Sujith Manikandan
     * @return returns the view of the index page containing the html content for the main page of our application
     * */
    public Result index() {
        return ok(views.html.index.render());
    }




    /**
     * @author Tharun Balaji
     * @author Thansil Mohammed Syed Hamdulla
     * @param request The http request sent through the fetch API from the client side using javascript for the submission of the search field
     * @return A wrapped object containing all the data about the search results such as videoID,videoTitle,ChannelTitle,ChannelId,description and url of the thumbnail
     * */
    public CompletionStage<Result> submitInput(Http.Request request) {
     return CompletableFuture.supplyAsync(() -> {
         boolean f = false;
         String sT = request.getQueryString("searchTerms");

         try
         {
             for(int i=0;i<sT.length();i++)
                 if(Character.isLetterOrDigit(sT.charAt(i)))
                     f = true;

             if(!f)
                 throw new IllegalArgumentException();

             //encode the query with proper delimiters to avoid the errors with spaces.
             sT = URLEncoder.encode(sT, "UTF-8");

             //get the YouTube service object
             youtube = YoutubeService.getService();

             //Maps the list of a list of string and returns it back to the client side JS.
             ObjectMapper objectMapper = new ObjectMapper();
             videos = new SearchData(youtube,sT,API_KEY);

             ArrayList<String> descriptions = new ArrayList<String>();

             for(List<String> s : videos.getVideos()){
                 descriptions.add(s.get(3));
             }

             String sentiment = SearchData.getSentimentAnalysis(descriptions);

             Map<String, Object> response = new HashMap<>();
             response.put("data", videos.getVideos());
             response.put("senti", sentiment);

             return ok(objectMapper.writeValueAsString(response));
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
             //Get the official YouTube api object created
             youtube = YoutubeService.getService();

             TagsData videos = new TagsData(youtube,videoId,API_KEY);

             //Returns all the information obtained about the video using the id back to the client side JS.
             return ok(views.html.tags.render(videoId,videos.getVideoTitle(),videos.getChannelTitle(),videos.getDescription(),videos.getThumbnail(),videos.getTagsResponse()));
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
         boolean f = false;
         String sT = request.getQueryString("searchTerm");

         try
         {
             for(int i=0;i<sT.length();i++)
                 if(Character.isLetterOrDigit(sT.charAt(i)))
                     f = true;

             if(!f)
                 throw new IllegalArgumentException();

             //encode the query with proper delimiters to avoid the errors with spaces.
             sT = URLEncoder.encode(sT, "UTF-8");

             //get the YouTube service object
             youtube = YoutubeService.getService();

             //Maps the list of a list of string and returns it back to the client side JS.
             ObjectMapper objectMapper = new ObjectMapper();
             SearchData videos = new SearchData(youtube,sT,API_KEY,true);

             return ok(objectMapper.writeValueAsString(videos.getVideos()));
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
                // Initialize YouTube service if not already initialized
                youtube = YoutubeService.getService();

                // Fetch the channel data using the ChannelData model
                ChannelData channelData = new ChannelData(youtube, channelId, API_KEY);

                // Render the channel profile page with channel details and recent videos
                return ok(views.html.ChannelData.render(
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


    public CompletionStage<Result> wordStats(String searchNumber) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Render the channel profile page with channel details and recent videos
                return ok(views.html.StatsData.render(
                    videos.getWordStats()
                ));
            } catch (Exception e) {
                e.printStackTrace();
                return internalServerError("Error fetching channel data");
            }
        });
    }


}
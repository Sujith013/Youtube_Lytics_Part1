/**
 * @author Sujith Manikandan
 * @author Tharun Balaji
 * @author Thansil Mohamed Syed Hamdulla
 * @author Prakash Yuvaraj
 * @version 1.0
 * @since 01-11-2024
 * */
package controllers;

import Models.VideoData;
import Models.SearchData;
import Models.YoutubeService;

import play.mvc.*;

import java.net.URLEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.security.GeneralSecurityException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

import com.google.api.services.youtube.YouTube;

/**
 * This is the one and only main controller for the project that handles the HTTP requests and renders
 * pages for the entire application.
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    //The api key
    private static final String API_KEY = "AIzaSyAugi0_hJ_OgciWZoKLnYybGcZlq4CLJiw";
    private static YouTube youtube;


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

         String sT = request.getQueryString("searchTerms");

         try
         {
             //encode the query with proper delimiters to avoid the errors with spaces.
             sT = URLEncoder.encode(sT, "UTF-8");

             //get the YouTube service object
             youtube = YoutubeService.getService();

             //Maps the list of a list of string and returns it back to the client side JS.
             ObjectMapper objectMapper = new ObjectMapper();
             SearchData videos = new SearchData(youtube,sT,API_KEY);

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
      * @author Sujith Manikandan
      * @param videoId the id of the video for which the content is required
      * @return A wrapped object containing all the data about the video such as title,channel,description,thumbnails and tags of the video.
      * */
     public CompletionStage<Result>tagIndex(String videoId) {
     return CompletableFuture.supplyAsync(() -> {

         try{
             //Get the official youtube api object created
             youtube = YoutubeService.getService();

             VideoData videos = new VideoData(youtube,videoId,API_KEY);

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
         String sT = request.getQueryString("searchTerm");

         try
         {
             //encode the query with proper delimiters to avoid the errors with spaces.
             sT = URLEncoder.encode(sT, "UTF-8");

             //get the YouTube service object
             youtube = YoutubeService.getService();

             //Maps the list of a list of string and returns it back to the client side JS.
             ObjectMapper objectMapper = new ObjectMapper();
             SearchData videos = new SearchData(youtube,sT,API_KEY);

             return ok(objectMapper.writeValueAsString(videos.getVideos()));
         }
         catch (IOException | GeneralSecurityException e)
         {
             e.printStackTrace();
             return internalServerError("Error fetching YouTube data");
         }
       });
     }
}
package Models;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

/**
 * @author Sujith Manikandan
 * The java class containing the attributes and methods for the video data to be obtained and stored using the YouTube API
 * */
public class VideoData {
    private String videoTitle,channelTitle,description,thumbnail,tagsResponse;

    /**
     * @author Sujith Manikandan
     * @param id video id for the video
     * @param youtube the official YouTube api object
     * @param api_key the official api key used to connect to the client
     * @throws IOException due to network or I/O issues such as connectivity issues
     * */
    public VideoData (YouTube youtube,String id,String api_key) throws IOException
    {
        YouTube.Videos.List video_list = youtube.videos().list(Collections.singletonList("snippet,contentDetails,statistics"));
        video_list.setId(Collections.singletonList(id));
        video_list.setKey(api_key);

        VideoListResponse response = video_list.execute();

        List<String> tagsResponseList = response.getItems().get(0).getSnippet().getTags();

        this.videoTitle = response.getItems().get(0).getSnippet().getTitle();
        this.channelTitle = response.getItems().get(0).getSnippet().getChannelTitle();
        this.description = response.getItems().get(0).getSnippet().getDescription();
        this.thumbnail = response.getItems().get(0).getSnippet().getThumbnails().getHigh().getUrl();

        this.tagsResponse = String.join("+", tagsResponseList);
    }

    /**
     * @return the description of the video
     * */
    public String getDescription(){
        return this.description;
    }

    /**
     * @return the title of the video
     * */
    public String getVideoTitle(){
        return this.videoTitle;
    }

    /**
     * @return the title of the channel
     * */
    public String getChannelTitle(){
        return this.channelTitle;
    }

    /**
     * @return the url for the thumbnail of the video
     * */
    public String getThumbnail(){
        return this.thumbnail;
    }

    /**
     * @return the tags of the video as a string
     * */
    public String getTagsResponse(){
        return this.tagsResponse;
    }
}

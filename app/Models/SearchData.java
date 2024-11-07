package Models;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

/**
 * The java class containing the attributes and methods for the search data to be obtained and stored using the YouTube API
 * */
public class SearchData {
    private final List<List<String>> videos;

    /**
     * @author Tharun Balaji
     * @author Thansil Mohammed Syed Hamdulla
     * @param query the search query/keyword to be searched with to get relevant results
     * @param youtube the official YouTube api object
     * @param api_key the official api key used to connect to the client
     * @throws IOException due to network or I/O issues such as connectivity issues
     * */
    public SearchData(YouTube youtube, String query,String api_key) throws IOException {
        if(api_key.isEmpty())
            throw new NullPointerException();
        else if(api_key.length()<39)
            throw new IllegalArgumentException("API key length too short");
        else if(api_key.length()>39)
            throw new IllegalArgumentException("API key length too long");

        boolean f = true;

        for(int i=0;i<api_key.length();i++)
            if(!Character.isLetterOrDigit(api_key.charAt(i)) && api_key.charAt(i)!='_' && api_key.charAt(i)!='-')
                f = false;

        if(!f)
            throw new IllegalArgumentException("API key must only contain alphanumeric characters with - and _");


        YouTube.Search.List search = youtube.search().list(Collections.singletonList("snippet"));
        search.setQ(query);
        search.setMaxResults(50L);
        search.setKey(api_key);

        // Execute the search request and get the response
        SearchListResponse response = search.execute();

        // Filter and map the response to extract video details
        this.videos = response.getItems().stream()
                .filter(video -> "youtube#video".equals(video.getId().getKind()))
                .map(video -> Arrays.asList(
                        video.getSnippet().getTitle(),
                        "https://www.youtube.com/watch?v=" + video.getId().getVideoId(),
                        "https://www.youtube.com/@"+video.getSnippet().getChannelTitle(),
                        video.getSnippet().getDescription(),
                        video.getSnippet().getThumbnails().getHigh().getUrl(),
                        "/channel/"+video.getSnippet().getChannelId()))
                .limit(10)
                .collect(Collectors.toList());
    }


    /**
     * @author Sujith Manikandan
     * @param videoId the id of the video
     * @param tag the tag to be matched with
     * @param API_KEY the api key to fetch the data
     * @throws NullPointerException when no tags are present in the video
     * @return a boolean value to determine whether the given tag is present in the video or not
     * */
    public static boolean checkTagPresent(String videoId,String tag,String API_KEY) throws NullPointerException
    {
        try{
            //Get the official YouTube api object created
            YouTube youtube = YoutubeService.getService();

            VideoData videos = new VideoData(youtube,videoId,API_KEY);

            String[] tags = videos.getTagsResponse().split("\\+");

                for (String p : tags)
                    if (p.equals(tag))
                        return true;

            return false;
        }
        catch (IOException | GeneralSecurityException | NullPointerException e)
        {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @author Tharun Balaji
     * @author Sujith Manikandan
     * @author Thansil Mohammed Syed Hamdulla
     * @param query the search query/keyword to be searched with to get relevant results
     * @param youtube the official YouTube api object
     * @param api_key the official api key used to connect to the client
     * @throws IOException due to network or I/O issues such as connectivity issues
     * */
    public SearchData(YouTube youtube, String query,String api_key,boolean tag) throws IOException {
        if(api_key.isEmpty())
            throw new NullPointerException();
        else if(api_key.length()<39)
            throw new IllegalArgumentException("API key length too short");
        else if(api_key.length()>39)
            throw new IllegalArgumentException("API key length too long");

        boolean f = true;

        for(int i=0;i<api_key.length();i++)
            if(!Character.isLetterOrDigit(api_key.charAt(i)) && api_key.charAt(i)!='_' && api_key.charAt(i)!='-')
                f = false;

        if(!f)
            throw new IllegalArgumentException("API key must only contain alphanumeric characters with - and _");


        YouTube.Search.List search = youtube.search().list(Collections.singletonList("snippet"));
        search.setQ(query);
        search.setMaxResults(300L);
        search.setKey(api_key);

        // Execute the search request and get the response
        SearchListResponse response = search.execute();

        // Filter and map the response to extract video details
        this.videos = response.getItems().stream()
                .filter(video -> "youtube#video".equals(video.getId().getKind()))
                .filter(video -> checkTagPresent(video.getId().getVideoId(),query,api_key))
                .map(video -> Arrays.asList(
                        video.getSnippet().getTitle(),
                        "https://www.youtube.com/watch?v=" + video.getId().getVideoId(),
                        "https://www.youtube.com/@"+video.getSnippet().getChannelTitle(),
                        video.getSnippet().getDescription(),
                        video.getSnippet().getThumbnails().getHigh().getUrl(),
                        "/channel/"+video.getSnippet().getChannelId()))
                .limit(10)
                .collect(Collectors.toList());
    }


    /**
     * @return the list of the list of videos containing all the information
     * */
    public List<List<String>> getVideos(){
        return this.videos;
    }
}
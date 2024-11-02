package Models;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

/**
 * @author Sujith Manikandan
 * The java class containing the attributes and methods for the search data to be obtained and stored using the YouTube API
 * */
public class SearchData {
    private List<List<String>> videos;

    /**
     * @author Sujith Manikandan
     * @param query the search query/keyword to be searched with to get relevant results
     * @param youtube the official YouTube api object
     * @param api_key the official api key used to connect to the client
     * @throws IOException due to network or I/O issues such as connectivity issues
     * */
    public SearchData(YouTube youtube, String query,String api_key) throws IOException {
        YouTube.Search.List search = youtube.search().list(Collections.singletonList("snippet"));
        search.setQ(query);
        search.setMaxResults(20L);
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
                        "https://www.youtube.com/channel/"+video.getSnippet().getChannelId()))
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
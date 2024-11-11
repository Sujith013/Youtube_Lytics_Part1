package Models;

import java.util.Arrays;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.util.List;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

public class ChannelData {

    private String channelTitle;
    private String description;
    private String subscriberCount;
    private String thumbnailUrl;
    private List<List<String>> recentVideos;

    /**
     * Constructor to initialize and fetch channel information and recent videos
     * @author Thansil Syed
     * @param youtube   YouTube service instance
     * @param channelId Channel ID to fetch details for
     * @param apiKey    YouTube API key
     * @throws IOException in case of a network or API error
     */
    public ChannelData(YouTube youtube, String channelId, String apiKey) throws IOException {
        fetchChannelData(youtube, channelId, apiKey);
        fetchRecentVideos(youtube, channelId, apiKey);
    }


    /**
     * Constructor to initialize and fetch channel information and recent videos
     * @author Thansil Mohammed Syed Hamdulla
     * @param youtube   YouTube service instance
     * @param channelId Channel ID to fetch details for
     * @param apiKey    YouTube API key
     * @throws IOException in case of a network or API error
     */
    private void fetchChannelData(YouTube youtube, String channelId, String apiKey) throws IOException {
        // API call to get channel details
        YouTube.Channels.List request = youtube.channels().list(Arrays.asList("snippet", "statistics"));

        request.setId(Collections.singletonList(channelId));
        request.setKey(apiKey);
        ChannelListResponse response = request.execute();

        if (response.getItems().isEmpty()) {
            throw new IOException("Channel not found");
        }

        this.channelTitle = response.getItems().get(0).getSnippet().getTitle();
        this.description = response.getItems().get(0).getSnippet().getDescription();
        this.subscriberCount = response.getItems().get(0).getStatistics().getSubscriberCount().toString();
        this.thumbnailUrl = response.getItems().get(0).getSnippet().getThumbnails().getDefault().getUrl();

        System.out.println(this.channelTitle);
        System.out.println(this.description);
        System.out.println(this.subscriberCount);
        System.out.println(this.thumbnailUrl);
    }


    /**
     * Constructor to initialize and fetch channel information and recent videos
     * @author Thansil Syed
     * @param youtube   YouTube service instance
     * @param channelId Channel ID to fetch details for
     * @param apiKey    YouTube API key
     * @throws IOException in case of a network or API error
     */
    private void fetchRecentVideos(YouTube youtube, String channelId, String apiKey) throws IOException {
        // API call to get channel's recent videos
        YouTube.PlaylistItems.List request = youtube.playlistItems().list(List.of("snippet"));
        request.setPlaylistId("UU" + channelId.substring(2)); // YouTube channel's uploads playlist ID
        request.setMaxResults(20L);
        request.setKey(apiKey);

        PlaylistItemListResponse response = request.execute();

        this.recentVideos = response.getItems().stream()
                .map(item -> Arrays.asList(
                        item.getSnippet().getTitle(),
                        "https://www.youtube.com/watch?v=" + item.getSnippet().getResourceId().getVideoId(),
                        "https://www.youtube.com/@" + item.getSnippet().getChannelTitle(),
                        item.getSnippet().getDescription(),
                        item.getSnippet().getThumbnails().getHigh().getUrl(),
                        "https://www.youtube.com/channel/" + item.getSnippet().getChannelId()))
                .limit(10)
                .collect(Collectors.toList());

        System.out.println(this.recentVideos);
    }

    /**
     * @return the title of the channel
     * */
    public String getChannelTitle() {
        return channelTitle;
    }

    /**
     * @return the description of the channel
     * */
    public String getDescription() {
        return description;
    }

    /**
     * @return the subscriber count of the
     * */
    public String getSubscriberCount() {
        return subscriberCount;
    }

    /**
     * @return the thumbnail of the url
     * */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * @return the details of the list of recent videos
     * */
    public List<List<String>> getRecentVideos() {
        return recentVideos;
    }
}

package Models;

import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChannelDataTest {

    @Mock
    private YouTube mockYoutube;

    @Mock
    private YouTube.Channels mockChannels;

    @Mock
    private YouTube.Channels.List mockChannelListRequest;

    @Mock
    private YouTube.PlaylistItems mockPlaylistItems;

    @Mock
    private YouTube.PlaylistItems.List mockPlaylistRequest;

    @Mock
    private ChannelListResponse mockChannelResponse;

    @Mock
    private PlaylistItemListResponse mockPlaylistResponse;

    private static final String CHANNEL_ID = "UC12345";
    private static final String API_KEY = "API_KEY_TEST";

    private ChannelData channelData;

    /**
     * setup to return the mock responses for the required class functions
     * @author Thansil Mohammed Syed Hamdulla
     * @throws IOException due to invalid api and network issues*/
    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        // Set up mocks for YouTube API calls
        when(mockYoutube.channels()).thenReturn(mockChannels);
        when(mockChannels.list(Arrays.asList("snippet", "statistics"))).thenReturn(mockChannelListRequest);
        when(mockChannelListRequest.setId(Collections.singletonList(CHANNEL_ID))).thenReturn(mockChannelListRequest);
        when(mockChannelListRequest.setKey(API_KEY)).thenReturn(mockChannelListRequest);
        when(mockChannelListRequest.execute()).thenReturn(mockChannelResponse);

        when(mockYoutube.playlistItems()).thenReturn(mockPlaylistItems);
        when(mockPlaylistItems.list(List.of("snippet"))).thenReturn(mockPlaylistRequest);
        when(mockPlaylistRequest.setPlaylistId("UU" + CHANNEL_ID.substring(2))).thenReturn(mockPlaylistRequest);
        when(mockPlaylistRequest.setMaxResults(10L)).thenReturn(mockPlaylistRequest);
        when(mockPlaylistRequest.setKey(API_KEY)).thenReturn(mockPlaylistRequest);
        when(mockPlaylistRequest.execute()).thenReturn(mockPlaylistResponse);

        // Mocking channel data
        Channel mockChannel = mock(Channel.class);
        ChannelSnippet mockSnippet = mock(ChannelSnippet.class);
        ChannelStatistics mockStatistics = mock(ChannelStatistics.class);

        when(mockSnippet.getTitle()).thenReturn("Test Channel");
        when(mockSnippet.getDescription()).thenReturn("Test Channel Description");
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setUrl("https://example.com/thumbnail.jpg");
        when(mockSnippet.getThumbnails()).thenReturn(new ThumbnailDetails().setDefault(thumbnail));

        when(mockStatistics.getSubscriberCount()).thenReturn(BigInteger.valueOf(1000));

        when(mockChannel.getSnippet()).thenReturn(mockSnippet);
        when(mockChannel.getStatistics()).thenReturn(mockStatistics);

        when(mockChannelResponse.getItems()).thenReturn(Collections.singletonList(mockChannel));

        // Mocking playlist items
        PlaylistItem mockPlaylistItem = mock(PlaylistItem.class);
        PlaylistItemSnippet mockPlaylistSnippet = mock(PlaylistItemSnippet.class);
        ResourceId mockResourceId = mock(ResourceId.class);

        when(mockPlaylistSnippet.getTitle()).thenReturn("Test Video Title");
        when(mockPlaylistSnippet.getChannelId()).thenReturn(CHANNEL_ID);
        when(mockPlaylistSnippet.getChannelTitle()).thenReturn("Test Channel");
        when(mockPlaylistSnippet.getDescription()).thenReturn("Test Video Description");
        when(mockPlaylistSnippet.getThumbnails()).thenReturn(new ThumbnailDetails().setHigh(thumbnail));
        when(mockResourceId.getVideoId()).thenReturn("video12345");
        when(mockPlaylistSnippet.getResourceId()).thenReturn(mockResourceId);
        when(mockPlaylistItem.getSnippet()).thenReturn(mockPlaylistSnippet);

        when(mockPlaylistResponse.getItems()).thenReturn(Collections.nCopies(10, mockPlaylistItem));
    }


    /**
     * The test function that checks whether the channel data is initialized properly or not and checks for the values.
     * @author Thansil Mohammed Syed Hamdulla
     * @throws IOException due to invalid api and network issues*/
    @Test
    public void testChannelDataInitialization() throws IOException {
        // Initialize ChannelData and verify fields
        channelData = new ChannelData(mockYoutube, CHANNEL_ID, API_KEY);

        assertEquals("Test Channel", channelData.getChannelTitle());
        assertEquals("Test Channel Description", channelData.getDescription());
        assertEquals("1000", channelData.getSubscriberCount());
        assertEquals("https://example.com/thumbnail.jpg", channelData.getThumbnailUrl());
        assertNotNull("Recent videos list should not be null", channelData.getRecentVideos());
        assertEquals("Recent videos list should contain 10 items", 10, channelData.getRecentVideos().size());

        List<String> videoData = channelData.getRecentVideos().get(0);
        assertEquals("Test Video Title", videoData.get(0));
        assertTrue(videoData.get(1).contains("https://www.youtube.com/watch?v=video12345"));
        assertEquals("Test Video Description", videoData.get(3));
        assertEquals("https://example.com/thumbnail.jpg", videoData.get(4));
    }


    /**
     * The test function that checks whether the channel is null or not.
     * @author Thansil Mohammed Syed Hamdulla
     * @throws IOException due to invalid api and network issues*/
    @Test
    public void testChannelTitleNotNull() throws IOException {
        channelData = new ChannelData(mockYoutube, CHANNEL_ID, API_KEY);
        assertNotNull("Channel title should not be null", channelData.getChannelTitle());
    }


    /**
     * The test function that checks whether the subscriber count is a valid number.
     * @author Thansil Mohammed Syed Hamdulla
     * @throws IOException due to invalid api and network issues*/
    @Test
    public void testSubscriberCountFormat() throws IOException {
        channelData = new ChannelData(mockYoutube, CHANNEL_ID, API_KEY);
        assertTrue("Subscriber count should be numeric", channelData.getSubscriberCount().matches("\\d+"));
    }


    /**
     * The test function that checks whether the recent videos return the expected values and are not null.
     * @author Thansil Mohammed Syed Hamdulla
     * @throws IOException due to invalid api and network issues*/
    @Test
    public void testRecentVideosContent() throws IOException {
        channelData = new ChannelData(mockYoutube, CHANNEL_ID, API_KEY);
        List<List<String>> recentVideos = channelData.getRecentVideos();

        for (List<String> video : recentVideos) {
            assertEquals("Video data should contain 6 elements", 6, video.size());
            assertNotNull("Video title should not be null", video.get(0));
            assertTrue("Video URL should contain 'https://www.youtube.com/watch?v='", video.get(1).contains("https://www.youtube.com/watch?v="));
        }
    }


    /**
     * The test function that checks whether the url of the thumbnail is in the proper format.
     * @author Thansil Mohammed Syed Hamdulla
     * @throws IOException due to invalid api and network issues*/
    @Test
    public void testChannelThumbnailUrl() throws IOException {
        channelData = new ChannelData(mockYoutube, CHANNEL_ID, API_KEY);
        assertTrue("Thumbnail URL should start with 'https://'", channelData.getThumbnailUrl().startsWith("https://"));
    }
}

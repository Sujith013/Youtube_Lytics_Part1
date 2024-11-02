package Models;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.ThumbnailDetails;

import org.junit.Test;
import org.junit.Before;

import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VideoDataTest {

    private YouTube mockYouTube;

    private static final String VIDEO_ID = "HrCbXNRP7eg";
    private static final String API_KEY = "AIzaSyAugi0_hJ_OgciWZoKLnYybGcZlq4CLJiw";
    private static final String VIDEO_TITLE = "The Problem With Being “Too Nice” at Work | Tessa West | TED\n";
    private static final String CHANNEL_TITLE = "TED";
    private static final String DESCRIPTION = "Are you \"too nice\" at work? Social psychologist Tessa West shares her research on how people attempt to mask anxiety with overly polite feedback — a practice that's more harmful than helpful — and gives three tips to swap generic, unhelpful observations with clear, consistent feedback, even when you feel awkward. If you love watching TED Talks like this one, become a TED Member to support our mission of spreading ideas: https://ted.com/membership Follow TED! X: https://twitter.com/TEDTalks Instagram: https://www.instagram.com/ted Facebook: https://facebook.com/TED LinkedIn: https://www.linkedin.com/company/ted-conferences TikTok: https://www.tiktok.com/@tedtoks The TED Talks channel features talks, performances and original series from the world's leading thinkers and doers. Subscribe to our channel for videos on Technology, Entertainment and Design — plus science, business, global issues, the arts and more. Visit https://TED.com to get our entire library of TED Talks, transcripts, translations, personalized talk recommendations and more. Watch more: https://go.ted.com/tessawest https://youtu.be/HrCbXNRP7eg TED's videos may be used for non-commercial purposes under a Creative Commons License, Attribution–Non Commercial–No Derivatives (or the CC BY – NC – ND 4.0 International) and in accordance with our TED Talks Usage Policy: https://www.ted.com/about/our-organization/our-policies-terms/ted-talks-usage-policy. For more information on using TED for commercial purposes (e.g. employee learning, in a film or online course), please submit a Media Request at https://media-requests.ted.com #TED #TEDTalks #work";
    private static final String THUMBNAIL_URL = "https://i.ytimg.com/vi/HrCbXNRP7eg/hqdefault.jpg";
    private static final List<String> TAGS = List.of("tag1", "tag2", "tag3");

    @Before
    public void setUp() throws IOException {
        // Mock the main YouTube object and its nested classes
        mockYouTube = mock(YouTube.class);
        YouTube.Videos mockVideos = mock(YouTube.Videos.class);
        YouTube.Videos.List mockVideoList = mock(YouTube.Videos.List.class);

        // Mock the response objects
        VideoListResponse mockResponse = mock(VideoListResponse.class);
        Video mockVideo = mock(Video.class);
        VideoSnippet mockSnippet = mock(VideoSnippet.class);
        ThumbnailDetails mockThumbnails = mock(ThumbnailDetails.class);
        Thumbnail mockThumbnail = mock(Thumbnail.class);

        // Set up the behavior for the YouTube API calls
        when(mockYouTube.videos()).thenReturn(mockVideos);
        when(mockVideos.list(Collections.singletonList("snippet,contentDetails,statistics"))).thenReturn(mockVideoList);
        when(mockVideoList.setId(Collections.singletonList(VIDEO_ID))).thenReturn(mockVideoList);
        when(mockVideoList.setKey(API_KEY)).thenReturn(mockVideoList);
        when(mockVideoList.execute()).thenReturn(mockResponse);

        // Configure the mock response to return the necessary video information
        when(mockResponse.getItems()).thenReturn(Collections.singletonList(mockVideo));
        when(mockVideo.getSnippet()).thenReturn(mockSnippet);
        when(mockSnippet.getTitle()).thenReturn(VIDEO_TITLE);
        when(mockSnippet.getChannelTitle()).thenReturn(CHANNEL_TITLE);
        when(mockSnippet.getDescription()).thenReturn(DESCRIPTION);
        when(mockSnippet.getTags()).thenReturn(TAGS);
        when(mockSnippet.getThumbnails()).thenReturn(mockThumbnails);
        when(mockThumbnails.getHigh()).thenReturn(mockThumbnail);
        when(mockThumbnail.getUrl()).thenReturn(THUMBNAIL_URL);
    }


    @Test
    public void testVideoDataInitialization() throws IOException {
        // Instantiate VideoData with the mocked YouTube instance
        VideoData videoData = new VideoData(mockYouTube, VIDEO_ID, API_KEY);
        System.out.println(videoData.getVideoTitle());
        System.out.println(videoData.getChannelTitle());
        System.out.println(videoData.getDescription());
        System.out.println(videoData.getThumbnail());
        System.out.println(videoData.getTagsResponse());
        // Assertions to verify that VideoData is correctly populated
        assertEquals("Video title should match", VIDEO_TITLE, videoData.getVideoTitle());
        assertEquals("Channel title should match", CHANNEL_TITLE, videoData.getChannelTitle());
        assertEquals("Description should match", DESCRIPTION, videoData.getDescription());
        assertEquals("Thumbnail URL should match", THUMBNAIL_URL, videoData.getThumbnail());
        assertEquals("Tags should be joined with '+'", String.join("+", TAGS), videoData.getTagsResponse());
    }
}

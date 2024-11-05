package Models;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.VideoListResponse;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;

import java.util.List;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VideoDataTest {

    @Mock
    private YouTube mockYouTube;

    @Mock
    private YouTube.Videos mockVideos;

    @Mock
    private YouTube.Videos.List mockVideoList;

    @Mock
    private VideoListResponse mockResponse;

    @Mock
    private Video mockVideo;

    @Mock
    private VideoSnippet mockSnippet;

    @Mock
    private ThumbnailDetails mockThumbnails;

    @Mock
    private Thumbnail mockThumbnail;

    String VIDEO_ID = "HrCbXNRP7eg";
    String API_KEY = "AIzaSyAugi0_hJ_OgciWZoKLnYybGcZlq4CLJiw";
    String VIDEO_TITLE = "The Problem With Being “Too Nice” at Work | Tessa West | TED\n";
    String CHANNEL_TITLE = "TED";
    String DESCRIPTION = "Are you \"too nice\" at work? Social psychologist Tessa West shares her research on how people attempt to mask anxiety with overly polite feedback — a practice that's more harmful than helpful — and gives three tips to swap generic, unhelpful observations with clear, consistent feedback, even when you feel awkward. If you love watching TED Talks like this one, become a TED Member to support our mission of spreading ideas: https://ted.com/membership Follow TED! X: https://twitter.com/TEDTalks Instagram: https://www.instagram.com/ted Facebook: https://facebook.com/TED LinkedIn: https://www.linkedin.com/company/ted-conferences TikTok: https://www.tiktok.com/@tedtoks The TED Talks channel features talks, performances and original series from the world's leading thinkers and doers. Subscribe to our channel for videos on Technology, Entertainment and Design — plus science, business, global issues, the arts and more. Visit https://TED.com to get our entire library of TED Talks, transcripts, translations, personalized talk recommendations and more. Watch more: https://go.ted.com/tessawest https://youtu.be/HrCbXNRP7eg TED's videos may be used for non-commercial purposes under a Creative Commons License, Attribution–Non Commercial–No Derivatives (or the CC BY – NC – ND 4.0 International) and in accordance with our TED Talks Usage Policy: https://www.ted.com/about/our-organization/our-policies-terms/ted-talks-usage-policy. For more information on using TED for commercial purposes (e.g. employee learning, in a film or online course), please submit a Media Request at https://media-requests.ted.com #TED #TEDTalks #work";
    String THUMBNAIL_URL = "https://i.ytimg.com/vi/HrCbXNRP7eg/hqdefault.jpg";
    List<String> TAGS = List.of("Ted", "Ted Talks", "TedTalk");

    @Before
    public void setUp() throws IOException {
        // Mock the main YouTube object and its nested classes
        mockYouTube = mock(YouTube.class);
        mockVideos = mock(YouTube.Videos.class);
        mockVideoList = mock(YouTube.Videos.List.class);

        // Mock the response objects
        mockResponse = mock(VideoListResponse.class);
        mockVideo = mock(Video.class);
        mockSnippet = mock(VideoSnippet.class);
        mockThumbnails = mock(ThumbnailDetails.class);
        mockThumbnail = mock(Thumbnail.class);

        // Set up the behavior for the YouTube API calls
        when(mockYouTube.videos()).thenReturn(mockVideos);
        when(mockVideoList.execute()).thenReturn(mockResponse);
        when(mockVideoList.setKey(API_KEY)).thenReturn(mockVideoList);
        when(mockVideoList.setId(Collections.singletonList(VIDEO_ID))).thenReturn(mockVideoList);
        when(mockVideos.list(Collections.singletonList("snippet,contentDetails,statistics"))).thenReturn(mockVideoList);

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
    public void testVideoDataGrabbing() throws IOException {
        // API key testing
        //Case 1: Null pointer exception
        assertThrows(NullPointerException.class, () -> new VideoData(mockYouTube, VIDEO_ID, ""));

        //Case 2: Illegal argument exception, api length too short
        assertThrows(IllegalArgumentException.class, () -> new VideoData(mockYouTube, VIDEO_ID, "abbbbbdidi"));

        //Case 3: Illegal argument exception, api length too long
        assertThrows(IllegalArgumentException.class, () -> new VideoData(mockYouTube, VIDEO_ID, "abbbbbdidissssssssssijfijfinfianfdinfinafinidni"));

        //Case 4: Illegal argument exception, api key contains illegal characters
        assertThrows(IllegalArgumentException.class, () -> new VideoData(mockYouTube, VIDEO_ID, "AI/aSyAugi0_hJ_OgciWZoKLnY*%cZlq4CLJi&"));


        // Video ID testing
        //Case 1: Null pointer exception
        assertThrows(NullPointerException.class, () -> new VideoData(mockYouTube, "", API_KEY));

        //Case 2: Illegal argument exception, id length too short
        assertThrows(IllegalArgumentException.class, () -> new VideoData(mockYouTube, "HrCbXNRe", API_KEY));

        //Case 3: Illegal argument exception, id length too long
        assertThrows(IllegalArgumentException.class, () -> new VideoData(mockYouTube, "HrCbXNRP7egsss", API_KEY));

        //Case 4: Illegal argument exception, id contains illegal characters
        assertThrows(IllegalArgumentException.class, () -> new VideoData(mockYouTube, "HrCbXNRP/%_", API_KEY));


        //Successful case
        VideoData videoData = new VideoData(mockYouTube, VIDEO_ID, API_KEY);
        assertNotNull("videoData object was successfully created and the grabbed values were initialized",videoData);

        // Assertions to verify that VideoData is correctly initialized
        assertEquals("Video title should match", VIDEO_TITLE, videoData.getVideoTitle());
        assertEquals("Description should match", DESCRIPTION, videoData.getDescription());
        assertEquals("Thumbnail URL should match", THUMBNAIL_URL, videoData.getThumbnail());
        assertEquals("Channel title should match", CHANNEL_TITLE, videoData.getChannelTitle());
        assertEquals("Tags should be joined with '+'", String.join("+", TAGS), videoData.getTagsResponse());
    }
}

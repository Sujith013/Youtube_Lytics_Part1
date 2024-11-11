package Models;

import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import java.util.*;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;


public class SearchDataTest {

    @Mock
    private YouTube mockYoutube;

    @Mock
    private YouTube.Search mockSearch;

    @Mock
    private YouTube.Search.List mockSearchList;

    @Mock
    private SearchListResponse mockResponse;

    @Mock
    private SearchResult[] mockVideos = new SearchResult[20];

    @Mock
    private SearchResultSnippet[] mockSnippet = new SearchResultSnippet[20];

    @Mock
    private ResourceId[] mockId = new ResourceId[20];

    @Mock
    private ThumbnailDetails[] mockThumbnailDetails = new ThumbnailDetails[20];

    @Mock
    private Thumbnail[] mockThumbnail = new Thumbnail[20];

    @Mock
    private TagsData mockTagData;

    String VALID_VIDEO_ID = "12345678987";

    private final String query = "ted+talks";
    private final String api_key = "AIzaSyAugi0_hJ_OgciWZoKLnYybGcZlq4CLJiw";

    private static final String[] VIDEO_KIND = {"youtube#channel","youtube#channel","youtube#playlist","youtube#playlist","youtube#playlist","youtube#playlist","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video","youtube#video"};
    private static final String[] VIDEO_ID = {"https://www.youtube.com/watch?v=null","https://www.youtube.com/watch?v=null","https://www.youtube.com/watch?v=null","https://www.youtube.com/watch?v=null","https://www.youtube.com/watch?v=null","https://www.youtube.com/watch?v=null","https://www.youtube.com/watch?v=2LkDU0iKaro","https://www.youtube.com/watch?v=PBaFURjVrm0","https://www.youtube.com/watch?v=eIho2S0ZahI","https://www.youtube.com/watch?v=HrCbXNRP7eg","https://www.youtube.com/watch?v=8jPQjjsBbIc","https://www.youtube.com/watch?v=O9pD6LTF4Bk","https://www.youtube.com/watch?v=arj7oStGLkU","https://www.youtube.com/watch?v=PY9DcIMGxMs","https://www.youtube.com/watch?v=-moW9jvvMr4","https://www.youtube.com/watch?v=H14bBuluwB8","https://www.youtube.com/watch?v=gUV5DJb6KGs","https://www.youtube.com/watch?v=lg48Bi9DA54","https://www.youtube.com/watch?v=dIYmzf21d1g","https://www.youtube.com/watch?v=Hu4Yvq-g7_Y"};
    private static final String[] VIDEO_TITLE = {"TED","TEDx Talks","The 20 Most-Watched TEDTalks","10 funniest TED Talks","Most Interesting Ted Talks","TED Talks Daily","Networking Doesn't Have to Feel Gross | Daniel Hallak | TED","How to hack your brain for better focus | Sasha Hamdani | TEDxKC","How to Speak So That People Want to Listen | Julian Treasure | TED","The Problem With Being ΓÇ£Too NiceΓÇ¥ at Work | Tessa West | TED","How to stay calm when you know you&#39;ll be stressed | Daniel Levitin | TED","What Nobody Tells You About Your Twenties | Livi Redden | TEDxBayonne","Inside the Mind of a Master Procrastinator | Tim Urban | TED","Everything you think you know about addiction is wrong | Johann Hari | TED","A simple way to break a bad habit | Judson Brewer | TED","Grit: The Power of Passion and Perseverance | Angela Lee Duckworth | TED","How to talk to the worst parts of yourself | Karen Faith | TEDxKC","The science behind dramatically better conversations | Charles Duhigg | TEDxManchester","How to Claim Your Leadership Power | Michael Timms | TED","How to Get Your Brain to Focus | Chris Bailey | TEDxManchester"};
    private static final String[] CHANNEL_TITLE = {"https://www.youtube.com/@TED","https://www.youtube.com/@TEDx Talks","https://www.youtube.com/@TED","https://www.youtube.com/@Yoong Cheong Sin","https://www.youtube.com/@Bryan Leonardo","https://www.youtube.com/@TED Audio Collective","https://www.youtube.com/@TED","https://www.youtube.com/@TEDx Talks","https://www.youtube.com/@TED","https://www.youtube.com/@TED","https://www.youtube.com/@TED","https://www.youtube.com/@TEDx Talks","https://www.youtube.com/@TED","https://www.youtube.com/@TED","https://www.youtube.com/@TED","https://www.youtube.com/@TED","https://www.youtube.com/@TEDx Talks","https://www.youtube.com/@TEDx Talks","https://www.youtube.com/@TED","https://www.youtube.com/@TEDx Talks"};
    private static final String[] CHANNEL_ID = {"https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCsT0YIqwnpJCM-mx7-gSA4Q","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCo05wfWBCsmAeL50CPA_Vyw","https://www.youtube.com/channel/UCKpc2NQtnrEx9Srs9EDDLUA","https://www.youtube.com/channel/UCy9b8cNJQmxX8Y2bdE6mQNw","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCsT0YIqwnpJCM-mx7-gSA4Q","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCsT0YIqwnpJCM-mx7-gSA4Q","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCsT0YIqwnpJCM-mx7-gSA4Q","https://www.youtube.com/channel/UCsT0YIqwnpJCM-mx7-gSA4Q","https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug","https://www.youtube.com/channel/UCsT0YIqwnpJCM-mx7-gSA4Q"};
    private static final String[] DESCRIPTION = {"The TED Talks channel features the best talks and performances from the TED Conference, where the world's leading thinkers ...","TEDx is an international community that organizes TED-style events anywhere and everywhere -- celebrating locally-driven ideas ...","A list of the 20 most-watched talks on all the platforms we track: TED.com, YouTube, iTunes, embed and download, Hulu and ...","","","Every weekday, TED Talks Daily brings you the latest talks in audio. Join host and journalist Elise Hu for thought-provoking ideas ...","Networking doesn't always have to feel like a self-serving transaction, says executive coach Daniel Hallak. Highlighting the ...","The modern world constantly fragments our attention. In this funny, insightful talk, Dr. Hamdani, a psychiatrist and ADHD expert, ...","Have you ever felt like you're talking, but nobody is listening? Here's Julian Treasure to help you fix that. As the sound expert ...","Are you \"too nice\" at work? Social psychologist Tessa West shares her research on how people attempt to mask anxiety with ...","Visit http://TED.com to get our entire library of TED Talks, transcripts, translations, personalized talk recommendations and more.","The fact of the matter is many of the biggest decisions that leave a long-lasting impact on our lives generally occur in our teens ...","Tim Urban knows that procrastination doesn't make sense, but he's never been able to shake his habit of waiting until the last ...","Visit http://TED.com to get our entire library of TED Talks, transcripts, translations, personalized talk recommendations and more.","Visit http://TED.com to get our entire library of TED Talks, transcripts, translations, personalized Talk recommendations and more.","Visit http://TED.com to get our entire library of TED Talks, transcripts, translations, personalized talk recommendations and more.","NOTE FROM TED: This talk contains a discussion of suicidal ideation. If you are struggling with suicidal thoughts, please consult a ...","In a world of increasing complexity but decreasing free time, the role of the trusted 'explainer' has never been more important.","When faced with challenges, do you often seek someone else to blame? Leadership expert Michael Timms shows why this ...","The latest research is clear: the state of our attention determines the state of our lives. So how do we harness our attention to focus ..."};
    private static final String[] URL = {"https://yt3.ggpht.com/ytc/AIdro_l_fFETDQgTAl5rWb38pxJww-4kszJH_n0G4fKP1BdK-jc=s800-c-k-c0xffffffff-no-rj-mo","https://yt3.ggpht.com/70r5TkYTLC0cpKLAiQEvcWLeIHB8yxoiog0nQIK9MmnZHqkICy0YA-jAaqfT2ChOBwehskjf5g=s800-c-k-c0xffffffff-no-rj-mo","https://i.ytimg.com/vi/iG9CE55wbtY/hqdefault.jpg","https://i.ytimg.com/vi/buRLc2eWGPQ/hqdefault.jpg","https://i.ytimg.com/vi/MB5IX-np5fE/hqdefault.jpg","https://i.ytimg.com/vi/24wBKuU2rfE/hqdefault.jpg","https://i.ytimg.com/vi/2LkDU0iKaro/hqdefault.jpg","https://i.ytimg.com/vi/PBaFURjVrm0/hqdefault.jpg","https://i.ytimg.com/vi/eIho2S0ZahI/hqdefault.jpg","https://i.ytimg.com/vi/HrCbXNRP7eg/hqdefault.jpg","https://i.ytimg.com/vi/8jPQjjsBbIc/hqdefault.jpg","https://i.ytimg.com/vi/O9pD6LTF4Bk/hqdefault.jpg","https://i.ytimg.com/vi/arj7oStGLkU/hqdefault.jpg","https://i.ytimg.com/vi/PY9DcIMGxMs/hqdefault.jpg","https://i.ytimg.com/vi/-moW9jvvMr4/hqdefault.jpg","https://i.ytimg.com/vi/H14bBuluwB8/hqdefault.jpg","https://i.ytimg.com/vi/gUV5DJb6KGs/hqdefault.jpg","https://i.ytimg.com/vi/lg48Bi9DA54/hqdefault.jpg","https://i.ytimg.com/vi/dIYmzf21d1g/hqdefault.jpg","https://i.ytimg.com/vi/Hu4Yvq-g7_Y/hqdefault.jpg"};

    private static final List<List<String>> expectedVideos = Arrays.asList(Arrays.asList("Networking Doesn't Have to Feel Gross | Daniel Hallak | TED","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=2LkDU0iKaro","https://www.youtube.com/@https://www.youtube.com/@TED","Networking doesn't always have to feel like a self-serving transaction, says executive coach Daniel Hallak. Highlighting the ...","https://i.ytimg.com/vi/2LkDU0iKaro/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug"),Arrays.asList("How to hack your brain for better focus | Sasha Hamdani | TEDxKC","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=PBaFURjVrm0","https://www.youtube.com/@https://www.youtube.com/@TEDx Talks","The modern world constantly fragments our attention. In this funny, insightful talk, Dr. Hamdani, a psychiatrist and ADHD expert, ...","https://i.ytimg.com/vi/PBaFURjVrm0/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCsT0YIqwnpJCM-mx7-gSA4Q"),Arrays.asList("How to Speak So That People Want to Listen | Julian Treasure | TED","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=eIho2S0ZahI","https://www.youtube.com/@https://www.youtube.com/@TED","Have you ever felt like you're talking, but nobody is listening? Here's Julian Treasure to help you fix that. As the sound expert ...","https://i.ytimg.com/vi/eIho2S0ZahI/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug"),Arrays.asList("The Problem With Being ΓÇ£Too NiceΓÇ¥ at Work | Tessa West | TED","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=HrCbXNRP7eg","https://www.youtube.com/@https://www.youtube.com/@TED","Are you \"too nice\" at work? Social psychologist Tessa West shares her research on how people attempt to mask anxiety with ...","https://i.ytimg.com/vi/HrCbXNRP7eg/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug"),Arrays.asList("How to stay calm when you know you&#39;ll be stressed | Daniel Levitin | TED","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=8jPQjjsBbIc","https://www.youtube.com/@https://www.youtube.com/@TED","Visit http://TED.com to get our entire library of TED Talks, transcripts, translations, personalized talk recommendations and more.","https://i.ytimg.com/vi/8jPQjjsBbIc/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug"),Arrays.asList("What Nobody Tells You About Your Twenties | Livi Redden | TEDxBayonne","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=O9pD6LTF4Bk","https://www.youtube.com/@https://www.youtube.com/@TEDx Talks","The fact of the matter is many of the biggest decisions that leave a long-lasting impact on our lives generally occur in our teens ...","https://i.ytimg.com/vi/O9pD6LTF4Bk/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCsT0YIqwnpJCM-mx7-gSA4Q"),Arrays.asList("Inside the Mind of a Master Procrastinator | Tim Urban | TED","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=arj7oStGLkU","https://www.youtube.com/@https://www.youtube.com/@TED","Tim Urban knows that procrastination doesn't make sense, but he's never been able to shake his habit of waiting until the last ...","https://i.ytimg.com/vi/arj7oStGLkU/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug"),Arrays.asList("Everything you think you know about addiction is wrong | Johann Hari | TED","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=PY9DcIMGxMs","https://www.youtube.com/@https://www.youtube.com/@TED","Visit http://TED.com to get our entire library of TED Talks, transcripts, translations, personalized talk recommendations and more.","https://i.ytimg.com/vi/PY9DcIMGxMs/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug"),Arrays.asList("A simple way to break a bad habit | Judson Brewer | TED","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=-moW9jvvMr4","https://www.youtube.com/@https://www.youtube.com/@TED","Visit http://TED.com to get our entire library of TED Talks, transcripts, translations, personalized Talk recommendations and more.","https://i.ytimg.com/vi/-moW9jvvMr4/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug"),Arrays.asList("Grit: The Power of Passion and Perseverance | Angela Lee Duckworth | TED","https://www.youtube.com/watch?v=https://www.youtube.com/watch?v=H14bBuluwB8","https://www.youtube.com/@https://www.youtube.com/@TED","Visit http://TED.com to get our entire library of TED Talks, transcripts, translations, personalized talk recommendations and more.","https://i.ytimg.com/vi/H14bBuluwB8/hqdefault.jpg","/channel/https://www.youtube.com/channel/UCAuUUnT6oDeKwE6v1NGQxug")
            );

    public static LinkedHashMap<String,Long> expectedWordStats = new LinkedHashMap<>();


    /**
     * The setup function to return the responses for the class functions
     * @author Sujith Manikandan
     * @author Tharun Balaji
     * @author Thansil Mohamed Syed Hamdulla
     * @author Prakash Yuvaraj
     * @throws IOException related to network and API issues
     * */
    @Before
    public void setup() throws IOException {
        mockYoutube = mock(YouTube.class);
        mockSearch = mock(YouTube.Search.class);
        mockResponse = mock(SearchListResponse.class);
        mockSearchList = mock(YouTube.Search.List.class);
        mockTagData = mock(TagsData.class);

        // Mock the YouTube search setup
        when(mockYoutube.search()).thenReturn(mockSearch);
        when(mockSearch.list(Collections.singletonList("snippet"))).thenReturn(mockSearchList);

        // Set up the query and max results
        when(mockSearchList.setQ(query)).thenReturn(mockSearchList);
        when(mockSearchList.setKey(api_key)).thenReturn(mockSearchList);
        when(mockSearchList.setMaxResults(20L)).thenReturn(mockSearchList);

        // Mock the response to return our mock video
        when(mockSearchList.execute()).thenReturn(mockResponse);

        // Create the first mock video
        for(int i=0;i<20;i++) {
            mockVideos[i] = mock(SearchResult.class);
            mockSnippet[i] = mock(SearchResultSnippet.class);
            mockId[i] = mock(ResourceId.class);
            mockThumbnail[i] = mock(Thumbnail.class);
            mockThumbnailDetails[i] = mock(ThumbnailDetails.class);

            when(mockVideos[i].getId()).thenReturn(mockId[i]);
            when(mockVideos[i].getSnippet()).thenReturn(mockSnippet[i]);
            when(mockSnippet[i].getThumbnails()).thenReturn(mockThumbnailDetails[i]);
            when(mockThumbnailDetails[i].getHigh()).thenReturn(mockThumbnail[i]);

            when(mockId[i].getKind()).thenReturn(VIDEO_KIND[i]);
            when(mockId[i].getVideoId()).thenReturn(VIDEO_ID[i]);
            when(mockSnippet[i].getTitle()).thenReturn(VIDEO_TITLE[i]);
            when(mockSnippet[i].getChannelId()).thenReturn(CHANNEL_ID[i]);
            when(mockSnippet[i].getChannelTitle()).thenReturn(CHANNEL_TITLE[i]);
            when(mockSnippet[i].getDescription()).thenReturn(DESCRIPTION[i]);
            when(mockThumbnail[i].getUrl()).thenReturn(URL[i]);
        }

        when(mockResponse.getItems()).thenReturn(List.of(mockVideos));
    }


    /**
     * The function that tests various cases for the main search function of YouTube
     * @author Sujith Manikandan
     * @author Tharun Balaji
     * @author Thansil Mohamed Syed Hamdulla
     * @author Prakash Yuvaraj
     * @throws IOException related to network and API issues
     * */
    @Test
    public void testYouTubeSearch() throws Exception {
        // Execute the method
        SearchData ytSearch = new SearchData(mockYoutube, query, api_key);

        assertEquals("The processed video list should match the expected output.", expectedVideos, ytSearch.getVideos());
        assertNotNull(ytSearch.getVideos());


        // API key testing
        //Case 1: Null pointer exception
        assertThrows(NullPointerException.class, () -> new TagsData(mockYoutube, query, ""));

        //Case 2: Illegal argument exception, api length too short
        assertThrows(IllegalArgumentException.class, () -> new TagsData(mockYoutube, query, "abbbbbdidi"));

        //Case 3: Illegal argument exception, api length too long
        assertThrows(IllegalArgumentException.class, () -> new TagsData(mockYoutube, query, "abbbbbdidissssssssssijfijfinfianfdinfinafinidni"));

        //Case 4: Illegal argument exception, api key contains illegal characters
        assertThrows(IllegalArgumentException.class, () -> new TagsData(mockYoutube, query, "AI/aSyAugi0_hJ_OgciWZoKLnY*%cZlq4CLJi&"));
    }




    /**
     * The test to verify the words List
     * @author Prakash Yuvaraj
     * @throws IOException related to network and API issues
     * */
     @Test
     public void testWordsInList() throws Exception {
            SearchData ytSearch = new SearchData(mockYoutube, query, api_key);

            List<String> happyWords = Arrays.asList("joyful", "excited", "elated", "happy");
            for (String word : happyWords) {
                assertTrue("Happy word list should contain: " + word, ytSearch.getHappyList().contains(word));
            }

            List<String> sadWords = Arrays.asList("sad", "unhappy", "miserable", "heartbroken");
            for (String word : sadWords) {
                assertTrue("Sad word list should contain: " + word, ytSearch.getSadList().contains(word));
            }
        }



    /**
     * The test to verify the emojis List
     * @author Prakash Yuvaraj
     * @throws IOException related to network and API issues
     * */
     @Test
     public void testEmojisInList() throws Exception{
            SearchData ytSearch = new SearchData(mockYoutube, query, api_key);

            List<String> happyEmojis = Arrays.asList("😊", "😁", "😄");
            for (String emoji : happyEmojis) {
                assertTrue("Happy emoji list should contain: " + emoji, ytSearch.getHappyEmojis().contains(emoji));
            }

            List<String> sadEmojis = Arrays.asList("😞", "😢", "😭");
            for (String emoji : sadEmojis) {
                assertTrue("Sad emoji list should contain: " + emoji, ytSearch.getSadEmojis().contains(emoji));
            }
        }



    /**
     * The test to verify the emoticons List
     * @author Prakash Yuvaraj
     * @throws IOException related to network and API issues
     * */
     @Test
     public void testEmoticonsInList() throws Exception{
            SearchData ytSearch = new SearchData(mockYoutube, query, api_key);

            List<String> happyEmoticons = Arrays.asList(":-)", ":)", ":D");
            for (String emoticon : happyEmoticons) {
                assertTrue("Happy emoticon list should contain: " + emoticon, ytSearch.getHappyEmoticons().contains(emoticon));
            }

            List<String> sadEmoticons = Arrays.asList(":-(", ":(", ":'(");
            for (String emoticon : sadEmoticons) {
                assertTrue("Sad emoticon list should contain: " + emoticon, ytSearch.getSadEmoticons().contains(emoticon));
            }
        }


    /**
     * The function that tests the final sentiment 4 different descriptions
     * @author Prakash Yuvaraj
     * @throws IOException related to network and API issues
     * */
    @Test
    public void testSentimentAnalysis() throws Exception{
            SearchData ytSearch = new SearchData(mockYoutube, query, api_key);

            List<String> descriptions1 = Arrays.asList("joyful and excited :-)", "I feel sad and heartbroken :-(");
            List<String> descriptions2 = Arrays.asList("I feel 😊", "It's a great day 😁", "I'm so excited 😄");
            List<String> descriptions3 = Arrays.asList("Feeling down 😢", "It's been a rough day :-(", "Missed opportunities :-(");
            List<String> descriptions4 =  Arrays.asList("I'm so 😊", "Life is joyful", "Feeling 😞 and down", "It's a sad day :-(");

            String final_sentiment1 = ytSearch.getSentimentAnalysis(descriptions1);
            assertEquals("Expected 2 happy words in descriptions", 3, ytSearch.getHappyCount());
            assertEquals("Expected 2 sad words in descriptions", 3, ytSearch.getSadCount());

            String final_sentiment2 = ytSearch.getSentimentAnalysis(descriptions2);
            assertEquals("Expected 3 happy words in descriptions", 4, ytSearch.getHappyCount());
            assertEquals("Expected 0 sad words in descriptions", 0, ytSearch.getSadCount());

            String final_sentiment3 = ytSearch.getSentimentAnalysis(descriptions3);
            assertEquals("Expected 0 happy words in descriptions", 0, ytSearch.getHappyCount());
            assertEquals("Expected 3 sad words in descriptions", 5, ytSearch.getSadCount());

            String final_sentiment4 = ytSearch.getSentimentAnalysis(descriptions4);
            assertEquals("Expected 2 happy words in descriptions", 2, ytSearch.getHappyCount());
            assertEquals("Expected 2 sad words in descriptions", 4, ytSearch.getSadCount());

            List<String> descriptions5 =  Arrays.asList("joyful", "excited", ":-)", ":-)", ":-)", ":-)", ":-)", ":-)", ":-)", ":-)");
            String final_sentiment5 = ytSearch.getSentimentAnalysis(descriptions5);
            assertEquals("Expected 10 happy words in descriptions", 10, ytSearch.getHappyCount());
            assertEquals("Expected 0 sad words in descriptions", 0, ytSearch.getSadCount());
            assertEquals("The final sentiment is happy for >70% case",":-)",final_sentiment5);

            List<String> descriptions6 =  Arrays.asList("sad", "unhappy", ":-(", ":-(", ":-(", ":-(", ":-(", ":-(", ":-(", ":-(");
            String final_sentiment6 = ytSearch.getSentimentAnalysis(descriptions6);
            assertEquals("Expected 0 happy words in descriptions", 0, ytSearch.getHappyCount());
            assertEquals("Expected 10 sad words in descriptions", 10, ytSearch.getSadCount());
            assertEquals("The final sentiment is sad for (<30%) case",":-(",final_sentiment6);

            List<String> descriptions7 =  Arrays.asList("neutral", ":-)", ":-(", "balanced", "okay", ":-|", ":-)", ":-(", ":-|", "neutral");
            String final_sentiment7 = ytSearch.getSentimentAnalysis(descriptions7);
            assertEquals("Expected 2 happy words in descriptions", 2, ytSearch.getHappyCount());
            assertEquals("Expected 2 sad words in descriptions", 2, ytSearch.getSadCount());
            assertEquals("The final sentiment is neutral for both happy and sad (30% - 70%)(exclusive) case",":-|",final_sentiment7);

            List<String> descriptions8 =  Arrays.asList(":-)", ":-)", ":-)", ":-)", ":-)", ":-)", ":-)", "neutral", "sad", "okay");
            String final_sentiment8 = ytSearch.getSentimentAnalysis(descriptions8);
            assertEquals("Expected 7 happy words in descriptions", 7, ytSearch.getHappyCount());
            assertEquals("The final sentiment is happy when happy is exactly 70% case",":-)",final_sentiment8);

            List<String> descriptions9 =  Arrays.asList(":-(", ":-(", ":-(", ":-(", ":-(", ":-(", ":-(", "neutral", "happy", "okay");
            String final_sentiment9 = ytSearch.getSentimentAnalysis(descriptions9);
            assertEquals("Expected 7 sad words in descriptions", 7, ytSearch.getSadCount());
            assertEquals("The final sentiment is sad when sad is exactly 70% case",":-(",final_sentiment9);
        }



    /**
     * The function that tests various cases for the wordStats function in the SearchData class
     * @author Tharun Balaji
     * @throws IOException related to network and API issues
     * */
    @Test
    public void testCalculateWordStats() throws Exception {
        SearchData ytSearch = new SearchData(mockYoutube, query, api_key);

        LinkedHashMap<String, Long> wordStats = ytSearch.getWordStats();

        expectedWordStats.put("ted",8L);
        expectedWordStats.put("to",8L);
        expectedWordStats.put("our",7L);
        expectedWordStats.put("the",7L);
        expectedWordStats.put("of",7L);
        expectedWordStats.put("talk",5L);
        expectedWordStats.put("and",5L);
        expectedWordStats.put("entire",4L);
        expectedWordStats.put("translations",4L);
        expectedWordStats.put("you",4L);
        expectedWordStats.put("personalized",4L);
        expectedWordStats.put("library",4L);
        expectedWordStats.put("get",4L);
        expectedWordStats.put("more",4L);
        expectedWordStats.put("com",4L);
        expectedWordStats.put("recommendations",4L);
        expectedWordStats.put("transcripts",4L);
        expectedWordStats.put("http",4L);
        expectedWordStats.put("talks",4L);
        expectedWordStats.put("visit",4L);
        expectedWordStats.put("a",3L);
        expectedWordStats.put("that",3L);
        expectedWordStats.put("expert",2L);
        expectedWordStats.put("in",2L);
        expectedWordStats.put("is",2L);
        expectedWordStats.put("have",2L);
        expectedWordStats.put("s",2L);
        expectedWordStats.put("t",2L);
        expectedWordStats.put("but",2L);
        expectedWordStats.put("doesn",2L);
        expectedWordStats.put("like",2L);
        expectedWordStats.put("on",2L);
        expectedWordStats.put("been",1L);
        expectedWordStats.put("attempt",1L);
        expectedWordStats.put("executive",1L);
        expectedWordStats.put("her",1L);
        expectedWordStats.put("fix",1L);
        expectedWordStats.put("nobody",1L);
        expectedWordStats.put("hallak",1L);
        expectedWordStats.put("lives",1L);
        expectedWordStats.put("work",1L);
        expectedWordStats.put("impact",1L);
        expectedWordStats.put("sense",1L);
        expectedWordStats.put("insightful",1L);
        expectedWordStats.put("ever",1L);
        expectedWordStats.put("as",1L);
        expectedWordStats.put("at",1L);
        expectedWordStats.put("re",1L);
        expectedWordStats.put("coach",1L);
        expectedWordStats.put("fact",1L);
        expectedWordStats.put("teens",1L);
        expectedWordStats.put("talking",1L);
        expectedWordStats.put("matter",1L);
        expectedWordStats.put("long",1L);
        expectedWordStats.put("serving",1L);
        expectedWordStats.put("shares",1L);
        expectedWordStats.put("how",1L);
        expectedWordStats.put("world",1L);
        expectedWordStats.put("modern",1L);
        expectedWordStats.put("too",1L);
        expectedWordStats.put("are",1L);
        expectedWordStats.put("decisions",1L);
        expectedWordStats.put("psychologist",1L);
        expectedWordStats.put("funny",1L);
        expectedWordStats.put("knows",1L);
        expectedWordStats.put("tessa",1L);
        expectedWordStats.put("many",1L);
        expectedWordStats.put("procrastination",1L);
        expectedWordStats.put("people",1L);
        expectedWordStats.put("nice",1L);
        expectedWordStats.put("generally",1L);
        expectedWordStats.put("help",1L);
        expectedWordStats.put("adhd",1L);
        expectedWordStats.put("able",1L);
        expectedWordStats.put("self",1L);
        expectedWordStats.put("transaction",1L);
        expectedWordStats.put("shake",1L);
        expectedWordStats.put("sound",1L);
        expectedWordStats.put("feel",1L);
        expectedWordStats.put("networking",1L);
        expectedWordStats.put("constantly",1L);
        expectedWordStats.put("research",1L);
        expectedWordStats.put("dr",1L);
        expectedWordStats.put("habit",1L);
        expectedWordStats.put("his",1L);
        expectedWordStats.put("biggest",1L);
        expectedWordStats.put("leave",1L);
        expectedWordStats.put("west",1L);
        expectedWordStats.put("tim",1L);
        expectedWordStats.put("fragments",1L);
        expectedWordStats.put("always",1L);
        expectedWordStats.put("last",1L);
        expectedWordStats.put("occur",1L);
        expectedWordStats.put("felt",1L);
        expectedWordStats.put("this",1L);
        expectedWordStats.put("julian",1L);
        expectedWordStats.put("daniel",1L);
        expectedWordStats.put("never",1L);
        expectedWordStats.put("anxiety",1L);
        expectedWordStats.put("psychiatrist",1L);
        expectedWordStats.put("hamdani",1L);
        expectedWordStats.put("lasting",1L);
        expectedWordStats.put("here",1L);
        expectedWordStats.put("waiting",1L);
        expectedWordStats.put("treasure",1L);
        expectedWordStats.put("highlighting",1L);
        expectedWordStats.put("make",1L);
        expectedWordStats.put("says",1L);
        expectedWordStats.put("mask",1L);
        expectedWordStats.put("listening",1L);
        expectedWordStats.put("social",1L);
        expectedWordStats.put("with",1L);
        expectedWordStats.put("urban",1L);
        expectedWordStats.put("attention",1L);
        expectedWordStats.put("until",1L);
        expectedWordStats.put("he",1L);

        assertNotNull(wordStats);

        assertEquals(expectedWordStats, wordStats);

        List<Long> values = new ArrayList<>(wordStats.values());

        boolean descending = true;

        for (int i = 1; i < values.size(); i++)
            if(values.get(i)>values.get(i-1)) {
                descending = false;
                break;
            }

        assertTrue("The wordStats map is sorted in descending order", descending);
    }
}
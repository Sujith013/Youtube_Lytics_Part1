package Models;

import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.services.youtube.YouTube;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;

/**
 * @author Sujith Manikandan
 * @author Tharun Balaji
 * @author Thansil Mohamed Syed Hamdulla
 * @author Prakash Yuvaraj
 * The main YouTube class that creates and returns the YouTube API object on call.
 * */
public class YoutubeService {
    private static YouTube youtube;

    /**
     * @author Sujith Manikandan
     * @author Tharun Balaji
     * @author Thansil Mohamed Syed Hamdulla
     * @author Prakash Yuvaraj
     * @throws IOException due to network or I/O issues such as connectivity issues
     * @throws GeneralSecurityException due to missing credentials and permissions restrictions
     * @return The YouTube object
     * */
    public static YouTube getService() throws GeneralSecurityException, IOException {
        if (youtube == null) {
            youtube = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    request -> {} // No need for an HTTPRequestInitializer with API key
            ).setApplicationName("YoutubeLytics").build();
        }
        return youtube;
    }
}
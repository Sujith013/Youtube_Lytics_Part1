package Models;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class YoutubeService {
    private static YouTube youtube;

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

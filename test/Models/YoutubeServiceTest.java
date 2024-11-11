package Models;

import org.junit.Test;
import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.services.youtube.YouTube;

import static org.junit.Assert.*;

public class YoutubeServiceTest {

    /**
     * Checks if the YouTube service object is not null
     * @author Sujith Manikandan
     * @author Tharun Balaji
     * @author Thansil Mohamed Syed Hamdulla
     * @author Prakash Yuvaraj
     * @throws IOException related to api and network issues*/
    @Test
    public void testGetServiceReturnsNonNull() throws GeneralSecurityException, IOException {
        YouTube service = YoutubeService.getService();
        assertNotNull("The YouTube service instance should not be null", service);
    }


    /**
     * Checks if the YouTube service object creation returns the same instance every time
     * @author Sujith Manikandan
     * @author Tharun Balaji
     * @author Thansil Mohamed Syed Hamdulla
     * @author Prakash Yuvaraj
     * @throws IOException related to api and network issues*/
    @Test
    public void testGetServiceIsSame() throws GeneralSecurityException, IOException {
        YouTube service1 = YoutubeService.getService();
        YouTube service2 = YoutubeService.getService();
        assertSame("The same YouTube instance should be returned each time", service1, service2);
    }
}

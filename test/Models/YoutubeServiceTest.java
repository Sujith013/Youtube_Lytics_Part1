package Models;

import org.junit.Test;
import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.services.youtube.YouTube;

import static org.junit.Assert.*;

public class YoutubeServiceTest {

    @Test
    public void testGetServiceReturnsNonNull() throws GeneralSecurityException, IOException {
        YouTube service = YoutubeService.getService();
        assertNotNull("The YouTube service instance should not be null", service);
    }

    @Test
    public void testGetServiceIsSame() throws GeneralSecurityException, IOException {
        YouTube service1 = YoutubeService.getService();
        YouTube service2 = YoutubeService.getService();
        assertSame("The same YouTube instance should be returned each time", service1, service2);
    }
}

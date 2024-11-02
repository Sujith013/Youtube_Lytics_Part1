package controllers;

import org.junit.Test;
import org.junit.Before;
import play.mvc.Http;
import play.mvc.Result;
import play.Application;
import play.test.WithApplication;
import play.inject.guice.GuiceApplicationBuilder;

import static org.junit.Assert.*;
import static play.test.Helpers.GET;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.route;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;


public class HomeControllerTest extends WithApplication {

    private static final String SEARCH_TERM = "abc+def";
    private static Http.Request mockRequest;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Before
    public void setUp()
    {
        mockRequest = Mockito.mock(Http.Request.class);
        when(mockRequest.getQueryString("searchTerms")).thenReturn(SEARCH_TERM);
    }

    @Test
    public void testSubmitInputQuery() throws Exception
    {
        //CASE 1: With the proper return status as the wordings have no spaces
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/search?searchTerms=mrbeast");

        Result result = route(app, request);
        assertEquals(OK, result.status());



        //CASE 2: With the proper return status although the words have spaces
        request = new Http.RequestBuilder()
                .method(GET)
                .uri("/search?searchTerms=ted+talks");

        result = route(app, request);
        assertEquals(OK, result.status());



        //CASE 3: Assert to throw a null pointer exception as expected
        assertThrows(NullPointerException.class, () -> {
            final Http.RequestBuilder request1 = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/search?searchTerms=");
            final Result result1 = route(app, request1);// Call the method with a null argument
        });



        //CASE 4: Assert to throw an illegal argument exception as expected for the spaces
        assertThrows(IllegalArgumentException.class, () -> {
            final Http.RequestBuilder request1 = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/search?searchTerms=ted talks");
            final Result result1 = route(app, request1);// Call the method with a null argument
        });



        //CASE 5: Assert to throws an illegal argument exception as expected for a search with no alphanumeric values
        assertThrows(IllegalArgumentException.class, () -> {
            final Http.RequestBuilder request1 = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/search?searchTerms===+.");
            final Result result1 = route(app, request1);// Call the method with a null argument
        });
    }


    @Test
    public void tagQuery() throws Exception
    {
        //CASE 1: With the proper return status as the wordings have no spaces
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/searchTag?searchTerm=mrbeast");

        Result result = route(app, request);
        assertEquals(OK, result.status());



        //CASE 2: With the proper return status although the words have spaces
        request = new Http.RequestBuilder()
                .method(GET)
                .uri("/searchTag?searchTerm=ted+talks");

        result = route(app, request);
        assertEquals(OK, result.status());



        //CASE 3: Assert to throw a null pointer exception as expected
        assertThrows(NullPointerException.class, () -> {
            final Http.RequestBuilder request1 = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/searchTag?searchTerm=");
            final Result result1 = route(app, request1);// Call the method with a null argument
        });



        //CASE 4: Assert to throw an illegal argument exception as expected for the spaces
        assertThrows(IllegalArgumentException.class, () -> {
            final Http.RequestBuilder request1 = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/searchTag?searchTerm=ted talks");
            final Result result1 = route(app, request1);// Call the method with a null argument
        });



        //CASE 5: Assert to throws an illegal argument exception as expected for a search with no alphanumeric values
        assertThrows(IllegalArgumentException.class, () -> {
            final Http.RequestBuilder request1 = new Http.RequestBuilder()
                    .method(GET)
                    .uri("/searchTag?searchTerm===+.");
            final Result result1 = route(app, request1);// Call the method with a null argument
        });
    }
}

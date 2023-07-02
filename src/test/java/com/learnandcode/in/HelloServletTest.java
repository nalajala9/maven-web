package com.learnandcode.in;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HelloServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private ServletConfig servletConfig;

    private HelloServlet helloServlet;

    @Before
    public void setUp() throws IOException {
        // Read the index.html file
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("index.html");
        Mockito.when(servletContext.getResourceAsStream("/index.html")).thenReturn(expectedStream);
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
        helloServlet = new HelloServlet();
        helloServlet.init(servletConfig);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        // Capture the response output
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        // Execute the servlet code
        helloServlet.doGet(request, response);

        // Flush and close the writer
        printWriter.flush();
        printWriter.close();

        // Get the response output
        String responseOutput = stringWriter.toString().trim();

        // Read the expected content from index.html
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("index.html");
        BufferedReader expectedReader = new BufferedReader(new InputStreamReader(expectedStream));
        StringBuilder expectedContent = new StringBuilder();
        String line;
        while ((line = expectedReader.readLine()) != null) {
            expectedContent.append(line);
        }
        expectedReader.close();

        // Normalize the HTML content by removing whitespace and newlines
        String normalizedExpectedContent = expectedContent.toString().replaceAll("\\s", "");
        String normalizedResponseOutput = responseOutput.replaceAll("\\s", "");

        // Assert that the normalized response output matches the normalized expected content
        assertEquals(normalizedExpectedContent, normalizedResponseOutput);

        // Verify that the content type is set to text/html
        verify(response).setContentType("text/html");
    }

    @Test
    public void testDoGetWithIOException() throws ServletException, IOException {
        // Simulate an IOException
        doThrow(IOException.class).when(response).getWriter();

        // Execute the servlet code
        helloServlet.doGet(request, response);

        // Verify that the exception is caught and printed
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    // Add more test cases to cover different scenarios

}

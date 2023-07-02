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

@RunWith(MockitoJUnitRunner.class)
public class HelloServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Before
    public void setUp() throws IOException {
        // Read the index.html file
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("index.html");
        Mockito.when(servletContext.getResourceAsStream("/index.html")).thenReturn(expectedStream);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        // Capture the response output
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        // Set up the ServletConfig
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);

        // Execute the servlet code
        HelloServlet helloServlet = new HelloServlet();
        helloServlet.init(servletConfig); // Initialize the servlet with the ServletConfig
        helloServlet.doGet(request, response);

        // Flush and close the writer
        printWriter.flush();
        printWriter.close();

        // Get the response output
        String responseOutput = stringWriter.toString().trim();

        // Read the expected content from index.html
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("index.html");
        if (expectedStream != null) {
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
        } else {
            // Handle the case where the resource stream is null
            assertEquals("Error: index.html not found", responseOutput);
        }
    }

    @Test
    public void testDoGetWithIOException() throws ServletException, IOException {
        // Simulate an IOException when getting the response writer
        Mockito.when(response.getWriter()).thenThrow(new IOException());

        // Set up the ServletConfig
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);

        // Execute the servlet code
        HelloServlet helloServlet = new HelloServlet();
        helloServlet.init(servletConfig); // Initialize the servlet with the ServletConfig
        helloServlet.doGet(request, response);
    }
}

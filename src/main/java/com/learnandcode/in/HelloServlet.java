package com.learnandcode.in;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        // Read the HTML file
        InputStream inputStream = getServletContext().getResourceAsStream("/index.html");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        PrintWriter writer = response.getWriter();
        String line;
        while ((line = reader.readLine()) != null) {
            writer.println(line);
        }
        writer.close();
    }
}

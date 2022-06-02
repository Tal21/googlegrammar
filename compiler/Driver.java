import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Driver {

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 7251), 0);
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10); // up to 10 ppl can handle your request

    MyHttpHandler myHttpHandler = new MyHttpHandler();
    server.createContext("/test", myHttpHandler);
    server.setExecutor(threadPoolExecutor); // lets server handle multiple HHTP requests at the same time
    server.start(); // starts the server
    // two handlers, one input form (submit buttons), one autocompletion (gets response)

    //logger.info("Server started on port 7251");
  } // end main


  private static class MyHttpHandler implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {
      String requestParamValue=null;
      if("GET".equals(httpExchange.getRequestMethod())) {
         requestParamValue = handleGetRequest(httpExchange);
      }
      // else if("POST".equals(httpExchange)) {
      //    requestParamValue = handlePostRequest(httpExchange);
      // }
      handleResponse(httpExchange,requestParamValue);
    }

    private String handleGetRequest(HttpExchange httpExchange) {
      return httpExchange.
        getRequestURI()
        .toString()
        .split("\\?")[1]
        .split("=")[1];
   }

   private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws IOException {
      OutputStream outputStream = httpExchange.getResponseBody();
      StringBuilder htmlBuilder = new StringBuilder();
      htmlBuilder.append("<html>")  // generating a wesite with a specific HTML page
        .append("<head>")
        .append("<style>")
        .append("body {background-color: grey;}")
        .append("h1 {color: white; }")
        .append("label {font-size: 20px; font-family:courier; }")
        .append("input {size: 40px;}")

        .append("</style>")
        .append("</head>")

        .append("<body>")
        .append("<center>")
        .append("<h1 style= font-family:courier; >")
        .append("Welcome to our Google Project!")
        //.append(requestParamValue)
        .append("</h1>")
        .append("<form action=\"/action_page.php\">")
        .append("<label for=\"fname\">Insert text for autocompletion:</label>")
        .append("<br><input type=\"text\" id=\"ftext\" name=\"ftext\"><br><br>")
        .append("<input type=\"submit\" value=\"Submit\">")
        .append("</center>")
        .append("</body>")
        .append("</html>");

        // encode HTML content
        // String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());
        String htmlResponse = htmlBuilder.toString();

        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length()); // 200 is a status code, everyting is good response
        outputStream.write(htmlResponse.getBytes()); // writes up response you were writing
        outputStream.flush(); // cleans it up
        outputStream.close(); // cleans it up
    }
  } // end MyHttpHandler

}

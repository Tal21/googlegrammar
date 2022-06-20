package compiler;
import compiler.autocomp;
//import compiler.grammarchecker;

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

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import org.json.JSONTokener;
import org.json.JSONObject;
import org.json.JSONArray;

public class Driver {

  public static void main(String[] args) throws Exception {
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
      try {
        handleResponse(httpExchange, requestParamValue);
      }
      catch (Exception e) {
        throw new IOException(e.getMessage());
      }
    }

    private String handleGetRequest(HttpExchange httpExchange) {
      return httpExchange.
        getRequestURI()
        .toString()
        .split("\\?")[1]
        .split("=")[1];
   }

   private void handleResponse(HttpExchange httpExchange, String requestParamValue) throws Exception {
      String autocompleted = returnAutoComp(requestParamValue);
      OutputStream outputStream = httpExchange.getResponseBody();
      StringBuilder htmlBuilder = new StringBuilder();
      htmlBuilder.append("<html>")  // generating a wesite with a specific HTML page
        .append("<head>")
        .append("<style>")
        .append("body {background-color: grey; font-family: courier; position:relative; background-image:linear-gradient(0deg, rgba(100,252,223,1) 0%, rgba(38,144,170,1) 20%, rgba(65,82,112,1) 100%);}")
        .append("h1 {color: white; }")
        .append("label {font-size: 20px; }")
        .append("input {size: 40px;}")
        .append("table{ border-spacing:4px; border-color:grey; }")
        .append("form {width: 100px; background-color: transparent;}")

        .append("</style>")
        .append("</head>")

        .append("<body>")
        .append("<center>")
          .append("<div class = banner")
            .append("<table>")
              .append("<tbody>")
                .append("<tr>")
                  .append("<td>")
                    .append("<h1>") //style= font-family:courier;
                    .append("Welcome to our Google Project!")
                  //.append(requestParamValue)
                    .append("</h1>")
                  .append("</td>")
                .append("</tr>")
              .append("</tbody")
            .append("<table>")
          .append("</div>")

        .append("<form action=\"test\">")
        .append("<label for=\"fname\">Insert text for autocompletion:</label>")
        .append("<p> </p>")
        .append("<br><input type=\"text\" id=\"ftext\" name=\"ftext\" placeholder=\"Enter text to autocomplete:\" value=\""+autocompleted+"\"><br><br>")
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

    public static String returnAutoComp(String requestParamValue) throws Exception {
        autocomp auto = new autocomp();
        String url = "https://api-inference.huggingface.co/models/gpt2";  // example url which return json data
        String data = auto.readUrl(url, requestParamValue);
        String output = auto.stringFullToken(requestParamValue, data);
        JSONTokener tokener = new JSONTokener(data);
        JSONArray arr = new JSONArray(tokener);
        return arr.getJSONObject(0).get("generated_text").toString();

        // grammarchecker grammar = new grammarchecker();
        // String url = "https://api-inference.huggingface.co/models/vennify/t5-base-grammar-correction";  // example url which return json data
        // String data = grammar.readUrl(url, requestParamValue);
        // String output = grammar.stringFullToken(requestParamValue, data);
        // JSONTokener tokener = new JSONTokener(data);
        // JSONArray arr = new JSONArray(tokener);
        // return arr.getJSONObject(0).get("generated_text").toString();
    }
  } // end MyHttpHandler

}

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

import org.apache.commons.lang3.StringUtils;

public class MyHttpHandler implements HttpHandler {

  public MyHttpHandler() {

  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    String requestParamValue=null;
    if("GET".equals(httpExchange.getRequestMethod())) {
       requestParamValue = handleGetRequest(httpExchange);
    }
    else if("POST".equals(httpExchange)) {
       requestParamValue = handlePostRequest(httpExchange);
    }
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
    htmlBuilder.append("<html>").
      append("<body>").
      append("<h1>").
      append("Hello ")
      .append(requestParamValue)
      .append("</h1>")
      .append("</body>")
      .append("</html>");

      // encode HTML content
      String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());

      // this line is a must
      httpExchange.sendResponseHeaders(200, htmlResponse.length());
      outputStream.write(htmlResponse.getBytes());
      outputStream.flush();
      outputStream.close();
  }
} // end MyHttpHandler

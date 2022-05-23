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

  public static void main(String[] args) {
    HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 7251), 0);
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);

    MyHttpHandler myHttpHandler = new MyHttpHandler();
    server.createContext("/test", myHttpHandler);
    server.setExecutor(threadPoolExecutor);
    server.start();
    logger.info("Server started on port 7251");
    server.start();

    HttpExchange exchange = new HttpExchange();

    MyHttpHandler handler = new MyHttpHandler();
    //handler.handleGETRequest(exchange);
    handler.handle(exchange);
  } // end main


  private class MyHttpHandler implements HttpHandler {
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

  class PausableThreadPoolExecutor extends ThreadPoolExecutor {
   private boolean isPaused;
   private ReentrantLock pauseLock = new ReentrantLock();
   private Condition unpaused = pauseLock.newCondition();

   protected void beforeExecute(Thread t, Runnable r) {
     super.beforeExecute(t, r);
     pauseLock.lock();
     try {
       while (isPaused) unpaused.await();
     } catch (InterruptedException ie) {
       t.interrupt();
     } finally {
       pauseLock.unlock();
     }
   }

   public void pause() {
     pauseLock.lock();
     try {
       isPaused = true;
     } finally {
       pauseLock.unlock();
     }
   }

   public void resume() {
     pauseLock.lock();
     try {
       isPaused = false;
       unpaused.signalAll();
     } finally {
       pauseLock.unlock();
     }
   }
 } // end PausableThreadPoolExecutor

}

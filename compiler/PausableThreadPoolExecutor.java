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

public class PausableThreadPoolExecutor extends ThreadPoolExecutor {
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

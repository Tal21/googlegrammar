import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Test {

  public static void main(String[] args) throws MalformedURLException, IOException {
    URL url = new URL("https://api-inference.huggingface.co/models/distilgpt2");

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.connect();

    // getting response code
    int responsecode = conn.getResponseCode();

    // if (responsecode != 200) {
    //   throw new RuntimeException("HttpResponseCode: " + responsecode);
    // }

    String inline = "";
    Scanner sc = new Scanner(url.openStream());

    while(sc.hasNext()) {
      inline += sc.nextLine();
    }

    sc.close();
  }
}

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import org.json.*;

class autocomp {


public static void main(String[] args) throws Exception {
  String url = "https://api-inference.huggingface.co/models/gpt2";  // example url which return json data
  String data = readUrl(url, "please tell me more details about your");

  //JSONObject json = new JSONObject(data);
  System.out.println(data);
}



private static String readUrl(String urlString, String postData) throws Exception {
    BufferedReader reader = null;
    String token = "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";
    try {
        URL url = new URL(urlString);
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        byte[] postDataBytes = postData.getBytes("UTF-8");

        conn.setRequestProperty("Authorization", "Bearer "+token);
        conn.setRequestMethod("POST");
        //conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        int read;
        char[] chars = new char[1024];
        while ((read = reader.read(chars)) != -1)
            buffer.append(chars, 0, read);

        return buffer.toString();
    } finally {
        if (reader != null)
            reader.close();
    }
}

}

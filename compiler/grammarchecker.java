package compiler;

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.charset.*;
import org.json.JSONTokener;
import org.json.JSONObject;
import org.json.JSONArray;

public class grammarchecker {

  public static void main(String[] args) throws Exception {
    System.out.println("input a sentence for grammarchecking or input 'exit' to exit:");
    Scanner in = new Scanner(System.in);
    String strIn = in.nextLine();
    String url = "https://api-inference.huggingface.co/models/vennify/t5-base-grammar-correction";  // example url which return json data

    boolean finished;
    while (strIn.equals("exit") == false) {
      String data = readUrl(url, strIn);
      String output = stringFullToken(strIn, data);
      JSONTokener tokener = new JSONTokener(data);
      JSONArray arr = new JSONArray(tokener);
      System.out.println(arr.getJSONObject(0).get("generated_text"));
      strIn = in.nextLine();
    }
    in.close();
  }

  public static String stringFullToken(String input, String data) {
    StringTokenizer tokendata = new StringTokenizer(data);
    StringTokenizer intok = new StringTokenizer(input);

    String printtoken = "";
    for (int i = 0; i <= tokendata.countTokens()+1; i++) {
      printtoken = printtoken + " " + tokendata.nextToken();
    }

    return printtoken;
  }

  // Post data: hello my name is greg

  public static String readUrl(String urlString, String postData) throws Exception {
      BufferedReader reader = null;
      String token = "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";
      try {
          URL url = new URL(urlString);
          HttpURLConnection conn= (HttpURLConnection) url.openConnection(); //connect to internet

          String apiInput = "{inputs: \"" + postData + "\"}";
          byte[] postDataBytes = apiInput.getBytes("UTF-8"); //encoding

          conn.setRequestProperty("Authorization", "Bearer " + token); //authorization token
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

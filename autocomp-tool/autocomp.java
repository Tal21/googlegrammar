import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.charset.*;
import org.json.JSONTokener;
import org.json.JSONObject;
import org.json.JSONArray;
//import javax.json.stream;

class autocomp {


  public static void main(String[] args) throws Exception {
      System.out.println("input a sentence for autocompletion or input 'exit' to exit:");
      Scanner in = new Scanner(System.in);
      String strIn = in.nextLine();
      String url = "https://api-inference.huggingface.co/models/gpt2";  // example url which return json data

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

  //hello returns a link
  public static String stringFullToken(String input, String data){
    StringTokenizer tokendata = new StringTokenizer(data);
    StringTokenizer intok = new StringTokenizer(input);
    /*
    int incount = 0;
    while(intok.hasMoreTokens()){
      String h = intok.nextToken(); //what is h for?
      incount++;
    }
    */

    String printtoken = "";
    for(int i = 0; i <= tokendata.countTokens()+1; i++){
      printtoken = printtoken + " " + tokendata.nextToken();
    }
    /*
    String nex = tokendata.nextToken();
    while (!nex.equals(",")||!nex.equals(".")||!nex.equals(";")||tokendata.hasMoreTokens()){
        printtoken = printtoken+  " " + nex;
    }
    */
    return printtoken;
  }




  private static String readUrl(String urlString, String postData) throws Exception {
      BufferedReader reader = null;
      String token = "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";
      try {
          URL url = new URL(urlString);
          HttpURLConnection conn= (HttpURLConnection) url.openConnection(); //connect to internet
          byte[] postDataBytes = postData.getBytes("UTF-8"); //encoding

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

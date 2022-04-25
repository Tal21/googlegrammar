
import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.charset.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
//import org.json.JSONObject;
//import javax.json.stream;

class autocomp {


public static void main(String[] args) throws Exception {
  /*
  String url = "https://api-inference.huggingface.co/models/gpt2";  // example url which return json data
  String data = readUrl(url, "please tell me more details about your");

  JSONObject json = new JSONObject(data);
  System.out.println(data);
*/

    System.out.println("input a sentence for autocompletion or input exit to exit");
    Scanner in = new Scanner(System.in);
    String strIn = in.nextLine();
    String url = "https://api-inference.huggingface.co/models/gpt2";  // example url which return json data
    String data = readUrl(url, strIn);
    System.out.println(stringFullToken(strIn, data));
    boolean finished;
    while (strIn.equals("exit") == false) {
    //  Scanner in = new Scanner(System.in);
      // System.out.println("input a sentence for autocompletion or input exit to exit");
      url = "https://api-inference.huggingface.co/models/gpt2";  // example url which return json data
      data = readUrl(url, strIn);
      String output = stringFullToken(strIn, data);
      // Gson g = new Gson();
      // JSONObject jsobj = g.toJSON(output);
      //
      System.out.println(output);
      JSONParser parser = new JSONParser();
      JSONObject json = (JSONObject) parser.parse(output);
      System.out.println("test");
      System.out.println(stringOneToken(json, strIn, data));

    //  System.out.println(json.stringify());

      // JSONObject j = new JSONObject();
      // JSONValue v = new JSONValue(
      // Object obj = v.parse(output);
      // JSONObject jsonObject = (JSONObject) obj;
      strIn = in.nextLine();
      //
      // String parsed = (String) jsonObject.get("generated_text");
      // System.out.println(parsed);

    }
    in.close();
    // StringTokenizer tokendata = new StringTokenizer(data);
    // StringTokenizer intok = new StringTokenizer(strIn);
    // int incount = 0;
    // while(intok.hasMoreTokens()){
    //   String h = intok.nextToken();
    //   incount++;
    // }
    //
    // String printtoken = "";
    // int i = 0;
    // while(i < incount + 1){
    //   printtoken = printtoken+  " " + tokendata.nextToken();
    //   i++;
    // }

    //JSONObject json = new JSONObject(data);
    //System.out.println(createSentenceToken(strIn, data));

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
  for(int i = 0; i < tokendata.countTokens(); i++){
    printtoken = printtoken+  " " + tokendata.nextToken();
  }
  /*
  String nex = tokendata.nextToken();
  while (!nex.equals(",")||!nex.equals(".")||!nex.equals(";")||tokendata.hasMoreTokens()){
      printtoken = printtoken+  " " + nex;
  }
  */
  return printtoken;
}

public static String stringOneToken(JSONObject output, String input, String data){
  StringTokenizer tokendata = new StringTokenizer(data);
  StringTokenizer intok = new StringTokenizer(input);

  String out = output.toString();

  String[] tokens = out.split("\\s+");

  String printToken = "";
  int temp = 0;
  for (int i = 0; i < intok.countTokens(); i++){
    printToken = printToken + tokens[i] + " ";
    temp  = i;
  }
  printToken = printToken + tokens[temp+1];
  return printToken;
}

public static String stringSentenceToken(JSONObject output, String input, String data){
  StringTokenizer tokendata = new StringTokenizer(data);
  StringTokenizer intok = new StringTokenizer(input);
  /*
  int incount = 0;
  while(intok.hasMoreTokens()){
    String h = intok.nextToken(); //what is h for?
    incount++;
  }
  */
  String out = output.toString();

  String[] tokens = out.split("\\s+");

  String printToken = "";
  int temp = 0;
  for (int i = 0; i < intok.countTokens(); i++){
    printToken = printToken + tokens[i] + " ";
    temp = i;
  }
  printToken = printToken + tokens[temp+1];

  String nex = tokendata.nextToken();
  int i = intok.countTokens();
  while (tokens[i].indexOf(".") < 0 && tokens[i].indexOf("!") < 0 && tokens[i].indexOf("?") < 0){
    printToken = printToken + tokens[i] + " ";
  }

  return printToken;
}


private static String readUrl(String urlString, String postData) throws Exception {
    BufferedReader reader = null;
    String token = "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";
    try {
        URL url = new URL(urlString);
        HttpURLConnection conn= (HttpURLConnection) url.openConnection(); //connect to internet
        byte[] postDataBytes = postData.getBytes("UTF-8"); //encoding

        conn.setRequestProperty("Authorization", "Bearer "+token); //authorization token
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

public class jsonAuto{

  public static void main(String[] args){

    System.out.println("input a sentence for autocompletion or input exit to exit");
    Scanner in = new Scanner(System.in);
    String strIn = in.nextLine();
    String url = "https://api-inference.huggingface.co/models/gpt2";  // example url which return json data
    String data = readUrl(url, strIn);
    System.out.println(data);
    boolean finished;
    while (strIn.equals("exit") == false) {
      url = "https://api-inference.huggingface.co/models/gpt2";  // example url which return json data
      data = readUrl(url, strIn);
      String output = stringFullToken(strIn, data);
      System.out.println(data);
      JSONParser parser = new JSONParser();
      System.out.println(parser.parse(data));
      System.out.println("test");
      strIn = in.nextLine();

    }
    in.close();
  }


  public static String jsonSentenceParser(StringBuffer input){
    
    return "see other doc";
  }


  private static StringBuffer readUrl(String urlString, String postData) throws Exception {
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

          return buffer;//buffer.toString();

      } finally {
          if (reader != null)
              reader.close();
      }
  }

}

public used{
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

  //method with JSON string buffer Object

  public static String jsonSentenceParser(StringBuffer input){
    return "see other doc";
  }
}

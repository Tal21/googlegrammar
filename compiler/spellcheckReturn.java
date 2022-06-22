import java.util.*;
import java.io.*;

public class spellcheckReturn {

  static int errorCount = 0;
  static ArrayList<String> similarWords = new ArrayList<String>();

  public static void main(String[] args) {
    String[] words = readDictionary("words.txt");
    System.out.println(spellCheck("thankd yu for alwayz being here", words));
    System.out.println(similarWord("thankd", words));
  }

  public static ArrayList<String> spellCheck(String input, String[] dictionary) {
    String check = "";
    boolean errors = false;
    ArrayList<String> incorrectWords = new ArrayList<String>();

    Scanner sc = new Scanner(input);
    sc.useDelimiter("\\s+");

    while(sc.hasNext()) {
      check = sc.next();

      if (!checkWord(check, dictionary)) {
        incorrectWords.add(check);
      }
    }
    return incorrectWords;
  }

  public static boolean checkWord(String input, String[] dictionary) {
    boolean checked = false;
    int i = 0;

    while(!checked && (i < dictionary.length)) {
      if (input.trim().equalsIgnoreCase(dictionary[i])) {
        checked = true;
      }
      i++;
    }
    return checked;
  }

  public static String[] readDictionary(String dictionary) {
    ArrayList<String> rec = new ArrayList<String>();

    try {
      Scanner sc = new Scanner(new File(dictionary));
      sc.useDelimiter("\n");

      while (sc.hasNext()) {
        rec.add(sc.next());
      }
    }
    catch (Exception e) {
      System.out.println(e);
    }

    String[] recArr = new String[rec.size()];
    recArr = rec.toArray(recArr);

    return recArr;
  }

  public static String similarWord(String word, String[] dictionary) {
    String[] threeWords = new String[3];
    String ret = "";

    for (String s : dictionary) {
      if (s.length() == word.length()) {
        int difference = checkDifference(s, word);
        if (difference <= 1) {
          // checks if AL is empty
          if (similarWords.isEmpty()) {
            similarWords.add(s);
          }
          else {
            similarWords.add(s);
          }
        }
      }
    }
    if (similarWords.size() >= 3) {
      for (int i = 0; i < threeWords.length; i++) {
        threeWords[i] = similarWords.get(i);
      }
    }
    else {
      for (int i = 0; i < similarWords.size(); i++) {
        threeWords[i] = similarWords.get(i);
      }
    }
    ret = threeWords[0] + ", " + threeWords[1] + ", " + threeWords[2];
    return ret;
  }

  public static int checkDifference(String s, String word) {
    int difference = 0;
    for (int i = 0; i < word.length(); i++) {
      if (s.charAt(i) != word.charAt(i)) {
        difference++;
      }
    }
    return difference;
  }

}

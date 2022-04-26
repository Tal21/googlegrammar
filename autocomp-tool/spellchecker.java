import java.util.*;
import java.io.*;

public class spellchecker {

  static int errorCount = 0;

  public static void main(String[] args) {
    String[] words = readDictionary("words.txt");

    System.out.println("Input a sentence for spellchecking or input 'exit' to exit:");
    Scanner in = new Scanner(System.in);
    String strIn = in.nextLine();

    while (strIn.equals("exit") == false) {
      if (spellCheck(strIn, words) == 0) {
        System.out.println("No spelling errors!");
        System.out.println("\nInput a sentence for spellchecking or input 'exit' to exit:");
        strIn = in.nextLine();
      }
      else {
        System.out.println("Number of incorrectly spelled words: " + errorCount);
        System.out.println("\nInput a sentence for spellchecking or input 'exit' to exit:");
        strIn = in.nextLine();
      }
    }

    in.close();
  }

  public static int spellCheck(String input, String[] dictionary) {
    String check = "";
    boolean errors = false;

    Scanner sc = new Scanner(input);
    sc.useDelimiter("\\s+");

    while(sc.hasNext()) {
      check = sc.next();

      if (!checkWord(check, dictionary)) {
        System.out.println("\n" + check + " is spelled incorrectly!");
        errorCount++;
        //errors = true;

        System.out.println("\nWould you like to add " + check + " to the dictionary? (yes/no)");
        Scanner in = new Scanner(System.in);
        String strIn = in.nextLine();

        if (strIn.toLowerCase().equals("yes") || strIn.toLowerCase().equals("y")) {
          try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("words.txt", true));
            writer.write(check);
            writer.close();
            errorCount--;
          }
          catch (IOException e) {
            System.out.println(e);
          }
        }
        else {
          System.out.println("\nThe word was not added.");
        }
      }
    }
    /*if (errorCount == 0) {
      errors = false;
    }*/
    return errorCount;
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

}

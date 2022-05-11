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

        String[] threeWords = similarWord(input, dictionary);
        System.out.println("Did you mean: " + threeWords[0] + "[1], " + threeWords[1] + "[2], or " + threeWords[2] + "[3]? (no/1/2/3)");
        Scanner in = new Scanner(System.in);
        String strIn = in.nextLine();
        if (strIn.equals("1")) {
          check = threeWords[0];
          errorCount--;
        }
        else if (strIn.equals("2")) {
          check = threeWords[1];
          errorCount--;
        }
        else if (strIn.equals("3")) {
          check = threeWords[2];
          errorCount--;
        }
        else {
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
            //put in closest word
          }
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

  public static String closestWord(String word){
    /* bad method
      loop through dictionary for words with same Length
      for each word with same Length
      go through each character and calculate letter difference
      if letter difference count is greater than carrying variable replace.

    */
    return "";
  }

  public static String[] similarWord(String word, String[] dictionary) {
    ArrayList<String> similarWords = new ArrayList<String>();
    String[] threeWords = new String[3];

    for (String s : dictionary) {
      if (s.length() == word.length()) {
        int difference = checkDifference(s, word);
        if (difference <= 3) {
          if (similarWords.isEmpty()) {
            similarWords.add(s);
          }
          else {
            for (int i = 0; i <= similarWords.size(); i++) {
              int diffAtI = checkDifference(similarWords.get(i), word);
              if (difference <= diffAtI) {
                similarWords.add(i, s);
              }
            }
          } // end else
        } // end second if
      } // end first if
    } // end main for loop
    for (int i = 0; i < threeWords.length; i++) {
      threeWords[i] = similarWords.get(i);
    }
    return threeWords;
  } // end method

  public static int checkDifference(String s, String word) {
    int difference = 0;

    for (int i = 0; i < word.length(); i++) {
      if (s.charAt(i) != word.charAt(i)) {
        difference++;
      }
    }
    return difference;
  }

    // List<String> words = myList.stream()
    //                      .filter(s -> s.contains(word)
    //                      .collect(Collectors.toList()));
  }

}

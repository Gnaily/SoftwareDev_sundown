/**
 * Class for Running the Xyes printing programs
 */
public class Xyes {

  public static void main(String[] args) {
    boolean limit = args.length > 0 && args[0].equals("-limit");
    String wordToPrint = getWordToPrint(args);

    int count = 0;

    while (!limit || count < 20) {
      System.out.println(wordToPrint);
      count++;
    }

  }

  /**
   * Gets the word to print out to the command line
   *
   * @param args Command line arguments passed in to the function
   * @return The word to print
   */
  public static String getWordToPrint(String[] args) {
    String word = "";
    for (int i = 0; i < args.length; i++) {
      if (i == 0 && args[i].equals("-limit")) {
        continue;
      } else {
        word = word + args[i] + " ";
      }
    }

    if (word.equals("")) {
      return "hello world";
    }

    return word.substring(0, word.length() - 1);
  }

}

/**
 *
 */
public class Xyes {

  public static void main(String[] args) {
    String wordToPrint = "hello world";
    boolean limit = false;



    int count = 0;

    while (!limit || count < 20) {
      System.out.println(wordToPrint);
      count++;
    }

  }

  public static boolean shouldLimit(String limit) {
    return limit.equals("-limit");
  }

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

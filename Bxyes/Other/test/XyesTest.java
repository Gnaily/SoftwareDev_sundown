import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XyesTest {

  @Test
  public void testGetWord() {
    assertEquals("hello world", Xyes.getWordToPrint(new String[] {}));
    assertEquals("hello world", Xyes.getWordToPrint(new String[] {"-limit"}));
    assertEquals("apple", Xyes.getWordToPrint(new String[] {"apple"}));
    assertEquals("apple orange banana", Xyes.getWordToPrint(new String[] {"apple", "orange", "banana"}));
    assertEquals("pineapple", Xyes.getWordToPrint(new String[] {"-limit", "pineapple"}));
  }
}
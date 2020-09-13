import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XyesTest {

  @Test
  public void shouldLimit() {
    assertTrue(Xyes.shouldLimit("-limit"));
    assertFalse(Xyes.shouldLimit("hello"));
  }

  @Test
  public void testGetWord() {
    assertEquals("hello world", Xyes.getWordToPrint(new String[] {}));
    assertEquals("hello world", Xyes.getWordToPrint(new String[] {"-limit"}));
    assertEquals("apple", Xyes.getWordToPrint(new String[] {"apple"}));
    assertEquals("apple orange banana", Xyes.getWordToPrint(new String[] {"apple", "orange", "banana"}));
    assertEquals("pineapple", Xyes.getWordToPrint(new String[] {"-limit", "pineapple"}));
  }
}
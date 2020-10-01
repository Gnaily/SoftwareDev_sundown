import com.github.stefanbirkner.systemlambda.SystemLambda;

import org.junit.Test;

import static org.junit.Assert.*;

public class TCPConnectionTest {

  @Test
  public void processCliArgsString() throws Exception {
    int status = SystemLambda.catchSystemExit(() -> {
        TCPConnection.processCliArgs(new String[]{"hello"});
      });
    assertEquals(1, status);
  }

  @Test
  public void processCliArgsTwoArguments() throws Exception {
    int status = SystemLambda.catchSystemExit(() -> {
      TCPConnection.processCliArgs(new String[]{"1", "2"});
    });
    assertEquals(1, status);
  }

  @Test
  public void processCliArgsNegative() throws Exception {
    int status = SystemLambda.catchSystemExit(() -> {
      TCPConnection.processCliArgs(new String[]{"-1"});
    });
    assertEquals(1, status);
  }

  @Test
  public void processCliArgsGood() throws Exception {
    int port = TCPConnection.processCliArgs(new String[]{"100"});
    assertEquals(100, port);
  }
}
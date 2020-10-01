import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class for handling a TCP connection and returning JSON
 */
public class TCPConnection {
  public static final int TIMEOUT = 3000;
  public static final int DEFAULTPORT = 4567;

  public static void main(String[] args) throws IOException {
    int port = processCliArgs(args);
    // open network for incoming message
    ServerSocket serverSocket = new ServerSocket(port);
    serverSocket.setSoTimeout(TIMEOUT);
    Socket socket = serverSocket.accept();
    InputStream inputStream = socket.getInputStream();
    Scanner scan = new Scanner(inputStream);

    // parse json
    XJson json = new XJson();
    json.processInput(scan);
    String output = json.formatJson();

    // send message back & end program
    OutputStream outputStream = socket.getOutputStream();
    outputStream.write(output.getBytes());
    serverSocket.close();
    socket.close();
  }

  /**
   * Processes the command line arguments. If no arguments are given, use default port number.
   * @param args command line arguments containing the port number or empty to use default
   * @return the port number to connect to
   */
  public static int processCliArgs(String[] args) {
    int port = DEFAULTPORT;
    if (args.length > 1) {
      System.out.println("usage: ./xtcp positive-integer");
      System.exit(1);
    }
    if (args.length == 1) {
      try {
        port = Integer.parseInt(args[0]);
        if (port <= 0) {
          throw new IllegalArgumentException("number cannot be negative");
        }
      }
      catch (Exception e) {
        // number passed in was not an integer
        System.out.println("usage: ./xtcp positive-integer");
        System.exit(1);
      }
    }
    return port;
  }
}

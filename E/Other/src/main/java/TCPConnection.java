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

  public static void main(String[] args) throws IOException {

    // handle determining the port to open
    int port = 4567;
    if (args.length > 1) {
      // passed in more than one command line argument
      System.out.println("usage: ./xtcp positive-integer");
      return;
    }
    if (args.length == 1) {
      try {
        port = Integer.parseInt(args[0]);
      }
      catch (Exception e) {
        // number passed in was not an integer
        System.out.println("usage: ./xtcp positive-integer");
        return;
      }
    }

    // open network for incoming message
    ServerSocket serverSocket = new ServerSocket(port);
    serverSocket.setSoTimeout(3000);
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
}

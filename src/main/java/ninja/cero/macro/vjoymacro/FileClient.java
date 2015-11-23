package ninja.cero.macro.vjoymacro;

import java.io.*;
import java.net.Socket;

public class FileClient {
    public static void main(String[] args) throws IOException {
        String hostName = "10.211.55.3";
        int portNumber = 11234;

        System.out.println("Start.");
        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                InputStream in = FileClient.class.getResourceAsStream("/examples/yang88.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ) {
            System.out.println("Connection established.");
            String line = reader.readLine();
            while (line != null) {
                out.println(line);
                line = reader.readLine();
            }
        }
    }
}

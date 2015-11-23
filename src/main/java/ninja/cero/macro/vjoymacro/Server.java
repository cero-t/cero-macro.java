package ninja.cero.macro.vjoymacro;

import jnr.ffi.LibraryLoader;
import ninja.cero.macro.vjoymacro.converter.Converter;
import ninja.cero.macro.vjoymacro.entity.Operation;
import ninja.cero.macro.vjoymacro.processor.OperationProcessor;
import ninja.cero.macro.vjoymacro.vjoy.VjoyAccessor;
import ninja.cero.macro.vjoymacro.vjoy.VjoyInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public void run() throws InterruptedException, IOException {
        VjoyInterface vjoyInterface = LibraryLoader.create(VjoyInterface.class).load("vJoyInterface.dll");
        if (!vjoyInterface.vJoyEnabled()) {
            throw new RuntimeException("vJoy not enabled");
        }

        int portNumber = 11234;

        try (VjoyAccessor vjoyAccessor = new VjoyAccessor(vjoyInterface, 1);
             ServerSocket serverSocket = new ServerSocket(portNumber)) {
            vjoyAccessor.init();
            OperationProcessor processor = new OperationProcessor(vjoyAccessor);
            Converter converter = new Converter();

            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    List<String> lines = new ArrayList<>();
                    String line = in.readLine();
                    while (line != null) {
                        lines.add(line);
                        line = in.readLine();
                    }

                    if (lines.size() == 1 && lines.get(0).equals("exit")) {
                        break;
                    }

                    List<Operation> operations = converter.linesToOperations(lines);
                    System.gc();
                    Thread.sleep(1000);
                    processor.process(operations);
                }
            }
        }
    }
}

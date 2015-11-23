package ninja.cero.macro.vjoymacro;

import jnr.ffi.LibraryLoader;
import ninja.cero.macro.vjoymacro.converter.Converter;
import ninja.cero.macro.vjoymacro.entity.Operation;
import ninja.cero.macro.vjoymacro.processor.OperationProcessor;
import ninja.cero.macro.vjoymacro.vjoy.VjoyAccessor;
import ninja.cero.macro.vjoymacro.vjoy.VjoyInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Text {
    public void run(String path) throws IOException, InterruptedException {
        VjoyInterface vjoyInterface = LibraryLoader.create(VjoyInterface.class).load("vJoyInterface.dll");
        if (!vjoyInterface.vJoyEnabled()) {
            throw new RuntimeException("vJoy not enabled");
        }

        try (VjoyAccessor vjoyAccessor = new VjoyAccessor(vjoyInterface, 1)) {
            vjoyAccessor.init();

            OperationProcessor processor = new OperationProcessor(vjoyAccessor);
            Converter converter = new Converter();

            Scanner scanner = new Scanner(System.in);
            while (scanner.nextLine().length() > 0) {
                List<String> lines = Files.readAllLines(Paths.get(path));
                List<Operation> operations = converter.linesToOperations(lines);

                System.gc();
                Thread.sleep(1000);
                processor.process(operations);
            }
        }

    }
}

package ninja.cero.macro.vjoymacro;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
            new Server().run();
        } else if (args.length > 0) {
            new Text().run(args[0]);
        } else {
            new Text().run("./examples/zangief.txt");
        }
    }
}

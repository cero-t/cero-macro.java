package ninja.cero.macro.vjoymacro.processor;

import ninja.cero.macro.vjoymacro.entity.Operation;
import ninja.cero.macro.vjoymacro.vjoy.VjoyAccessor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OperationProcessor {
    static final long frame = 1000_000_000 / 60;

    protected VjoyAccessor vjoyAccessor;

    public OperationProcessor(VjoyAccessor vjoyAccessor) {
        this.vjoyAccessor = vjoyAccessor;
    }

    public void process(List<Operation> operations) throws InterruptedException {
        List<String> log = new LinkedList<>();

        long delta = 0;
        long afterLastSleep = System.nanoTime();

        for (Operation operation : operations) {
            processOperation(operation);

            // adjust 60fps
            long beforeSleep = System.nanoTime();
            long sleepTime = operation.frames * frame - (beforeSleep - afterLastSleep) - delta;

            // System.out.println("adjust : " + sleepTime / 1000_000);
            TimeUnit.NANOSECONDS.sleep(sleepTime);
            afterLastSleep = System.nanoTime();
            delta = afterLastSleep - beforeSleep - sleepTime;
            // System.out.println("delta : " + delta);
        }

        vjoyAccessor.release("lp");
        vjoyAccessor.release("mp");
        vjoyAccessor.release("hp");
        vjoyAccessor.release("lk");
        vjoyAccessor.release("mk");
        vjoyAccessor.release("hk");
        vjoyAccessor.push("5");
    }

    private void processOperation(Operation operation) {
        if (operation.direction != 0) {
            vjoyAccessor.push(String.valueOf(operation.direction));
        }

        if (operation.lp != 0) {
            if (operation.lp == 1) {
                vjoyAccessor.push("lp");
            } else {
                vjoyAccessor.release("lp");
            }
        }

        if (operation.mp != 0) {
            if (operation.mp == 1) {
                vjoyAccessor.push("mp");
            } else {
                vjoyAccessor.release("mp");
            }
        }

        if (operation.hp != 0) {
            if (operation.hp == 1) {
                vjoyAccessor.push("hp");
            } else {
                vjoyAccessor.release("hp");
            }
        }

        if (operation.lk != 0) {
            if (operation.lk == 1) {
                vjoyAccessor.push("lk");
            } else {
                vjoyAccessor.release("lk");
            }
        }

        if (operation.mk != 0) {
            if (operation.mk == 1) {
                vjoyAccessor.push("mk");
            } else {
                vjoyAccessor.release("mk");
            }
        }

        if (operation.hk != 0) {
            if (operation.hk == 1) {
                vjoyAccessor.push("hk");
            } else {
                vjoyAccessor.release("hk");
            }
        }
    }
}

package ninja.cero.macro.vjoymacro.converter;

import ninja.cero.macro.vjoymacro.entity.Operation;
import ninja.cero.macro.vjoymacro.entity.State;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    public List<Operation> linesToOperations(List<String> lines) {
        List<State> states = linesToStates(lines);
        List<Operation> operations = statesToOperations(states);
        return operations;
    }

    protected List<State> linesToStates(List<String> lines) {
        List<State> states = new ArrayList<>();

        for (String line : lines) {
            states.add(lineToState(line));
        }

        return states;
    }

    protected State lineToState(String line) {
        State state = new State();

        String[] pair = line.split("\\s+");
        String[] keys = pair[0].split(",");
        state.frames = Integer.parseInt(pair[1]);

        for (String key : keys) {
            if (key.replaceAll("[a-z]", "").length() > 0) {
                state.direction = Integer.parseInt(key);
            } else if (key.equals("lp")) {
                state.lp = true;
            } else if (key.equals("mp")) {
                state.mp = true;
            } else if (key.equals("hp")) {
                state.hp = true;
            } else if (key.equals("lk")) {
                state.lk = true;
            } else if (key.equals("mk")) {
                state.mk = true;
            } else if (key.equals("hk")) {
                state.hk = true;
            }
        }

        return state;
    }

    protected List<Operation> statesToOperations(List<State> states) {
        State lastState = new State();

        List<Operation> operations = new ArrayList<>();
        for (State state : states) {
            Operation operation = new Operation();

            if (lastState.direction != state.direction) {
                operation.direction = state.direction;
            }
            if (lastState.lp != state.lp) {
                operation.lp = state.lp ? 1 : -1;
            }
            if (lastState.mp != state.mp) {
                operation.mp = state.mp ? 1 : -1;
            }
            if (lastState.hp != state.hp) {
                operation.hp = state.hp ? 1 : -1;
            }
            if (lastState.lk != state.lk) {
                operation.lk = state.lk ? 1 : -1;
            }
            if (lastState.mk != state.mk) {
                operation.mk = state.mk ? 1 : -1;
            }
            if (lastState.hk != state.hk) {
                operation.hk = state.hk ? 1 : -1;
            }

            operations.add(operation);
            operation.frames = state.frames;

            lastState = state;
        }

        return operations;
    }
}

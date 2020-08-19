import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class Pair<T, U> {
    T state;
    U position;

    Pair(T state, U position) {
        this.state = state;
        this.position = position;
    }
}

public class Machine {

    private String transitionsFileName;
    private String inputFileName;
    private List<String> lines;
    private Multimap<Character, int[]> transitions = ArrayListMultimap.create();
    private List<Integer> finalStates = new ArrayList<>();
    private Stack<Pair<Integer, Integer>> currentStates;

    private Machine(String transitionsFileName, String inputFileName) {
        this.transitionsFileName = transitionsFileName;
        this.inputFileName = inputFileName;
    }

    private void init() throws IOException {
        lines = Files.readAllLines(Paths.get(transitionsFileName), StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); ++i) {
            if (i < lines.size() - 1) {
                int from = Character.getNumericValue(lines.get(i).charAt(0));
                char symbol = lines.get(i).charAt(1);
                int to = Character.getNumericValue(lines.get(i).charAt(2));
                transitions.put(symbol, new int[]{from, to});
            } else {
                String[] states = lines.get(i).split("\\s+");
                for (String state : states) {
                    finalStates.add(Integer.parseInt(state));
                }
            }
        }
        lines = Files.readAllLines(Paths.get(inputFileName), StandardCharsets.UTF_8);
    }

    private void transit(int from, char sym, int pos) {
        if (transitions.containsKey(sym)) {
            Collection<int[]> values = transitions.get(sym);
            for (int[] value : values) {
                if (value[0] == from)
                    currentStates.push(new Pair<>(value[1], pos + 1));
            }
        }
    }

    private boolean check() {
        currentStates = new Stack<>();
        currentStates.push(new Pair<>(0, 0));
        for (String line : lines) {
            int len = line.length();
            if (len < 256) {
                while (!currentStates.isEmpty()) {
                    Pair<Integer, Integer> currentState = currentStates.pop();
                        if (currentState.position < len)
                            transit(currentState.state, line.charAt(currentState.position), currentState.position);
                        else if (finalStates.contains(currentState.state))
                            return true;
                }
            }
            else {
                Field field = null;
                try {
                    field = String.class.getDeclaredField("value");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                assert field != null;
                field.setAccessible(true);
                try {
                    final char[] chars = (char[]) field.get(line);
                    while (!currentStates.isEmpty()) {
                        Pair<Integer, Integer> currentState = currentStates.pop();
                        if (currentState.position < len)
                            transit(currentState.state, chars[currentState.position], currentState.position);
                        else if (finalStates.contains(currentState.state))
                            return true;
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Machine machine = new Machine("delta.txt", "input.txt");
        try {
            machine.init();
            System.out.println(machine.check());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


package nsu.g16203.grigorovich;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class MyContext {
    Stack<String> stack = new Stack<>();
    Map<String, Double> defs = new HashMap<>();

    public String replace(String var) {
        double temp = 0.0;
        try {
            temp = Double.parseDouble(var);
        } catch (NumberFormatException e) {
            for (Map.Entry entry : defs.entrySet() ) {
                if (entry.getKey().equals(var))
                    temp = Double.parseDouble(entry.getValue().toString());
                else throw new IllegalArgumentException("Unknown variable");
            }
        }
        return String.valueOf(temp);
    }
}

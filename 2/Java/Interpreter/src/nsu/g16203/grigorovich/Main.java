package nsu.g16203.grigorovich;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        OperationMaker om = new OperationMaker();
        MyContext context = new MyContext();
        om.init();
        String str = main.streamToString().replaceAll("[\\s]+", " ");
        String[] arg = str.split(" " );
        for (int j = 0; j < arg.length; j++) {
            if (arg[j].chars().allMatch(Character::isAlphabetic))
                arg[j] = arg[j].substring(0,1).toUpperCase() + arg[j].substring(1).toLowerCase();
        }
        main.interpret(arg, context, om, "42", 0);
    }

    public int interpret(String[] arg, MyContext context, OperationMaker om, String topElement, int position) {
        boolean f = false;
        if (!topElement.equals("0.0") && !topElement.equals("0")) {
            for (int i = position; i < arg.length; ++i) {
                if (arg[i].chars().allMatch(Character::isDigit) || (arg[i].chars().anyMatch(Character::isDigit) && arg[i].contains("."))) {
                    context.stack.push(arg[i]);
                    continue;
                }
                if (arg[i].equals("[")) {
                    int q = i;
                    while (!context.stack.peek().equals("0.0") && !context.stack.peek().equals("0")) {
                        f = true;
                        q = interpret(arg, context, om, context.stack.pop(), i + 1);
                    }
                    if(!f) {
                        for (;q < arg.length; ++q)
                            if (arg[q].equals("]")) {
                                i = q ;
                                context.stack.pop();
                                f = false;
                                break;
                            }
                            continue;
                    }
                    i = q + 1;
                    context.stack.pop();
                    f = false;
                    continue;
                }
                if (arg[i].equals("]")) {
                    return i - 1;
                }
                if (arg[i].equals("PUSH")) {
                    Operation op = om.createOperation(arg[i]);
                    ++i;
                    context = op.exec(context, Double.parseDouble(context.replace(arg[i])));
                    continue;
                }
                if (arg[i].equals("DEFINE")) {
                    Operation op = om.createOperation(arg[i]);
                    ++i;
                    context.stack.push(arg[i]);
                    ++i;
                    context.stack.push(arg[i]);
                    context = op.exec(context);
                    continue;
                }
                if (arg[i].length() == 1 && arg[i].chars().allMatch(Character::isLetter)) {
                    context.stack.push(context.replace(arg[i]));
                } else {
                    Operation op = om.createOperation(arg[i]);
                    context = op.exec(context);
                }
            }
        }
        return 0;
    }

    public String streamToString() {
        StringBuilder text = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        String temp;
        while (sc.hasNext()) {
//        while ((temp = sc.nextLine()).length() > 0){
            temp = sc.nextLine();
            text.append(temp).append(" ");
        }
        text.deleteCharAt(text.length()-1);
        return text.toString();
    }
}

package nsu.g16203.grigorovich;

public class Compare implements Operation {
    @Override
    public MyContext exec(MyContext context, double... b) {
        double arg_a, arg_b;
        arg_b = Double.parseDouble(context.stack.pop());
        arg_a = Double.parseDouble(context.stack.pop());
        if (arg_a < arg_b)
            context.stack.push("1.0");
        else
            context.stack.push("0.0");
        return context;
    }
}

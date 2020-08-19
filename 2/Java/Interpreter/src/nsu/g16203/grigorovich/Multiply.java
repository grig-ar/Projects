package nsu.g16203.grigorovich;

class Multiply implements Operation {
    @Override
    public MyContext exec(MyContext context, double... b) {
        double arg_a, arg_b;
        arg_b = Double.parseDouble(context.stack.pop());
        arg_a = Double.parseDouble(context.stack.pop());
        context.stack.push(String.valueOf(arg_a * arg_b));
        return context;
    }
}

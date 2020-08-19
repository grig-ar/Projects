package nsu.g16203.grigorovich;

class SqRoot implements Operation {

    @Override
    public MyContext exec(MyContext context, double... b) {
        double arg_b;
        arg_b = Double.parseDouble(context.stack.pop());
        if (arg_b < 0) throw new IllegalArgumentException("Negative value");
        context.stack.push(String.valueOf(Math.sqrt(arg_b)));
        return context;
    }
}

package nsu.g16203.grigorovich;

class Define implements Operation {

    @Override
    public MyContext exec(MyContext context, double... b) {
        double arg_b;
        String arg_a;
        arg_b = Double.parseDouble(context.stack.pop());
        arg_a = context.stack.pop();
        context.defs.put(arg_a, arg_b);
        return context;
    }
}

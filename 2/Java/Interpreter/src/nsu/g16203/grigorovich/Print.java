package nsu.g16203.grigorovich;

class Print implements Operation {

    @Override
    public MyContext exec(MyContext context, double... b) {
        double arg_b;
        arg_b = Double.parseDouble(context.stack.peek());
        System.out.println(arg_b);
        return context;
    }
}

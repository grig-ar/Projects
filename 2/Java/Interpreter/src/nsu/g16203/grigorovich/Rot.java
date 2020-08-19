package nsu.g16203.grigorovich;

class Rot implements Operation {

    @Override
    public MyContext exec(MyContext context, double... b) {
        String arg_a = context.stack.pop();
        String arg_b = context.stack.pop();
        String arg_c = context.stack.pop();
        context.stack.push(arg_b);
        context.stack.push(arg_a);
        context.stack.push(arg_c);
        return context;
    }
}

package nsu.g16203.grigorovich;

class Dup implements Operation {

    @Override
    public MyContext exec(MyContext context, double... b) {
        String arg_a = context.stack.pop();
        context.stack.push(arg_a);
        context.stack.push(arg_a);
        return context;
    }
}

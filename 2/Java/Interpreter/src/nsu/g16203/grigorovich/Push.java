package nsu.g16203.grigorovich;

class Push implements Operation {

    @Override
    public MyContext exec(MyContext context, double... b) {
        context.stack.push(String.valueOf(b[0]));
        return context;
    }
}

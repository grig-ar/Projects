package nsu.g16203.grigorovich;

class Drop implements Operation {

    @Override
    public MyContext exec(MyContext context, double... b) {
        context.stack.pop();
        return context;
    }
}

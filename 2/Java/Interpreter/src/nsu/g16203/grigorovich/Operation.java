package nsu.g16203.grigorovich;

interface Operation {
    MyContext exec(MyContext context, double... b);
}

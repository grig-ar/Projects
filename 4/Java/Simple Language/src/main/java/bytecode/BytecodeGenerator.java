package bytecode;

import langInterface.Block;

public class BytecodeGenerator {
    //private final String className;

//    public BytecodeGenerator(String className) {
//        //this.className = className;
//    }

    public byte[] generate(Block block) {
        //ClassDeclaration classDeclaration = compilationUnit.getClassDeclaration();
        ClassGenerator classGenerator = new ClassGenerator();
        return classGenerator.generate(block).toByteArray();
    }
}

package bytecode;

import langInterface.Block;

public class CompilationUnit {
    private final Block mainBlock;

    private final String className;

    public CompilationUnit(Block mainBlock, String className) {
        this.mainBlock = mainBlock;
        this.className = className;
    }

    public Block getMainBlock() {
        return mainBlock;
    }

    public String getClassName() {
        return className;
    }
}

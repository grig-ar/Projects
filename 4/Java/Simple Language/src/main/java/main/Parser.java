package main;

import bytecode.CompilationUnit;
import generated.SimpleLangLexer;
import generated.SimpleLangParser;
import listeners.SimpleLangTreeWalkErrorListener;
import org.antlr.v4.runtime.*;
import visitors.CompilationUnitVisitor;

import java.io.IOException;

public class Parser {
    CompilationUnit getCompilationUnit(String fileAbsolutePath) throws IOException {
        CharStream charStream = CharStreams.fromFileName(fileAbsolutePath);
        SimpleLangLexer lexer = new SimpleLangLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        SimpleLangParser parser = new SimpleLangParser(tokenStream);

        ANTLRErrorListener errorListener = new SimpleLangTreeWalkErrorListener();
        parser.addErrorListener(errorListener);
        CompilationUnitVisitor compilationUnitVisitor = new CompilationUnitVisitor();
        return parser.compilationUnit().accept(compilationUnitVisitor);
    }
}

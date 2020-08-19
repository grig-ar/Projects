grammar SimpleLang;

compilationUnit : block EOF ;
block : '{' statement* '}' ;

statement :  block
           | variableInitialization
           | variableDeclaration
           | assignment
           | printStatement
           | breakStatement
           | ifStatement
           | forStatement
           | expression ;

variableDeclaration : VARIABLE name primitiveType ;

variableInitialization : variableDeclaration EQUALS expression ;

assignment : name EQUALS expression ;

printStatement : PRINT expression ;

ifStatement :  'if'  ('(')? expression (')')? trueStatement=statement ('else' falseStatement=statement)? ;

breakStatement : BREAK ;

forStatement : 'for' forConditions statement ;

forConditions : iterator=variableReference 'in' startExpr=expression range='..' endExpr=expression ;

expression          :'(' expression ')'                         #parenthesisExpression
                    | <assoc=right> expression '^' expression   #powerExpression
                    | expression (ASTERISK|SLASH) expression    #mulDivExpression
                    | expression (PLUS|MINUS) expression        #addSubExpression
                    | value                                     #valueExpression
                    | variableReference                         #varReference
                    | expression cmp='>' expression             #conditionalExpression
                    | expression cmp='<' expression             #conditionalExpression
                    | expression cmp='==' expression            #conditionalExpression
                    | expression cmp='!=' expression            #conditionalExpression
                    | expression cmp='>=' expression            #conditionalExpression
                    | expression cmp='<=' expression            #conditionalExpression
                    | expression (OR|AND) expression            #complexExpression
                    ;

variableReference : ID ;

name : ID ;

primitiveType : 'string' | 'int' ;

value : NUMBER
      | STRING ;

fragment LETTER     : [a-zA-Z] ;
fragment DIGIT      : [0-9] ;
VARIABLE            : 'var' ;
ASTERISK            : '*' ;
SLASH               : '/' ;
PLUS                : '+' ;
MINUS               : '-' ;
AND                 : '&&' ;
OR                  : '||' ;
COMPARE             : '>' | '<' | '==' | '!=' | '>=' | '<=' ;
PRINT : 'show:' ;
BREAK : 'break' ;
EQUALS : '=' ;
NUMBER : [0-9]+ ;
STRING : '"'~('\r' | '\n' | '"')*'"' ;
ID : [a-zA-Z0-9]+ ;
WS: [ \t\n\r]+ -> skip ;
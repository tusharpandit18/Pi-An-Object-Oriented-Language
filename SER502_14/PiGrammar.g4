/**
 * Define a grammar called PiGrammar
 */
grammar PiGrammar;
program : (statement)+ ;

statement : ( print_statement 
			| assignment 
			| if_statement
			| loop_statement
			| declaration
			| exit_skip
			)+;
			
			
print_statement :  (   print_var
					 | print_num
					 | print_line
                    );
print_var : 'print' VAR K_DOT ;

print_num : 'print' DIGIT K_DOT ;

print_line : 'print'  LINE  K_DOT ;

assignment : number_assign | bool_assign | string_assign ;

number_assign : VAR AR_OP expression ;

bool_assign : VAR AR_OP K_TRUE_FALSE K_DOT;

string_assign : VAR AR_OP LINE K_DOT ;

expression : (   VAR K_DOT
			   | DIGIT K_DOT
			   | VAR AR_OP expression 
			   | DIGIT AR_OP expression
			  ) ;

if_statement : K_IF condition K_COLON (statement)* ( K_ENDIF | K_ELSE K_COLON (statement)* | K_ELSE if_statement )* ;

condition : (   VAR COMP_OP DIGIT 
			  | VAR COMP_OP VAR 
			);

loop_statement : K_WHILE condition K_COLON (statement)* K_ENDWHILE K_DOT;

declaration : (   declare
	            | declare_and_assign
               ) ;

declare : (   'number' VAR K_DOT 
			| 'boolean' VAR K_DOT 
			| 'string' VAR K_DOT
          );

declare_and_assign : (  number_declare_assign 
					  |  boolean_declare_assign 
					  |  string_declare_assign
                     ) ;
                     
number_declare_assign : 'number' number_assign ;

boolean_declare_assign : 'boolean' bool_assign ;			  

string_declare_assign : 'string' string_assign ;

exit_skip : ( 'exit' K_DOT  | 'skip' K_DOT ) ;

DATATYPE : (  'number' 
			| 'string'
			| 'boolean'
		   );

K_TRUE_FALSE:   ('true' | 'false') ;          
K_ENDIF :       'endif' ;
K_WHILE :       'while' ;
K_ENDWHILE :    'endwhile' ;
K_IF :          'if' ;
K_ELSE :        'else' ;
K_DOT :         '.' ;
K_COLON:        ':' ;
K_D_QUOTES :    '"' ;

COMP_OP : (  'EQ' 
		   | 'LT' 
		   | 'GT' 
		   | 'LTEQ' 
		   | 'GTEQ'
           ) ;

AR_OP : (  '=' 
		 | '+' 
		 | '-' 
		 | '*' 
		 | '/'
        );
         
DIGIT : [0-9]+ ;
VAR :   ('a'..'z')+ ;

LINE :  ('"'[[a-zA-Z0-9_ ]*'"');

WS :    [ \t\r\n]+ -> skip ; 

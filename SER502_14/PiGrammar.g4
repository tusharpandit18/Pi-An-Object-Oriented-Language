/**
 * Define a grammar called PiGrammar
 */
grammar PiGrammar;
r  : 'PiGrammar' ID ;

ID : [a-z]+ ;

WS : [ \t\r\n]+ -> skip ;


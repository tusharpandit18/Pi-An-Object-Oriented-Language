import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Executer {
	// Class to read in line input and generate parse tree
	static String parse_tree = "";
	static ArrayList<String> intermediate_code = new ArrayList<String>();
    public static void main( String[] args) throws Exception 
    {

        ANTLRInputStream input = new ANTLRInputStream( System.in);

        PiGrammarLexer lexer = new PiGrammarLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        PiGrammarParser parser = new PiGrammarParser(tokens);

        ParseTree tree = parser.program(); // begin parsing at rule 'r'
        
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree        
        parse_tree = tree.toStringTree(parser);
        
    }
}

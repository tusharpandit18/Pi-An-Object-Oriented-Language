import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
public class Executer 
{
	static String parse_tree = "";
	static ArrayList<String> intermediate_code = new ArrayList<String>();
	
	//static HashMap<String,String[]> symbol_table = new HashMap<String,String[]>(); 
    public static void main( String[] args) throws Exception 
    {
    	
    	//String fileName = "Fibonacci.txt";
//    	Scanner sc = new Scanner(System.in);
//    	String filename = sc.next();
    	String filename = "";
    	if (args.length > 0){
    		filename = args[0];
    	}
    	//System.out.println(args[0]);
        File file = new File(filename);
        FileInputStream fis = null;
        
        try {
        	fis = new FileInputStream(file);
        }
        catch (IOException e){
        	
        }
       //sc.close();
        //ANTLRInputStream input = new ANTLRInputStream( System.in);
        ANTLRInputStream input = new ANTLRInputStream(fis);

        PiGrammarLexer lexer = new PiGrammarLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        PiGrammarParser parser = new PiGrammarParser(tokens);

        ParseTree tree = parser.program(); // begin parsing at rule 'r'
        
        //System.out.println(tree.toStringTree(parser)); // print LISP-style tree        
        parse_tree = tree.toStringTree(parser);
        
//        // To generate intermediate code
        String classname = filename.substring(0, filename.length()-3) + "pi"  ;     
        IntermediateCodeGenerator icg = new IntermediateCodeGenerator();
        intermediate_code = icg.generate(parse_tree,classname);
        String [] int_code_array = new String [intermediate_code.size()];
        for (int i=0; i<int_code_array.length; i++){
        	int_code_array[i] = intermediate_code.get(i);
        }
        
//        Runtime rt = new Runtime();
//          System.out.println(" ***** Output******");
//        rt.run_code(int_code_array);
    }
}
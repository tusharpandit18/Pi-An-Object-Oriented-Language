import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


public class IntermediateCodeGenerator {
	
	//static HashMap<String,String[]> symbol_table = new HashMap<String,String[]>();
	public static void main(String[] args) {
	
	}
	public ArrayList<String> generate(String parseTree, String classname){
		
	HashMap<String,String> symbols = new HashMap<String,String>();
	symbols.put("+","ADDX");
	symbols.put("-","SUBX");
	symbols.put("/","DIVX");
	symbols.put("*","MULX");
	// Writer object to generate intermediate code
	
		
	//String parseTree = "(program (statement (declaration (declare number vara .)) (declaration (declare_and_assign (number_declare_assign number (number_assign varb = (expression 4 + (expression 5 + (expression a .))))))) (declaration (declare boolean c .)) (declaration (declare_and_assign (boolean_declare_assign boolean (bool_assign d = true .)))) (declaration (declare string e .)) (declaration (declare_and_assign (string_declare_assign string (string_assign srr = \"Hello\" .)))) (print_statement (print_var print a .)) (print_statement (print_var print b .)) (print_statement (print_var print c .))))";
	//String parseTree = Runner.parse_tree;
	// Split the tree and store in an array
	String instructions[] = parseTree.split("[()]+"); 
	ArrayList<String> intermediate_code = new ArrayList<String>();
	
	// Key - variable name
	// array : index-0 - type, index-1 - value, index -3 attribute
	// value will be assigned in runtime
	
	
/*	for (String i:instructions){
		System.out.println(i);
	}*/
	
	for (int  i = 0; i< instructions.length ; i++){
				
		// Ignore empty rows
		if (! instructions[i].equals("")){
			// Write functions for each operation
			
			if (instructions[i].trim().equals("declaration")){
				//Ignore empty cells if any
				while (instructions[i] == "")i++;
				// goto next valid statement and check if it is declare or declare and assign
				i++;
								
				if (instructions[i].split(" ")[0].equals("declare")){
					String []row = instructions[i].split(" ");
//					String key = row[2];
//					String []value = new String[3];
//					value[0] = row [1];
					//value[1] = "";
					// enter in intermediate code
					intermediate_code.add(row[0]+ " " + row[1] +" " + row[2]);
					
					// enter in symbol table
//					if (symbol_table.containsKey(key)){
//						System.out.println("Exception - duplicate variable declaration");
//					}
//					else {
//						symbol_table.put(key,value);
//					}
				}
				else if (instructions[i].split(" ")[0].equals("declare_and_assign")){

					//Ignore empty cells
					while (instructions[i] == "")i++;
					// goto next valid statement and check if it is declare or declare and assign
					i++;
					if (instructions[i].split(" ")[0].equals("number_declare_assign")){
						i++;
						// get the variable name and write it after assign
						String var =  instructions[i].split(" ")[1];
//						String key = var;
//						String value[] = new String[3];
//						value[0] = "number";
						
						//add to symbol table
//						if (symbol_table.containsKey(key)){
//							System.out.println("Exception - duplicate variable declaration");
//						}
//						else {
//							symbol_table.put(key,value);
//						}
						
						// add intermediate code entry
						intermediate_code.add("DECL NMBR" + " " + var);
						i++;
						String rs = "";
						
						Stack<String> operatorSt = new Stack<String>();
						Stack<String> operandSt = new Stack<String>();
						
						while (instructions[i].length()> 1 && instructions[i].split(" ")[0].equals("expression")){
							// evaluate the expressions and write it to rs
							operatorSt.push(instructions[i].split(" ")[2]);
							operandSt.push(instructions[i].split(" ")[1]);
							//rs += instructions[i].split(" ")[1]+" "+instructions[i].split(" ")[2]+" ";
							i++;
						}
						// pop to remove . from operandSt
						operatorSt.pop();
						if (operatorSt.isEmpty()){
							intermediate_code.add("LOAD "+ var +" " + operandSt.peek());
						}
						else {
							boolean flag = true;
							String op2 = "";
							String op1 = "";
							String operator ="";
							while (operandSt.size()> 0 && operatorSt.size()>0 ){
								if (flag){
								 op2 = operandSt.pop();
								 op1 = operandSt.pop();
								 operator =  operatorSt.pop();
								 flag = false;
								}
								else {
									op2 = "ACCM";
									op1 = operandSt.pop();
									operator =  operatorSt.pop();
								}
								intermediate_code.add(symbols.get(operator) + " " + op1 + " " + op2);
							}
						
						intermediate_code.add("LOAD "+ var + " ACCM");
						}
						// read stack
						
						// to remove last .
						//rs = rs.substring(0, rs.length()-2);
						//intermediate_code.add(var + " = " + rs);
						//intermediate_code.add("LOAD " + var + " " + rs);
					}
					else if (instructions[i].split(" ")[0].equals("boolean_declare_assign")){
						i++;
						
						String[] row = instructions[i].split(" ");
						
						//Add declaration
						intermediate_code.add("DECL BOOL" + " " + row[1]);
//						String key = row[1];
//						String value[] = new String[3];
//						value[0] = "boolean";
						
						//add to symbol table
//						if (symbol_table.containsKey(key)){
//							System.out.println("Exception - duplicate variable declaration");
//						}
//						else {
//							symbol_table.put(key,value);
//						}
						// add to intermediate code
						//intermediate_code.add(row[1]+" "+row[2]+" "+ row[3]);
						intermediate_code.add("LOAD "+ row[1]+" "+ row[3]);
					}
					else if (instructions[i].split(" ")[0].equals("string_declare_assign")){
						i++;
						String[] row = instructions[i].split(" ");
						String line_input = instructions[i].split("\"")[1];
						
//						String key = row[1];
//						String value[] = new String[3];
//						value[0] = "string";
						intermediate_code.add("DECL STRX" + " " + row[1]);
						//add to symbol table
//						if (symbol_table.containsKey(key)){
//							System.out.println("Exception - duplicate variable declaration");
//						}
//						else {
//							symbol_table.put(key,value);
//						}
						// add to intermediate code
						//intermediate_code.add(row[1]+" "+row[2]+" \""+ line_input+"\"");
						intermediate_code.add("LOAD "+ row[1]+" "+ " \""+ line_input+"\"");
					}
				}
				
			}
}
	}
	//System.out.println();
	//System.out.println("  Intermediate code ");
	try{
		String filename = classname + ".txt";
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		 
		for (String i:intermediate_code){
			//	System.out.println(i);
			writer.println(i);
		}
		writer.close();
		 
	} catch (IOException e) {
		   // do something
		System.out.println("IOException occurred while writing the file");
	}

	return intermediate_code;
  }	
}
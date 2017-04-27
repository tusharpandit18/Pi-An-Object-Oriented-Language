import java.util.*;

public class IntermediateCodeGenerator {

	/* Key - variable name
	   array : index-0 - type, index-1 - attribute, index -3 to store value
	   value will be assigned in runtime
	 */
	static HashMap<String,String[]> symbol_table = new HashMap<String,String[]>();

	public static void main(String[] args){
		// Getting a temporary parse tree generated from Executer class
		String parseTree = "(program (statement (declaration (declare number vara .)) (declaration (declare_and_assign (number_declare_assign number (number_assign varb = (expression 4 + (expression 5 + (expression a .))))))) (declaration (declare boolean c .)) (declaration (declare_and_assign (boolean_declare_assign boolean (bool_assign d = true .)))) (declaration (declare string e .)) (declaration (declare_and_assign (string_declare_assign string (string_assign srr = \"Hello\" .)))) (print_statement (print_var print a .)) (print_statement (print_var print b .)) (print_statement (print_var print c .))))";
		//String parseTree = Runner.parse_tree;
		// Split the tree and store in an array
		String instructions[] = parseTree.split("[()]+"); 
		ArrayList<String> intermediate_code = new ArrayList<String>();


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
						String key = row[2];
						String []value = new String[3];
						value[0] = row [1];
						//value[1] = "";
						// enter in intermediate code
						intermediate_code.add(row[0]+ " " + row[2]);

						// enter in symbol table
						symbol_table.put(key,value);
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
							String key = var;
							String value[] = new String[3];
							value[0] = "number";

							//add to symbol table
							symbol_table.put(key,value);

							// add intermediate code entry
							intermediate_code.add("declare" + " " + var);
							i++;
							String rs = "";
							while (instructions[i].length()> 1 && instructions[i].split(" ")[0].equals("expression")){
								// evaluate the expressions and write it to rs

								rs += instructions[i].split(" ")[1]+" "+instructions[i].split(" ")[2]+" ";
								i++;
							}
							// to remove last .
							rs = rs.substring(0, rs.length()-2);
							intermediate_code.add(var + " = " + rs);
						}
					}
				}
				else if (instructions[i].trim().equals("print_statement")){
					//Ignore empty cells
					while (instructions[i] == " ")i++;
					// goto next valid statement and check if it is declare or declare and assign
					i++;
					// print statement will be of 3 types, variable, value or string
					// split this string and check type of print statement
					String []print = instructions[i].split(" ");

					// print variable
					if (print[0].trim().equals("print_var")){					
						intermediate_code.add(print[1] + "_var " + print[2]);
					}
					// print a statement
					else if (print[0].trim().equals("print_num")){
						intermediate_code.add(print[1] + "_num " + print[2]);
					}
					else if (print[0].trim().equals("print_line")){
						String []line = instructions[i].split("\"");
						String quote_to_print = "\"" + line[1]+ "\"";

						intermediate_code.add(print[1] + "_line " +  quote_to_print);
					}

				}

			}
		}
	}
}




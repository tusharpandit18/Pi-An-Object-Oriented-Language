import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;



public class Runtime {
	HashMap<String,String[]> symbol_table = new HashMap<String,String[]>();
	
	public static void main(String[] args) {
		//Scanner sc = new Scanner(System.in);
		String filename = "";
    	if (args.length > 0){
    		filename = args[0];
    	}
    	ArrayList<String> list = new ArrayList<String>();
    	try {
	    	for (String line : Files.readAllLines(Paths.get(filename))) {
	    	    list.add(line);
	    	}
    	}
		catch (IOException e){
			System.out.println("Exception caught while reading fiel");
		}
    	//sc.close();
    	String []int_code_array = new String[list.size()];
    	for (int i=0; i<int_code_array.length; i++){
        	int_code_array[i] = list.get(i);
        }
    	Runtime ob = new Runtime();
    	ob.run_code(int_code_array);	
	
		
	}
	public void run_code(String []intCode){
		
		HashMap<String,String> type = new HashMap<String,String>();
		type.put("NMBR", "number");
		type.put("BOOL", "boolean");
		type.put("STRX", "string");
		
		HashMap<String,Character> operations = new HashMap<String,Character>();
		operations.put("ADDX", '+');
		operations.put("SUBX", '-');
		operations.put("MULX", '*');
		operations.put("DIVX", '/');
		
		double accumulator = 0.0;
		
		boolean if_executed = true;
		int while_start = -1;
		for (int i=0; i<intCode.length; i++){
			if (intCode[i].split(" ")[0].equals("DECL")){

				String []row = intCode[i].split(" ");
				String key = row[2];
				if (symbol_table.containsKey(key)){
					System.out.println("Exception - duplicate variable declaration");
				}
				else {
					String []value = new String[3];
					value[0] = type.get(row[1]);
					symbol_table.put(key,value);
					
				}
				
			}
			else if (intCode[i].split(" ").length > 2 &&
					 intCode[i].split(" ")[0].equals("PRNT") && 
					 intCode[i].split(" ")[1].equals("VARX")){
				
				String key = intCode[i].split(" ")[2];
				String []val = symbol_table.get(key);
				
				if (val != null) {
					if (val[1] == null){
						System.out.println("null");
					}
					else {
						System.out.println(val[1]);
					}
				}
				else {
					System.out.println("Exception: variable not declared");
				}
			
			}
			else if (intCode[i].split(" ").length > 2 &&
					 intCode[i].split(" ")[0].equals("PRNT") && 
					 intCode[i].split(" ")[1].equals("NMBR")){
				
				System.out.println(intCode[i].split(" ")[1]);
			}
			else if (intCode[i].split(" ").length > 2 &&
					 intCode[i].split(" ")[0].equals("PRNT") && 
					 intCode[i].split(" ")[1].equals("LINE")){	
				
				System.out.println("\""+intCode[i].split("\"")[1]+"\"");
			}
			else if (intCode[i].split(" ")[0].equals("IFST")){
				String []row =  intCode[i].split(" ");
				boolean condition_result = evaluate(row[2],row[1], row[3], symbol_table);
				
				//if condition evaluated to false either go to else part or endif part
				
				if (!condition_result){
					
					if_executed = false;
					while (!(intCode[i].equals("ELSE") || (intCode[i].equals("EDIF")))){
						i++;
					}
					i--;
				}
				else {
					if_executed = true;
				}
				
			}
			// for else if condition
			else if (intCode[i].split(" ").length > 1 &&
					intCode[i].split(" ")[0].equals("ELSE")){
				// check whether if has already executed
				// if yes skip all rows in else
				if ( if_executed){
					while (!(intCode[i].equals("ELSE"))){
						i++;
					}
					i--;
				}
				else {
					if_executed = true;
				}
				
				//System.out.println("entered else");
			}
			// only for else
			else if (intCode[i].split(" ").length == 1 &&
					intCode[i].split(" ")[0].equals("ELSE")){
				// check whether if has already executed
				// if yes skip all rows in else
				if ( if_executed){
					while (!(intCode[i].equals("ELSE"))){
						i++;
					}
					i--;
				}
				
				//System.out.println("entered else");
			}
			else if (intCode[i].split(" ")[0].equals("EDIF")){
				if_executed = false;
			}
			else if (intCode[i].split(" ")[0].equals("LOOP")){
				String []row =  intCode[i].split(" ");
				Boolean condition_result = evaluate(row[2],row[1], row[3], symbol_table);
				if (condition_result){
					
					while_start = i;
				}
				
				if (!condition_result) {
					while(!(intCode[i].split(" ")[0].equals("EDLP"))){
						
						i++;
					}
				}
			}

			else if (intCode[i].split(" ")[0].equals("EDLP")){
				i = while_start - 1;
			}
			
			// Expression evaluation
			// check if input is any of ADDX, SUBX, MUL or DIVX
			else if (operations.containsKey(intCode[i].split(" ")[0])){
				
				while (operations.containsKey(intCode[i].split(" ")[0])){
					
					String []row = intCode[i].split(" ");
					Character operator = operations.get(row[0]);
					String op1 = row[1];
					String op2 = row[2];
					
					//if both variable
					if (symbol_table.containsKey(op1) &&
						symbol_table.containsKey(op2)	){
						double value1 = Double.parseDouble(symbol_table.get(op1)[1]);
						double value2 = Double.parseDouble(symbol_table.get(op2)[1]);
						
						if (operator == '+')
						accumulator += value1 + value2;
						else if (operator == '-')
							accumulator += value1 - value2;
						else if (operator == '*')
							accumulator += value1 * value2;
						else if (operator == '/')
							accumulator += value1 / value2;
					}
					// if 1 var and 2 number
					else if (symbol_table.containsKey(op1) &&
						     !symbol_table.containsKey(op2)){
						
						double value1 = Double.parseDouble(symbol_table.get(op1)[1]);
						double value2 = Double.parseDouble(op2);
						
						if (operator == '+')
						accumulator += value1 + value2;
						else if (operator == '-')
							accumulator += value1 - value2;
						else if (operator == '*')
							accumulator += value1 * value2;
						else if (operator == '/')
							accumulator += value1 / value2;
						
					}
					
					else if (!symbol_table.containsKey(op1) &&
							symbol_table.containsKey(op2)){
						double value1 = Double.parseDouble(op1);
						double value2 = Double.parseDouble(symbol_table.get(op2)[1]);
						
						if (operator == '+')
						accumulator += value1 + value2;
						else if (operator == '-')
							accumulator += value1 - value2;
						else if (operator == '*')
							accumulator += value1 * value2;
						else if (operator == '/')
							accumulator += value1 / value2;
							
					}
					// check for both integers					
					else if (!op1.matches(".*[^0-9].*") && 
						!op2.matches(".*[^0-9].*")){
						
						double value1 = Double.parseDouble(op1);;
						double value2 = Double.parseDouble(op2);
						
						if (operator == '+')
						accumulator += value1 + value2;
						else if (operator == '-')
							accumulator += value1 - value2;
						else if (operator == '*')
							accumulator += value1 * value2;
						else if (operator == '/')
							accumulator += value1 / value2;
						
					}
					else {
						System.out.println("Incompatible types");
					}
					i++;
				}
				i--;
			}
			
			// Assignment statement
			else if (intCode[i].split(" ")[0].equals("LOAD")){
				String []row = intCode[i].split(" ");
				String var = row[1];
				
				double result = 0.0;
				if (!symbol_table.containsKey(var)){
					System.out.println("Exception - variable not declared ");
				}
				else if(row.length == 3){
					
					String []val = symbol_table.get(var);
					
					// if assignment value is numeric
					
					 if (row[2].equals("ACCM")){
						
						val[1] = String.valueOf(accumulator);
						symbol_table.put(var, val);	
						accumulator = 0;
					}
					 else if (!row[2].matches(".*[^0-9].*")) {
						val[1] = row[2];
						symbol_table.put(var, val);
						
					}
					// check for boolean values
					else if (row[2].equals("true")||
							 row[2].equals("false")){
						val[1] = row[2];
						symbol_table.put(var, val);
					}
					
					// check if value being assigned is a variable
					else {
						if (symbol_table.containsKey(row[2])){
							String []valueOfRHS = symbol_table.get(row[2]);
							val[1] = valueOfRHS[1];
							symbol_table.put(var, val);
						}
						//
						else {
							System.out.println("variable being assigned is not declared");
						}
					}
				}
//				else {
//				// Expression evaluation
//				int count = 2;
//				double op1 = 0.0, op2 = 0.0;
//				String opr = "";
//				int flag = 0;
//				while (count < row.length-1){
//				//double intermediate_result = 0.0;
//				// if a variable is present in the expression
//				// flag is for first computation, for later computations op1 will be intermediate result
//				if (flag == 0)	{
//					// if a variable is being assigned
//					if ( symbol_table.containsKey(row[count])
//						&&	symbol_table.get(row[count])[0].equals("number") ) {
//						
//						String temp = symbol_table.get(row[count])[1];
//						
//						if (temp == null ){
//							System.out.println("Exception: "+ temp +" not initialized");
//						}
//						else {
//							op1 = Double.parseDouble(symbol_table.get(row[count])[1]);
//						}
//					
//					}
//					// if a number is being assigned
//					else if (row[count].matches("[0-9]+")){						
//						op1 = Double.parseDouble(row[count]);
//					}
//					// a variable is being assigned but not of type number
//					else if (symbol_table.containsKey(row[count])){						
//						System.out.println("Illegal datatype exception");
//					}
//					// RHS is a string but not declared as a variable
//					else {
//						System.out.println(row[count]+ " not declared");
//					}
//					// if a variable is being assigned
//					if ( symbol_table.containsKey(row[count+2])
//						&&	symbol_table.get(row[count+2])[0].equals("number") ) {
//						
//						String temp = symbol_table.get(row[count+2])[1];
//											
//						// check if initialized
//						if (temp == null ){
//							System.out.println(temp +" not initialized");
//						}
//						else {
//							op2 = Double.parseDouble(symbol_table.get(row[count+2])[1]);
//						}
//					}
//					// if a number id being assigned
//					else if (row[count+2].matches("[0-9]+")){						
//						op2 = Double.parseDouble(row[count+2]);
//						
//					}
//					// if a variable is being assigned but not of type number
//					else if (symbol_table.containsKey(row[count+2])){
//						System.out.println("Illegal datatype exception");
//					}
//					// RHS is a string but not declared as a variable
//					else if (symbol_table.containsKey(row[count+2])){
//						System.out.println("Illegal datatype for the operation -- Exception");
//					}
//					else {
//						System.out.println(row[count+2]+ " not declared -- Exception");
//					}
//					opr = row[count+1];
//					count = count + 2;
//					flag = 1;
//				}
//				else {
//					op1 = result;
//					if ( symbol_table.containsKey(row[count+2])
//							&& symbol_table.get(row[count+2])[0].equals("number") ) {
//						
//						String temp = symbol_table.get(row[count+2])[1];
//						
//						// check if initialized
//						if (temp == null ){
//							System.out.println(temp +" not initialized");
//						}
//						else {
//							op2 = Double.parseDouble(symbol_table.get(row[count+2])[1]);
//						}
//						
//					}
//					else if (row[count+2].matches("[0-9]+")){						
//						op2 = Double.parseDouble(row[count+2]);
//					}
//					else if (symbol_table.containsKey(row[count+2])){
//						System.out.println("Illegal datatype exception");
//					}
//					else {
//						System.out.println(row[count+2]+ " not declared --  Exception");
//					}
//					opr = row[count+1];
//					count = count + 2;
//				}
//					
//				
//					if (opr.equals("+")){
//					    result = op1 + op2;	
//					}
//					if (opr.equals("-")){
//						result = op1 - op2;					
//					}
//					if (opr.equals("*")){
//						result = op1 * op2;
//					}
//					if (opr.equals("/")){
//						result = op1 / op2;
//					}
//					 
//				  }
//				String []value = symbol_table.get(var);
//				value[1] = String.valueOf(result);
//				symbol_table.put(var, value);
//				
//				}
				
			}
		}
	}
	
public boolean evaluate(String lhs, String operator, String rhs, HashMap<String,String[]> symbol_table){
		
		if (operator.equals("EQ")){
			// if both sides number
			if (!lhs.matches(".*[^0-9].*") && !rhs.matches(".*[^0-9].*")){
				return (Double.parseDouble(lhs) == Double.parseDouble(rhs));				
			}
			// if lhs is number and rhs is variable
			else if (!lhs.matches(".*[^0-9].*")){
				// check if rhs is declared and initialized
				if (symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(rhs)[0];
					String rhs_val = symbol_table.get(rhs)[1];
					double op2 = 0.0;
					// convert the rhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op2 = Double.parseDouble(rhs_val);
					}
					return (Double.parseDouble(lhs) == op2);
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// if rhs is number and lhs is variable
			else if (!rhs.matches(".*[^0-9].*")){
				// check if lhs is declared and initialized
				if (symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(lhs)[0];
					String lhs_val = symbol_table.get(lhs)[1];
					double op1 = 0.0;
					// convert the lhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op1 = Double.parseDouble(lhs_val);
					}
					return (op1 == Double.parseDouble(rhs));
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// both string
			else {
				//check if variable is declared
				if (symbol_table.containsKey(lhs) &&
				    symbol_table.containsKey(rhs)	){
					String type1 =  symbol_table.get(lhs)[0];
					String val1 = symbol_table.get(lhs)[1];
					String type2 =  symbol_table.get(rhs)[0];
					String val2 = symbol_table.get(rhs)[1];
					
					// check only if types are similar
					if ((type1.equals("number")&& type2.equals("number"))){
						double op1 = Double.parseDouble(val1);
						double op2 = Double.parseDouble(val2);
						return (op1 == op2);
					}
					else if ((type1.equals("boolean")&& type2.equals("boolean"))){
						return (val1.equals(val2));
					}
					else if ((type1.equals("string")&& type2.equals("string"))){
						return (val1.equals(val2));
					}
					else {
						System.out.println("Incompatible types");
						return false;
					}
				}
				else {
					System.out.println("Exception : at least one variable not declared");
					return false;
				}
			}
		}
		else if (operator.equals("LT")){
			// if both sides number
			if (!lhs.matches(".*[^0-9].*") && !rhs.matches(".*[^0-9].*")){
				return (Double.parseDouble(lhs) < Double.parseDouble(rhs));				
			}
			// if lhs is number and rhs is variable
			else if (!lhs.matches(".*[^0-9].*")){
				// check if rhs is declared and initialized
				if (symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(rhs)[0];
					String rhs_val = symbol_table.get(rhs)[1];
					double op2 = 0.0;
					// convert the rhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op2 = Double.parseDouble(rhs_val);
					}
					return (Double.parseDouble(lhs) < op2);
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// if rhs is number and lhs is variable
			else if (!rhs.matches(".*[^0-9].*")){
				// check if lhs is declared and initialized
				if (symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(lhs)[0];
					String lhs_val = symbol_table.get(lhs)[1];
					double op1 = 0.0;
					// convert the lhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op1 = Double.parseDouble(lhs_val);
					}
					return (op1 < Double.parseDouble(rhs));
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// both string
			else {
				//check if variable is declared
				if (symbol_table.containsKey(lhs) &&
				    symbol_table.containsKey(rhs)	){
					String type1 =  symbol_table.get(lhs)[0];
					String val1 = symbol_table.get(lhs)[1];
					String type2 =  symbol_table.get(rhs)[0];
					String val2 = symbol_table.get(rhs)[1];
					
					// check only if types are similar
					if ((type1.equals("number")&& type2.equals("number"))){
						double op1 = Double.parseDouble(val1);
						double op2 = Double.parseDouble(val2);
						return (op1 < op2);
					}
					else {
						System.out.println("Incompatible types");
						return false;
					}
				}
				else {
					System.out.println("Exception : at least one variable not declared");
					return false;
				}
			}
		}
		else if (operator.equals("GT")){
			// if both sides number
			if (!lhs.matches(".*[^0-9].*") && !rhs.matches(".*[^0-9].*")){
				return (Double.parseDouble(lhs) > Double.parseDouble(rhs));				
			}
			// if lhs is number and rhs is variable
			else if (!lhs.matches(".*[^0-9].*")){
				// check if rhs is declared and initialized
				if (symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(rhs)[0];
					String rhs_val = symbol_table.get(rhs)[1];
					double op2 = 0.0;
					// convert the rhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op2 = Double.parseDouble(rhs_val);
					}
					return (Double.parseDouble(lhs) > op2);
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// if rhs is number and lhs is variable
			else if (!rhs.matches(".*[^0-9].*")){
				// check if lhs is declared and initialized
				if (symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(lhs)[0];
					String lhs_val = symbol_table.get(lhs)[1];
					double op1 = 0.0;
					// convert the lhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op1 = Double.parseDouble(lhs_val);
					}
					return (op1 > Double.parseDouble(rhs));
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// both string
			else {
				//check if variable is declared
				if (symbol_table.containsKey(lhs) &&
				    symbol_table.containsKey(rhs)	){
					String type1 =  symbol_table.get(lhs)[0];
					String val1 = symbol_table.get(lhs)[1];
					String type2 =  symbol_table.get(rhs)[0];
					String val2 = symbol_table.get(rhs)[1];
					
					// check only if types are similar
					if ((type1.equals("number")&& type2.equals("number"))){
						double op1 = Double.parseDouble(val1);
						double op2 = Double.parseDouble(val2);
						return (op1 > op2);
					}
					else {
						System.out.println("Incompatible types");
						return false;
					}
				}
				else {
					System.out.println("Exception : at least one variable not declared");
					return false;
				}
			}
		}
		else if (operator.equals("LTEQ")){
			// if both sides number
			if (!lhs.matches(".*[^0-9].*") && !rhs.matches(".*[^0-9].*")){
				return (Double.parseDouble(lhs) <= Double.parseDouble(rhs));				
			}
			// if lhs is number and rhs is variable
			else if (!lhs.matches(".*[^0-9].*")){
				// check if rhs is declared and initialized
				if (symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(rhs)[0];
					String rhs_val = symbol_table.get(rhs)[1];
					double op2 = 0.0;
					// convert the rhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op2 = Double.parseDouble(rhs_val);
					}
					return (Double.parseDouble(lhs) <= op2);
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// if rhs is number and lhs is variable
			else if (!rhs.matches(".*[^0-9].*")){
				// check if lhs is declared and initialized
				if (symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(lhs)[0];
					String lhs_val = symbol_table.get(lhs)[1];
					double op1 = 0.0;
					// convert the lhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op1 = Double.parseDouble(lhs_val);
					}
					return (op1 <= Double.parseDouble(rhs));
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// both string
			else {
				//check if variable is declared
				if (symbol_table.containsKey(lhs) &&
				    symbol_table.containsKey(rhs)	){
					String type1 =  symbol_table.get(lhs)[0];
					String val1 = symbol_table.get(lhs)[1];
					String type2 =  symbol_table.get(rhs)[0];
					String val2 = symbol_table.get(rhs)[1];
					
					// check only if types are similar
					if ((type1.equals("number")&& type2.equals("number"))){
						double op1 = Double.parseDouble(val1);
						double op2 = Double.parseDouble(val2);
						return (op1 <= op2);
					}
					else {
						System.out.println("Incompatible types");
						return false;
					}
				}
				else {
					System.out.println("Exception : at least one variable not declared");
					return false;
				}
			}
		}
		else if (operator.equals("GTEQ")){
			// if both sides number
			if (!lhs.matches(".*[^0-9].*") && !rhs.matches(".*[^0-9].*")){
				return (Double.parseDouble(lhs) >= Double.parseDouble(rhs));				
			}
			// if lhs is number and rhs is variable
			else if (!lhs.matches(".*[^0-9].*")){
				// check if rhs is declared and initialized
				if (symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(rhs)[0];
					String rhs_val = symbol_table.get(rhs)[1];
					double op2 = 0.0;
					// convert the rhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op2 = Double.parseDouble(rhs_val);
					}
					return (Double.parseDouble(lhs) >= op2);
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// if rhs is number and lhs is variable
			else if (!rhs.matches(".*[^0-9].*")){
				// check if lhs is declared and initialized
				if (symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  symbol_table.get(lhs)[0];
					String lhs_val = symbol_table.get(lhs)[1];
					double op1 = 0.0;
					// convert the lhs based on type
					// since lhs is a number , check only for number type
					if (type.equals("number")){
						op1 = Double.parseDouble(lhs_val);
					}
					return (op1 >= Double.parseDouble(rhs));
				}
				else {
					System.out.println("variable "+ rhs +" is not declared");
					return false;
				}
			}
			// both string
			else {
				//check if variable is declared
				if (symbol_table.containsKey(lhs) &&
				    symbol_table.containsKey(rhs)	){
					String type1 =  symbol_table.get(lhs)[0];
					String val1 = symbol_table.get(lhs)[1];
					String type2 =  symbol_table.get(rhs)[0];
					String val2 = symbol_table.get(rhs)[1];
					
					// check only if types are similar
					if ((type1.equals("number")&& type2.equals("number"))){
						double op1 = Double.parseDouble(val1);
						double op2 = Double.parseDouble(val2);
						return (op1 >= op2);
					}
					else {
						System.out.println("Incompatible types");
						return false;
					}
				}
				else {
					System.out.println("Exception : at least one variable not declared");
					return false;
				}
			}
		}
		return false;
	}

}

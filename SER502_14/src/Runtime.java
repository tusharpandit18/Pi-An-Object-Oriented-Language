
public class Runtime {
	
	public static void main(String[] args) {
		
	}
	public void run_code(String []intCode){
		boolean if_executed = true;
		int while_start = -1;
		for (int i=0; i<intCode.length; i++){
			if (intCode[i].split(" ")[0].equals("declare")){
				// ignore declaration statements
				// elements already put in symbol table in intermediate code generator step
				continue;
			}
			else if (intCode[i].split(" ")[0].equals("print_var")){
				String key = intCode[i].split(" ")[1];
				String []val = IntermediateCodeGenerator.symbol_table.get(key);
				
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
				// print null if variable is not assigned
				//System.out.println(val[1]);
				
			}
			else if (intCode[i].split(" ")[0].equals("print_num")){
				System.out.println(intCode[i].split(" ")[1]);
			}
			else if (intCode[i].split(" ")[0].equals("print_line")){				
				System.out.println("\""+intCode[i].split("\"")[1]+"\"");
			}
			else if (intCode[i].split(" ")[0].equals("if")){
				String []row =  intCode[i].split(" ");
				boolean condition_result = evaluate(row[1],row[2], row[3]);
				
				//if condition evaluated to false either go to else part or endif part
				if (!condition_result){
					if_executed = false;
					while (!(intCode[i].equals("else") || (intCode[i].equals("endif")))){
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
					intCode[i].split(" ")[0].equals("else")){
				// check whether if has already executed
				// if yes skip all rows in else
				if ( if_executed){
					while (!(intCode[i].equals("else"))){
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
					intCode[i].split(" ")[0].equals("else")){
				// check whether if has already executed
				// if yes skip all rows in else
				if ( if_executed){
					while (!(intCode[i].equals("else"))){
						i++;
					}
					i--;
				}
				
				//System.out.println("entered else");
			}
			else if (intCode[i].split(" ")[0].equals("endif")){
				if_executed = false;
			}
			else if (intCode[i].split(" ")[0].equals("while")){
				String []row =  intCode[i].split(" ");
				Boolean condition_result = evaluate(row[1],row[2], row[3]);
				if (condition_result){
					while_start = i;
				}
				
				if (!condition_result) {
					while(!(intCode[i].split(" ")[0].equals("endwhile"))){
						i++;
					}
				}
			}

			else if (intCode[i].split(" ")[0].equals("endwhile")){
				i = while_start - 1;
			}
			
			// Assignment statement
			else {
				String []row = intCode[i].split(" ");
				String var = row[0];
				double result = 0.0;
				if (!IntermediateCodeGenerator.symbol_table.containsKey(var)){
					System.out.println("Exception - variable not declared ");
				}
				else if(row.length == 3){
					
					String []val = IntermediateCodeGenerator.symbol_table.get(row[0]);
					
					// if assignment value is numeric
					if (!row[2].matches(".*[^0-9].*")) {
						val[1] = row[2];
						IntermediateCodeGenerator.symbol_table.put(row[0], val);
					}
					// check for boolean values
					else if (row[2].equals("true")||
							 row[2].equals("false")){
						val[1] = row[2];
						IntermediateCodeGenerator.symbol_table.put(row[0], val);
					}
					// check if value being assigned is a variable
					else {
						if (IntermediateCodeGenerator.symbol_table.containsKey(row[2])){
							String []valueOfRHS = IntermediateCodeGenerator.symbol_table.get(row[2]);
							val[1] = valueOfRHS[1];
							IntermediateCodeGenerator.symbol_table.put(row[0], val);
						}
						//
						else {
							System.out.println("variable being assigned is not declared");
						}
					}
				}
				else {
				// Expression evaluation
				// Precedence not handled
				int count = 2;
				double op1 = 0.0, op2 = 0.0;
				String opr = "";
				int flag = 0;
				while (count < row.length-1){
				//double intermediate_result = 0.0;
				// if a variable is present in the expression
				// flag is for first computation, for later computations op1 will be intermediate result
				if (flag == 0)	{
					// if a variable is being assigned
					if ( IntermediateCodeGenerator.symbol_table.containsKey(row[count])
						&&	IntermediateCodeGenerator.symbol_table.get(row[count])[0].equals("number") ) {
						
						String temp = IntermediateCodeGenerator.symbol_table.get(row[count])[1];
						
						if (temp == null ){
							System.out.println("Exception: "+ temp +" not initialized");
						}
						else {
							op1 = Double.parseDouble(IntermediateCodeGenerator.symbol_table.get(row[count])[1]);
						}
					
					}
					// if a number is being assigned
					else if (row[count].matches("[0-9]+")){						
						op1 = Double.parseDouble(row[count]);
					}
					// a variable is being assigned but not of type number
					else if (IntermediateCodeGenerator.symbol_table.containsKey(row[count])){						
						System.out.println("Illegal datatype exception");
					}
					// RHS is a string but not declared as a variable
					else {
						System.out.println(row[count]+ " not declared");
					}
					// if a variable is being assigned
					if ( IntermediateCodeGenerator.symbol_table.containsKey(row[count+2])
						&&	IntermediateCodeGenerator.symbol_table.get(row[count+2])[0].equals("number") ) {
						
						String temp = IntermediateCodeGenerator.symbol_table.get(row[count+2])[1];
											
						// check if initialized
						if (temp == null ){
							System.out.println(temp +" not initialized");
						}
						else {
							op2 = Double.parseDouble(IntermediateCodeGenerator.symbol_table.get(row[count+2])[1]);
						}
					}
					// if a number id being assigned
					else if (row[count+2].matches("[0-9]+")){						
						op2 = Double.parseDouble(row[count+2]);
						
					}
					// if a variable is being assigned but not of type number
					else if (IntermediateCodeGenerator.symbol_table.containsKey(row[count+2])){
						System.out.println("Illegal datatype exception");
					}
					// RHS is a string but not declared as a variable
					else if (IntermediateCodeGenerator.symbol_table.containsKey(row[count+2])){
						System.out.println("Illegal datatype for the operation -- Exception");
					}
					else {
						System.out.println(row[count+2]+ " not declared -- Exception");
					}
					opr = row[count+1];
					count = count + 2;
					flag = 1;
				}
				else {
					op1 = result;
					if ( IntermediateCodeGenerator.symbol_table.containsKey(row[count+2])
							&& IntermediateCodeGenerator.symbol_table.get(row[count+2])[0].equals("number") ) {
						
						String temp = IntermediateCodeGenerator.symbol_table.get(row[count+2])[1];
						
						// check if initialized
						if (temp == null ){
							System.out.println(temp +" not initialized");
						}
						else {
							op2 = Double.parseDouble(IntermediateCodeGenerator.symbol_table.get(row[count+2])[1]);
						}
						
					}
					else if (row[count+2].matches("[0-9]+")){						
						op2 = Double.parseDouble(row[count+2]);
					}
					else if (IntermediateCodeGenerator.symbol_table.containsKey(row[count+2])){
						System.out.println("Illegal datatype exception");
					}
					else {
						System.out.println(row[count+2]+ " not declared --  Exception");
					}
					opr = row[count+1];
					count = count + 2;
				}
					
				
					if (opr.equals("+")){
					    result = op1 + op2;	
					}
					if (opr.equals("-")){
						result = op1 - op2;					
					}
					if (opr.equals("*")){
						result = op1 * op2;
					}
					if (opr.equals("/")){
						result = op1 / op2;
					}
					 
				  }
				String []value = IntermediateCodeGenerator.symbol_table.get(var);
				value[1] = String.valueOf(result);
				IntermediateCodeGenerator.symbol_table.put(var, value);
				
				}
				
			}
		}
	}
	
	public boolean evaluate(String lhs, String operator, String rhs){
		
		if (operator.equals("EQ")){
			// if both sides number
			if (!lhs.matches(".*[^0-9].*") && !rhs.matches(".*[^0-9].*")){
				return (Double.parseDouble(lhs) == Double.parseDouble(rhs));				
			}
			// if lhs is number and rhs is variable
			else if (!lhs.matches(".*[^0-9].*")){
				// check if rhs is declared and initialized
				if (IntermediateCodeGenerator.symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String rhs_val = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String lhs_val = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs) &&
				    IntermediateCodeGenerator.symbol_table.containsKey(rhs)	){
					String type1 =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String val1 = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
					String type2 =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String val2 = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
					
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String rhs_val = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String lhs_val = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs) &&
				    IntermediateCodeGenerator.symbol_table.containsKey(rhs)	){
					String type1 =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String val1 = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
					String type2 =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String val2 = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
					
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String rhs_val = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String lhs_val = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs) &&
				    IntermediateCodeGenerator.symbol_table.containsKey(rhs)	){
					String type1 =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String val1 = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
					String type2 =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String val2 = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
					
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String rhs_val = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String lhs_val = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs) &&
				    IntermediateCodeGenerator.symbol_table.containsKey(rhs)	){
					String type1 =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String val1 = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
					String type2 =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String val2 = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
					
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(rhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String rhs_val = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs)){
					// variable is declared, get its value from symbol table
					String type =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String lhs_val = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
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
				if (IntermediateCodeGenerator.symbol_table.containsKey(lhs) &&
				    IntermediateCodeGenerator.symbol_table.containsKey(rhs)	){
					String type1 =  IntermediateCodeGenerator.symbol_table.get(lhs)[0];
					String val1 = IntermediateCodeGenerator.symbol_table.get(lhs)[1];
					String type2 =  IntermediateCodeGenerator.symbol_table.get(rhs)[0];
					String val2 = IntermediateCodeGenerator.symbol_table.get(rhs)[1];
					
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


public class Runtime {
	
	public static void main(String[] args) {
		
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

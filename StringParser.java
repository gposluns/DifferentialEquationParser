import java.util.ArrayList;


public class StringParser {

	private static final String NUMBERS = "0123456789.";
	
	private static int findEndBracketLocation (int startLocation, String string) throws IllegalArgumentException{
		int bracketCount = 1;
		for (int loc = startLocation + 1; loc < string.length(); loc++){
			if (string.charAt(loc) == '('){
				bracketCount++;
			}
			else {
				if (string.charAt(loc) == ')'){
					bracketCount--;
					if (bracketCount == 0){
						return loc;
					}
				}
			}
		}
		
		throw new IllegalArgumentException ("Brackets do not match");
	}
	
	private static int findNumEnd (int startLocation, String string){
		int loc = startLocation;
		for (loc++; loc < string.length();loc++){
			if (NUMBERS.indexOf (string.charAt(loc)) < 0){						
				return loc - 1;
			}
		}
		return string.length() - 1;
	}
	
	private static int findFactorEnd(int startLocation, String string){
		if (NUMBERS.indexOf (string.charAt(startLocation))>= 0){
			return findNumEnd (startLocation, string);
		}
		
		if (string.charAt (startLocation) == 'x' || string.charAt (startLocation) == 'X' || string.charAt (startLocation) == 'y' || string.charAt (startLocation) == 'Y' || string.charAt (startLocation) == 'e' || string.charAt (startLocation) == 'E'){
			if (startLocation < string.length() - 1 && string.charAt (startLocation + 1) == '^'){
				return findFactorEnd (startLocation + 2, string);
			}
			return startLocation;
		}
		
		if (startLocation < string.length()-1 && string.substring (startLocation, startLocation + 2).equalsIgnoreCase ("pi")){
			if (startLocation < string.length() - 1 && string.charAt (startLocation + 1) == '^'){
				return findFactorEnd (startLocation + 3, string);
			}
			return startLocation + 1;
		}
		
		return findEndBracketLocation (string.indexOf('(', startLocation), string);
	}
	
	protected static Evaluable parseTerm (String string) throws IllegalArgumentException{
		Term term = null;
		ArrayList<Evaluable> specialTerms = new ArrayList<Evaluable>();
		for (int loc = 0; loc < string.length(); loc++){
			//System.out.println (loc);
			if (NUMBERS.indexOf(string.charAt(loc)) >= 0){				
				int end = findNumEnd(loc, string);
				if (end < string.length() - 1 && string.charAt(end + 1) == '^'){
					double base = Double.parseDouble(string.substring (loc, end + 1));
					int factorStart = end + 2;
					end = findFactorEnd (end + 2, string);
					specialTerms.add (new ExponentialTerm(new Term (base), parseTerm(string.substring(factorStart, end + 1))));
				}			
				else if (term == null){
					term = new Term (Double.parseDouble(string.substring (loc, end + 1)));
				}
				else{
					term.addCoeff (Double.parseDouble(string.substring (loc, end + 1)));
				}
				loc = end;
			}
				
			else if (string.charAt(loc) == 'x' || string.charAt(loc) == 'X'){
				if (loc == string.length() - 1 || string.charAt(loc + 1) != '^'){
					if (term == null){
						term = new Term (1, new Term(1), new Term (0));
					}
					else {
						term.addXPow (new Term (1));
					}
				}
				else{
					try{
						int end = findFactorEnd(loc + 2, string);
						if (term == null){
							term = new Term (1, parseTerm(string.substring (loc + 2, end + 1)), new Term (0));
						}
						else {
							term.addXPow(parseTerm (string.substring (loc + 2, end + 1)));
						}
						loc = end;
					}
					catch (StringIndexOutOfBoundsException e){
						throw new IllegalArgumentException ("Syntax error: Hanging ^");
					}
				}
			}
				
			else if (string.charAt(loc) == 'y' || string.charAt(loc) == 'Y'){
				if (loc == string.length() - 1 || string.charAt(loc + 1) != '^'){
					if (term == null){
						term = new Term (1, new Term (0), new Term (1));
					}
					else {
						term.addYPow (new Term (1));
					}
				}
				else{
					try{
						int end = findFactorEnd(loc + 2, string);
						if (term == null){
							term = new Term (1, new Term(0), parseTerm (string.substring (loc + 2, end + 1)));
						}
						else{
							term.addYPow(parseTerm (string.substring (loc + 2, end + 1)));
						}
						loc = end;
					}
					catch (StringIndexOutOfBoundsException e){
						throw new IllegalArgumentException ("Syntax error: Hanging ^");
					}
				}
			}
			
			else if (string.charAt(loc) == 'e' || string.charAt (loc) == 'E'){
				if (loc < string.length() - 1 && string.charAt(loc + 1) == '^'){
					int end = findFactorEnd (loc + 2, string);
					specialTerms.add (new ExponentialTerm(new Term (Math.E), parseTerm(string.substring(loc + 2, end + 1))));
					loc = end;
				}			
				else if (term == null){
					term = new Term (Math.E, new Term (0), new Term (0));
				}
				else {
					term.addCoeff(Math.E);
				}
			}
			
			else if (loc < string.length() - 1 && string.substring (loc, loc + 2).equalsIgnoreCase("pi")){
				if (loc < string.length() - 2 && string.charAt(loc + 2) == '^'){
					int end = findFactorEnd (loc + 3, string);
					specialTerms.add (new ExponentialTerm(new Term (Math.E), parseTerm(string.substring(loc + 3, end + 1))));
					loc = end - 1;
				}			
				else if (term == null){
					term = new Term (Math.PI, new Term (0), new Term (0));
				}
				else {
					term.addCoeff(Math.PI);
				}
				loc++;
			}
			
			else if (string.charAt(loc) == '('){
				int end = findEndBracketLocation(loc, string);
				specialTerms.add (parseEquation(string.substring(loc + 1, end)));
				loc = end;
			}
			
			else if (string.charAt(loc) == '^'){
				int end = findFactorEnd (loc + 1, string);
				specialTerms.add (new ExponentialTerm(specialTerms.remove(specialTerms.size()-1), parseTerm (string.substring (loc + 1, end + 1))));
				loc = end;
			}
			
			else if (string.charAt(loc) == '/'){
				int end = findFactorEnd (loc + 1, string);
				specialTerms.add (new ReciprocalTerm (parseTerm(string.substring(loc + 1, end + 1))));
				loc = end;
			}
			
			else {
				int end = -1;
				try{
				
					if (string.substring(loc, loc + 5).equalsIgnoreCase("acos(")){	
						end = findEndBracketLocation(loc + 4, string);
						specialTerms.add (new SpecialTerm (SpecialTerm.Type.ACOS, parseEquation(string.substring(loc + 5, end))));
					}		
					else if (string.substring(loc, loc + 5).equalsIgnoreCase("asin(")){
						end = findEndBracketLocation(loc + 4, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.ASIN, parseEquation(string.substring(loc + 5, end))));
					}	
					else if (string.substring(loc, loc + 5).equalsIgnoreCase("atan(")){
						end = findEndBracketLocation(loc + 4, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.ATAN, parseEquation(string.substring(loc + 5, end))));
					}	
					else if (string.substring(loc, loc + 4).equalsIgnoreCase("cos(")){
						end = findEndBracketLocation(loc + 3, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.COS, parseEquation(string.substring(loc + 4, end))));
					}	
					else if (string.substring(loc, loc + 5).equalsIgnoreCase("cosh(")){
						end = findEndBracketLocation(loc + 4, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.COSH, parseEquation(string.substring(loc + 5, end))));
					}				
					else if (string.substring(loc, loc + 3).equalsIgnoreCase("ln(")){
						end = findEndBracketLocation(loc + 2, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.LN, parseEquation(string.substring(loc + 3, end))));
					}				
					else if (string.substring(loc, loc + 4).equalsIgnoreCase("log(")){
						end = findEndBracketLocation(loc + 3, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.LOG, parseEquation(string.substring(loc + 4, end))));
					}				
					else if (string.substring(loc, loc + 4).equalsIgnoreCase("sin(")){
						end = findEndBracketLocation(loc + 3, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.SIN, parseEquation(string.substring(loc + 4, end))));
					}				
					else if (string.substring(loc, loc + 5).equalsIgnoreCase("sinh(")){
						end = findEndBracketLocation(loc + 4, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.SINH, parseEquation(string.substring(loc + 5, end))));
					}				
					else if (string.substring(loc, loc + 4).equalsIgnoreCase("tan(")){
						end = findEndBracketLocation(loc + 3, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.TAN, parseEquation(string.substring(loc + 4, end))));
					}		
					else if (string.substring(loc, loc + 5).equalsIgnoreCase("tanh(")){
						end = findEndBracketLocation(loc + 4, string);
						specialTerms.add(new SpecialTerm (SpecialTerm.Type.TANH, parseEquation(string.substring(loc + 5, end))));
					} 
					else {
						
						if (string.substring(loc, loc + 4).equalsIgnoreCase("sec(")){
							end = findEndBracketLocation(loc + 3, string);
							specialTerms.add(new ReciprocalTerm(new SpecialTerm (SpecialTerm.Type.COS, parseEquation(string.substring(loc + 4, end)))));
						}
						else if (string.substring(loc, loc + 4).equalsIgnoreCase("csc(")){
							end = findEndBracketLocation(loc + 3, string);
							specialTerms.add(new ReciprocalTerm(new SpecialTerm (SpecialTerm.Type.SIN, parseEquation(string.substring(loc + 4, end)))));
						}
						else if (string.substring(loc, loc + 4).equalsIgnoreCase("cot(")){
							end = findEndBracketLocation(loc + 3, string);
							specialTerms.add(new ReciprocalTerm(new SpecialTerm (SpecialTerm.Type.TAN, parseEquation(string.substring(loc + 4, end)))));
						}
						else if (string.substring(loc, loc + 5).equalsIgnoreCase("asec(")){
							end = findEndBracketLocation(loc + 4, string);
							specialTerms.add(new SpecialTerm (SpecialTerm.Type.ACOS, new ReciprocalTerm (parseEquation(string.substring(loc + 5, end)))));
						}
						else if (string.substring(loc, loc + 5).equalsIgnoreCase("acsc(")){
							end = findEndBracketLocation(loc + 4, string);
							specialTerms.add(new SpecialTerm (SpecialTerm.Type.ASIN, new ReciprocalTerm (parseEquation(string.substring(loc + 5, end)))));
						}
						else if (string.substring(loc, loc + 5).equalsIgnoreCase("acot(")){
							end = findEndBracketLocation(loc + 4, string);
							specialTerms.add(new SpecialTerm (SpecialTerm.Type.ATAN, new ReciprocalTerm (parseEquation(string.substring(loc + 5, end)))));
						}
						
						else if (string.substring(loc, loc + 5).equalsIgnoreCase("sech(")){
							end = findEndBracketLocation(loc + 4, string);
							specialTerms.add(new ReciprocalTerm(new SpecialTerm (SpecialTerm.Type.COSH, parseEquation(string.substring(loc + 5, end)))));
						}
						else if (string.substring(loc, loc + 5).equalsIgnoreCase("csch(")){
							end = findEndBracketLocation(loc + 4, string);
							specialTerms.add(new ReciprocalTerm(new SpecialTerm (SpecialTerm.Type.SINH, parseEquation(string.substring(loc + 5, end)))));
						}
						else if (string.substring(loc, loc + 5).equalsIgnoreCase("coth(")){
							end = findEndBracketLocation(loc + 4, string);
							specialTerms.add(new ReciprocalTerm(new SpecialTerm (SpecialTerm.Type.TANH, parseEquation(string.substring(loc + 5, end)))));
						}
						else if (string.substring(loc, loc + 6).equalsIgnoreCase("asinh(")){
							end = findEndBracketLocation(loc + 5, string);
							Evaluable eval =  parseEquation(string.substring(loc + 6, end));
							Equation equation = new Equation ();
							equation.add (eval);
							Equation rooted = new Equation ();
							rooted.add(new ExponentialTerm (eval, new Term (2)));
							rooted.add(new Term(1));
							equation.add (new ExponentialTerm (rooted, new Term (1/2)));
							specialTerms.add(new SpecialTerm (SpecialTerm.Type.LN, equation));
						}
						else if (string.substring(loc, loc + 6).equalsIgnoreCase("acosh(")){
							end = findEndBracketLocation(loc + 5, string);
							Evaluable eval =  parseEquation(string.substring(loc + 6, end));
							Equation equation = new Equation ();
							equation.add (eval);
							Equation rooted = new Equation ();
							rooted.add(new ExponentialTerm (eval, new Term (2)));
							rooted.add(new Term(-1));
							equation.add (new ExponentialTerm (rooted, new Term (1/2)));
							specialTerms.add(new SpecialTerm (SpecialTerm.Type.LN, equation));
						}
						else if (string.substring(loc, loc + 6).equalsIgnoreCase("atanh(")){
							end = findEndBracketLocation(loc + 5, string);
							Evaluable eval =  parseEquation(string.substring(loc + 6, end));
							Equation top = new Equation ();
							top.add (eval);
							top.add (new Term(1));
							Equation bottom = new Equation ();
							bottom.add (new NegativeTerm (eval));
							bottom.add(new Term(1));
							LargeTerm big = new LargeTerm();
							big.add (top);
							big.add (new ReciprocalTerm (bottom));
							LargeTerm bigger = new LargeTerm();
							bigger.add (new SpecialTerm (SpecialTerm.Type.LN, big));
							bigger.add (new Term (1/2));
							specialTerms.add(bigger);
						}
						else if (string.substring(loc, loc + 6).equalsIgnoreCase("acsch(")){
							end = findEndBracketLocation(loc + 5, string);
							ReciprocalTerm eval =  new ReciprocalTerm (parseEquation(string.substring(loc + 6, end)));
							Equation equation = new Equation ();
							equation.add (eval);
							Equation rooted = new Equation ();
							rooted.add(new ExponentialTerm (eval, new Term (2)));
							rooted.add(new Term(1));
							equation.add (new ExponentialTerm (rooted, new Term (1/2)));
							specialTerms.add(new SpecialTerm (SpecialTerm.Type.LN, equation));
						}
						else if (string.substring(loc, loc + 6).equalsIgnoreCase("asech(")){
							end =  findEndBracketLocation(loc + 5, string);
							ReciprocalTerm eval =  new ReciprocalTerm (parseEquation(string.substring(loc + 6, end)));
							Equation equation = new Equation ();
							equation.add (eval);
							Equation rooted = new Equation ();
							rooted.add(new ExponentialTerm (eval, new Term (2)));
							rooted.add(new Term(-1));
							equation.add (new ExponentialTerm (rooted, new Term (1/2)));
							specialTerms.add(new SpecialTerm (SpecialTerm.Type.LN, equation));
						}
						else if (string.substring(loc, loc + 6).equalsIgnoreCase("acoth(")){
							end = findEndBracketLocation(loc + 5, string);
							ReciprocalTerm eval =  new ReciprocalTerm (parseEquation(string.substring(loc + 6, end)));
							Equation top = new Equation ();
							top.add (eval);
							top.add (new Term(1));
							Equation bottom = new Equation ();
							bottom.add (new NegativeTerm(eval));
							bottom.add(new Term(1));
							LargeTerm big = new LargeTerm();
							big.add (top);
							big.add (new ReciprocalTerm (bottom));
							LargeTerm bigger = new LargeTerm();
							bigger.add (new SpecialTerm (SpecialTerm.Type.LN, big));
							bigger.add (new Term (1/2));
							specialTerms.add(bigger);
						}
						else if (string.substring(loc, loc + 3).equalsIgnoreCase("log")){
							int bracketStart = string.indexOf('(', loc);
							Evaluable base = parseTerm (string.substring(loc + 3, bracketStart));
							end = findEndBracketLocation (bracketStart, string);
							LargeTerm conversion = new LargeTerm();
							conversion.add (new SpecialTerm (SpecialTerm.Type.LN, parseTerm(string.substring(bracketStart + 1, end))));
							conversion.add (new ReciprocalTerm(new SpecialTerm(SpecialTerm.Type.LN, base)));
							specialTerms.add (conversion);
						}
						
						else{
							throw new IllegalArgumentException ("Syntax Error: Invalid function at position " + loc + " in " + string +" Remember to use () for the function's argument, and that logn(x) is log in base n of x");
						}
					}
				
				}
				catch (StringIndexOutOfBoundsException e){
					throw new IllegalArgumentException ("Syntax Error: Invalid function at position " + loc + " in " + string+" Remember to use () for the function's argument, and that logn(x) is log in base n of x");
				}
				loc = end;
			}
				
		}
		
		if (term != null){
			specialTerms.add(term);
		}	
		//System.out.println (specialTerms);
		if (specialTerms.size() == 1){
			return specialTerms.get (0);
		}	
		return new LargeTerm (specialTerms);
	}
	
	protected static Evaluable parseEquation (String string){
		Equation equation = new Equation();
		
		int start = 0;
		boolean negative = false;
		for (int loc = 0; loc < string.length(); loc++){
			if (string.charAt (loc) == '('){
				loc = findEndBracketLocation (loc, string);
			}
			
			if (string.charAt (loc) == '+' || string.charAt(loc) == '-'){
				if (negative){
					equation.add (new NegativeTerm(parseTerm (string.substring(start, loc))));
				}
				else {
					equation.add (parseTerm (string.substring (start, loc)));
				}
				negative = string.charAt(loc) == '-';
				start = loc + 1;
			}
		}
		if (negative){
			equation.add (new NegativeTerm(parseTerm (string.substring(start, string.length()))));
		}
		else {
			equation.add (parseTerm (string.substring (start, string.length())));
		}
	
		
		return equation;
	}
	
	public static Evaluable parseMath (String string){
		String[] strings = string.trim().split (" ");
		string = "";
		for (String str:strings){
			string = string + str;
		}
		return parseEquation (string);
	}
	
}

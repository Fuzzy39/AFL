	package qwerty4967.AFL;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import qwerty4967.AFL.Function.AFLFunction;
import qwerty4967.AFL.Function.JavaFunction;
import qwerty4967.AFL.Function.MethodCode;
import qwerty4967.AFL.Interpret.Namespace;
import qwerty4967.AFL.Lang.Lang;
import qwerty4967.AFL.Lang.TokenType;
import qwerty4967.AFL.Parse.Tokenizer;
import qwerty4967.AFL.ParseTree.Token;

public class JavaFunctionInitializer 
{
	// this code is responsible for governing every function in AFL that is not written with AFL code.
	// this includes operators and other junk
	// notably, it does not include certain basic functions known as control statements.
	// these functions include, but may not be limited to: if, else, while, end, function, return, break, continue, and = (assignment operator).
	// Those functions require special access to the internal workings of the interpreter.
	
	// this code is very repetitive.
	// there is probably a better way to do this.
	
	protected static void initJavaFunctions()
	{
		initOperators();
		initArrays();
		initMisc();
		
		
	}
	
	private static void initOperators()
	{
		// here operators are ordered by their priority tier.
		// organize operators ('(' and ')') are notable exceptions, because they are not represented by functions.
		// another exception is the assignment operator, '=', which is a control function.
		
		//##### MULTIPLY CATEGORY OPERATORS #####
		
		// * (Multiplication) operator
		MethodCode mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue*bValue);
				
			}
			Shell.error("The '*' operator requires operands of type num.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		JavaFunction jf = new JavaFunction("*",2,mc);
		Namespace.addFunction(jf);
		
		// / (Division) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue/bValue);
				
			}
			Shell.error("The '/' operator requires operands of type num.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("/",2,mc);
		Namespace.addFunction(jf);
		
		// % (Modulo) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue%bValue);
				
			}
			Shell.error("The '%' operator requires operands of type num.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("%",2,mc);
		Namespace.addFunction(jf);
		
		
		//##### ADD CATEGORY OPERATORS #####
		
		// + (Addition) operator
		// This was the first operator I added, so I decided to keep it the less efficient, gross way.
	    mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			boolean aIsInvalid = a.getType()!=TokenType.number;
			boolean bIsInvalid = b.getType()!=TokenType.number;
			
			if(aIsInvalid || bIsInvalid)
			{
				return new Token(a.toOutputString()+b.toOutputString(), TokenType.string);
			}
			// they be numbers!
			double aNum= Double.parseDouble(a.getData());
			double bNum= Double.parseDouble(b.getData());
			double result = aNum+bNum;
			return new Token(result+"", TokenType.number);
		});
		
		jf = new JavaFunction("+",2,mc);
		Namespace.addFunction(jf);
		
		// - (Subtraction) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue-bValue);
				
			}
			Shell.error("The '-' operator requires operands of type num.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("-",2,mc);
		Namespace.addFunction(jf);

		
		//##### COMPARE CATEGORY OPERATORS #####
		
		// == (equals) Operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(a.getType()==b.getType())
			{
				// are they numbers?
				// are they numbers?
				if(areBothOfType(a,b,TokenType.number))
				{
					
					// guh
					double aValue = Double.parseDouble(a.getData());
					double bValue = Double.parseDouble(b.getData());
					return toToken(aValue==bValue);
					
				}
				else
				{
					return toToken(a.getData().equals(b.getData()));
					
				}
			}
			return new Token("false", TokenType.bool);
		});
		
		jf = new JavaFunction("==",2,mc);
		Namespace.addFunction(jf);
		
		
		//!= (not equals) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(a.getType()==b.getType())
			{
				// are they numbers?
				if(areBothOfType(a,b,TokenType.number))
				{
					
					// guh
					double aValue = Double.parseDouble(a.getData());
					double bValue = Double.parseDouble(b.getData());
					return toToken(aValue!=bValue);
					
				}
				else
				{
					return toToken(!(a.getData().equals(b.getData())));
					
				}
			}
			return new Token("true", TokenType.bool);
		});
		
		jf = new JavaFunction("!=",2,mc);
		Namespace.addFunction(jf);
		
		// >= (greater than or equal to) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue>=bValue);
				
			}
			Shell.error("The '>=' operator requires operands of type num.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction(">=",2,mc);
		Namespace.addFunction(jf);
		
		// <= (Less than or equal to) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue<=bValue);
				
			}
			Shell.error("The '<=' operator requires operands of type num.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("<=",2,mc);
		Namespace.addFunction(jf);
		
		// > (greater than) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue>bValue);
				
			}
			Shell.error("The '>' operator requires operands of type num.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction(">",2,mc);
		Namespace.addFunction(jf);
		
		//< operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.number))
			{
				
				// guh
				double aValue = Double.parseDouble(a.getData());
				double bValue = Double.parseDouble(b.getData());
				return toToken(aValue<bValue);
				
			}
			Shell.error("The '<' operator requires operands of type num.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("<",2,mc);
		Namespace.addFunction(jf);
		
		
		//##### LOGIC CATEGORY OPERATORS #####
		
		//|| (Or) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.bool))
			{
				
				// guh
				boolean aValue = Boolean.parseBoolean(a.getData());
				boolean bValue = Boolean.parseBoolean(b.getData());
				return toToken(aValue||bValue);
				
			}
			Shell.error("The '||' operator requires operands of type bool.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("||",2,mc);
		Namespace.addFunction(jf);
		
		//& (And) operator
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token a = tokens[0];
			Token b = tokens[1];
			
			if(areBothOfType(a,b,TokenType.bool))
			{
				
				// guh
				boolean aValue = Boolean.parseBoolean(a.getData());
				boolean bValue = Boolean.parseBoolean(b.getData());
				return toToken(aValue&bValue);
				
			}
			Shell.error("The '&' operator requires operands of type bool.", -2,"");
			return new Token("Error", TokenType.error);
		});
	
		jf = new JavaFunction("&",2,mc);
		Namespace.addFunction(jf);
		
				
	}
	
	private static void initArrays()
	{
		MethodCode mc = ((Token[] tokens) ->
		{
			// get the tokens
			return Namespace.newArray();
		});
	
		JavaFunction jf = new JavaFunction("newArray",0,mc);
		Namespace.addFunction(jf);
		
		
		
		// arrayAdd(array, toAdd)
		mc = ((Token[] tokens) ->
		{
			Token arrayPointer= tokens[0];
			Token toAdd = tokens[1];
			// Check for types.
			if(arrayPointer.getType()!=TokenType.arrayPointer)
			{
				Shell.error("Parameter 0 of 'arrayAdd' must be of type array.", -2,"");
				return new Token("Error",TokenType.error);
			}
			// add the new thing.
			ArrayList<Token>array = Namespace.getArray(arrayPointer);
			array.add(toAdd);
			
			
			if(tokenIsInArray(arrayPointer, array))
			{
				Shell.error("An array cannot be added to itself.", -2,"");
				array.remove(array.size()-1);
				return new Token("Error",TokenType.error);
			}
			//Namespace.setArray(arrayPointer, array);
			return new Token("Void",TokenType.voidToken);
			
		});
	
		jf = new JavaFunction("arrayAdd",2,mc);
		Namespace.addFunction(jf);
		
		
		// arrayGet(array, index)
		mc = ((Token[] tokens) ->
		{
			Token arrayPointer= tokens[0];
			Token arrayIndex = tokens[1];
			// check that everything is of the correct type.
			if(arrayPointer.getType()!=TokenType.arrayPointer)
			{
				Shell.error("Parameter 0 of 'arrayGet' must be of type array.", -2,"");
				return new Token("Error",TokenType.error);
			}
			if(arrayIndex.getType()!=TokenType.number)
			{
				Shell.error("Parameter 1 of 'arrayGet' must be of type num.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			// get the array.
			ArrayList<Token>array = Namespace.getArray(arrayPointer);
			// get the index.
			double index = Double.parseDouble(arrayIndex.getData());
			if(!arrayIndexIsValid(array, index))
			{
				return new Token ("Error", TokenType.error);
			}
			int i = (int)index;
			
			return array.get(i);
			
		});
	
		jf = new JavaFunction("arrayGet",2,mc);
		Namespace.addFunction(jf);
		
		
		// arraySize(array)
		mc = ((Token[] tokens) ->
		{
			Token arrayPointer= tokens[0];
			
			// Check for types.
			if(arrayPointer.getType()!=TokenType.arrayPointer)
			{
				Shell.error("Parameter 0 of 'arraySize' must be of type array.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			
			ArrayList<Token>array = Namespace.getArray(arrayPointer);
			
			return toToken(array.size());
			
		});
	
		jf = new JavaFunction("arraySize",1,mc);
		Namespace.addFunction(jf);
		
		// arrayset(array, index, value)
		mc = ((Token[] tokens) ->
		{
			Token arrayPointer= tokens[0];
			Token arrayIndex = tokens[1];
			Token value = tokens[2];
			
			// check that everything is of the correct type.
			if(arrayPointer.getType()!=TokenType.arrayPointer)
			{
				Shell.error("Parameter 0 of 'arraySet' must be of type array.", -2, "");
				return new Token("Error",TokenType.error);
			}
			if(arrayIndex.getType()!=TokenType.number)
			{
				Shell.error("Parameter 1 of 'arraySet' must be of type num.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			// get the array.
			ArrayList<Token>array = Namespace.getArray(arrayPointer);
			// get the index.
			double index = Double.parseDouble(arrayIndex.getData());
			if(!arrayIndexIsValid(array, index))
			{
				return new Token ("Error", TokenType.error);
			}
			int i = (int)index;
			if(tokenIsInArray(arrayPointer, array))
			{
				Shell.error("An array cannot be added to itself.", -2,"");
				return new Token("Error",TokenType.error);
			}
			array.set(i, value);
			return new Token("Void",TokenType.voidToken);
			
		});
	
		jf = new JavaFunction("arraySet",3,mc);
		Namespace.addFunction(jf);
		
		
		mc = ((Token[] tokens) ->
		{
			Token arrayPointer= tokens[0];
			
			// check that everything is of the correct type.
			if(arrayPointer.getType()!=TokenType.arrayPointer)
			{
				Shell.error("Parameter 0 of 'arrayRemove' must be of type array.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			// remove the last element from the array.
			// get the array.
			ArrayList<Token>array = Namespace.getArray(arrayPointer);
			
			if(array.size()!=0)
			{
				array.remove(array.size()-1);
			}

			return new Token("Void",TokenType.voidToken);
			
		});
	
		jf = new JavaFunction("arrayRemove",1,mc);
		Namespace.addFunction(jf);
		
		
		mc = ((Token[] tokens) ->
		{
			Token arrayPointer= tokens[0];
			Token indexToken= tokens[1];
			
			// check that everything is of the correct type.
			if(arrayPointer.getType()!=TokenType.arrayPointer)
			{
				Shell.error("Parameter 0 of 'arrayRemove' must be of type 'array'.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			if(indexToken.getType()!=TokenType.number)
			{
				Shell.error("Parameter 1 of 'arrayRemove' must be of type 'num'.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
		
			// remove the last element from the array.
			// get the array.
			ArrayList<Token>array = Namespace.getArray(arrayPointer);
			
			// get the number.
			// get the index.
			double index = Double.parseDouble(indexToken.getData());
			if(!arrayIndexIsValid(array, index))
			{
				return new Token ("Error", TokenType.error);
			}
			int i = (int)index;
			
			
			
			if(array.size()!=0)
			{
				array.remove(i);
			}
			// remove the token.
			

			return new Token("Void",TokenType.voidToken);
			
		});
	
		jf = new JavaFunction("arrayRemove",2,mc);
		Namespace.addFunction(jf);
	}
	
	private static void initMisc()
	{
		// get input.
		MethodCode mc = ((Token[] tokens) ->
		{
			// get the tokens
			return new Token(Shell.in(), TokenType.string);
		});
	
		JavaFunction jf = new JavaFunction("input",0,mc);
		Namespace.addFunction(jf);
		
		// typeOf(value) function. 
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			TokenType type = tokens[0].getType();
			switch(type)
			{
				case number:
					return new Token(Lang.TYPES[0],TokenType.type);
				case string:
					return new Token(Lang.TYPES[1],TokenType.type);
				case character:
					return new Token(Lang.TYPES[2],TokenType.type);
				case bool:
					return new Token(Lang.TYPES[3],TokenType.type);
				case arrayPointer:
					return new Token(Lang.TYPES[4],TokenType.type);
				case type:
					return new Token(Lang.TYPES[5],TokenType.type);
				default:
					Shell.error("Internal Error. Could not ascertain type.", -2,"");
					return new Token("Error",TokenType.error);                                                                           
				
			}
			
		});
	
		jf = new JavaFunction("typeOf",1,mc);
		Namespace.addFunction(jf);
		
		// toCharArray(String) function.
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token string = tokens[0];
			// check validity.
			if(string.getType()!=TokenType.string)
			{
				Shell.error("Parameter 0 of 'toCharArray' must be of type 'string'.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			// return a char array, I guess.
			char[] chars = string.getData().toCharArray();
			Token toReturn = Namespace.newArray();
			ArrayList<Token> arrayToReturn = new ArrayList<Token>();
			
			for(char c: chars)
			{
				Token charToken = new Token(""+c, TokenType.character);
				arrayToReturn.add(charToken);
			}
			Namespace.setArray(toReturn, arrayToReturn);
			return toReturn;
		});
	
		jf = new JavaFunction("toCharArray",1,mc);
		Namespace.addFunction(jf);
		
		
		// isType: check if a string is of a type.
		// ugh this will suck.
		// isType(string, type)
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token string = tokens[0];
			Token type = tokens[1];
			
			// check validity.
			if(string.getType()!=TokenType.string)
			{
				Shell.error("Parameter 0 of 'isType' must be of type 'string'.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			if(type.getType()!=TokenType.type)
			{
				Shell.error("Parameter 1 of 'isType' must be of type 'type'.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			if(type.getData().equals("array"))
			{
				Shell.error("Arrays are not a valid type to check for.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			return toToken(stringMatchesType(string.getData(), type.getData()));
		});
	
		jf = new JavaFunction("isType",2,mc);
		Namespace.addFunction(jf);
		
		// isType: check if a string is of a type.
		// ugh this will suck.
		// isType(string, type)
		mc = ((Token[] tokens) ->
		{
			// get the tokens
			Token string = tokens[0];
			Token type = tokens[1];
			
			// check validity.
			if(string.getType()!=TokenType.string)
			{
				Shell.error("Parameter 0 of 'toType' must be of type 'string'.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			if(type.getType()!=TokenType.type)
			{
				Shell.error("Parameter 1 of 'toType' must be of type 'type'.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			if(type.getData().equals("array"))
			{
				Shell.error("Arrays are not a valid type to check for.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			if(!stringMatchesType(string.getData(), type.getData()))
			{
				Shell.error("Cannot convert string \""+string.getData()+"\" to type \'"+type.getData()+"\'", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			string.setType(toTokenType(type.getData()));
			
			if(type.getData().equals("num"))
			{
				 string.setData(Double.parseDouble(string.getData())+"");
			}
			
			return string;
		});
	
		jf = new JavaFunction("toType",2,mc);
		Namespace.addFunction(jf);
		
		mc = ((Token[] tokens) ->
		{
			// wow, that's simple
			return toToken(Math.random());
			
		});
	
		jf = new JavaFunction("random",0,mc);
		Namespace.addFunction(jf);
		
		// Error is now a control statement to allow for displaying line numbers more properly. This code remains for posterity or something.
		// Error(message) 
		/*mc = ((Token[] tokens) ->
		{
			Token string = tokens[0];
			if(string.getType()!=TokenType.string)
			{
				Shell.error("Parameter 0 of 'error' must be of type 'string'.", -2,"");
				return new Token("Error",TokenType.error);
			}
			
			Shell.error(string.getData(), -2,"");
			return new Token("Error", TokenType.error); 
			
		});
	
		jf = new JavaFunction("error",1,mc);
		Namespace.addFunction(jf);*/
		
		mc = ((Token[] tokens) ->
		{
			// wow, that's simple
			Shell.clear();
			return new Token("Void",TokenType.voidToken);
			
		});
	
		jf = new JavaFunction("clear",0,mc);
		Namespace.addFunction(jf);
		
		
		mc = ((Token[] tokens) ->
		{
			Token milliseconds = tokens[0];
			if(milliseconds.getType()!=TokenType.number)
			{
				Shell.error("Parameter 0 of 'sleep' must be of type 'num'.", -2,"");
				return new Token("Error", TokenType.error);
			}
			double ms=Double.parseDouble(milliseconds.getData());
			try
			{
				TimeUnit.MILLISECONDS.sleep(((long)ms));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(-1);
			}
			return new Token("Void",TokenType.voidToken);
			
		});
	
		jf = new JavaFunction("sleep",1,mc);
		Namespace.addFunction(jf);
		
		
		// debug, I guess?
		mc = ((Token[] tokens) ->
		{
			
			Shell.out(Namespace.debug());
			return new Token("Void",TokenType.voidToken);
		});
	
		jf = new JavaFunction("printCompleteNamespace",0,mc);
		Namespace.addFunction(jf);
		
		
		
		
		mc = ((Token[] tokens) ->
		{
			// this is the dumbest workaround to a problem.
			
			Namespace.setVariable(new Token("params", TokenType.variable), new Token("",TokenType.string), new AFLFunction("_dummy",0,-1));
			return new Token("Void",TokenType.voidToken);
			
		});
		
		jf = new JavaFunction("_onStartup",0,mc);
		jf.call(new Token[0]); // note that we call this one.
		
		mc = ((Token[] tokens) ->
		{
			Shell.clear();
			System.out.println("YOU AREN'T READY TO LEARN OF THE DARK ARTS, YOUNG ONE...");
			System.exit(666);
			return null;
		});
	
		jf = new JavaFunction("horcrux",0,mc);
		Namespace.addFunction(jf);
	}
	
	 
	private static boolean areBothOfType(Token a, Token b, TokenType t)
	{
		if(a.getType()==b.getType())
		{
			// are they numbers?
			if(a.getType()==t)
			{
				return true;
			}
		}
		return false;
	}
	
	private static Token toToken(boolean b)
	{
		if(b)
		{
			return new Token("true", TokenType.bool);
		}
		else
		{
			return new Token("false", TokenType.bool);
		}
	}
	
	private static Token toToken(double d)
	{
		return new Token (d+"",TokenType.number);
	}
	
	private static boolean arrayIndexIsValid(ArrayList<Token> array, double index)
	{
		if(index%1!=0 || index<0)
		{
			Shell.error("Index "+index+" is invalid. Indexes must be positive whole numbers.", -2,"");
			return false;
		}
		
		// I mean, this makes some sense but it still feels like a weird edge case.
		/*if(array.size()==0)
		{
			Shell.error("Index "+index+" is out of bounds for array size "+array.size()+".", -2);
			return false;
		}*/
		
		if(index>=array.size())
		{
			Shell.error("Index "+index+" is out of bounds for array size "+array.size()+".", -2,"");
			return false;
		}
		return true;
	}
	
	private static boolean tokenIsInArray(Token checkAgainst, ArrayList<Token> array)
	{
		// do... stuff.
		//  we need to check that a token is not anywhere within an array or children arrays
		// which means this probably recursive
		for(Token t: array)
		{
			// check if it matches, then check if it's an array.
			if(t==checkAgainst)
			{
				return true;
			}
			
			if(t.getType()==TokenType.arrayPointer)
			{
				return(tokenIsInArray(checkAgainst, Namespace.getArray(t)));
			}
		}
		return false;
	}
	
	private static boolean stringMatchesType(String s, String t)
	{
		switch(t)
		{
			case "num":
				return stringIsNum(s);
			case "bool":
				return(s.equals("true")|| s.equals("false"));
			case "string":
				return true;
			case "char":
				if(s.length()==1)
				{
					return true;
				}
				return false;
			case "type":
				for(String type :Lang.TYPES)
				{
					if(type.equals(s))
					{
						return true;
					}
				}
				return false;
			default:
				Shell.out("Error involving invalid type");
		}
		return false;
	}
	
	private static boolean stringIsNum(String s)
	{
		char[] chars = s.toCharArray();
		for(char c:chars)
		{
			if(!(Character.isDigit(c)||c=='.'))
			{
				return false;
			}
		}
		return Tokenizer.validateNumber(s);
	}
	
	private static TokenType toTokenType(String type)
	{
		switch(type)
		{
			case "num":
				return TokenType.number;
			case "string":
				return TokenType.string;
			case "char":
				return TokenType.character;
			case "bool":
				return TokenType.bool;
			case "array":
				return TokenType.arrayPointer;
			case "type":
				return TokenType.type;
		}
		return TokenType.error;
	}
}

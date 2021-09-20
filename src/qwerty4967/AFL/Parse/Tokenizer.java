package qwerty4967.AFL.Parse;

import java.util.ArrayList;
import java.util.Arrays;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.ParseTree.*;
import qwerty4967.AFL.Lang.*;

public class Tokenizer 
{
	private static int lineNumber;
	// The first class that is actually particularly new to AFL!
	// So, you might wonder, what does it do?
	
	// well, it basically does what used to be pass one in Ziker.
	// it takes a string and turns them into tokens.
	
	protected static AFLFunction tokenize(AFLFunction main, ArrayList<String> statements)
	{

		// the parsing of each line of code is separate, so we can just loop through each line
		// and parse it individually.
		
		for( int i=0; i<statements.size(); i++)
		{
			// if this method returns false, the user has made some kind of syntax error. 
			// we need to pass this info up the chain.
			lineNumber=i+1;
			if(!tokenizeStatement(main, statements.get(i)))
			{
				return null;
			}
			locateFunctionCalls((Statement)main.getChild(i));
			
		}
		
		return main;
		
	}
	
	private static boolean tokenizeStatement(AFLFunction main, String toTokenize)
	{
		// first, create the statement we are going to tokenize.
		Statement statement = new Statement( lineNumber, main );
		Container container = new Container(statement);
		
		//next, look through each character meticulously.
		TokenType currentTokenType = null;
		TokenType currentCharType = null;
		
		String currentTokenData = "";
		
		for( int i = 0; i<toTokenize.length(); i++)
		{
			
			 char currentChar=toTokenize.charAt(i);
			 
			 currentCharType=getCharType(currentChar);
			 
			 if(currentCharType!=currentTokenType)
			 {
				 // two things.
				 // one, verify and create the current token.
				 // two, get ready to look for the next token
				 if(currentTokenType!=null)
				 {
					 
					 // create token also verifies the token.
					 if(!createToken(container, currentTokenData, currentTokenType))
					 {
						 return false;
					 }
					 currentTokenData="";
					
					 
				 }
				 
				 currentTokenType=currentCharType;
				 
				 //whitespace handling.
				 if(currentTokenType == null)
				 {
			 		// just set up for next time, nothing special.
			 		//currentTokenData+=currentChar;
			 		continue;
				 }
				 
				 switch(currentTokenType)
				 {
				 	
				 	case variable:
				 	case number:
				 	case operator:
				 		// just set up for next time, nothing special.
				 		currentTokenData+=currentChar;
				 		continue;
				 	case character:
				 		if(!createCharacter(i, toTokenize, container))
				 		{
				 			return false;
				 		}
				 		
				 		currentTokenType=null; // not sure if this is required...
				 		i+=2; // a character's length.
				 		continue;
				 	case string:
				 		int newI =createString(i, toTokenize, container);
				 		if(newI==-1)
				 		{
				 			return false;
				 		}
				 		
				 		currentTokenType=null; // not sure if this is required...
				 		i=newI; // a character's length.
				 		continue;
				 	default:
				 		Shell.error("Internal error. Found invalid token type "+currentCharType, lineNumber);
				 		System.exit(-1);
				 }
			 }
			 else
			 {
				 if(currentCharType!=null)
				 {
					 currentTokenData+=currentChar;
					 continue;
				 }
			 }
		}
		
		// create a token with the last bit of data
		if(currentTokenData.length()>0)
		{
			
			 if(!createToken(container, currentTokenData, currentTokenType))
			 {
				 return false;
			 }
		}

		return true;
	}
	
	private static TokenType getCharType( char c)
	{
		if(Character.isWhitespace(c))
		{
			return null;
		}
		
		// check for numbers, simply.
		if( Character.isDigit(c)||c=='.')
		{
			return TokenType.number;
		}
		// check for Strings
		if(c=='"')
		{
			return TokenType.string;
		}
		
		if(c=='\'')
		{
			return TokenType.character;
		}
		
		// we don't actually know percisely what this one is, but we will call it a variable.p-
		if(Character.isLetter(c))
		{
			return TokenType.variable;
		}
		
		return TokenType.operator;	
	}
	
	private static boolean createToken(Container parent, String tokenData, TokenType type)
	{
		Shell.out("Creating Token with data '"+tokenData+"'", 3);
		 
		// keep in mind, characters never enter these cursed realms
		//TODO make this a seperate token.
		type=verifyToken(tokenData, type, parent);
		if(type==null)
		{
			return false;
		}
		
		if(type!=TokenType.operator)
		{
			new Token(tokenData, type, parent);
		}
		return true;
		
	}
	
	private static TokenType verifyToken(String tokenData, TokenType type, Container parent)
	{
		switch(type)
		{
			case number:
				if(!validateNumber(tokenData))
				{
					Shell.error("Invalid token '"+tokenData+"'. Is it a number? ", lineNumber);
					return null;
				}
				break;
			case variable:
				// can't pull out fucntions
				type=identifyKeywords(tokenData);
				break;
			case operator:
				if(!validateOperator(tokenData, parent))
				{
					Shell.error("Invalid Operator '"+tokenData+"'.", lineNumber);
					return null;
				}
				break;
			default:
				Shell.error("Internal error. Found invalid token type '"+type+"' while verifying tokens.", -1);
		 		System.exit(-1);
		}
		return type;
		
	}
	
	private static boolean validateNumber(String s)
	{
		// can't have more than 1 decimal point & decimal point cannot be at the end of the string.
		
		if(s.charAt(s.length()-1)=='.')
		 {
			 
			 return false;
			 
		 }
		 
		 int dots=0;
		 for(int i=0; i<s.length(); i++)
		 {
			 char c = s.charAt(i);
			
			 
			 if(c=='.')
			 {
				 dots++;
			 }
			
			 if(dots>1)
			 {
				 return false;
			 }
			 
		 }
		 // you've probably got a problem there...
		 // shouldn't ever get here.
		 // ^ The above comment was in Ziker 4 code
		 // I haven't the faintest idea what it means.
		 
		 return true;
	}
	
	private static TokenType identifyKeywords(String data)
	{
		for(String bool : Lang.BOOLS)
		{
			if(data.equals(bool))
			{
				return TokenType.bool;
			}
		}
		
		for(String type : Lang.TYPES)
		{
			if(data.equals(type))
			{
				return TokenType.type;
			}
		}
		
		// NOTE: this can also include functions. we aren't bothered with those just yet.
		return TokenType.variable;
	}
	
	private static boolean validateOperator( String data, Container parent)
	{
		// split by organize Operators, then identify and create operators.
		
		// first step is to create the regex, which will be made dynamically, in case I ever need another
		// organize operator.
		// frankly from where I'm standing now this seems pretty unlikely.
		// whatever.
		String[] ops = data.split(createOperatorRegex());
		outer: for(String op:ops)
		{
			for(String toCheck:Operators.list())
			{
				if(op.equals(toCheck))
				{
					// create the operator
					new Token(op,TokenType.operator, parent);
					continue outer;
				}
			}
			return false;
		}
		return true;
	}
	
	private static String createOperatorRegex()
	{
		String escape = "\\.[]{}()<>*+-=!?^$|";
		String lookBehind="?<=";
		String lookAhead = "?=";
		String toReturn = "";
		// That looks like gibberish, it means organize operators.
		for(String op : Operators.getGroup(Operators.PRIORITY_GROUP.organize))
		{
			// escape characters if they need it
			op = escape.contains(op) ? ("\\"+op):op;
			
			toReturn+="|(("+lookBehind+op+")|("+lookAhead+op+"))";
		}
		toReturn = toReturn.substring(1);
		return toReturn;
	}
	
	private static boolean createCharacter(int startingIndex, String statement, Container parent)
	{
		if(!validateCharacter(startingIndex, statement))
		{
			Shell.error("Invalid Character.", lineNumber);
			return false;
		}
		
		// we are assuming that the character is perfectly fine.
		String character = Character.toString(statement.charAt(startingIndex+1));
		new Token(character,TokenType.character, parent);
		return true;
	}
	
	private static boolean validateCharacter(int startingIndex, String statement)
	{
		// perform various checks
		// first, bounds.
		if(startingIndex+2>=statement.length())
		{
			return false;
		}
		
		// next, quotes.
		
		if(statement.charAt(startingIndex)!='\'')
		{
			return false;
		}
		
		if(statement.charAt(startingIndex+2)!='\'')
		{
			return false;
		}
		
		// seems good
		
		return true;
		
	}
	
	// returns length of string.
	private static int createString(int startingIndex, String statement, Container parent)
	{
		int toReturn = validateString(startingIndex, statement);
		if(toReturn == -1)
		{
			return -1;
		}
		
		String string = statement.substring(startingIndex+1, toReturn+1);
		Shell.out("Found string \""+string+"\".", 3);
		
		new Token(string,TokenType.string, parent);
		
		return toReturn+1;
	}
	
	/**
	 *  
	 * @param startingIndex
	 * @param statement
	 * @return the character index of the end of the string
	 */
	private static int validateString(int startingIndex, String statement)
	{
		
		for(int i = startingIndex+1; i<statement.length(); i++)
		{
			if(statement.charAt(i)=='"')
			{
				return i-1;
			}
		}
		
		Shell.error("Invalid String.", lineNumber);
		return -1;
	}
	
	private static void locateFunctionCalls(Statement s)
	{
		Shell.out("Looking for functions...",3);
		Container c = (Container)s.getChild(0);
		
		for(int i = 0; i<c.getSize()-1; i++)
		{
			Token t = (Token)c.getChild(i);
			Token paren = (Token)c.getChild(i+1);
			if(t.getType()==TokenType.variable)
			{
				if(paren.getType()==TokenType.operator)
				{
					// hardcoded, uh-oh.
					if(paren.getData().equals("("))
					{
						t.setType(TokenType.function);
					}
				}
			}
		}
	}
}

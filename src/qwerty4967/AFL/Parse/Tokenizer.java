package qwerty4967.AFL.Parse;

import java.util.ArrayList;

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
		int j=0;
		lineNumber=1;
		for( int i = 0; i<statements.size(); i++)
		{
			// if this method returns false, the user has made some kind of syntax error. 
			// we need to pass this info up the chain.
			
			int size = main.getSize();
			
			if(!tokenizeStatement(main, statements.get(i)))
			{
				return null;
			}
			if(size==main.getSize())
			{
				// the line was a comment.
				continue;
			}
		
			locateFunctionCalls((Statement)main.getChild(j));
			j++;
			// this code is awful.
			lineNumber=j+1;
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
			 if(currentChar == '#')
			 {
				// abort everything, fools!
				// fly! fly!
				// create a token with the last bit of data
				if(currentTokenData.length()>0)
				{
					
					 return createToken(container, currentTokenData, currentTokenType);
					
				}
				
				main.removeChild(statement);
				return true;
				// make sure it's not all borken
				// I'm keeping that spelling.
				
				
			 }
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
				 		
				 		// if it's an escape sequence. this is SUPER lazy.
				 		if(i+1<toTokenize.length())
				 		{
				 				if(toTokenize.charAt(i+1)=='\'')
				 					i++;
				 		}
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
		
		if(type== TokenType.number)
		{
			// just for consistent formatting.
			tokenData = Double.parseDouble(tokenData)+"";
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
	
	public static boolean validateNumber(String s)
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
		String character = ""+getCharacterFromExpression(startingIndex,statement);
		new Token(character,TokenType.character, parent);
		return true;
	}
	
	private static boolean validateCharacter(int startingIndex, String statement)
	{
		// perform various checks
		// first, bounds.
		if(startingIndex+2 >= statement.length())
		{
			return false;
		}
		
		// next, quotes.
		
		if(statement.charAt(startingIndex)!='\'')
		{
			return false;
		}
			
		// outer bound.
		if(statement.charAt(startingIndex+2)=='\'')
		{
			// is the character a '\'? if so, BURN IT
			
			
			
			if(statement.charAt(startingIndex+1)=='\'' )
			{
				return false;
			}
		
			// sorry, that was violent.
			if(statement.charAt(startingIndex+1)!='\\' )
			{
				return true;
			}
			
		}
		
		
		// is it an escape sequence?
		
		// check that bounds are valid.
		if(startingIndex+3 >= statement.length())
		{
			return false;
		}
		
		// check that the quoute is in the right place
		if(statement.charAt(startingIndex+3)!='\'')
		{
			return false;
		}
		
		// Check that it is an escape sequence
		if(statement.charAt(startingIndex+1)!='\\')
		{
			return false;
		}
		// it is!
		// is it valid though?
		
		char toEscape = statement.charAt(startingIndex+2);
		
		
		for(char c :Lang.ESCAPED_CHARS)
		{
			if(toEscape==c)
			{
				return true;
			}
		}
		
		return false;
		
		
	}
	
	private static char getCharacterFromExpression( int startingIndex, String expression)
	{
		// get's the intended character from an expression like 'a' or '\n'.
		// this method assumes that the expresion is a valid character.
		// get the juicy bits.
		
		expression = expression.substring(startingIndex+1);
		// okay, we need to find the first quote.
		int finalIndex = expression.indexOf('\'');
		expression = expression.substring(0,finalIndex);
		
		// now, does this look silly? yes, yes it does. but it works
		// this code is attrocious.
		if(expression.equals("\\"))
		{
			return '\'';
		}
		if(expression.length()==1)
		{
			return expression.charAt(0);
		}
		
		return getEscape(expression.charAt(1));
		
	}
	
	private static char getEscape (char c)
	{
		switch(c)
		{
			case 'n':
				return '\n';
			case '\\':
				return '\\';
			case '\'':
				return '\'';
			case '"':
				return '"';
		}
		return '?';
	}
	
	// returns length of string.
	private static int createString(int startingIndex, String statement, Container parent)
	{
		int escapes=0;
		int endingIndex = -1;
		for(int i = startingIndex+1; i<statement.length(); i++)
		{
			
			if(statement.charAt(i)=='\\')
			{
				// a chase is afoot!
				// We've found an escape sequence!
				// but we're not sure whether it is valid.
				// first we have to check that the statement continues after the \.
				if(i+1>=statement.length())
				{
					continue;
				}
				
				// okay, now we check the next character.
				char toEscape = statement.charAt(i+1);
				char escaped = getEscape(toEscape);
				// make sure it was a valid escape sequence.
				if(escaped == '?')
				{
					Shell.error("Invalid Escape Sequence in String.", lineNumber);
					return -1;
				}
				
				// okay, no doubt about it, it's valid.
				// our job is to modify the string we're building, then.
				// I've not really used StringBuilder, but it seems cool.
				StringBuilder sb = new StringBuilder(statement);
				sb.deleteCharAt(i);
				sb.deleteCharAt(i);
				sb.insert(i, escaped);
				statement=sb.toString();
				escapes++;
				continue;
			}
			
			if(statement.charAt(i)=='"')
			{
				endingIndex=i+escapes;
				break;
			}
		}
		
		if(endingIndex==-1)
		{
			Shell.error("Invalid String.", lineNumber);
			return -1;
		}
		
		// ending index is not 0.
		String string = statement.substring(startingIndex+1, endingIndex-escapes);
		
		Shell.out("Found string \""+string+"\".", 3);
		
		new Token(string,TokenType.string, parent);
		
		return endingIndex;
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
		
		// this comment is here to break the clean diagonal of the braces here.
		// it felt unnatural, I had to break it up somehow.
		
	}
}

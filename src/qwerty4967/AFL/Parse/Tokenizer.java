package qwerty4967.AFL.Parse;

import java.util.ArrayList;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.ParseTree.*;
import qwerty4967.AFL.Lang.*;

public class Tokenizer 
{
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
			if(!tokenizeStatement(main, statements.get(i), i+1))
			{
				return null;
			}
			
		}
		
		return main;
		
	}
	
	private static boolean tokenizeStatement(AFLFunction main, String toTokenize, int statementNumber)
	{
		// first, create the statement we are going to tokenize.
		Statement statement = new Statement( statementNumber, main );
		
		//next, look through each character meticulously.
		TokenType currentTokenType = null;
		TokenType currentCharType = null;
		boolean isStringMode = false;
		String currentTokenData = "";
		
		for( int i = 0; i<=toTokenize.length(); i++)
		{
			if(!isStringMode)
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
						 if(!createToken(statement, currentTokenData, currentTokenType))
						 {
							 return false;
						 }
						 currentTokenData="";
					 }
					 
					 currentTokenType=currentCharType;
					 switch(currentTokenType)
					 {
					 	
					 	case variable:
					 	case number:
					 	case operator:
					 		// just set up for next time, nothing special.
					 		currentTokenData+=currentChar;
					 		continue;
					 		break;
					 	case character:
					 		System.out.println("NOPE");
					 		return false;
					 	case string:
					 		System.out.println("NOPE");
					 		return false;
					 	default:
					 		Shell.error("internal error. Invalid token type "+currentCharType, statementNumber);
					 		System.exit(-1);
					 }
				 }
				 else
				 {
					 currentTokenData+=currentChar;
					 continue;
				 }
			}
			
		}
		return false;
	}
	
	private static TokenType getCharType( char c)
	{
		// check for numbers, simply.
		if( c>='0'||c<='9'||c=='.')
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
	
	private boolean createToken(Statement parent, String tokenData, TokenType type)
	{
		// keep in mind, characters never enter these cursed realms
		//TODO make this a seperate token.
		switch(type)
		{
			case number:
				if(!validateNumber(tokenData))
				{
					return false;
				}
			case variable:
				// can't pull out fucntions
				type=identifyKeywords(tokenData);
				break;
			case operator:
				if(!validateOperator(tokenData))
				{
					return false;
				}
		}
	}
}

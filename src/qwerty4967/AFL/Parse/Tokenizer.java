package qwerty4967.AFL.Parse;

import java.util.ArrayList;

import qwerty4967.AFL.Lang;
import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.ParseTree.*;

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
		Lang.tokenType currentTokenType = null;
		Lang.tokenType currentCharType = null;
		String currentTokenData = "";
		
		for( int i = 0; i<=toTokenize.length(); i++)
		{
			
		}
		return false;
	}
	
	
}

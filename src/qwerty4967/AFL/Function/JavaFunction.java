package qwerty4967.AFL.Function;

import qwerty4967.AFL.ParseTree.Token;
import qwerty4967.AFL.Lang.*;

public class JavaFunction extends Function 
{

	private final MethodCode FUNCTION;
	
	public JavaFunction(String name, int parameters, MethodCode function)
	{
		super(name, parameters);
		FUNCTION=function;
		
	}
	
	@Override
	public Token call(Token[] Tokens)
	{
		if(Tokens == null)
		{
			return new Token(
					"Internal Error on function '"+getName()+"': A function's parameters cannot be null.",
					TokenType.error
					);
		}
		
		if(Tokens.length!=getParameters())
		{
			return new Token(
					"Inncorrect number of parameters for function '"+getName()+"'. Expected "+getParameters()+".",
					TokenType.error
					);
		}
		
		return FUNCTION.call(Tokens);
	}

}

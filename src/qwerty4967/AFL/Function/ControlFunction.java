package qwerty4967.AFL.Function;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Lang.TokenType;
import qwerty4967.AFL.ParseTree.Token;

public class ControlFunction extends Function 
{

	public ControlFunction(String name, int params)
	{
		super(name,params);
	}
	
	@Override
	public Token call(Token[] Tokens) 
	{
		// no chance this ever happens, hopefully.
		Shell.error("Control Function '"+this.getName()+"' cannot be called in this context. [This particular message is an internal error.]",-1);
		return new Token("Error", TokenType.error);
	}

}

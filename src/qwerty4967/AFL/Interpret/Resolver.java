package qwerty4967.AFL.Interpret;
import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Lang.*;
import qwerty4967.AFL.ParseTree.*;
public class Resolver 
{
	protected static Token resolve( Container c )
	{
		
		Element e = c.getChild(0);
		if(e instanceof Token)
		{
			return useToken((Token)e);
		}
		
		// it's a function.
		Shell.error("AFL does not yet support function calls or operators. Sorry!", c.getStatementNumber());
		return new Token("Error", TokenType.error);
	}
	
	// this is primarily to grab variables.
	private static Token useToken (Token t)
	{
		if(t.getType()== TokenType.variable)
		{
			// uh oh...
			// gotta look for variables.
			Token toReturn=Namespace.getVariableValue(t, t.getFunction());
			if(toReturn==null)
			{
				//Shell.error("Variable '"+t.getData()+"' is not defined in this context.", t.getStatementNumber());
				return new Token("Error",TokenType.error);
			}
			return toReturn;
		}
		return t;
	}
}

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
			return (Token)e;
		}
		
		// it's a function.
		Shell.error("AFL does not yet support function calls or operators. Sorry!", c.getStatementNumber());
		return new Token("Error", TokenType.error);
	}
}

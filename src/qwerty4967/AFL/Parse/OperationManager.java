package qwerty4967.AFL.Parse;
import qwerty4967.AFL.ParseTree.*;
import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Lang.*;


public class OperationManager 
{
	
	protected static boolean functionizeOperators(Container c)
	{
		Shell.error("Actually, none of Tokenizer is functional.", c.getStatementNumber());
		return false;
	}
}

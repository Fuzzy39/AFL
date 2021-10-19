package qwerty4967.AFL.Interpret;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Function.*;
import qwerty4967.AFL.Lang.TokenType;
import qwerty4967.AFL.ParseTree.*;

public class Interpreter 
{
	
	// well, here we are.
	// The interpreter.
	// code is now processed effectively
	// it's time to run it.
	// and don't sweat it, 'cause I've got a plan.
	
	// not necessarily a great one.
	// but a plan.
	
	
	public static Token interpret(AFLFunction toInterpret)
	{
		// okay, the modus operandi here is we try to get the next child, and if we can't,  we call onEndOfContainer
		// when we get the next child, we check whether it's a control.
		// if it isn't, we call resolveNormally
		// if it is, we absolutely destroy whoever dare attempted to do such a thing...
		
		if(toInterpret.getSize()==0)
		{
			return null;
		}
		
		// this would work, if it weren't a control statement.
		Element currentChild = getFirstChild(toInterpret);
		
		if(currentChild instanceof Token)
		{
			// uh-oh, we didn't get the child, we got a return value.
			Token toReturn = (Token)currentChild;
			if(toReturn.getType()==TokenType.error)
			{
				Shell.errorAt(toInterpret.getName(), currentChild.getStatementNumber());	
			}
			return toReturn;
		}
		
		while(true)
		{
			// okay, so we've got the current child, so we attempt to resolve
			Token returnToken = attemptResolve(currentChild);
			// I know, this is ugly, but I'm crunched on time right now...
			if(returnToken!=null)
			{
				if(returnToken.getType()==TokenType.error)
				{
					Shell.errorAt(toInterpret.getName(), currentChild.getStatementNumber());	
				}
				return returnToken;
			}
			
			// now, we get the next child, and attemp to start again.
			currentChild=getNextChild(currentChild);
			
			if(currentChild instanceof Token)
			{
				// uh-oh, we didn't get the child, we got a return value.
				Token toReturn = (Token)currentChild;
				if(toReturn.getType()==TokenType.error)
				{
					Shell.errorAt(toInterpret.getName(), currentChild.getStatementNumber());	
				}
				return toReturn;
			}
			
		
		}
	}
	
	private static Element getFirstChild(AFLFunction function)
	{
		// this would work, if it weren't a control statement.
		Element e = function.getChild(0);
		
		// it is totally a statement of some kind though.
		Statement s = (Statement)e;
		
		// what do we do if it is?
		// well right now we can claim ineptitude.
		if(!(s instanceof ControlStatement))
		{
			return s;
		}
		
		// it is a control statement.
		Shell.error("AFL does not yet support control statements.", 1);
		return new Token ("Error", TokenType.error);
		
	}
	private static Token attemptResolve(Element toResolve)
	{
		// this is explicitly not a control statement.
		// we're assuming the 'element' is a statement.
		// is this super lazy? you bet.
		
		Statement resolvable = (Statement)toResolve;
		
		Token result = Resolver.resolve(resolvable);
		if(result!=null)
		{
			if(result.getType()!=TokenType.error)
			{
				Shell.out(result.getData());
				
			}
			else
			{
				return result;
			}
		}
		
		return null;
	}
	
	private static Element getNextChild(Element currentChild)
	{
		// currentChild, is, of course, a statement, and not a controlStatement, either.
		// get the parent
		HasChildren currentContainer = currentChild.getParent();
		int currentID=currentChild.getID();
		
		// check whether there is a next child.
		if(currentID+1 == currentContainer.getSize())
		{
			// hmm...
			// are we done here?
			if(currentContainer instanceof AFLFunction)
			{
				// we are!
				return new Token ("Void", TokenType.voidToken);
			}
			else
			{
				Shell.error("AFL does not yet support control statements, and if you're seeing this, something went wrong.", currentID);
				return new Token ("Error", TokenType.error);
			}
		}
		
		// there is, cool
		// check if it's a control statement.
		Statement nextChildCanidate = (Statement)currentContainer.getChild(currentID+1);
		if(nextChildCanidate instanceof ControlStatement)
		{
			Shell.error("AFL does not yet support control statements.", currentID+1);
			return new Token ("Error", TokenType.error);
		}
		
		// it does not.
		return nextChildCanidate;
		
	}
}

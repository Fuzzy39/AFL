package qwerty4967.AFL.Parse;
import qwerty4967.AFL.ParseTree.*;
import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Lang.*;
import qwerty4967.AFL.Lang.Operators.PRIORITY_GROUP;


public class OperationManager 
{
	
	protected static boolean functionizeOperators(Container c, boolean parameterMode)
	{
		// so, what's the plan, Stan?
		// this is actually fairly simple...
		// after writing this all out, it's only a hundred lines of code, so it seems a bit silly to make it it's own class.
		// oh well, can't really hurt.
		//int lineNumber = c.getFunction()==null?GroupManager.getStatementNumber():c.getFunction().getStatementNumber();
		
		for(PRIORITY_GROUP g: PRIORITY_GROUP.values())
		{
			// for each operator Priority group, we process every operator in that group that we come across.
			//one concern is that the container will be changing size while this happens. This might end up being an issue...
			// if it is, this entire method needs to be rethought.
			
			for(int i = 0; i<c.getSize(); i++)
			{
				
				// okay, check if the child matches.
				Element e = c.getChild(i);
				
				if(ElementIsOfOperatorGroup(e,g))
				{
					// if they do, process the token.
					Token t = (Token)e;
					Shell.out("Found Operator '"+t.getData()+"'!", 4);
					if(!validateOperator(t))
					{
						return false;
					}
					else
					{
						functionizeOperator(t);
						i-=2;
					}
				}
			}
		}
		
		if(!parameterMode)
		{
			// we need to ensure that the container only has one child.
			if(c.getSize()!=1)
			{
				Shell.error("Invalid Syntax.", c.getStatementNumber(), c.getFile());
				return false;
			}
		}
		return true;
		
	}
	
	
	private static boolean ElementIsOfOperatorGroup(Element e, PRIORITY_GROUP g)
	{
		// check if a token is a member of a given operator priority group.
		if(e instanceof Token)
		{
			Token t = (Token)e;
			if(t.getType()==TokenType.operator)
			{
				String[] searchIn = Operators.SORTED[g.ordinal()];
				for(String checkAgainst: searchIn)
				{
					if(t.getData().equals(checkAgainst))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean validateOperator(Token operator)
	{
		// check that the operator is:
		// not the first or last token
		// has two non operator neighbors
		HasChildren c = operator.getParent();
		int opID = operator.getID();
		if(opID==0 || opID==c.getSize()-1)
		{
			if(c.getSize()==1)
			{
				Shell.error("Operator '"+operator.getData()+"' doesn't have any operands. Expected two.", operator.getStatementNumber(), operator.getFile());	
			}
			else
			{
				Shell.error("Operator '"+operator.getData()+"' only has one operand. Expected two.", operator.getStatementNumber(), operator.getFile());
			}
			return false;
		}
		
		//check the previous token.
		// I don't belive there is any situation in which two operators can be next to each other,
		// unless they are parentheses, which are already handled elsewhere, 
		//so I guess this doesn't need to exist?
		// Actually, incorrect!
		if(c.getChild(opID-1) instanceof Token)
		{
			Token prev = (Token)c.getChild(opID-1);
			if(prev.getType()==TokenType.operator)
			{
				Shell.error("Operator '"+operator.getData()+"' can't act on another operator.", operator.getStatementNumber(),operator.getFile());
				return false;
			}
		}
		
		//check the next token.
		if(c.getChild(opID+1) instanceof Token)
		{
			Token next = (Token)c.getChild(opID+1);
			if(next.getType()==TokenType.operator)
			{
				Shell.error("Operator '"+operator.getData()+"' can't act on another operator.", operator.getStatementNumber(),operator.getFile());
				return false;
			}
		}
		
		// all seems clear, as far as I can tell.
		return true;
		
	}
	
	
	private static void functionizeOperator(Token operator)
	{
		// now it's time to do the actual thing this class was made for.
		// nice.
		// we need to 'tokenize' this
		//er, somehow.
		HasChildren c = operator.getParent();
	
		int ID = operator.getID();
		String op = operator.getData();
		c.removeChild(operator);
		
		// create a new function.
		FunctionCall func = new FunctionCall(op,c);
		c.moveChild(ID, func); // move it to the appropriate index.
		// now give it kids.
		
		Element prev = c.getChild(ID-1);
		func.addChild(prev); // hopefully nothing explodes here
		
		Element next = c.getChild(ID); // would be +1 but didn't that change?
		func.addChild(next);
		
	}
}

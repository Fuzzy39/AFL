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
	
	static Token toAFLReturn;
	static Element nextChild = null;
	
	public static Token interpret(AFLFunction toInterpret, Token[] parameters)
	{
		// okay, the modus operandi here is we try to get the next child, and if we can't,  we call onEndOfContainer
		// when we get the next child, we check whether it's a control.
		// if it isn't, we call resolveNormally
		// if it is, we absolutely destroy whoever dare attempted to do such a thing...
		toAFLReturn=new Token("Error",TokenType.error);
		
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
				Shell.errorAt(toInterpret.getName(), 1);	
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
			Element pastChild = currentChild;
			currentChild=getNextChild(currentChild);
			
			if(currentChild instanceof Token)
			{
				// uh-oh, we didn't get the child, we got a return value.
				Token toReturn = (Token)currentChild;
				if(toReturn.getType()==TokenType.error)
				{
					Shell.errorAt(toInterpret.getName(), pastChild.getStatementNumber()+1);	
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
		if(s instanceof ControlStatement)
		{
			nextChild=null;
			
			ControlStatement cs = (ControlStatement)s;
			
			// control statements can be nested.
			// this is an issue for the first statment.
			while(true)
			{
				nextChild=null;
				// if the control statement is return or has an error or something.
				if(!interpretControlStatement(cs))
				{
					return toAFLReturn;
				}
				
				// if the control statement doesn't call for itself to be dropped into.
				if(nextChild==null)
				{
					return getNextChild(s);
				}
				
				
				
				
				// things are not nested.
				if(!(nextChild instanceof ControlStatement))
				{
					
					return nextChild;
				}
				// they are!
				s=(Statement)nextChild;
				cs=(ControlStatement)nextChild;
			}
			
		}
		return s;
	}
	private static Token attemptResolve(Element toResolve)
	{
		// this is explicitly not a control statement.
		// we're assuming the 'element' is a statement.
		// is this super lazy? you bet.
		
		Statement resolvable = (Statement)toResolve;
		
		Token result = Resolver.resolve(resolvable.getChild(0));
		if(result!=null)
		{
			if(result.getType()!=TokenType.error)
			{
				if(result.getType()!=TokenType.voidToken)
				{
					Shell.out(result.getData());
				}
				
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
		// currentChild, is, of course, a statement.
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
				ControlStatement currentControlStatement = (ControlStatement)currentContainer;
				if(currentControlStatement.getFunctionName().equals("if"))
				{
					// we know what to do here!
					return getNextChild(currentControlStatement);
				}
				
				if(currentControlStatement.getFunctionName().equals("while"))
				{
					// we need to check the conditional, then act on it.
					nextChild=null;
					
					boolean b = processConditional(currentControlStatement);
					if(!b)
					{
						return new Token ("Error", TokenType.error);
					}
					if(nextChild==null)
					{
						// b is false. alas.
						return getNextChild(currentControlStatement);
					}
					// yet another loop
		 			return nextChild;
					
				}
				
				Shell.error("AFL has discovered a new breed of controlStatement, and if you're seeing this, something went very wrong.", currentID);
				return new Token ("Error", TokenType.error);
			}
		}
		
		// there is, cool
		
		// check if it's a control statement.
		Statement nextChildCanidate = (Statement)currentContainer.getChild(currentID+1);
		if(nextChildCanidate instanceof ControlStatement)
		{
			
			// BATTEN DOWN THE HATCHES!
			// All Men on Deck!
			// etc.
			// this is a level one gremlin conspircacy!
			nextChild=null;
			
			ControlStatement cs = (ControlStatement)nextChildCanidate;
			
			while(true)
			{
				nextChild=null;
				// if the control statement is return or has an error or something.
				if(!interpretControlStatement(cs))
				{
					return toAFLReturn;
				}
				
				// if the control statement doesn't call for itself to be dropped into.
				if(nextChild==null)
				{
					return getNextChild(nextChildCanidate);
				}
				
				
				
				
				// things are not nested.
				if(!(nextChild instanceof ControlStatement))
				{
					
					return nextChild;
				}
				// they are!
				nextChildCanidate=(Statement)nextChild;
				cs=(ControlStatement)nextChild;
			}
		}
		
		// it does not.
		return nextChildCanidate;
		
	}
	
	private static boolean interpretControlStatement(ControlStatement cs)
	{
		// have you ever consumed a spicy gremlin?
		//yes
		// fantastic!
		// well, that's what this code does, exactly
		String name = cs.getFunctionName();
		switch(name)
		{
			case "=":
				return processAssignment(cs);
			case "return":
				// returns are processed separately, because they can return things
				processReturn(cs);
				return false;
			case "if":
			case "while":
				return processConditional(cs);
			case "break":
				processBreak(cs);
				return true;
			default:
				Shell.error("AFL does not yet support control statement '"+name+"'.", cs.getStatementNumber());
				return false;
			
		}
		
		//return true;
		
		
	}
	
	private static boolean processAssignment(ControlStatement cs)
	{
		Element toAssign = cs.getParameters().getChild(1);
		Element toAssignTo = cs.getParameters().getChild(0);
		
		Token variable;
		if(!(toAssignTo instanceof Token))
		{
			Shell.error("Cannot assign values to expressions.", cs.getStatementNumber());
			return false;
		}
		variable=(Token)toAssignTo;
		
		if(variable.getType()!=TokenType.variable)
		{
			Shell.error("Cannot assign values to constants.", cs.getStatementNumber());
			return false;
		}
		// ought to be good.
		
	
		Token variableValue = Resolver.resolve(toAssign);
		if(variableValue.getType()==TokenType.error)
		{
			return false;
		}
		if(variableValue.getType()==TokenType.voidToken)
		{
			Shell.error("Variables cannot be assigned nothing.", cs.getStatementNumber());
			return false;
		}
		
		// everything *should* be valid.
		if(!Namespace.setVariable(variable, variableValue, cs.getFunction()))
		{
			return false;
		}
		
		return true;
	}
	
	private static void processReturn(ControlStatement cs)
	{
		int params = cs.getParameters().getSize();
		if(params==0)
		{
			toAFLReturn=new Token("Void",TokenType.voidToken);
			return;
		}
		// params is one
		Element returnExpression = cs.getParameters().getChild(0);
		Token toReturn = Resolver.resolve(returnExpression);
		if(toReturn.getType()==TokenType.error)
		{
			return;
		}
		// it's good
		toAFLReturn = toReturn;
		return;
	}
	
	private static boolean processConditional(ControlStatement cs)
	{
		Element condition = cs.getParameters().getChild(0);
		Token conditionValue = Resolver.resolve(condition);
		
		if(conditionValue.getType()==TokenType.error)
		{
			return false;
		}
		
		if(conditionValue.getType()!=TokenType.bool)
		{
			Shell.error("Conditional statements require boolean conditions.", cs.getStatementNumber());
			return false;
		}
		
		// things are seemingly valid.
		boolean ifCondition = Boolean.parseBoolean(conditionValue.getData());
		if(cs.getSize()==0)
		{
			ifCondition=false;
		}
		
		if(ifCondition)
		{
			nextChild=cs.getChild(0);
		}
		
		
		return true;
		
	}
	
	private static void processBreak(ControlStatement cs)
	{
		// what do we need to do?
		// the thing!
		// woo!
		ControlStatement parent = (ControlStatement)cs.getParent();
		while(true)
		{
			// get the loop that we are to break out of
			
			if(parent.getFunctionName().equals("while")) 
			{
				nextChild=getNextChild(parent);
				return;
			}
			parent = (ControlStatement)parent.getParent();
		}
	}
	
}


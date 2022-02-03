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
			return new Token("Void", TokenType.voidToken);
		}
		
		// this would work, if it weren't a control statement.
		
		Element currentChild = getFirstChild(toInterpret);
		
		if(currentChild instanceof Token)
		{
			// uh-oh, we didn't get the child, we got a return value.
			Token toReturn = (Token)currentChild;
			if(toReturn.getType()==TokenType.error)
			{
				Shell.errorAt(toInterpret.getName(), toInterpret.getStatementNumber(), toInterpret.getFile());	
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
					Shell.errorAt(toInterpret.getName(), currentChild.getStatementNumber(), toInterpret.getFile());	
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
					Shell.errorAt(toInterpret.getName(), pastChild.getStatementNumber()+1, toInterpret.getFile());	
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
			return getNextChildOfControlStatement( cs );
			
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
					Shell.out(result.toOutputString());
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
		 			if(nextChild instanceof ControlStatement)
		 			{
		 				ControlStatement cs = (ControlStatement)nextChild;
		 				return getNextChildOfControlStatement( cs );
		 			}
		 			
		 			return nextChild;
					
				}
				
				Shell.error(
						"AFL has discovered a new breed of controlStatement, and if you're seeing this, something went very wrong.",
						currentID, 
						currentChild.getFunction().getName()
						);
				return new Token ("Error", TokenType.error);
			}
		}
		
		// there is, cool
		
		// check if it's a control statement.
		Statement nextChildCanidate = (Statement)currentContainer.getChild(currentID+1);
		if(nextChildCanidate instanceof ControlStatement)
		{
			// okay, the standard thing.
			// Edges are annoying.
			return getNextChildOfControlStatement( ((ControlStatement)nextChildCanidate) );
			
		}
		
		// it does not.
		return nextChildCanidate;
		
	}
	
	private static Element getNextChildOfControlStatement(ControlStatement cs)
	{
		// BATTEN DOWN THE HATCHES!
		// All Men on Deck!
		// etc.
		// this is a level one gremlin conspircacy!
	
		
	
		
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
				return getNextChild(cs);
			}
			
			
			
			
			// things are not nested.
			if(!(nextChild instanceof ControlStatement))
			{
				
				return nextChild;
			}
			// they are!
			
			cs=(ControlStatement)nextChild;
		}
	}
	
	
	// the boolean here isn't an error condition.
	// I've forgotten what it is though
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
			case "continue":
				processContinue(cs);
				return true;
			case "error":
				processError(cs);
				return false;
			default:
				Shell.error("AFL does not yet support control statement '"+name+"'.", cs.getStatementNumber(), cs.getFunction().getFile());
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
			toAFLReturn = new Token("Error", TokenType.error);
			Shell.error("Cannot assign values to expressions.", cs.getStatementNumber(),cs.getFunction().getFile());
			return false;
		}
		variable=(Token)toAssignTo;
		
		if(variable.getType()!=TokenType.variable)
		{
			toAFLReturn = new Token("Error", TokenType.error);
			Shell.error("Cannot assign values to constants.", cs.getStatementNumber(),cs.getFunction().getFile());
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
			
			Shell.error("Variables cannot be assigned nothing.", cs.getStatementNumber(),cs.getFunction().getFile());
			toAFLReturn = new Token("Error", TokenType.error);
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
			toAFLReturn = new Token("Error", TokenType.error);
			Shell.error("Conditional statements require boolean conditions.", cs.getStatementNumber(),cs.getFunction().getFile());
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
		// what a useless comment, jeez.
		
		ControlStatement parent = (ControlStatement)cs.getParent();
		while(true)
		{
			// get the loop that we are to break out of
			
			if(parent.getFunctionName().equals("while")) 
			{
				// we found it, and now we set the next child to the element after the loop.
				// this functionally means that we stop running code in the loop and start running the code after it
				// we've broken free!
				nextChild=getNextChild(parent);
				return;
			}
			parent = (ControlStatement)parent.getParent();
		}
	}
	
	private static void processContinue(ControlStatement cs)
	{
		// Desperately pretending this method wasn't a copy-paste of processBreak...
	
		
		ControlStatement parent = (ControlStatement)cs.getParent();
		while(true)
		{
			// get the loop that we are to break out of
			
			if(parent.getFunctionName().equals("while")) 
			{
				// We found our loop.
				// we set the start of the loop as our next child.
				// this isn't good enough, and you'll shortly see why if you don't already know.
				
				// hopefully assuming that the loop has at least one child is fine.
				// why wouldn't it be?
				nextChild=parent.getChild(0);
				break;
			}
			parent = (ControlStatement)parent.getParent();
		}
		
		// that problem, you're asking?
		// well, the first child of the loop could be a control statement.
		// if it is, and we don't catch it here, we crash, because attemptResolve cannot resolve a ControlStatement.
		// I'm starting to realize that there are probably smarter ways to deal with this...
		// oh well.
		// this is probably the last of the code that I'm going to end up writing in interpreter, so...
		// it's fine, I swear.
		// whether this is a smart way to deal with this or not, it's not too hard to deal with.
		
		if(nextChild instanceof ControlStatement)
		{
			// not literal returning, in this case...
			// this class is poorly coded, I'll admit that.
			ControlStatement toReturn = (ControlStatement)nextChild;
			nextChild =  getNextChildOfControlStatement(toReturn);
		}
		
	}
	
	
	private static void processError(ControlStatement cs)
	{
		// By the time I added Error as a control Statement, I've entirely forgotten how this is supposed to work...
		// not ideal.
		
		// This should get the container or element that ought to be the error message, though we've got to check to know for sure.
		Element param = cs.getParameters().getChild(0);
		
		toAFLReturn = new Token("Error", TokenType.error);
		// We resolve it, ideally it's a string, but we need to check.
		Token errorMessage = Resolver.resolve(param);
		
		if(errorMessage.getType()!=TokenType.string)
		{
			// We gonna tussle now, big boi
			// *ahem
			// We've got an error with our error.
			if(errorMessage.getType()!=TokenType.error)
			{
				Shell.error("Parameter 0 of 'error' should be of type 'string'. ", cs.getStatementNumber(), cs.getFunction().getFile());
			}
			
			return;
		}
		
		// okay, so we know that the error message is valid.
		// should be a peice of cake.
		
		Shell.error(errorMessage.getData(), cs.getStatementNumber(), cs.getFunction().getFile());
		
	}
	
	
}


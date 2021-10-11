package qwerty4967.AFL.Parse;

import java.util.ArrayList;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Function.AFLFunction;
import qwerty4967.AFL.Interpret.Namespace;
import qwerty4967.AFL.Lang.Lang;
import qwerty4967.AFL.Lang.TokenType;
import qwerty4967.AFL.ParseTree.*;

public class Contextualizer 
{
	// find and properly manage control statements!
	// This is the third and final pass of Parser.
	
	protected static AFLFunction contextualize( AFLFunction main )
	{
		if(!extractFunctions(main))
		{
			return null;
		}
		
		if(!contextualizeFunction(main))
		{
			return null;
		}
		
		return main;
	}
	
	private static boolean extractFunctions(AFLFunction main)
	{
		// so our goal here is to find function calls and ignore all else.
		//Luckily, all of the statements are nice and lined up, no nesting, as of yet.
		// so we start by finding the definitions.
		// then we do the more complex matter of figuring out weather a function ends...
		// the thing is, this is also before control functions are sorted out from the rest of them.
		// oh well, I suppose.
		
		for(int i=0; i<main.getSize(); i++)
		{
			Statement s = (Statement)main.getChild(i);
			if(isFunctionDefinition(s))
			{
				
				i=getFunctionEnd(s);
				if(i== -1)
				{
					return false;
				}
				if(!createFunction(s, s.getID(), i))
				{
					return false;
				}
				continue;
			}
		}
		
		return true;
	}
	
	private static boolean isFunctionDefinition(Statement s)
	{
		// now, recall that functions can be overloaded, so, make sure the parameters check out.
		if(!(s.getChild(0) instanceof FunctionCall))
		{
			return false;
		}
		
		FunctionCall f = (FunctionCall)s.getChild(0);
			
		if(!f.getFunctionName().equals("function"))
		{
			return false;
			
		}
		
		// well, it's a function definition.
		// no doubt about it.
		// is it valid though? that's still an open question.
		return true;
		
	}

	
	private static int getFunctionEnd(Statement s)
	{
		// the goal here is to get the index of the last element that is part of the function, which should be
		// an 'end' function.
		int i = s.getID();
		AFLFunction main =s.getFunction();
		int depth = 1;
		
		for(i++; i<main.getSize(); i++)
		{
			Element e = ((Container)main.getChild(i)).getChild(0);
			// look for any relevant function calls.
			if(!(e instanceof FunctionCall))
			{
				continue;
			}
			FunctionCall f = (FunctionCall)e;
			String function = f.getFunctionName();
			
			if(function.equals("while")|| function.equals("if")|| function.equals("else"))
			{
				depth++;
			}
			
			if(function.equals("end"))
			{
				if(f.getSize()==0)
				{
					depth--;
				}
				else
				{
					Shell.error("invalid call of function 'end'. This function has no parameters.", f.getStatementNumber());
					return -1;
				}
			}
			
			if(depth==0)
			{
				return i;
			}
		}
		
		Shell.error("Invalid function definition. No end of function.", s.getStatementNumber());
		return -1;
	}
	
	private static boolean createFunction(Statement s, int startIndex, int endIndex)
	{
		
		if(!validateFunctionDefinition(s))
		{
			return false;
		}
		
		
		FunctionCall f =  (FunctionCall)(s.getChild(0));
		String name =((Token)f.getChild(0)).getData();
		String parameterData =((Token)f.getChild(1)).getData();
		int parameters = Integer.parseInt(parameterData);
		AFLFunction function = new AFLFunction(name, parameters); 
		
		
		// transfer tokens to function.
		AFLFunction main = s.getFunction();
		transferTokens(main, function, startIndex, endIndex);
		if(!contextualizeFunction(function))
		{
			return false;
		}
		
		if(!Namespace.addFunction(function))
		{
			Shell.error("Function '"+function.getName()+"', with "+function.getParameters()+" parameters, already exists. ", startIndex+1);
			return false;
		}
		
		return true;
				
	}
	
	private static boolean validateFunctionDefinition(Statement s)
	{
		// we already know that the definition's name is function, so for that part we are good.
		FunctionCall definition = (FunctionCall)(s.getChild(0));
		if(definition.getSize()!=2)
		{
			Shell.error("Invalid functon definition. Expected 'function(\"name\", parameterNumber)'.", s.getStatementNumber());
			return false;
		}
		
		if(!(definition.getChild(0) instanceof Token))
		{
			Shell.error("Invalid function definition. Only contstants are allowed as parameters.", s.getStatementNumber());
			return false;
		}
		
		if(!(definition.getChild(1) instanceof Token))
		{
			Shell.error("Invalid function definition. Only contstants are allowed as parameters.", s.getStatementNumber());
			return false;
		}
		
		Token name = ((Token)definition.getChild(0));
		Token parameters= ((Token)definition.getChild(1));
		
		if(name.getType()!=TokenType.string)
		{
			Shell.error("Invalid function definition. The first parameter should be a string.", s.getStatementNumber());
			return false;
		}
		
		if(parameters.getType()!=TokenType.number)
		{
			Shell.error("Invalid function definition. The second parameter should be a number.", s.getStatementNumber());
			return false;
		}
		
		String functionName = name.getData();
		
		// check the name
		for(String str: Lang.CONTROL_FUNCTIONS)
		{
			if(str.equals(functionName))
			{
				Shell.error("Invalid function definition. Control Function's are reserved name.", s.getStatementNumber());
				return false;
			}
		}
		
		if(!onlyHasLetters(functionName))
		{
			Shell.error("Invalid function definition. Valid function names only contain letters.", s.getStatementNumber());
			return false;
		}
		
		String parameterString=parameters.getData();
		
		if(parameterString.contains("."))
		{
			Shell.error("Invalid function definition. Functions must have a whole number of parameters.", s.getStatementNumber());
			return false;
		}
		
		int parameterNumber=Integer.parseInt(parameterString);
		
		if(parameterNumber<0 || parameterNumber>=256)
		{
			Shell.error("Invalid function definition. Invalid parameter number.", s.getStatementNumber());
			return false;
		}
		return true;
	}
	
	private static boolean onlyHasLetters(String str)
	{
		if (str.length()==0)
		{
			return false;
		}
		
		char[] characters = str.toCharArray();
		for(char c: characters)
		{
			if(!Character.isLetter(c))
			{
				return false;
			}
		}
		return true;
	}
	
	
	private static void transferTokens(AFLFunction oldF, AFLFunction newF, int startIndex, int endIndex)
	{
		// DOCTOR, I'VE GOT A CASE OF:
		//gremlonic-physikeosphere
		// WHATEVER SHALL I DO????
		
		// uh, whut?
		// we need  
		int length = endIndex-startIndex-1;
		
		// Straight up delete the first and last bits of the function.
		
		oldF.removeChild(endIndex);
		oldF.removeChild(startIndex);
		
		for(;length>0; length--)
		{
			Element toMove = oldF.getChild(startIndex);
			newF.addChild(toMove);
			oldF.removeChild(toMove);
		}
		
	}
	
	// a bizzare and arcane process...
	private static boolean contextualizeFunction( AFLFunction function)
	{
		// excellent design, if we're being honest
		// we need to measure depth.
		
		int depth = 0;
		Container currentContainer = null;
		ArrayList<Element> toProcess=getFunctionStatements(function);
		
		for(Element e:toProcess)
		{
			// search each element.
			// look for statements which have functions.
			Statement s = (Statement)e;
			if(s.getChild(0) instanceof Token )
			{
				addToContainer(s, currentContainer);
				continue;
			}
			
			FunctionCall f = (FunctionCall)s.getChild(0);
			String name = f.getFunctionName();
			
			// check that the funcition might be a control function, and act accordingly.
			if(isInvalidControlStatement(f))
			{
				Shell.error("Invalid Control Statement \'"+name+"\'. Wrong parameter amount.", e.getStatementNumber());
				return false;
			}
			
			switch(name)
			{
				case "if":
				case "while":
					ControlStatement cs= transformToControlStatement(s);
					addToContainer(cs, currentContainer);
					depth++;
					currentContainer=cs;
					break;
				case "end":
					function.removeChild(e);
					depth--;
					if(depth<0)
					{
						Shell.error("Misplaced 'end()'. Remove this token.", e.getStatementNumber());
						return false;
					}
					currentContainer= getHigherLevelContainer(currentContainer);
					break;
				case "return":
				case "=":
					cs =transformToControlStatement(s);
					addToContainer(cs, currentContainer);
					break;
				case "else":
					cs = transformElseToControlStatement(s);
					addToContainer(cs, currentContainer);
					currentContainer=cs;
					if(currentContainer ==null)
					{
						return false;
					}
					depth++;
					break;
				default:
					addToContainer(s, currentContainer);
			}
			
		}
		
		if( depth != 0)
		{
			Shell.error("Missing expected function 'end'.", function.getSize());
			return false;
		}
		Shell.out("Produced Function:\n"+function,4);
		return true;
	}
	
	private static ArrayList<Element> getFunctionStatements(AFLFunction function)
	{
		ArrayList<Element> toReturn= new ArrayList<Element>();
		for(int i =0; i<function.getSize(); i++)
		{
			toReturn.add(function.getChild(i));
		}
		
		return toReturn;
	}
	
	// returns true only if the statement is a control statement and invalid.
	private static boolean isInvalidControlStatement(FunctionCall f)
	{
		// this element is actually a full blown statement and we all know it
		// well, except the compiler, so better tell it.
		String name = f.getFunctionName();
		int size=f.getSize();
		switch(name)
		{
			case "if":
			case "while":
				if(size!=1)
				{
					return true;
				}
				break;
			case "else":
			case "end":
				if(size!=0)
				{
					return true;
				}
				break;
			case "return":
				if(size>=2)
				{
					return true;
				}
				break;
			case "=":
				if(size!=2)
				{
					return true;
				}
				break;
			
		}
		
		return false;
	}
	
	private static void addToContainer(Element toAdd, Container container)
	{
		if(container==null)
		{
			// nothing ought to be done
			return;
		}
		
		container.addChild(toAdd);
		// do we need to remove it from the old container?
	}
	
	private static ControlStatement transformToControlStatement(Statement s)
	{
		// is it really this simple?
		return new ControlStatement(s);
	}
	
	private static ControlStatement transformElseToControlStatement(Statement s)
	{
		// Start by getting the if's conditional.
		
		//delete the else.
		FunctionCall elseFunc =(FunctionCall)s.getChild(0);
		s.removeChild(elseFunc);
		
		
		Element ifElement = s.getParent().getChild(s.getID()-1);
		if(!(ifElement instanceof ControlStatement))
		{
			Shell.error("An If must precede an else.", s.getStatementNumber());
			return null;
		}
		ControlStatement ifStatement = (ControlStatement)ifElement;
		if(!ifStatement.getFunctionName().equals("if"))
		{
			Shell.error("An If must precede an else.", s.getStatementNumber());
			return null;
		}
		// if conditional.
		Element ifParameter = ifStatement.getParameters().getChild(0);
		
		// first, change the if to an else, then add an == false
		FunctionCall ifFunc = new FunctionCall("if", s);
		FunctionCall elseCompare = new FunctionCall("==", ifFunc);
		new Token("false",TokenType.bool,elseCompare);
		elseCompare.addChild(ifParameter);
		
		
		return new ControlStatement(s);
	}
	
	private static Container getHigherLevelContainer(Container c)
	{
		HasChildren toReturn = c.getParent();
		if(toReturn instanceof Container)
		{
			return (Container)toReturn;
		}
		
		return null;
	}
}

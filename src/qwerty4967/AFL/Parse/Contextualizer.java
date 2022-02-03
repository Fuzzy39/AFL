package qwerty4967.AFL.Parse;

import java.util.ArrayList;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Function.*;
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
				
				int x=getFunctionEnd(s);
				if(x== -1)
				{
					return false;
				}
				if(!createFunction(s, s.getID(), x))
				{
					return false;
				}
				i--;
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
			
			if(function.equals("while")|| function.equals("if")|| function.equals("else")|| function.equals("for"))
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
					Shell.error("invalid call of function 'end'. This function has no parameters.", f.getStatementNumber(),s.getFunction().getFile());
					return -1;
				}
			}
			
			if(depth==0)
			{
				return i;
			}
		}
		
		Shell.error("Invalid function definition. No end of function.", s.getStatementNumber(), s.getFunction().getFile());
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
		int parameters = (int)Double.parseDouble(parameterData);
		AFLFunction function = new AFLFunction(name, parameters,s.getStatementNumber()); 
		
		
		// transfer tokens to function.
		AFLFunction main = s.getFunction();
		
		if(!transferTokens(main, function, startIndex, endIndex))
		{
			return false;
		}
		
		if(!contextualizeFunction(function))
		{
			return false;
		}
		
		if(!Namespace.addFunction(function))
		{
			Shell.error("Function '"+function.getName()+"', with "+function.getParameters()+" parameters, already exists. ", startIndex+1,s.getFunction().getFile());
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
			Shell.error("Invalid functon definition. Expected 'function(\"name\", parameterNumber)'.", s.getStatementNumber(),s.getFunction().getFile());
			return false;
		}
		
		if(!(definition.getChild(0) instanceof Token))
		{
			Shell.error("Invalid function definition. Only contstants are allowed as parameters.", s.getStatementNumber(),s.getFunction().getFile());
			return false;
		}
		
		if(!(definition.getChild(1) instanceof Token))
		{
			Shell.error("Invalid function definition. Only contstants are allowed as parameters.", s.getStatementNumber(),s.getFunction().getFile());
			return false;
		}
		
		Token name = ((Token)definition.getChild(0));
		Token parameters= ((Token)definition.getChild(1));
		
		if(name.getType()!=TokenType.string)
		{
			Shell.error("Invalid function definition. The first parameter should be a string constant.", s.getStatementNumber(),s.getFunction().getFile());
			return false;
		}
		
		if(parameters.getType()!=TokenType.number)
		{
			Shell.error("Invalid function definition. The second parameter should be a number constant.", s.getStatementNumber(),s.getFunction().getFile());
			return false;
		}
		
		String functionName = name.getData();
		

		if(!onlyHasLetters(functionName))
		{
			Shell.error("Invalid function definition. Valid function names only contain letters.", s.getStatementNumber(),s.getFunction().getFile());
			return false;
		}
		
		// check that the name doesn't match controlFunctions.
		for(String controlFunction: Lang.CONTROL_FUNCTIONS)
		{
			if(controlFunction.equals(functionName))
			{
				Shell.error("Functions cannot have the same name as '"+controlFunction+"'. Because it is reserved for AFL. ", s.getStatementNumber(),s.getFunction().getFile());
				return false;
			}
		}
		
		String parameterString=parameters.getData();
		double parameterPotential = Double.parseDouble(parameterString);
		
		if(Math.floor(parameterPotential)!=parameterPotential)
		{
			Shell.error("Invalid function definition. Functions must have a whole number of parameters.", s.getStatementNumber(),s.getFunction().getFile());
			return false;
		}
		
		int parameterNumber=(int)(parameterPotential);
		
		if(parameterNumber<0 || parameterNumber>=256)
		{
			Shell.error("Invalid function definition. Invalid parameter number.", s.getStatementNumber(),s.getFunction().getFile());
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
	
	
	private static boolean transferTokens(AFLFunction oldF, AFLFunction newF, int startIndex, int endIndex)
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
			Statement s = (Statement)toMove;
			if(isFunctionDefinition(s))
			{
				Shell.error("Function definitions cannot be nested. Or did you forget an 'end()'?", s.getStatementNumber(),oldF.getFile());
				return false;
			}
			newF.addChild(toMove);
			oldF.removeChild(toMove);
		}
		
		//correctLineNumbers(oldF);
		//correctLineNumbers(newF);
		return true;
	}
	
  /*private static void correctLineNumbers(AFLFunction f)
	{
		for(int i = 0; i<f.getSize(); i++)
		{
			// so, just do the thing, I guess.
			Element e = f.getChild(i);
			Statement s = (Statement)e;
			s.setStatementNumber(i+1);
		}
	}*/
	
	// a bizzare and arcane process...
	private static boolean contextualizeFunction( AFLFunction function)
	{
		// excellent design, if we're being honest
		// we need to measure depth.
		
		int depth = 0;
		Container currentContainer = null; // null when container is the function.
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
				Shell.error("Invalid Control Statement \'"+name+"\'. Wrong parameter amount.", e.getStatementNumber(), e.getFunction().getFile());
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
						Shell.error("Misplaced 'end()'. Remove this token.", e.getStatementNumber(), e.getFunction().getFile());
						return false;
					}
					currentContainer= getHigherLevelContainer(currentContainer);
					break;
				case "break":
				case "continue":
					cs =transformToControlStatement(s);
					
					// check we're in a loop
					if(!isInLoop(currentContainer))
					{
						Shell.error("Cannot use '"+name+"' outside of a loop.", e.getStatementNumber(), e.getFunction().getFile());
						return false;
					}
					
					addToContainer(cs, currentContainer);
					break;
				case "error":
				case "return":
				case "=":
					cs =transformToControlStatement(s);
					addToContainer(cs, currentContainer);
					break;
					
				case "else":
					
					cs = transformElseToControlStatement(s, currentContainer);
					if(cs == null)
					{
						return false;
					}
					addToContainer(cs, currentContainer);
					currentContainer=cs;
					depth++;
					break;
				default:
					addToContainer(s, currentContainer);
			}
			
		}
		
		if( depth != 0)
		{
			
			Shell.error("Missing expected function 'end'.", currentContainer.getStatementNumber(), function.getFile());
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
			case "error":
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
		return new ControlStatement(s.getStatementNumber(), s);
	}
	
	private static ControlStatement transformElseToControlStatement(Statement s, HasChildren currentContainer)
	{
		// Start by getting the if's conditional.
		
		//delete the else.
		FunctionCall elseFunc =(FunctionCall)s.getChild(0);
		s.removeChild(elseFunc);
		
		
		Element ifElement;
		if(currentContainer==null)
		{
			ifElement= s.getParent().getChild(s.getID()-1);
		}
		else
		{
			if(currentContainer.getSize()==0)
			{
				Shell.error("An If must precede an else.", s.getStatementNumber(), s.getFunction().getFile());
				return null;
			}
			else
			{
				ifElement=currentContainer.getChild(currentContainer.getSize()-1);
			}
		}
		
		if(!(ifElement instanceof ControlStatement))
		{
			Shell.error("An If must precede an else.", s.getStatementNumber(),s.getFunction().getFile());
			return null;
		}
		ControlStatement ifStatement = (ControlStatement)ifElement;
		if(!ifStatement.getFunctionName().equals("if"))
		{
			Shell.error("An If must precede an else.", s.getStatementNumber(), s.getFunction().getFile());
			return null;
		}
		// if conditional.
		Element ifParameter = ifStatement.getParameters().getChild(0);
		
		// first, change the if to an else, then add an == false
		FunctionCall ifFunc = new FunctionCall("if", s);
		FunctionCall elseCompare = new FunctionCall("==", ifFunc);
		new Token("false",TokenType.bool,elseCompare);
		elseCompare.addChild(ifParameter.copy());
		
		
		return new ControlStatement(s.getStatementNumber(), s);
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
	
	private static boolean isInLoop(HasChildren s)
	{
		// loop-de-doop, poop-de-loop
		HasChildren parent=s;
		while(parent instanceof ControlStatement)
		{
				ControlStatement cs = (ControlStatement)parent;
				if(cs.getFunctionName().equals("while"))
				{
					return true;
				}
				parent = cs.getParent();
		}
		return false;
		
	}
}

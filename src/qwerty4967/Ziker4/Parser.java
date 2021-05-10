package qwerty4967.Ziker4;

import java.util.ArrayList;

public class Parser 
{
	public static void test() 
	{
		// I would be surprised if you are reading this comment, because it means that this method wasn't
		// deleted.
		// it was a test of the tree structure
		// the expected output:
		/*
		 *  Ziker 4: REPORT: FunctionalGroup:
			name: test
			returnType:none
			perameters:[]
			
			Children:
			    DataElement [data=[]]
			    ElementContainer:
			        DataElement [data=[]]
			        ElementContainer:
			            DataElement [data=[]]
			            DataElement [data=[]]
			    DataElement [data=[]]
		 */
		
		FunctionalGroup test = new FunctionalGroup("test");
		try
		{
			// make a big 'ol tree
			new StatementData(test);
			Node node1 = new Node(test);
			new StatementData(test, node1);
			Node node2 = new Node(test, node1);
			new StatementData(test, node2);
			new StatementData(test, node2);
			//new StatementData(test, node1);
			new StatementData(test);
			
		}
		catch(Exception e)
		{
			Shell.out("Uh-oh, it broke");
			return;
		}
		
		Shell.out("REPORT: "+ test.toString(),"Ziker 4" );
	}
	
	
	public static void Parse( String code)
	{
		// okay.
		
		// We've got a string of code.
		// that's nice.
		// all we need to do is turn a string into a tree structure reflecting the control structures in the code as well
		// as order of operations, with annotated types along the way.
		
		// how hard could that possibly be?
		
		// all kidding aside, I do have a loose plan here.
		
		// there will be 3 'passes' on the code
		
		// in the preliminary pass, the one string will be broken into segments (statements).
		// in the control pass, the statements will be organized into a tree based on basic control structures (ifs, loops, etc.)
		// in the syntax pass, the statements will be broken into tokens, and the tokens will be tagged with their type, operator, int, variable, etc.
		
		// that sounds a bit less daunting. 
		
		// kinda...
		
		// no time for dwaddling, let's get a move on.
		
		
		try // this feels clumsy, and it is...
		{
			Shell.out("Begining Tree Construction...","Ziker 4",1);
			
			// preliminary pass is fairly simple.
			Shell.out("Beggining Preliminary Pass (Pass 0)","Ziker 4",3);
			ArrayList<String> statements = preliminaryPass(code);
			Shell.out("Results of pass 0:\n"+statements,"Ziker 4", 1);
			
			// control pass.
			Shell.out("Beggining Control Pass (Pass 1)","Ziker 4",3);
			FunctionalGroup program = controlPass(statements);
			if(program== null)
			{	
				Shell.out("Control pass 1 errored out... returning","Ziker 4",2);
				return;
			}
			Shell.out("Results of pass 1:\n"+program,"Ziker 4",1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	private static ArrayList<String> preliminaryPass(String code)
	{
		// okay, how hard can this be?
		// time to learn about string tokenizers.
		// or maybe even those stream tokenizers, if they're more usefull.
		
		ArrayList<String> toReturn = new ArrayList<String>();
		
		if(!code.contains(";"))
		{
			Shell.out("Code contained 1 statement.","Pass 0",2);
			toReturn.add(code);
			return toReturn;
		}
		
		String[] tokens = code.split(";");
		
		// we need to remove any and all whitespace from our precious new baby statements.
		
		for(int i = 0; i<tokens.length;i++)
		{
			String token = tokens[i].strip();
			
			// if a token is just whitespace it's probably easier to toss it now.
			if(token.equalsIgnoreCase(""))
			{
				continue;
			}
			
			toReturn.add( token);
		}
		
		Shell.out("Code contained "+toReturn.size()+" statements.","Pass 0",2);
		return toReturn;
	
		// easy peasy.
		
	}
	
	private static FunctionalGroup controlPass(ArrayList<String> statements) throws Exception
	{
		// pfft... okay, I've been thinking about this for a while.
		// this doesn't seem too hard, with the major exception of functions.
		// what's basically gonna happen is if we find a function ( or block) we toss it in another method and run away.
		// in the case of a block we replace the declaration of the block with a function call to it. 
		//goto statements would then move the pointer to that statement.
		
		// or something... most of the details there aren't clear
		
		// create the main FunctionalGroup
		FunctionalGroup program = new FunctionalGroup("_MAIN");
		ElementContainer currentParent = null;
		//first thing's first however. we loop through the ArrayList.
		
		for(int i = 0; i<statements.size(); i++)
		{
			String statement=statements.get(i);
			
			// if we find a CONTROL_KEYWORD
			boolean isControl = false;
			
			// then we loop through each CONTROL_KEYWORD and look for a match.
			for(int j = 0; j<Lang.CONTROL_KEYWORDS.length; j++)
			{
				// make sure the statement is long enough to do that though.
				if(statement.length()<Lang.CONTROL_KEYWORDS[j].length())
				{
					continue;
				}
				
				// now do the actual checking.
				
				if(statement.equalsIgnoreCase(Lang.CONTROL_KEYWORDS[j]))
				{
					isControl=true;
					
					switch(Lang.CONTROL_KEYWORDS[j])
					{
						case "else":
							// for an else to make any sense it has to go after an if.
							// this if is completely nonsensical, but if I did it right it checks that the parent is not an if.
							if(! ((String) ((StatementData)currentParent.getChild(0)).getData(1)).substring(0,2).equalsIgnoreCase("if"))
							{
								Shell.out("Error: No if procedes this else.","Ziker 4",0,i);
								return null;
							}
							// we have determined the else makes sense.
							// now to make it into a reality.
							// this replaces the else.
							statement="if("+((String) ((StatementData)currentParent.getChild(0)).getData(1)).substring(2)+"==false)";
							currentParent = currentParent.getParent();
							// then we can just treat it like an if, I think.
						case "if":	
						case "while":
							// so now we make a new container and set it as our current parent...
							currentParent = createNode(program, currentParent);
							
							// then we make an aditional dataElement.
							// after trimming the front off.
							
							StatementData controlConditional = new StatementData(program,currentParent);
							
							//statement.substring(Lang.CONTROL_KEYWORDS[j].length());
							
							ArrayList<Object> data = new ArrayList<Object>();
							data.add(i); // statement number.
							data.add(statement);
							
							
							controlConditional.setData(data);
							continue;
						
							
							
						case "end":
							// we go up one in the order.
							if(currentParent == null)
							{
								// this code is rather invalid...
								Shell.out("Error: Keyword 'end' doesn't match any block declaration.","Ziker 4",0,i);
								return null;
							}
							
							if(currentParent.getParent()==null)
							{
								currentParent=null;
								continue;
							}
							
							currentParent = currentParent.getParent();
							
							continue;
							
						case "function":
							// oh my
							// what to do here...
							throw new Exception("NO FUNCTIONS RIGHT NOW YOU IMBECILE");
						default:
							throw new Exception("Something's broken...");
						
					}
					
				}
				
				// code is not control code, just go ahead and add it to the pile.
				StatementData statementData;
				
				if(currentParent!=null)
				{
					statementData= new StatementData(program,currentParent);
				}
				else
				{
					statementData= new StatementData(program);
				}
				
				//statement.substring(Lang.CONTROL_KEYWORDS[j].length());
				
				ArrayList<Object> data = new ArrayList<Object>();
				data.add(i); // statement number.
				data.add(statement);
				
				
				statementData.setData(data);
			}
		}
		
		return program;
	}
	
	private static Node createNode(FunctionalGroup program, ElementContainer parent)
	{
		if( parent==null)
		{
			return new Node(program);
		}
		
		try
		{
			return new Node(program, parent);
		}
		catch(Exception e)
		{
			System.out.println(e);
			Shell.out("SOMETHING HAS GONE WRONG. expect unstable behavior.","Ziker 4");
			return null; // it should really just crash at this point.
		}
		
	}
	
	
}

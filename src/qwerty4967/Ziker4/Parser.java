package qwerty4967.Ziker4;

import java.util.ArrayList;

public class Parser 
{
	/**
	 * For one reason or another, I haven't deleted this method
	 * I may as well right now...
	 * I don't know, it's kinda fun to keep it semi-hidden.
	 * of course it's the first one in the code, right at the top.
	 */
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
	
	
	/**
	 * this thing does a lot. It turns a string containing a program into something that ziker can interpret.
	 * that's the goal, at least.
	 * 
	 * @param code the code to be parsed
	 */
	public static void Parse( String code)
	{
		// okay.
		
		// We've got a string of code.
		// that's nice.
		// all we need to do is turn a string into a tree structure reflecting the control structures in the code as well
		// as order of operations, with annotated types along the way.
		
		// how hard could that possibly be?
		
		// all kidding aside, I do have a loose plan here.
		
		// there will be 3 'passes' on the code ( later: this is dated, expect 5)
		
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
			Shell.out("Begining Preliminary Pass (Pass 0)","Ziker 4",3);
			ArrayList<String> statements = preliminaryPass(code);
			Shell.out("Results of pass 0:\n"+statements,"Ziker 4", 1);
			
			// control pass.
			Shell.out("Begining Control Pass (Pass 1)","Ziker 4",3);
			FunctionalGroup program = controlPass(statements);
			if(program== null)
			{	
				Shell.out("Pass 1 errored out... returning","Ziker 4",2);
				return;
			}
			Shell.out("Results of pass 1:"+program+"\n","Ziker 4",1);
			
			// token pass.
			Shell.out("Begining Token Pass (Pass 2):\n","Ziker 4",3);
			program=tokenPass(program);
			if(program== null)
			{	
				Shell.out("Pass 2 errored out... returning","Ziker 4",2);
				return;
			}
			Shell.out("Results of pass 2:"+program+"\n","Ziker 4");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		
		
		
		
	}
	
	/**
	 * does some basic work on the code, just splits by ;
	 * @param code the code to be parsed
	 * @return an array of statements
	 */
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
	
	/**
	 * The second pass.
	 * Detects if, else, while and end, and begins the construction of the program.
	 * Also can give errors on occasion, if these are improperly formatted
	 * @param statements a sequence of programs
	 * @return the begining of a program.
	 * @throws Exception if something goes wrong. shouldn't happen.
	 */
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
		
		loop:for(int i = 0; i<statements.size(); i++)
		{
			String statement=statements.get(i);
			
			// if we find a CONTROL_KEYWORD
		
		
			
			// then we loop through each CONTROL_KEYWORD and look for a match.
			for(int j = 0; j<Lang.CONTROL_KEYWORDS.length; j++)
			{
				String controlWord = Lang.CONTROL_KEYWORDS[j];
				
				// make sure the statement is long enough to do that though.
				if(statement.length()<controlWord.length())
				{
					continue;
				}
				
				// now do the actual checking.
				
				if(statement.substring(0,controlWord.length()).equalsIgnoreCase(controlWord))
				{
					
					Shell.out("Detected a(n) "+controlWord,"Pass 1",3);
					switch(controlWord)
					{
						
						case "else":
							// for an else to make any sense it has to go after an if.
							// this if is completely nonsensical, but if I did it right it checks that the parent is not an if.
							
								
							if(		currentParent==null||
									! ((String) ((StatementData)currentParent.getChild(0)).getData(1)).substring(0,2).equalsIgnoreCase("if")
							)
							{
								Shell.out("Error: No 'if' statement procedes this else.","Ziker 4",0,i);
								return null;
							}
							
							// Else tokens cannot have anything else after them.
							if(! statement.equalsIgnoreCase(controlWord))
							{
								Shell.out("Error: Unexpected Tokens after 'else'","Ziker 4",0,i);
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
							data.add(i+1); // statement number.
							data.add(statement);
							
							
							controlConditional.setData(data);
							continue loop;
						
							
							
						case "end":
							// we go up one in the order.
							if(currentParent == null)
							{
								// this code is rather invalid...
								Shell.out("Error: Keyword 'end' doesn't match any block declaration.","Ziker 4",0,i+1);
								Shell.out("Program's state at time of error:"+program,"Ziker 4", 3);
								return null;
							}
							
							// end can't have other tokens after it, much like else.
							if(! statement.equalsIgnoreCase(controlWord))
							{
								Shell.out("Error: Unexpected Tokens after 'end'","Ziker 4",0,i);
								return null;
							}
							
							if(currentParent.getParent()==null)
							{
								currentParent=null;
								continue loop;
							}
							
							currentParent = currentParent.getParent();
							
							continue loop;
							
						case "function":
							// oh my
							// what to do here...
							throw new Exception("Function's aren't currently implemented.");
						default:
							throw new Exception("Something's broken...");
						
					}
					
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
			data.add(i+1); // statement number.
			data.add(statement);
			
			
			statementData.setData(data);
			
		}
		
		if(currentParent!=null)
		{
			// somebody forgot ends!
			Shell.out("Error: Unexpected end of input.","Ziker 4");
			return null;
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
	

	/**
	 * third pass.
	 * attempts to identify tokens in the baby program
	 * tokens that cannot be identified are identified in the fourth pass, the validation pass
	 * no validation is done for this one though.
	 * @param program
	 * @return
	 * @throws Exception
	 */
	private static FunctionalGroup tokenPass(FunctionalGroup program) throws Exception
	{
		// below is a passage not entirely relevant to understandng this method:
		// --------------------------------------------
		
		// So....
		// how hard could this part possibly be?
		// (a note to past self: GAAAAAAAAAAAAAAAAAAAAAAAHHHHHH)
		// Well I'm going to figure maybe twice as hard as pass 1, since this pass will have two parts.
		
		// in between the last sentence and this one, I decided to make a third pass, though I might not depending
		// on how complicated things end up being.
		
		// uh... right then.
		// what will token pass do?
		// it will take each statementData in the program and turn it into a statement, though in this pass
		//  the statementData in the satement won't be structured. That's for the final pass.
		// I should look at Stream tokenizer, probably.
		
		// hmm... turns out it isn't very usefull
		// or it could be but it would probably be easier to just do it myself
		// I think
		// the only usefull thing is it's ablity to recognize strings.
		// in Ziker 2 and 3 that was quite the challenge.
		// though I don't remember why...
		// seems simple enough?
		
		// okay, okay.
		// so I've done some thinking.
		// this shouldn't be any more daunting than controlPass.
		// which was still a bit daunting, but whatever.
			
		// okay, code time.
		// first thing's first.
		// building the classes I forgot I needed for this...	
		// TokenData and Statement, for future reference.
		
		// ---------------------------------
		
		// this method is responsible for turning a big string into unstructured TokenData.
		// the TokenData is structured in finalPass.
		
		// We need to start by 'looping' ( climbing maybe? it is a tree... ) the program, and
		// turning each StatementData into a Statement, which will be a separate method.
		// that sounds like it makes sense.
		
		// you know, since this is gonna be used in multiple places, I should make it it's own thing.
		// needs to be an independent class...
		
		// done!
		
		// it *probably* works.
	
		TreeClimber climber = new TreeClimber(program, false);
		while(true)
		{
			
			DataElement data = climber.nextData();
			if(data==null)
			{
				break;
			}
			 
			
			Shell.out("Processing statement "+data.getData(0)+":\n", "Pass 2", 2);
			Shell.out("data: \""+ data.getData(1)+"\" parent: "+data.getParent(),"Pass 2", 3);
			parseLine((String)data.getData(1), (int)data.getData(0), program, data.getParent());
			data.remove();
			
			
		}
		return program;
		
		// okay, let's talk specifics now.
		// once we've gotten the line we need to look at, we will go character by character.
		
		
	}
	
	/**
	 * uh, what does this do?
	 * I wrote this a week ago, I have no clue.
	 * It takes a statementdata and turns it into a statement.
	 * It helps with tokenPass
	 * 
	 * @param data
	 * @param lineNumber
	 * @param program the program
	 * @param parent the parent of the statement. if the statement is in root, this is null.
	 * @return a statement, naturally.
	 * @throws Exception if something goes wrong. shouldn't happen.
	 */
	private static Statement parseLine( String data, int lineNumber, FunctionalGroup program, ElementContainer parent) throws Exception
	{
		// first of all, create the statement.
		Statement statement;
		if(parent==null)
		{
			statement = new Statement(lineNumber,program);
		}
		else // what a surprise...
		{
			try 
			{
				statement = new Statement(lineNumber,program,parent);
			} 
			catch (Exception e) 
			{
			
				e.printStackTrace();
				return null; // meh. lazy
			}
			
		}
		
		// now do the hard part....
		// so... how?
		// with one line, of course.
		findTokens(data, program, statement);
		
		
		return statement;
	}
	
	/**
	 * This takes the statement's data and turns it into tokens, without validation or anything nice
	 * it gives a very rough picture, basically speaking.
	 * 
	 * @param data
	 * @param program
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	private static  ArrayList<TokenData> findTokens(String data ,FunctionalGroup program, ElementContainer parent) throws Exception
	{
		// what does this need to do?
		// actually do the hard part, find the tokens.
		// how will it do that, you ask?
		// Character by character.
		// that kinda sounds like a joke, but no
		// okay as I write this my eyelid is twitching and it's very hard to think
		//I have a plan though
		// it's been brewing all of today
		//we have an string that contains the current token
		// and a thing which includes it's type.
		// or, more specifically, 'prototype'
		// because keywords, functions, variables and booleans look pretty similar - they're just words, after all.
		// numbers and operators are different though.
		// so at first it will be one of three types.
		
		// though operators and the 'word tokens' will be properly classified in another method.
		// so, without (much) further ado
		// let's begin.
		
		ArrayList<TokenData> tokens = new ArrayList<TokenData>();
		
		String currentToken = "";
		int type = -1;
		while(data.length()>0)
		{
			
			
			
			
			// take this character to take a good look at it.
			char identifier = data.charAt(0);
			data=data.substring(1);
			int characterType=-1;		
		
			// we need to find identifiers for everthing, then see if they match.
			if(Character.isWhitespace(identifier))
			{
				if(type!=Lang.tokenTypes.string.ordinal()&type!=Lang.tokenTypes.character.ordinal()&type!=-1)
				{
					// it's time for a new token.
					tokens.add(createToken(currentToken, type, program, parent));
					currentToken="";
					type = -1;
					continue;
				}
				
				if(type==-1)
				{
					// we don't want to do anything with a random piece of whitespace.
					continue;
				}
				
			}
			else if(identifier=='"')
			{
				// we have found a string
				characterType=Lang.tokenTypes.string.ordinal();
				
			}
			
			else if(identifier=='\'')
			{
				// ah, a character
				characterType=Lang.tokenTypes.character.ordinal();
				
			}
			
			else if(isNumber(identifier) )
			{
				// a number
				characterType=Lang.tokenTypes.number.ordinal();
				
			}
			
			else if(isLetter(identifier) )
			{
				// could be anything
				characterType=Lang.tokenTypes.prototext.ordinal();
				
			}
			else
			{
				// if it's not a number or a letter, it's probably a symbol
				// we don't check if things are valid at this point.
				characterType=Lang.tokenTypes.operator.ordinal();
			}
			
			
			// now that we've figured out the type of the character, we must do a bespoke response.
			switch(type)
			{
			
				case -1:
					type = characterType;
					currentToken="";
					if(characterType!=Lang.tokenTypes.string.ordinal()
					& characterType!=Lang.tokenTypes.character.ordinal())
					{
						currentToken+=identifier; // seems hacky.
					}
					continue;
				case 4: // character ( I would use the enum directly, but apparently I have to jump through hoops for that, so I'm picking the lazy route, this thing is due soon)
				case 5: // string
					// we ignore everything except itself.
					if(characterType == type)
					{
						tokens.add(createToken(currentToken,type,program, parent));
						currentToken=""; 
						type = -1;
						continue;
					}
					currentToken+=identifier; // add the token to the thing.
					continue;
				// otherwise, anything will disturb things. (Later: what did this comment mean?)
				default:	
					if(characterType != type)
					{
						tokens.add(createToken(currentToken,type,program, parent));
						currentToken="";
						if(characterType!=Lang.tokenTypes.string.ordinal()
						& characterType!=Lang.tokenTypes.character.ordinal())
						{
							currentToken+=identifier; // seems hacky.
						}
						type = characterType;
						continue;
					}
					currentToken+=identifier; // add the token to the thing.
					continue;
				
			}
			
		
		}
		// add the dregs
		//System.out.println(currentToken);
		if(currentToken!="")
		{
			tokens.add(createToken(currentToken, type, program, parent));
		}
		return tokens;
		
	}
	/**
	 * this just does the busywork involved in making a token.
	 * just a whole lot easier.
	 * @param tokenText
	 * @param type
	 * @param program
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	private static TokenData createToken(String tokenText, int type, FunctionalGroup program, ElementContainer parent) throws Exception
	{
		Shell.out("Creating token with data: \""+tokenText+"\"", "Pass 2", 3);
		TokenData token;
		assert parent!=null;
		
			
		
		
		token = new TokenData(program, parent);
		
		
		ArrayList<Object> dataData = new ArrayList<Object>();
		dataData.add(tokenText);
		dataData.add(type);
		token.setData(dataData);
		return token;
		
	}
	
	/**
	 * checks whether a character is a letter from a-z to A-Z
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isLetter(char c)
	{
		 return (c >= 'a' && c <= 'z') ||
		           (c >= 'A' && c <= 'Z');
	}
	
	/**
	 * checks whether a character is part of a number, 0-9 and .
	 * @param c
	 * @return
	 */
	private static boolean isNumber(char c)
	{
		 return (c >= '0' && c <= '9') || c=='.';
	}
}

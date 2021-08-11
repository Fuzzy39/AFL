package qwerty4967.Ziker4;

import java.util.ArrayList;

import qwerty4967.Ziker4.FunctionStructures.*;

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
			//new StatementData(test);
			Node node1 = new Node(test);
			TokenData t =new TokenData(test, node1);
			ArrayList<Object> data = new ArrayList<Object>();
			data.add("data");
			data.add(7);
			t.setData(data);
			moveToken(t,node1);
			//t.remove();
			System.out.println("Node:"+node1);
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
		
		// there will be 3 'passes' on the code ( later: this is dated, expect 5) (even later: very dated, expect 4?)
		
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
			
			// token pass.
			Shell.out("Begining Token Pass (Pass 1)","Ziker 4",3);
			FunctionalGroup program = tokenPass(statements);
			if(program== null)
			{	
				Shell.out("Pass 1 errored out... returning","Ziker 4",2);
				return;
			}
			Shell.out("Results of pass 1:"+program+"\n","Ziker 4",1);
			
			// operations pass
			Shell.out("Begining Operation Pass (Pass 2)","Ziker 4",3);
			program = operationPass(program);
			if(program== null)
			{	
				Shell.out("Pass 2 errored out... returning","Ziker 4",2);
				return;
			}
			Shell.out("Results of pass 2:"+program+"\n","Ziker 4",0);
			
			/*
			Shell.out("Begining Token Pass (Pass 2):\n","Ziker 4",3);
			program=tokenPass(program);
			if(program== null)
			{	
				Shell.out("Pass 2 errored out... returning","Ziker 4",2);
				return;
			}
			Shell.out("Results of pass 2:"+program+"\n","Ziker 4");
			*/
			
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
	 * This code is currently unused and might be deleted in the future.
	 * It has many many errors in it due to how the structure of the program has changed since it
	 * stopped being used.
	 * @return the begining of a program.
	 * @throws Exception if something goes wrong. shouldn't happen.
	 */
	/*private static FunctionalGroup controlPass(ArrayList<String> statements) throws Exception
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
	}*/
	
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
	 * second pass.
	 * attempts to identify tokens in the baby program
	 * tokens that cannot be identified are identified in the fourth pass, the validation pass
	 * no validation is done for this one though.
	 * @param program
	 * @return
	 * @throws Exception
	 */
	private static FunctionalGroup tokenPass(ArrayList<String> statements) throws Exception
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
	
		
		
		// okay, let's talk specifics now.
		// once we've gotten the line we need to look at, we will go character by character.
		
		// blah blah blah - yeah okay, flipping this on it's head now
		// it makes a ton more sense if this is the first pass, doesn't it
		// do we don't need to do any monkeying around in a tree.
		// yeah?
		// lets go, then.
		
		// yep, same stuff.
		FunctionalGroup program = new FunctionalGroup("_MAIN");
		
		//then it should be simple?
		
		for( int i=0; i<statements.size(); i++)
		{
			if(parseLine(statements.get(i), i+1, program, null)==null)
			{
				return null;
			}
			
		}
		
		return program;
		// a lot of comments for very little substance.
		
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
		if(!verifyTokens(findTokens(data, program, statement)))
		{
			// oof, an error
			return null;
		}
		
		
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
				Shell.out("Found a string","Pass 1",3);	
				characterType=Lang.tokenTypes.string.ordinal();
				
			}
			
			else if(identifier=='\'')
			{
				// ah, a character
				Shell.out("Found a character","Pass 1",3);	
				characterType=Lang.tokenTypes.character.ordinal();
				
			}
			
			else if(isNumber(identifier) )
			{
				// a number
				//Shell.out("Found a number","Pass 1",3);	
				characterType=Lang.tokenTypes.number.ordinal();
				
			}
			
			else if(isLetter(identifier) )
			{
				// could be anything
				//Shell.out("Found a prototext","Pass 1",3);	
				characterType=Lang.tokenTypes.prototext.ordinal();
				
			}
			else
			{
				//Shell.out("Found an operator","Pass 1",3);	
				// if it's not a number or a letter, it's probably a symbol
				// we don't check if things are valid at this point.
				// ooh, a dirty edge case I have to fix.
				// great.
				
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
				case 7: // operator
					//uh, okay
					// so if we have "()" we need to split it up.
					// or "[]"
					
					if(identifier == ')' || identifier=='(' )
					{
						tokens.add(createToken(currentToken,type,program, parent));
						currentToken=identifier+"";
						tokens.add(createToken(currentToken,type,program, parent));
						currentToken="";
						type=-1;
						continue;
					}
					
					if(currentToken.equals(")")||currentToken.equals("("))
					{
						
						if(characterType==type)
						{
							tokens.add(createToken(currentToken,type,program, parent));
							currentToken=identifier+"";
							continue;
						}
					}
					
				
					
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
		
		if(type==Lang.tokenTypes.string.ordinal()|| type==Lang.tokenTypes.character.ordinal())
		{
			// this is absolutely horrific.
			// so is my spelling, according to eclipse.
			if(currentToken.length()==0||
			(!((currentToken.charAt(currentToken.length()-1)=='"'&type==Lang.tokenTypes.string.ordinal())||
			(currentToken.charAt(currentToken.length()-1)=='\''& type==Lang.tokenTypes.character.ordinal()))))	
			{
				Shell.out("Error: No ending \" or '","Ziker 4",0);
				return null;
			}
		}
		
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
		Shell.out("Creating token with data: \""+tokenText+"\"", "Pass 1", 3);
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
	
	private static boolean isValidNumber(String s)
	{
		/*
		 * valid:
		 * 01928
		 * 90.30
		 * .30
		 * 
		 * invalid:
		 * ....
		 * 20.
		 * 36.39.00
		 * 
		 */
		 if(s.charAt(s.length()-1)=='.')
		 {
			 
			 return false;
			 
		 }
		 
		 int dots=0;
		 for(int i=0; i<s.length(); i++)
		 {
			 char c = s.charAt(i);
			
			 
			 if(c=='.')
			 {
				 dots++;
			 }
			
			 if(dots>1)
			 {
				 return false;
			 }
			 
		 }
		 // you've probably got a problem there...
		 // shouldn't ever get here.
		 return true;
	}
	
	
	
	private static boolean verifyTokens(ArrayList<TokenData> tokens) throws Exception
	{
		/*
		 * so, we have a bunch of tokens with, uh, types
		 * lets make sure those types make sense.
		 * 
		 */
		
		if(tokens==null || tokens.size()==0)
		{
			// clearly not valid.
			return false;
		}
		
		// that's a bit of a complicated way to get this marginally useful number, but there you go.
		int statementNumber=((Statement)(tokens.get(0).getParent())).getStatementNumber();
		
		// okay if  things are gonna do things we need to loop through every token and do certain things depending on what type they are.
		
		loop: for(int i = 0; i<tokens.size(); i++)
		{
			String data = (String)(tokens.get(i).getData(0));
			switch( (int)(tokens.get(i).getData(1)) ) // switch on the token type
			{
				case 3: // number. there ought to be a better way to type this, but noo, it wants constant things. grumble grumble.
					if(isValidNumber(data))
					{
						continue;
					}
					
					Shell.out("Error: \""+data+"\" isn't a valid number.","Ziker 4",0,statementNumber);
					return false;
				case 5: // character
					if(data.length()==1)
					{
						continue;
					}
					
					Shell.out("Error: \""+data+"\" isn't a valid character.","Ziker 4",0,statementNumber);
					return false;
				case 7: // operators
					for(int j=0; j<Lang.OPERATORS.length; j++)
					{
						
						for(int k=0; k<Lang.OPERATORS[j].length; k++)
						{
							if(Lang.OPERATORS[j][k].equalsIgnoreCase(data))
							{
								continue loop;
							}
						}
						
					}
					Shell.out("Error: \""+data+"\" isn't a valid operator.","Ziker 4",0,statementNumber);
					return false;
					
					
				case 8: // the big one, prototext.
					for(int j=0; j<Lang.KEYWORDS.length; j++)
					{
						if(Lang.KEYWORDS[j].equalsIgnoreCase(data))
						{
							ArrayList<Object> newData = new ArrayList<Object>();
							newData.add(data);
							newData.add(Integer.valueOf(2)); // keyword
							tokens.get(i).setData(newData);
							continue;
						}
					}
					
					for(int j=0; j<Lang.BOOLS.length; j++)
					{
						if(Lang.BOOLS[j].equalsIgnoreCase(data))
						{
							ArrayList<Object> newData = new ArrayList<Object>();
							newData.add(data);
							newData.add(Integer.valueOf(6));// boolean
							tokens.get(i).setData(newData);
							continue;
						}
					}
					
			}
		}
		
		// note to self
		// this function isn't done, here loop through things again and sort out variables and functions
		
		// thanks, past self, I did it, are you proud?
		// ...
		// I guess you don't exist, do you?
		// hmm.

		
		for(int i = 0; i<tokens.size(); i++)
		{
			String data = (String)(tokens.get(i).getData(0));
			if((int)(tokens.get(i).getData(1))==8) //find prototext
			{
				// exempt the last entry from being checked as a function
				if(i==tokens.size()-1)
				{
					ArrayList<Object> newData = new ArrayList<Object>();
					newData.add(data);
					newData.add(Integer.valueOf(0));//variable
					tokens.get(i).setData(newData);
					continue;
				}
				
				if((int)(tokens.get(i+1).getData(1))==7) // check for an operator after
				{
					if(((String)(tokens.get(i+1).getData(0))).equals("("))
					{
						// it's a function
						// PANIC
						/*Shell.out("Functions are not currently implemented.", "Ziker 4");
						return false;*/
						
						ArrayList<Object> newData = new ArrayList<Object>();
						newData.add(data);
						newData.add(Integer.valueOf(1));//function
						tokens.get(i).setData(newData);
						continue;
						
					}
				}
				
				ArrayList<Object> newData = new ArrayList<Object>();
				newData.add(data);
				newData.add(Integer.valueOf(0));//variable
				tokens.get(i).setData(newData);
				continue;
			}
			
			
			
			
		}
		
		
		return true;
		
		
	}
	
	
	private static FunctionalGroup operationPass(FunctionalGroup program) throws Exception
	{
		//Alright...
		// How hard could this possibly be?
		// for every priority group we...
		// do a bunch of loops?
		//
		
		
		// here's the situation
		// we are going to loop through every statement and magic it away
		// and another (recursive) function will be responsible for processing that statement.
		// so this function will be relatively simple.
		
		// also we create a dummy to store groups ( think '(1+1)')
		Statement groupDummy;
					
		
		for(int i = 0; i<program.getSize(); i++)
		{
			// this will throw an exception if the cast fails, but if it fails
			// we have some big problems.
			
			
			groupDummy = new Statement(i+1, program);
			ElementContainer c = (ElementContainer)program.getChild(i);
			
			if(c instanceof Statement)
			{
				
				if(extractTopLevelGroups(c)==null)
				{
					return null;
				}
				
				
				if(processOperations(c)==null)
				{
					return null;
				}
				
				// now we process the operations of each of the groups.
				for(int j = 0; j<groupDummy.getSize(); j++)
				{
					if(extractTopLevelGroups((Node)groupDummy.getChild(j))==null)
					{
						return null;
					}
					
					if(processOperations((Node)groupDummy.getChild(j))==null)
					{
						return null;
					}
				}
				
				// then stitch the groups back into one big statement.
				// TODO fix the line number -1 problem ( after the stiching though)
				
				if(!stitchGroups((Statement)c))
				{
					return null;
				}
				
				// and rip it to shreds when the statement is done.
				groupDummy.remove();
				
				// check that the statement isn't empty.
				if(c.getSize()==0)
				{
					c.remove();
					i--;
					continue;
				}
			}
			
			
		}
		
		
					
		return program;
		
	}
	
	private static ElementContainer processOperations(ElementContainer container)
	{
		// okay
		// we just need to find any operators
		// seems simple enough.
		Shell.out("Processing Container: "+container,"Ziker 4",2);
		
		
		//assert container.getSize()>0;
		if(container.getSize()==0)
		{
			return container;
		}
		
		if(container.getSize()==1)
		{
			if(!(container.getChild(0) instanceof ElementContainer))
			{
				if((int)(((TokenData)(container.getChild(0))).getData(1))==Lang.tokenTypes.operator.ordinal())
				{
					Shell.out("Error: Missplaced Operator '"+((TokenData)(container.getChild(0))).getData(0)+"'. Delete this token.",
							  container.getGroup().getName(),0, ((TokenData)(container.getChild(0))).getStatementNumber() );
					return null;
				}
				
				return container;
			}
			
		}
		
		
		
		// okay, now look for the stuff.
		// just copy all the code over more or less?
		TokenData data = null;
		for(int priorityGroup = 0; priorityGroup<Lang.OPERATORS.length; priorityGroup++)
		{
			
			// okay so we get to do a funny thing here...
			// after we looked for groups, we check if this container is a function call.
			// if it is, we need to check for every parameter.
			if(priorityGroup==Lang.OPERATION_PRIORITY_GROUPS.assign.ordinal())
			{
				if(isGroupPointerAFunctionCall(getGroupPointer(container)))
				{
					// looks like we need to check for multiple parameters.
					if(!processFunctionParameters(container))
					{
						return null;
					}
					return container;
				}
			}
			// look through every element.
			for(int j = container.getSize()-1; j>=0;j--)
			{
				
				FunctionalElement d = container.getChild(j);
				assert d instanceof TokenData; // I suppose this isn't really necessary 
				data = (TokenData)d;
				
				
				if((int)data.getData(1) == Lang.tokenTypes.operator.ordinal())// if the data is an operator...
				{
					// now we get to do the 'fun' parts.
					// yaaaay...
					
					
					int operator=-1;
					for(int i = 0; i<Lang.OPERATORS[priorityGroup].length; i++)
					{
						if(Lang.OPERATORS[priorityGroup][i].equals(data.getData(0)))
						{
							// we found something, alright.
							// what to do, what to do...
							operator = i;
							Shell.out(data+" is a(n) "+Lang.OPERATION_PRIORITY_GROUPS.values()[priorityGroup]+" Operator.","Ziker 4",3);
							break;
						}
					}
					
					if(operator!=-1)
					{
						
						// Now we splice, having found the operator.
						// returned to us will be a new container with 3 containers, and we need to look through each, and if there is more
						// than one token in them, process them.
						
						 
						if(null==processOperator(data, priorityGroup, operator))
						{
							// there was some kind of error.
							return null;
						}
						
						
						
						for(int i=0; i<container.getSize(); i++)
						{
							if(container.getChild(i) instanceof ElementContainer)
							{
								//System.out.println("Going to process:"+container.getGroup());
								// then we process it
								if(processOperations((ElementContainer)(container.getChild(i)))==null)
								{
									return null;
								}
								
							}
							
						}
						
						// hopefully nothing terrible happens?
						/*if(container.getParent()!= null)
						{
							container.getParent().removeChild(container);
						}*/
						return container; // doesn't matter. should just be returning a boolean but meh.
						
					}
			

				}
				
				
				
			}
			
		}
		// in this case you probably have gotten something like '1 2 3' or just a bunch of normal words
		// whatever it is, it's not going to be valid code.
		// well, that would be the case.
		// if not for functions existing.
		// if there's something along the lines of 'function, groupPointer'
		// then that's totally valid.
		// so we need to look for that.
		
		// uh
		// how?
		// let's try a naive approach and see if anything explodes
		
		for(int i = 0; i<container.getSize()-1; i++)
		{
			// look for a function proceeded by a groupPointer
			FunctionalElement d = container.getChild(i);
			assert d instanceof TokenData; // I suppose this isn't really necessary 
			data = (TokenData)d;
			
			if((int)data.getData(1) == Lang.tokenTypes.function.ordinal())
			{
				// okay, I woud check that it isn't the last token,
				// but that's implied.
				
				// assert that the next token is a parenthetical group
				
				d = container.getChild(i+1);
				assert d instanceof TokenData; // I suppose this isn't really necessary 
				data = (TokenData)d;
				
				assert(int)data.getData(1) == Lang.tokenTypes.groupPointer.ordinal();
				return container;
				
			}
		}
		
		if(isGroupPointerAFunctionCall(getGroupPointer(container)))
		{
			return container;
		}
		
		Shell.out("Invalid token sequence.",container.getGroup().getName(),0, data.getStatementNumber() );
		return null;
	}
	
	private static ElementContainer processOperator( TokenData operator, int priorityGroup, int operatorID)
	{
		// first check for the dreaded 'organize' operators.
		ElementContainer parent = operator.getParent();
		
		if(Lang.OPERATION_PRIORITY_GROUPS.values()[priorityGroup]==Lang.OPERATION_PRIORITY_GROUPS.organize)
		{
			// this is going to get funky
			// are ya ready kids?
			// probably not.
			/*if(operatorID==0||operatorID==1)
			{	
				Shell.out("Arrays are not currently implemented.",parent.getGroup().getName(),0,operator.getStatementNumber());
				return null;
			}*/
			
			// we are dealing with the dreaded ().
			// oh my
			// what are we in for
			// we do this in a seperate method, probably.
			assert false;
		}
		
		//Let the chopping commence?
		// no, not so quick, bucko
		// we need to make sure there are things to chop.
		// make sure there's no "1 +" situations.
		// or + + + situations either
		
		Shell.out("Processing operator '"+Lang.OPERATORS[priorityGroup][operatorID]+"' normally. ","Ziker 4",3);
		
		// first, check that an operator is neither the first or last token.
		if(parent.getChildID(operator)==0||parent.getChildID(operator)==parent.getSize()-1)
		{
			Shell.out("Error on Operator '"+operator.getData(0)+"'. Missing operand.",parent.getGroup().getName(),0,operator.getStatementNumber());
			return null;
		}
		
		// now check that both of it's neighbors are operators.
		// this is really kind of a disgusting if, but I'm kinda lazy.
		if( ((int)(((TokenData)(parent.getChild(parent.getChildID(operator)-1))).getData(1)))==Lang.tokenTypes.operator.ordinal() ||
		    ((int)(((TokenData)(parent.getChild(parent.getChildID(operator)+1))).getData(1)))==Lang.tokenTypes.operator.ordinal())
		{
			Shell.out("Error on Operator '"+operator.getData(0)+"'. One or both operands are operators.",parent.getGroup().getName(),0,operator.getStatementNumber());
			return null;
		}
		
		// now we can probably chop safely.
		// ah, not quite.
		
		int leftSize = parent.getChildID(operator);
		int rightSize = (parent.getSize()-1)-parent.getChildID(operator);
		Shell.out("Tokens before operator: "+leftSize,"Ziker 4",3);
		Shell.out("Tokens after operator: "+rightSize,"Ziker 4",3);
		
		
		// now we have all the information we need to chop.
		// start by re-making the operator.
		
		String operation = (String)operator.getData(0);
		operator.remove();
		TokenData t = null;
		
		try 
		{
			t = new TokenData(parent.getGroup(),parent);
			ArrayList<Object> data = new ArrayList<Object>();
			data.add(operation);
			data.add(Lang.tokenTypes.operation.ordinal());
			t.setData(data);
		} 
		
		catch (Exception e)
		{
			
			e.printStackTrace();
			return null;
		}
		
		
		
		
		// choppy chop


		
		// first, the left half.	
		
		if(leftSize==1)
		{
			// I'm not certain this will work, but whatever.
			TokenData d = (TokenData)parent.getChild(0);
			moveToken(d,parent);
		
			
		}
		else
		{
			// first create a container,
			// then loop through and move elements.
			Node leftNode;
			try
			{
				leftNode = new Node(parent.getGroup(),parent);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
			
			for(;leftSize>0;leftSize--)
			{
				TokenData d = (TokenData)parent.getChild(0);
				moveToken(d,leftNode);
			}
			
		}
		
		// then the right half.
		if(rightSize==1)
		{
			// I'm not certain this will work, but whatever.
			TokenData d = (TokenData)parent.getChild(0);
			moveToken(d,parent);
		}
		else
		{
			// first create a container,
			// then loop through and move elements.
			Node rightNode;
			try
			{
				rightNode = new Node(parent.getGroup(),parent);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
			
			
			for(;rightSize>0;rightSize--)
			{
				TokenData d = (TokenData)parent.getChild(0);
				moveToken(d,rightNode);
			}
			
		}
		
		Shell.out("Operator Processed.","Ziker 4", 3);
		
		return parent;
	}
	
	
	private static void moveToken (TokenData token, ElementContainer newLocation)
	{
		
		ArrayList<Object>data = token.getData();
		token.remove();
		
		try
		{
			TokenData d2 = new TokenData(newLocation.getGroup(), newLocation);
			d2.setData(data);
			//newLocation.addChild(d2);
		}
		catch(Exception e)
		{
			// I doubt this will happen...
			e.printStackTrace();
		}	
	}
	
	private static Boolean extractTopLevelGroups(ElementContainer container)
	{
		// this method locates all top level parentheses groups and takes them out of the relevant container.
		// okay, how to do that?
		// not sure...
		// well, seems simple enough...
		// returns true if it found any tokens, returns false if not, and returns null if something wasn't formatted properly.
		
		
		
		for(int i = 0; i<container.getSize(); i++)
		{
			// is the container guaranteed not to have containers as children?
			// yes, I think.
			
			assert container.getChild(i)instanceof TokenData;
			// better safe than sorry.
			
			TokenData currentToken =  (TokenData)container.getChild(i);
			//Boolean currentResult=false;
			
			// now just find any "(" operators.
			
			if(((int)currentToken.getData(1)) == Lang.tokenTypes.operator.ordinal())
			{
				// also worth noting that if we find a ) we throw an error.
				
				if(((String)currentToken.getData(0)).equals(")"))
				{
					
					Shell.out("Error: Unexpected token ')'. Remove this token.",container.getGroup().getName(),0,currentToken.getStatementNumber());
					return null;
					
				}
				
				if(((String)currentToken.getData(0)).equals("("))
				{
					
					Shell.out("found a group.","Ziker 4",2);
					// my oh my.
					// lookee what we found here...
					// so at this point we probably make another method.
					// I should probably be better at breaking up methods.
					
					int startingIndex = i;
					int endingIndex = findTopLevelGroup(container, i);
					
					if(endingIndex==-1)
					{
						return null;
					}
					
					// We now know where the group is.
					if(!extractGroup(startingIndex, endingIndex,container))
					{
						return null;
					}
					
					// so this is also recursive.
					Boolean result = extractTopLevelGroups(container);
					if(result==null)
					{
						return null;
					}
					
					
					return true;	
					
				}
			}
			
		}
		return false;
	}
	
	
	private static int findTopLevelGroup(ElementContainer container, int startingIndex)
	{
		
		// find the ending Index of a ')' corresponding to the given top level group starting with a '(' operator at startingIndex within container.
		
		//first we need to find the end of the group.
		int endingIndex=-1;
		int depth=1;
	
		for(int i = startingIndex+1; i<container.getSize(); i++)
		{
			// look for any '(' or ')'
			
			assert container.getChild(i)instanceof TokenData;
			TokenData currentToken =  (TokenData)container.getChild(i);
			String tokenData = (String)(currentToken.getData(0));
			
			// is directly checking for the thing bad?
			// yes.
			// I'm not quite sure how to avoid it though.
			// could access the operator array, but that's less readable and only slightly better.
			if(tokenData.equals("("))
			{
				depth++;
				continue;
			}
			
			if(tokenData.equals(")"))
			{
				depth--;
				if(depth==0)
				{
					endingIndex=i;
					break;
				}
				continue;
			}
			
		}
		
		if(endingIndex==-1)
		{
			// we did not complete the group.
			// uh-oh.
			// wow you can't really tell what this is even doing.
			// it's just an error message though.
			Shell.out("Error: An '(' was missing a coresponding ')'.",container.getGroup().getName(),0,
			((TokenData)(container.getChild(startingIndex))).getStatementNumber());
		
		}
		
		return endingIndex;
	
	}
	
	private static boolean extractGroup(int startingIndex, int endingIndex, ElementContainer container)
	{
		// first we need to find a the dummy container to store the group in.
		// should be the last element in the functionalGroup.
	/*if(endingIndex-startingIndex==1)
		{
			// () is not valid.
			Shell.out("Error: Parrenthese groups cannot be empty.",container.getGroup().getName(),0,
			((TokenData)(container.getChild(0))).getStatementNumber());
			return false;
		}*/
		
		Statement groupDummy = (Statement)container.getGroup().getChild(container.getGroup().getSize()-1);
		
		Node newLocation;
		try 
		{
			// I really wish I hadn't made making elements throw things.
			// very annoying.
			newLocation = new Node(container.getGroup(), groupDummy);
		} 
		catch (Exception e) 
		{
			
			e.printStackTrace();
			return false;
		}
		
		// move everything.
		for(int i = endingIndex-startingIndex-1;i>0;i-- )
		{
			TokenData t = (TokenData)container.getChild(startingIndex+1);
			moveToken(t,newLocation);
		}
		
		// then remove the original ')'
		// we keep the '(' to transform it.
		container.getChild(startingIndex+1).remove();
		
		// okay, now we do the... things.
		// we transform the '(' operation to the location of a group.
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(groupDummy.getChildID(newLocation)+"");
		data.add(Lang.tokenTypes.groupPointer.ordinal());
		
		try 
		{
			
			((TokenData)(container.getChild(startingIndex))).setData(data);
		} 
		catch (Exception e) 
		{
			
			e.printStackTrace();
			return false;
		}
		
		
		Shell.out("Group successfully extracted:","Ziker 4",2);
		Shell.out("Container without Group:\n"+container,"Ziker 4",3);
		Shell.out("Lone Group:\n"+newLocation,"Ziker 4",4);
		return true;
		
	}
	
	private static boolean stitchGroups(Statement statement)
	{
		// try to stitch groups until there are no more groups to stitch.
		Shell.out("Attempting to stitching any existing groups.","Ziker 4",3);
		while(findStitchableGroup(statement)) {}
		return true;
	}
	
	private static boolean findStitchableGroup(ElementContainer container)
	{
		// finds and then stitches stitchable groups.
		
		// loop through children, if there's a group, stitch it, if there's a child, call this on it.
		for(int i =0; i<container.getSize(); i++)
		{
			
			FunctionalElement e = container.getChild(i);
			
			if(e instanceof Node)
			{
				if(findStitchableGroup((ElementContainer)e))
				{
					return true;
				}
				continue;
			}
			
			if(e instanceof TokenData)
			{
				// easier reading
				int tokenType = (int)((TokenData)e).getData(1);
				
				if(tokenType==Lang.tokenTypes.groupPointer.ordinal())
				{
					// we found a stitchable group.
					// now stitch it.
					
					stitchGroup((TokenData)e);
					return true;
				}
				continue;
			}
			
			// code should never reach here, because then the container we've been given is definitely not part of a (valid)statement.
			assert false;
			
		}
		return false;
	}
	
	private static void stitchGroup(TokenData groupPointer)
	{
		// just the tokenData that is the group pointer should be able to tell us everything we need to know.
		
		// just a note that if this method is used outside of the expected context that it will throw a ton of exceptions
		
		// okay first find the groupDummy.
		
		// it will always be the last element of the group.
		ElementContainer groupDummy = (ElementContainer)groupPointer.getGroup().getChild(groupPointer.getGroup().getSize()-1);
		
		// next, get the group that we need to stitch
		// we do this by first getting the data from the group pointer.
		String groupPointerData = (String)groupPointer.getData(0);
		int groupIndex = Integer.parseInt(groupPointerData);
		
		// now, among the dozen things we are assuming for this function to work, one of them
		// is that the index points to a valid group.
		
		Node group = (Node)(groupDummy.getChild(groupIndex)).copy();
		
		int groupPointerIndex=groupPointer.getID();
		ElementContainer parent = groupPointer.getParent();
		

		
		// avoid (a) being interpreted with a spurious node
		FunctionalElement toAdd = group;
		while(group.getSize()==1)
		{
			if(group.getChild(0) instanceof Node)
			{
				
				
				group=(Node)group.getChild(0);
				continue;
				
			}
			
			// check that this isn't a function call
			if(!isGroupPointerAFunctionCall(groupPointer))
			{
				toAdd=group.getChild(0);
			}
			break;
		}
		
		
		if(parent.getSize()==0)
		{
			for(int i = 0; i<group.getSize();i++)
			{
				if(group.getChild(i) instanceof ElementContainer)
				{
					toAdd=((ElementContainer)group.getChild(i)).copy();
				}
				else
				{
					toAdd=((TokenData)group.getChild(i)).copy();
				}
				
				parent.addChild(groupPointerIndex+i, toAdd);
			}
			return;
		}
		
		// that should be it...
		groupPointer.remove(); 
		parent.addChild(groupPointerIndex, toAdd);
		
		
	}
	
	private static boolean isGroupPointerAFunctionCall(TokenData GroupPointer)
	{
		if(GroupPointer== null)
		{
			return false;
		}
		
		if(GroupPointer.getID()==0)
		{
			return false;
		}
		
		TokenData d = (TokenData)GroupPointer.getParent().getChild(GroupPointer.getID()-1);
		int dType = (int)d.getData(1);
		if(dType==Lang.tokenTypes.function.ordinal())
		{
			return true;
		}
		
		return false;

	}
	
	// returns the tokendata that is the groupPointer to this element contianer, if the container is in fact a group. otherwise returns null.
	private static TokenData getGroupPointer(ElementContainer group)
	{
		if(group instanceof Statement)
		{
			// not a group.
			return null;
		}
		
		if(group.getParent().getID()!=group.getGroup().getSize()-1)
		{
			// not a group.
			return null;
		}
		
		int groupID=group.getID();
		FunctionalGroup program = group.getGroup();
		
		for(int i = 0; i< program.getSize(); i++)
		{
			Statement s =(Statement)program.getChild(i);
			for(int j=0; j<s.getSize(); j++)
			{
				//I'm assuming that if there are nodes or whatever then it's not gonna be there;
				// it's already processed.
				FunctionalElement e = s.getChild(j);
				
				if(e instanceof ElementContainer)
				{
					continue;
				}
				
				// tokendata
				TokenData d = (TokenData)e;
				int dType = (int)d.getData(1);
				
				if(dType==Lang.tokenTypes.groupPointer.ordinal())
				{
					// we found A group pointer.
					// but is it THE group pointer?
					
					if(Integer.parseInt((String)d.getData(0))==groupID)
					{
						return d;
					}
				}	
			}
		}
		
		assert false;
		return null;
	}



	private static boolean processFunctionParameters(ElementContainer parameters)
	{
		//I'm reasonably confident that parameters will have at least one element.
		
		
		ArrayList<TokenData> currentParameter = new ArrayList<TokenData>();
		boolean wasLastTokenOperator = isTokenDataOperator((TokenData)parameters.getChild(0));
		
		currentParameter.add((TokenData)parameters.getChild(0));
		
		for(int i = 1; i<parameters.getSize(); i++)
		{
			if((!wasLastTokenOperator)&!isTokenDataOperator((TokenData)parameters.getChild(i)))
			{
				//there is a seperate parameter here...
				if(currentParameter.size()>1)
				{
					// create a node at index i-1 and move all of currentParameter into it.
					Node newParameter= new Node(parameters.getGroup());
					// move the node to it's proper home.
					parameters.addChild(i-1, newParameter);
					
					for(TokenData j:currentParameter)
					{
						newParameter.addChild(j);
						
					}
					
				}
				currentParameter.clear();
			}
			
			// we are good...
			wasLastTokenOperator = isTokenDataOperator((TokenData)parameters.getChild(i));
			currentParameter.add((TokenData)parameters.getChild(i));
		}
		
		if(currentParameter.size()>1)
		{
			// create a node at index i-1 and move all of currentParameter into it.
			Node newParameter= new Node(parameters.getGroup());
			// move the node to it's proper home.
			parameters.addChild(newParameter);
			
			for(TokenData j:currentParameter)
			{
				newParameter.addChild(j);
				
			}
			currentParameter.clear();
		}
	
		//TODO now process everything. should be pretty simple.
		return true;
	}
	
	private static boolean isTokenDataOperator(TokenData d)
	{
		return ((int)(d.getData(1)))==Lang.tokenTypes.operator.ordinal();
	}

}

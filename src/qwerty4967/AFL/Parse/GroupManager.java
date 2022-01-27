package qwerty4967.AFL.Parse;

import java.util.ArrayList;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.Lang.TokenType;
import qwerty4967.AFL.ParseTree.*;


public class GroupManager 
{

	
	private static ArrayList<Group> Groups= new ArrayList<Group>();
	private static ArrayList<Group> FunctionizedGroups= new ArrayList<Group>();
	private static int statementNumber = -1;
	private static String file = "[GroupManager could not get file]";
	
	protected static void reset()
	{
		file = "[GroupManager could not get file]";
		statementNumber = -1;
		Groups.clear();
		FunctionizedGroups.clear();
	}
	
	public static int getStatementNumber()
	{
		return statementNumber;
	}
	
	public static String getFile()
	{
		return file;
	}
	
	private static void setStatementNumber(Element e)
	{
		// probably bad code, but, uh...
		int sn = e.getStatementNumber();
		if(sn!=-1)
		{
			statementNumber = sn;
		}
		file = e.getFile();
	}

	protected static boolean findGroups(Container c)
	{
		// First step is to find the first (
		// if there are none, that's fine
		// if there is one, call a function that will work out where
		// the group is actually located and will eventually extract the group
		// then reset until none is found.
		
		setStatementNumber(c);
		for(int i=0; i<c.getSize(); i++)
		{
			// t should always be a token, I think.
			Token t = (Token)c.getChild(i);
			
			if(t.getType()==TokenType.operator)
			{
				// yeah yeah, it's hard-coded.
				// what should I do instead?
				if(t.getData().equals("("))
				{
					int groupStart = t.getID();
					int groupEnd = findGroupEnd(c, groupStart);
					if(groupEnd == -1)
					{
						return false;
					}
					ExtractGroup(c, groupStart, groupEnd);
					i=0;
					continue;
					
					
				}
				if(t.getData().equals(")"))
				{
					Shell.error("Missing '(' or extraneous ')'.", c.getStatementNumber(),c.getFunction().getFile());
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	private static int findGroupEnd(Container c, int groupStart)
	{
		// this is pretty simple, just check depth.
		// groups can be nested, so we ought to be wary.
	
		int depth = 0;
		
		
		for(int i = groupStart; i<c.getSize(); i++)
		{
			// t should always be a token, I think.
			Token t = (Token)c.getChild(i);
			
			if(t.getType()==TokenType.operator)
			{
				if(t.getData().equals("("))
				{
					depth ++;
				}
				if(t.getData().equals(")"))
				{
					depth --;
					if(depth==0)
					{
						return i;
					}
				}
			}
		}
	
		Shell.error("Missing ')' or extraneous '('.", c.getStatementNumber(), c.getFunction().getFile());
		return -1;
	}
	
	
	private static void ExtractGroup(Container c, int groupStart, int groupEnd)
	{
		// create the group, before we extract all of the information from it.
		Group g = new Group(c);
		Groups.add(g);
		Container gc = new Container(g);
		
		// delete the (.
		Element e = c.getChild(groupStart);
		c.removeChild(e);
		
		int groupSize = groupEnd-groupStart-1;
		
		for(int i = 0; i<groupSize;i++)
		{
			e = c.getChild(groupStart);
			gc.addChild(e);
		}
		
		// delete the ).
		e = c.getChild(groupStart);
		c.removeChild(e);
		
		// add back a group Pointer.
		String groupID = Integer.toString(Groups.size()-1);
		Token groupPointer = new Token(groupID, TokenType.groupPointer, c);
		c.addChild( groupStart,groupPointer);
		// should be good
	}
	
	protected static boolean functionizeGroups(Container c)
	{
		// fill functionized Groups with nulls, if we have too.
		int diff = Groups.size()-FunctionizedGroups.size();
		if(diff>0)
		{
			for(;diff>0;diff--)
			{
				FunctionizedGroups.add(null);
			}
		}
		
		setStatementNumber(c);
		// How hard could this possibly be?
		for(int i=0; i<Groups.size(); i++)
		{
			Group g = Groups.get(i);
			// we add g to the list of functionized groups before we functionize to prevent an infinite loop of recursive nonsense
			if(g==null)
			{
				continue;
			}
			if(g.getOrigin()!=c)
			{
				continue;
			}
			
			int index = Groups.indexOf(g);
			Groups.set(index, null);
			FunctionizedGroups.set(index, g);
			
			// now, actually functionize the group
			Container toFunctionize = (Container)g.getChild(0);
			if(!Functionizer.functionize(toFunctionize, false))
			{
				return false;
			}
		}
		return true;
	}
		
	protected static void stitchGroups(Container c)
	{
		setStatementNumber(c);
		for(int i = 0; i<c.getSize(); i++)
		{
			// loop through all children, look for any groupPointers.
			Element child = c.getChild(i);
			if(child instanceof Token)
			{
				Token t = (Token)child;
				
				if(t.getType()==TokenType.groupPointer)
				{
					// we found one, time to get a stitching...
					
					int groupID = Integer.parseInt(t.getData());
					Group toStitch = FunctionizedGroups.get(groupID);
					
					stitchGroup(t,toStitch);
				}
			}
		}
	}
	
	private static void stitchGroup(Token pointer, Group toStitch)
	{
		Container c = (Container)pointer.getParent();
		int pointerID = pointer.getID();
		c.removeChild(pointer);
		
		// get the actual thing we are replacing the pointer with.
		Container toScrunch = (Container)toStitch.getChild(0);
		// better safe than sorry.
		if(toScrunch.getSize()!=1)
		{
			System.out.println("GROUP: "+toStitch+" NOT PROPERLY TOKENIZED!");
			System.exit(-1);
		}
		
		Element toReplace = toScrunch.getChild(0);
		c.addChild(pointerID, toReplace);
		// I think that's it...
	}
	
	protected static Group getGroup(int ID)
	{
		// this method assumes you will modify the group you get, so it disposes of it.
		Group toReturn = Groups.get(ID);
		Groups.set(ID, null);
		// I'm a bit wary that using add instead of set might break something,
		// but set crashes things...
		FunctionizedGroups.add(ID, null);
		return toReturn;
	}
	
	
	
}

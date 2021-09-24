package qwerty4967.AFL.Parse;

import java.util.ArrayList;

import qwerty4967.AFL.Shell;
import qwerty4967.AFL.ParseTree.*;


public class GroupManager 
{

	
	private static ArrayList<Group> Groups= new ArrayList<Group>();
	
	//TODO make this do, you know, anything at all.
	protected static boolean findGroups(Container c)
	{
		// temporary code.
		for(int i=0; i<c.getSize(); i++)
		{
			// I will be deleting these critical flaws
			Token t = (Token)c.getChild(i);
			if(t.getData().equals(")")||t.getData().equals("("))
			{
				Shell.error("Parentheses are not currently supported", t.getStatementNumber());
				return false;
			}
		}
		
		return true;
	}
	
	protected static boolean functionizeGroups(Container c)
	{
		return true;
	}
	
	protected static void stitchGroups(Container c)
	{
		return;
	}
	
	
	
}

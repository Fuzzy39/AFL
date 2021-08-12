/*package qwerty4967.Ziker4;

import qwerty4967.Ziker4.FunctionStructures.DataElement;
import qwerty4967.Ziker4.FunctionStructures.ElementContainer;
import qwerty4967.Ziker4.FunctionStructures.FunctionalElement;
import qwerty4967.Ziker4.FunctionStructures.FunctionalGroup;
import qwerty4967.Ziker4.FunctionStructures.Node;
import qwerty4967.Ziker4.FunctionStructures.Statement;

public class TreeClimber 
{

	// climbs programs.
	// poorly, If I am remembering correctly.
	
	// This code, as written, has a pretty marignal use case, 
	// and I don't really know how it works anymore...
	// changing it would be difficult
	// it's possible that this code isn't actually ever going to be used...
	
	private boolean climbsStatements; // does this treeclimber climb Statements, or ignore them?
	private FunctionalGroup program; // the tree to climb.
	private FunctionalElement lastElement;
	
	/**
	 * 
	 * @param program
	 * @param climbsStatements
	 *
	public TreeClimber(FunctionalGroup program, boolean climbsStatements)
	{
		this.climbsStatements=climbsStatements;
		this.program=program;
		this.lastElement=null;
	}
	
	/**
	 * gets the next dataelement.
	 * @return
	 */
	public DataElement nextData()
	{
		// my brain is empty, but not without ideas.
		// so, lets try them
		// and watch them fail horribly...
		// whatever.
		
		// the first step is to determine the element we are to check for.
		// then we need to check if it is valid.
		// if it is not valid there are various responses required.
		FunctionalElement toReturn=null;
		while(!(toReturn instanceof DataElement))
		{
			lastElement=getNext(lastElement, false);
			toReturn=lastElement;
			if(lastElement==null)
			{
				
				return null;
			}
			
		}
		return (DataElement) lastElement;
		
		
	}
	
	/**
	 * gets the next element
	 * @param prev
	 * @param ignoreContainers
	 * @return
	 */
	private FunctionalElement getNext( FunctionalElement prev, boolean ignoreContainers)
	{	

		// this method just rudely grabs what the next element is, regardless of what it is.
		// it doesn't check if it is data.
		
		
		
		if(prev==null)
		{
			// we are starting here, there was no last element.
			
			// no program, no elements
			if(program.getSize()==0)
			{
				return null;
			}
			
			
			// otherwise we just take the first element.
			Shell.out("taking first element","Group Monkey",4);
			return program.getChild(0);
			
		}
		
		if(prev.getGroup()==null)
		{
			// the element has been deleted.
			Shell.out("Reseting...","Group Monkey",4);
			lastElement=null;
			return getNext(null,ignoreContainers);
		}
		// first check if the previous element was a container.
		Shell.out("Last Element: "+prev,"Group Monkey",4);
		
		if(prev instanceof Node || (prev instanceof Statement & climbsStatements))
		{
			if( ((ElementContainer)prev).getSize() !=0)
			{
				if(! ignoreContainers)
				{
					Shell.out("It is a container.","Group Monkey",4);
					return ((ElementContainer)prev).getChild(0);
				}
			}
		}
				
		
		// in this case, there is a last, and we need to determine the next thing.
		// this should be one line of code, but it isnt.
		// so...
		// yeah.
		
		// what can happen in these conditions?
		// well...
		// first, the last could either be attached to root, or not.
		// then, the last could be the last of it's container or root.
		
		if(prev.getParent()==null)
		{
			// prev is attached to root.
			// now keep in mind that we don't care whether what we return is a container or not.
			int id =prev.getID();
			
			
			if(id+1<program.getSize())
			{
				Shell.out("Got element in root...","Group Monkey",4);
				return program.getChild(id+1);
			}
			
			// we have reached the end of the program.
			Shell.out("End of FunctionalGroup reached.","Group Monkey",4);
			return null;
		}
		
		// in this case, the  last element was part of a container.
		
		// what can happen here?
		// well, we can either reach the end of the container, or not.
		
		int id =prev.getID();
		
		if(id+1<prev.getParent().getSize())
		{
			Shell.out("Got element in a container.","Group Monkey",4);
			return prev.getParent().getChild(id+1);
		}
		
		// we have reached the end of the container.
		// what now?
		Shell.out("End of Container reached.","Group Monkey",4);
		return getNext(prev.getParent(),true);
	}
	
}

package qwerty4967.AFL.ParseTree;

import java.util.ArrayList;

import qwerty4967.AFL.Function.AFLFunction;


public class Container extends Element implements HasChildren
{
	// Time to get down to buisness
	// of the very abstract.
	// I've maybe slightly been dreading this, but only because I keep putting it off.
	// functional group was more complicated than this, and very similar.
	// introduce this object, which is a copy of functional group with most of it stripped out.
	// wow.

	private ArrayList<Element> children=new ArrayList<Element>();
	protected String name; // I had a comment here, but now it's not
	// what is the name for anyways?
	// toString, right.
	

	public Container()
	{
		super();
		this.name="Container";
	}
	
	public Container( HasChildren parent)
	{
		super(parent);
		this.name="Container";
	}
	
	
	public Container copy()
	{
		Container toReturn = new Container();
		for(int i = 0; i<this.getSize(); i++)
		{
			Element child = getChild(i);
			toReturn.addChild(child.copy());
		}
		return toReturn;
	}
	/**
	 * adds a child to the element.
	 * returns the id of the child.
	 */
	public int addChild( Element child)
	{
		// this never fails, I proclaimed proudly without any testing.
		// whatever.
		// add a check to make sure the same thing isn't in there twice
		addChild(children.size(),child);
		
		return children.size()-1;
		
	}
	
	
	public void addChild(int index, Element child)
	{
		// really very simple
		// having this method earlier would definitely reduce some awkwardness.
		child.moveTo(this,index);
		if(!children.contains(child))
		{
			children.add(index,child);
		}
		else
		{
			// would it be better to throw an exception here? yes.
			System.out.println("DONT MOVE A CHILD USING addChild! USE moveChild INSTEAD!");
			System.exit(-1);
		}
		updateChildrenIDs();
	}
	
	public void moveChild(int index, Element child)
	{
		if(!children.contains(child))
		{
			System.out.println("CANT MOVE A CHILD I DONT HAVE!");
			System.exit(-1);
		}
		
		children.remove(child);
		children.add(index,child);
		updateChildrenIDs();
	}
	/**
	 * removes a child 
	 */
	public void removeChild(Element child)
	{
		
		// this probably works correctly.
		// I don't think I've ever actually called this method...
		// not sure.
		
		children.remove(child);
		child.remove();
		updateChildrenIDs();
		
	}
	
	/**
	 * why isn't the javadoc working?
	 * I guess it didn't autofill?
	 * meh
	 * same idea, you can remove a child from it's ID.	
	 */
	public void removeChild(int ID)
	{
		
		removeChild(children.get(ID));
		
	}
	
	public Element getChild(int ID)
	{
		return children.get(ID);
	}
	
	// returns -1 on failure.
	/**
	 * gets id of child
	 * returns -1 on failure.
	 */
	public int getChildID(Element child)
	{
		updateChildrenIDs();
		for (int i = 0; i < children.size(); i++) 
		{
			if( children.get(i) == child)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/** 
	 * gets size
	 */
	public int getSize()
	{
		return children.size();
	}
	
	/**
	 * removes all children
	 */
	public void removeAllChildren()
	{
		
		children= new ArrayList<Element>();
	}

	@Override
	/**
	 * toString, pretty standard, ish.
	 */
	public String toString() 
	{
		// this is a mess
		// what does it do?
		// I know it's recursive...
		
		String tree="";
		for(int i = 0; i<children.size(); i++)
		{
			String child = children.get(i).toString();
			String[] childsChilds = child.split("\n");
			for(int j = 0; j<childsChilds.length;j++)
			{
				tree+="\n    "+childsChilds[j];
			}
			
		}
		
		return name+":    " + tree;
	}
	
	
	
	protected void updateChildrenIDs()
	{
		int j = 0;
		for(Element i : children)
		{
			i.ID=j;
			j++;
		}
	}
}

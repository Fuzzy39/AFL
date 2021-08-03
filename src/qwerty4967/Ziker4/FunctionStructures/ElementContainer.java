package qwerty4967.Ziker4.FunctionStructures;

import java.util.ArrayList;

public abstract class ElementContainer extends FunctionalElement implements HasChildren
{
	// Time to get down to buisness
	// of the very abstract.
	// I've maybe slightly been dreading this, but only because I keep putting it off.
	// functional group was more complicated than this, and very similar.
	// introduce this object, which is a copy of functional group with most of it stripped out.
	// wow.
	private ArrayList<FunctionalElement> children=new ArrayList<FunctionalElement>();
	protected String name; // since everything I'm writing is in the same package, isn't this functionally the same as making it public?
	// I just want subclasses to see it, nothing else.
	
	/**
	 * 
	 * @param group
	 */
	public ElementContainer( FunctionalGroup group)
	{
		super(group);
		this.name="ElementContainer";
	}
	
	/**
	 * 
	 * @param group
	 * @param parent
	 * @throws Exception
	 */
	public ElementContainer (FunctionalGroup group, ElementContainer parent) throws Exception
	{
		
		
		super(group, parent);
		this.name="ElementContainer";

	}
	
	/**
	 * adds a child to the element.
	 * returns the id of the child.
	 */
	public int addChild( FunctionalElement child)
	{
		// this never fails, I proclaimed proudly without any testing.
		// whatever.
		// add a check to make sure the same thing isn't in there twice
		child.addTo(this,children.size()-1);
		children.add(child);
		updateChildrenIDs();
		return children.size()-1;
	}
	
	
	public void addChild(int index, FunctionalElement child)
	{
		// really very simple
		// having this method earlier would definitely reduce some awkwardness.
		child.addTo(this,index);
		children.add(index,child);
		updateChildrenIDs();
	}
	
	/**
	 * removes a child 
	 */
	public void removeChild(FunctionalElement child)
	{
		// this code looks like it was half written?
		// what was the intention here?
		// does it matter if I delete this? Probably not.
		// I'll comment it out and see if anything explodes I guess
		/*
		if(child instanceof ElementContainer)
		{
			ElementContainer container = (ElementContainer)child;
			
		}*/
		
		children.remove(child);
		
		
	}
	
	/**
	 * why isn't the javadoc working?
	 */
	public void removeChild(int ID)
	{
		
		removeChild(children.get(ID));
		updateChildrenIDs();
	}
	
	public FunctionalElement getChild(int ID)
	{
		return children.get(ID);
	}
	
	// returns -1 on failure.
	/**
	 * gets id of child
	 * returns -1 on failure.
	 */
	public int getChildID(FunctionalElement child)
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
		
		children= new ArrayList<FunctionalElement>();
	}

	@Override
	/**
	 * toString, pretty standard, ish.
	 */
	public String toString() 
	{
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
	
	
	public abstract ElementContainer copy();
	
	
	protected void updateChildrenIDs()
	{
		int j = 0;
		for(FunctionalElement i : children)
		{
			i.ID=j;
			j++;
		}
	}
}

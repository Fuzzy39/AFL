package qwerty4967.Ziker4.FunctionStructures;

import java.util.ArrayList;

public class FunctionalGroup implements HasChildren
{
	// A Functional Group is a function made of Ziker code.
	// It is also any bock of independent code, which for all intents and purposes is a void function with no perameters.
	private ArrayList<FunctionalElement> allChildren = new ArrayList<FunctionalElement>(); // includes children of children, etc.
	private ArrayList<FunctionalElement> children=new ArrayList<FunctionalElement>();
	private final String name;
	private final int perameters; // how many perameters the functionalGroup has.
	// TODO figure out how perameters work better.
	/**
	 * 
	 * @param name
	 */
	public FunctionalGroup(String name)
	{
		this(name, 0);
	}	
	
	public FunctionalGroup(String name, int perameters)
	{
		this.name=name;
		this.perameters=perameters;
	}
	
	/**
	 * clone
	 * @param group
	 */
	public FunctionalGroup( FunctionalGroup group)
	{
		this.perameters = group.perameters;
		this.name=group.name;
		this.allChildren=group.allChildren;
		this.children=group.children;
		
	}
	/**
	 * adds a child to the group. returns ID.
	 */
	public int addChild( FunctionalElement child)
	{
		// this never fails, I proclaimed proudly without any testing.
		// whatever.
		
		// some serious work needs to be done for this to make sense
		//TODO fix this	

		
		children.add(child);
		allChildren.add(child);
		return children.size()-1;
	}
	
	public void addChild(int index, FunctionalElement child)
	{
		// really very simple
		// having this method earlier would definitely reduce some awkwardness.
		children.add(index,child);
		allChildren.add(index, child);
		updateChildrenIDs();
	}
	
	@Override
	/**
	 * A chonky method.
	 */
	public String toString() 
	{
		// Why have a giant convoluted to string when you can have a much simpler and more elegant one
		// it's Recursive, if you count calling other container's to strings, which in turn call even more.
		// it's nice and simple, is what it is.
		
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
		
		
		
		return "\n\nFunctionalGroup:\n" 
				+ "name: " + name
				+ "\nperameters:" + perameters 
				+ "\n\nChildren:" +  tree ;
	}

	
	

	@Override
	/**
	 * again, fairly standard.
	 */
	public boolean equals(Object obj) 
	{
		// I didn't write this, thanks eclipse!
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionalGroup other = (FunctionalGroup) obj;
		if (allChildren == null) {
			if (other.allChildren != null)
				return false;
		} else if (!allChildren.equals(other.allChildren))
			return false;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
	    else if (perameters!=other.perameters)
			return false;
		return true;
	}
	
	/**
	 * removes a specific child.
	 */
	public void removeChild(FunctionalElement child)
	{
		if(child instanceof ElementContainer)
		{
			ElementContainer container = (ElementContainer)child;
			removeChildrenOfChild(container);
		}
		
		allChildren.remove(child);
		children.remove(child);
		updateChildrenIDs();
		
	}
	
	/**
	 * helps with removing children, I think?
	 * @param container
	 */
	// because they aren't going to remove themselves, though they do add themselves.
	private void removeChildrenOfChild(ElementContainer container)
	{
		for(int i=0; i<container.getSize(); i++)
		{
			
			if(container.getChild(i) instanceof ElementContainer)
			{
				removeChildrenOfChild((ElementContainer)container.getChild(i));
			}
			allChildren.remove(container.getChild(i));	
		}
		updateChildrenIDs();
	}
	/**
	 * yeah, same stuff
	 */
	public void removeChild(int ID)
	{
		
		removeChild(children.get(ID));
	}
	
	/**
	 * I have a hunch that this javadoc isn't working quite right, but
	 * meh.
	 * gets a child from its id.
	 */
	public FunctionalElement getChild(int ID)
	{
		return children.get(ID);
	}
	
	// returns -1 on failure.
	/**
	 * same thing, but with an object.
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
	 * gets the size of the group
	 */
	public int getSize()
	{
		return children.size();
	}
	
	/**
	 * removes all children from the group
	 */
	public void removeAllChildren()
	{
		
		children= new ArrayList<FunctionalElement>();
	}
	
	/**
	 * Adds a child
	 * @param toAdd
	 * @return
	 */
	public boolean add( FunctionalElement toAdd)
	{
		// adds to all children.
		allChildren.add(toAdd);
		updateChildrenIDs();
		return true;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the perameters
	 */
	public int getPerameters() {
		return perameters;
	}
	
	/**
	 * 
	 * @return this doesn't work like you would expect it to. TODO fix this if you intend to use it.
	 */
	public FunctionalGroup copy()
	{
		// this doesn't actually work, obviously
		return new FunctionalGroup(this);
	}
	
	
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

package qwerty4967.Ziker4;

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
	public FunctionalGroup(String name)
	{
		this(name, 0);
	}	
	
	public FunctionalGroup(String name, int perameters)
	{
		this.name=name;
		this.perameters=perameters;
	}
	

	public int addChild( FunctionalElement child)
	{
		// this never fails, I proclaimed proudly without any testing.
		// whatever.
		
		children.add(child);
		allChildren.add(child);
		return children.size()-1;
	}
	
	@Override
	public String toString() 
	{
		// this is slightly long and convoluted, so bare with me.
		// the thing that makes this complicated is that we have to print all of the function's
		// children, which are organized into a tree.
		// we want that tree to be as easy to understand as possible, so
		// we need to figure out the proper indentation.
		
		String tree="";
		int depth=0;
		
		// if you have more than a thousand nested ifs you are doing something silly anyways.
		// lengths represents the amount of elements left in a certain element of the tree,
		// the higher the index the deeper in the tree you are.
		ArrayList<Integer> lengths =new ArrayList<Integer>();
		// fill the list with dummy values.
		for(int i = 0; i<1000; i++)
		{
			lengths.add(0);
		}
		
		
		lengths.set(0, allChildren.size());
	
		for(int i = 0; i<allChildren.size(); i++)
		{
			
			
			tree+="\n";
			
			// indent once for each level down we are.
			for(int j=0; j<depth; j++)
			{
				tree+="    ";
			}
			
			lengths.set(depth, lengths.get(depth)-1);
			
			if( allChildren.get(i) instanceof ElementContainer)
			{
				depth++; 
				lengths.set(depth,((ElementContainer)allChildren.get(i)).getSize());
				tree+="    ElementContainer:";
				continue;
			}
			
			tree+="    "+allChildren.get(i);
			
			// when we reach the end of the element container, make sure we haven't reached the end of multiple.
			if(lengths.get(depth)==0)
			{
				while(lengths.get(depth)==0)
				{
					if(depth==0)
					{
						break;
					}
					depth--;
				}
				
			}
			
		}
		
		return "\n\nFunctionalGroup:\n" 
				+ "name: " + name
				+ "\nperameters:" + perameters 
				+ "\n\nChildren:" +  tree ;
	}

	
	

	@Override
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

	public void removeChild(FunctionalElement child)
	{
		if(child instanceof ElementContainer)
		{
			ElementContainer container = (ElementContainer)child;
			removeChildrenOfChild(container);
		}
		
		allChildren.remove(child);
		children.remove(child);
		
		
	}
	
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
	}
	
	public void removeChild(int ID)
	{
		
		removeChild(children.get(ID));
	}
	
	public FunctionalElement getChild(int ID)
	{
		return children.get(ID);
	}
	
	// returns -1 on failure.
	public int getChildID(FunctionalElement child)
	{
		
		for (int i = 0; i < children.size(); i++) 
		{
			if( children.get(i) == child)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	
	public int getSize()
	{
		return children.size();
	}
	
	public void removeAllChildren()
	{
		
		children= new ArrayList<FunctionalElement>();
	}
	
	public boolean add( FunctionalElement toAdd)
	{
		// adds to all children.
		allChildren.add(toAdd);
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
	
	
	
}

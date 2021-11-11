package qwerty4967.AFL.ParseTree;

import java.util.ArrayList;

public class Group implements HasChildren 
{
	// Group represents a collection of Tokens and FunctionCalls that are within parentheses,
	// whether they're function parameters or just normal groups.
	// this is basically a straight copy of Container, except it's not an Element.
	private ControlStatement cs;
	private ArrayList<Element> children;
	
	public Group()
	{
		children=new ArrayList<Element>();
	}
	
	public Group(ControlStatement cs)
	{
		children=new ArrayList<Element>();
		this.cs=cs;
	}
	
	public ControlStatement getControlStatement()
	{
		return cs;
	}
	
	
	@Override
	public int addChild(Element child) 
	{
		addChild(children.size(),child);
		
		return children.size()-1;

	}

	@Override
	public void addChild(int index, Element child) 
	{
		// really very simple
		// having this method earlier would definitely reduce some awkwardness.
		if(child instanceof Statement)
		{
			System.out.println("ATTEMPTED TO ADD A STATEMENT TO A CONTAINER");
			System.exit(-1);
		}
		child.moveTo(this,index);
		children.add(index,child);
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
	
	@Override
	public void removeChild(Element child) 
	{
		// this probably works correctly.
		// I don't think I've ever actually called this method...
		// not sure.
		
		children.remove(child);
		child.remove();
		updateChildrenIDs();

	}

	@Override
	public void removeChild(int ID) 
	{
		removeChild(children.get(ID));

	}

	@Override
	public Element getChild(int ID) 
	{
		return children.get(ID);
	}

	@Override
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

	@Override
	public int getSize() 
	{
		return children.size();
	}

	@Override
	public void removeAllChildren() 
	{
		children= new ArrayList<Element>();

	}
	
	@Override
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
		
		return "Group:    " + tree;
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

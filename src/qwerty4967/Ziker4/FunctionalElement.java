
package qwerty4967.Ziker4;


public abstract class FunctionalElement 
{

	// although completely unimportant, I have written that this is the second class that I wrote, which is true.
	// so, uh...
	// A FunctionalElement is anything that is part of a FunctionalGroup, which is a user defined or imported function,
	// or the currently running program.
	// FunctionalElements are organized in a tree structure.
	// they are constructed from code in two passes in the Parser class.
	// more information on that can be found in the class.
	
	// So, I'm not exactly clear on what the element needs to work properly
	// I need to think on it.	
	
	// Okay, so I've come back and discovered that most of what this is is just being able to be added to an element container.
	// so let's make that work, yeah?
		

	private FunctionalGroup group;
	private ElementContainer parent;
	
	// I'm not sure if this is necessary, but I'll put it in because it can only make things easier.
	private int ID;
	
	// now that I think about it I wonder if java already has a tree thing like I'm making?
	// meh, probably not, mine is kind of weird.
	
	public FunctionalElement( FunctionalGroup group, ElementContainer parent) throws Exception
	{
		if( !group.add(this))
		{
			throw new Exception("Could not add FunctionalElement to Group "+group.getName());
		}
		
		this.group=group;
		
		int id=parent.addChild(this);
		
		if( id == -1) // adding child failed
		{
			// it might be possible for it to be impossible for adding this stuff to fail, I'm not sure.
			throw new Exception("Could not add FunctionalElement to it's parent. More information would probably be useful to you.");
		}
		
		this.ID=id;
		this.parent=parent;
		
	}
	
	public FunctionalElement( FunctionalGroup group)
	{
		// in this case the element has no container as a parent, and is directly part of the group.
		int id = group.addChild(this);
		
		if( id==-1)
		{
			throw new Exception("Could not add FunctionalElement to Group "+group.getName());
		}
		
		this.group=group;
		this.ID=id;
		this.parent=null;
		
	}
	
	public int getID()
	{
		return ID;
	}
	
	public ElementContainer getParent()
	{
		return parent;
	}
	
	public FunctionalGroup getGroup()
	{
		return group;
	}
	
	public boolean addTo( ElementContainer newParent)
	{
		int id=newParent.addChild();
		if( id==-1)
		{
			return false;
		}
		
		// now, it's actually usefull to check for the EXACT same object here,
		// it needs to be the same, not just a copy or something.
		// why? it doesn't have to be...
		// no real reason, but I know that an element should never travel between groups.
		
		if(!newParent.getGroup().equals(this.getGroup()))
		{
			return false;
		}
		
		this.ID=id;
		this.parent=newParent;
	}
	
	public void remove()
	{
		// delete the object, basically.
		if(parent!=null)
		{
			parent.removeChild(this);
			this.parent=null;
		}
		
		group.removeChild(this);
		this.ID=-1;
		this.group=null;
		
	}
	
	
	
	
}

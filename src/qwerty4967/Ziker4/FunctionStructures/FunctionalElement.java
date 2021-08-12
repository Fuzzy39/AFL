
package qwerty4967.Ziker4.FunctionStructures;


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
	protected int ID;
	
	// now that I think about it I wonder if java already has a tree thing like I'm making?
	// meh, probably not, mine is kind of weird.
	/**
	 * this throws an exception.
	 * it is the cauase of many problems
	 * it is impossible for this to actually cause an exception
	 * but yet it throws it.
	 * @param group
	 * @param parent
	 * @throws Exception
	 */
	public FunctionalElement( FunctionalGroup group, ElementContainer parent) throws Exception
	{
		
		if( !group.add(this))
		{
			throw new Exception("Could not add FunctionalElement to Group "+group.getName());
		}
		
		this.group=group;
		this.ID = -1;
		int id=parent.addChild(this);
		
		if( id == -1) // adding child failed
		{
			// it might be possible for it to be impossible for adding this stuff to fail, I'm not sure.
			throw new Exception("Could not add FunctionalElement to it's parent. More information would probably be useful to you.");
		}
		
		this.ID=id;
		this.parent=parent;
		
	}
	
	/**
	 * 
	 * @param group
	 */
	public FunctionalElement( FunctionalGroup group)
	{
		// in this case the element has no container as a parent, and is directly part of the group.
		int id = group.addChild(this);
		
	
		
		this.group=group;
		this.ID=id;
		this.parent=null;
		
	}
	
	/**
	 * clones
	 * @param from
	 */
	public FunctionalElement(FunctionalElement from)
	{

		this.group=from.group;
		this.ID=from.ID;
		this.parent=from.parent;
		
	}
	
	/** 
	 * get the id
	 * @return
	 */
	public int getID()
	{
		if(parent!=null)
		{	
			assert this==parent.getChild(ID);
		}
		else
		{
			assert this==group.getChild(ID);
		}
		
		return ID;
	}
	
	/**
	 * do the thing
	 * @return
	 */
	public ElementContainer getParent()
	{
		return parent;
	}
	
	/**
	 * this is really standard
	 * @return
	 */
	public FunctionalGroup getGroup()
	{
		return group;
	}
	
	/**
	 * add to a new parent.
	 * @param newParent
	 * @return
	 */
	protected boolean addTo( ElementContainer newParent, int id)
	{
		
		
		// now, it's actually usefull to check for the EXACT same object here,
		// it needs to be the same, not just a copy or something.
		// why? it doesn't have to be...
		// no real reason, but I know that an element should never travel between groups.
		
		if(!newParent.getGroup().equals(this.getGroup()))
		{
			return false;
		}
		
		if(newParent != parent)
		{
			if(parent!= null)
			{
				parent.removeChild(this);
			}
			else
			{
				// okay, we are moving from root to, presumably, a new container.
				if(this.ID!=-1)
				{
					group.removeChild(this);
				}
			}
		}
		
		this.ID=id;
		this.parent=newParent;
		return true;
	}
	
	/**
	 * remove from parent, delete the object more or less
	 */
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
	
	public void removeFromParent()
	{
		if(parent!=null)
		{
			parent.removeChild(this);
			this.parent=null;
		}
	}

	@Override
	/**
	 * standard
	 */
	public String toString() {
		return "FunctionalElement [group=" + group + ", parent=" + parent + ", ID=" + ID + "]";
	}



	
	/**
	 * standard.
	 */
	// according to eclipse, this may not work properly. I don't need the method anyways, so I can't be bothered to figure out why.
	/*public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionalElement other = (FunctionalElement) obj;
		if (ID != other.ID)
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}*/
	
	/**
	 * @return
	 */
	public abstract FunctionalElement copy();

	
	
}

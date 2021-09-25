package qwerty4967.AFL.ParseTree;

public interface HasChildren 
{
	public int addChild( Element child);
	public void addChild( int index, Element child);
	public void moveChild(int index, Element child);
	public void removeChild(Element child);
	public void removeChild(int ID);
	public Element getChild(int ID);
	public int getChildID(Element child);
	public int getSize();
	public void removeAllChildren();
	
	
}

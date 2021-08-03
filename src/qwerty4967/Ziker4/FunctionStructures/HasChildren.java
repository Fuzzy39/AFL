package qwerty4967.Ziker4.FunctionStructures;

public interface HasChildren 
{
	public int addChild( FunctionalElement child);
	public void addChild( int index, FunctionalElement child);
	public void removeChild(FunctionalElement child);
	public void removeChild(int ID);
	public FunctionalElement getChild(int ID);
	public int getChildID(FunctionalElement child);
	public int getSize();
	public void removeAllChildren();
	
	
}

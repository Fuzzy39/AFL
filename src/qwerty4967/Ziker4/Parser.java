package qwerty4967.Ziker4;

public class Parser 
{
	public static void test() 
	{
		Shell.out("TEST!");
		FunctionalGroup test = new FunctionalGroup("test");
		try
		{
		StatementData data = new StatementData(test);
		}
		catch(Exception e)
		{
			Shell.out("Fuck, it broke");
			return;
		}
		Shell.out("It didn't crash! Nice.");
		Shell.out("REPORT: "+ test.toString(),"Ziker 4" );
	}
}

import java.util.*;
import java.io.*;
    
public class test
{	
	public static void main (String [] args)
	{
		System.out.print("first line\n");
		System.out.print("second line\r");
		System.out.print("third line\r\n");
		
		/*FileInputStream fis;
		try
		{
			fis = new FileInputStream("test2.txt");	
			int n = 0;
			char c = ' ';
			while(n != -1)
			{
				n = fis.read();
				c = (char)n;
				if(n == 13)
				{
					n = fis.read();
					if (n == 10)
					{
						System.out.println(" ");
					}
				}
				else
				{
					System.out.println("int: " + n + " char: " + c);	
				}
				
			}
		}
		catch(FileNotFoundException fnfe)
		{
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		}
		catch(IOException ioe)
		{
			System.err.println("IOException: " + ioe.getMessage());
		}*/
		
	}
}
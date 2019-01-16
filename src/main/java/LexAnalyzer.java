/*******************************
 *	LexAnalyzer class, contains methods parse, Hash, StoreIdentifier, out
 *	Performs lexical analysis of input file
 *	@author - Ryan Patterson, Geoff Wellington
 *	@date - 06/03/10
 ******************************/

import java.util.*;
import java.io.*;

public class LexAnalyzer
{
	private String identifierArray [];
	private String tokenArray [];
	private Vector<String> outputVec;
	private Vector<String> storedInput;
	private int index;
	
	/******************
	*	Write constructor for Lex
	* 	includes tokenArray, parser,
	* 	identifierTable, and output vector.
	*	@author - Ryan Patterson
	*	@date - 6/3/10
	*******************/
	public LexAnalyzer ()
	{
		 identifierArray = new String [500];
		 tokenArray = new String [45];
			
		 tokenArray[2] = "program";
		 tokenArray[3] = "begin";
		 tokenArray[4] = "end";
		 tokenArray[5] = ";";
		 tokenArray[6] = "declare";
		 tokenArray[7] = ",";
		 tokenArray[8] = ":=";
		 tokenArray[9] = ".";
		 tokenArray[10] = "if";
		 tokenArray[11] = "then";
		 tokenArray[12] = "else";
		 tokenArray[13] = "end_if";
		 tokenArray[14] = "while";
		 tokenArray[15] = "loop";
		 tokenArray[16] = "end_loop";
		 tokenArray[17] = "input";
		 tokenArray[18] = "output";
		 tokenArray[19] = "+";
		 tokenArray[20] = "-";
		 tokenArray[21] = "*";
		 tokenArray[22] = "/";
		 tokenArray[23] = "(";
		 tokenArray[24] = ")";
		 tokenArray[25] = "do";
		 tokenArray[26] = "=";
		 tokenArray[27] = "\r\n";
		 tokenArray[28] = "end of file";
		 tokenArray[29] = "var";
		 tokenArray[30] = "const";
		 tokenArray[31] = "call";
		 tokenArray[32] = "procedure";
		 tokenArray[33] = "odd";
		 
		 tokenArray[40] = "<>";
		 tokenArray[41] = "<";
		 tokenArray[42] = ">";
		 tokenArray[43] = ">=";
		 tokenArray[44] = "<=";
		
		 outputVec = new Vector<String>(10, 10);
		 storedInput = new Vector<String>(10, 10);
		 index = 0;
	
	}//END CONSTRUCTOR
	
    /******************
   	*	Analyze function analyzes input char by char and stores 
   	*	in outputVec and storedInput accordingly
   	* 	@author - Geoff Wellington
   	* 	@date - 06/03/10
   	*******************/  
	public void Analyze(File source)
	{
		/*Scan types
		 * 1 = scan identifier
		 * 2 = scan number
		 * 3 = scan operators
		 */ 
		int scan = 0;		
		int [] output = new int [10];
		FileInputStream fis;
					
		try
		{
			fis = new FileInputStream(source);
			
			int n = 0;
			int count = -1;
			char c = ' ';
			boolean token = false;
			boolean done = false;
			boolean read = true;			
			
			while (!done)
			{	
				if(read == true)
				{
					n = fis.read();								
				}
				
				//check to see if ASCII is A-Z, a-z
				if((n >= 65 && n <= 90) || (n >= 97 && n <= 122))
				{
					scan = 1;
				}
				//check to see if ASCII is 0-9
				else if(n >= 48 && n <= 57 )
				{
					scan = 2;
				}
				//check to see if ASCII is one of ( ) * + , - . / : ; < = > 
				else if((n >= 40 && n <= 47) || (n >=58 && n <=62))
				{
					scan = 3;
				}
				//check for space
				else if(n == 32)
				{
					scan = 4;
				}
				//check for tab
				else if(n == 9)
				{
					scan = 5;
				}				
				//check for new line
				else if(n == 13 || n == 10)
				{					
					scan = 6;				
				}
				//check for EOF
				else if(n == -1)
				{
					if(outputVec.size() == index)
					{
						outputVec.add(index, "");
					}
					outputVec.add(index, (outputVec.get(index) + "28"));
					done = true;
				}
				//check for unknown characters
				else
				{
					c = (char)n;
					System.err.println("UNKNOWN CHARACTER " + c + " ON LINE " + (index+1) + ", TERMINATING COMPILER.\n");
					System.exit(1);
				}
				
				switch(scan)
				{
					//SCAN IDENTIFIERS
					case 1 :
						count = 1;
						c = (char)n;
						String id = "" + c;
						while(read)
						{
							n = fis.read();
							//space
							if(n == 32)
							{
								read = false;
							}
							//symbols ( ) * + , - . / : ; < = > 
							else if ((n >= 40 && n <= 47) || (n >=58 && n <=62))
							{
								read = false;
							}
							//letters A-Z, a-z and numbers 0-9 and symbol _
							else if ((n >= 65 && n <= 90) || (n >= 97 && n <= 122) || (n >= 48 && n <= 57 ) || (n == 95))
							{
								c = (char)n;
								id = id + c;
							}
							else
							{
								read = false;
							}								
							count++;
						}
						if(count > 10)
						{
							System.out.println("IDENTIFIER " + id + " ON LINE " + (index+1) + " WAS TRUNCATED.");
							id = id.substring(0, 10);
						}
						
						token = false;
						for(int ii = 0; ii < tokenArray.length; ii++)
						{
							if(id.equals(tokenArray[ii]))
							{
								if(outputVec.size() == index)
								{
									outputVec.add(index, "");
								}
								outputVec.setElementAt((outputVec.get(index) + ii + " * "), index);
								token = true;
							}
						}
						if(!token)
						{						
							int slot = StoreIdentifier(id);
							if(outputVec.size() == index)
							{
								outputVec.add(index, "");
							}
							
							outputVec.setElementAt((outputVec.get(index) + "0 " + slot + " * "), index);
						}
						if(storedInput.size() == index)
						{
							storedInput.add(index, "");
						}
							
						storedInput.setElementAt((storedInput.get(index) + id), index);						
						scan = 0;
					break;
					
					//SCAN NUMBERS
					case 2 :
						count = 0;
						String num = "";
						boolean dec = false;
						while (read)
						{
							//space and symbols ( ) * + , - / : ; < = > 
							if((n >= 40 && n <= 45) || (n == 47) || (n >=58 && n <=62) || n == 32)
							{
								read = false;
							}
							//letters A-Z, a-z
							else if((n >= 65 && n <= 90) || (n >= 97 && n <= 122))
							{
								System.out.println("INVALID NUMBER (INVALID CHAR) ON LINE " + (index+1) 
												   + ", TERMINATING COMPILER.\n");
								System.exit(1);
							}
							//numbers 0-9, symbol .
							else if((n >= 48 && n <= 57) || n == 46)
							{
								if(n == 46)
								{
									if(dec)
									{
										System.out.println("INVALID NUMBER (TOO MANY .'s) ON LINE " + (index+1) 
														   + ", TERMINATING COMPILER.\n");
										System.exit(1);
									}
									else
									{
										dec = true;
									}
								}									
								c = (char)n;
								num = num + c;
								n = fis.read();
								count++;
							}
							else
							{
								read = false;
							}
							if(count > 5)
							{
								
								System.out.println("INVALID NUMBER (TOO LONG) ON LINE " + (index+1) 
												   + ", TERMINATING COMPILER.\n");
								System.exit(1);
							}
						}
						
						if(storedInput.size() == index)
						{
							storedInput.add(index, "");
						}
						storedInput.setElementAt((storedInput.get(index) + num), index);
						if(outputVec.size() == index)
						{
							outputVec.add(index, "");
						}
						num = "1 " + num;
						outputVec.setElementAt((outputVec.get(index) +  num + " * "), index);
					break;
					
					//SCAN OPERATORS
					case 3 :
						String operator = "";						
						c =  (char)n;
						operator = operator + c;
						
						//check for combination symbols <> and <=
						if(operator.equals("<"))
						{
							n = fis.read();
							c = (char)n;
							if(c == '>' || c == '=')
							{
								operator = operator + c;
								read = true;
							}
							else
							{
								read = false;
							}						
						} 
						//check for combination symbol >=
						else if(operator.equals(">"))
						{
							n = fis.read();
							c = (char)n;
							if(c == '=')
							{
								operator = operator + c;
								read = true;
							}
							else
							{
								read = false;
							}						
						}
						//check for combination symbol :=
						else if(operator.equals(":"))
						{
							n = fis.read();
							c = (char)n;
							if(c == '=')
							{
								operator = operator + c;
								read = true;
							}
							else
							{
								System.out.println("UNEXPETED CHARACTER " + c + " FOLLOWING : ON LINE "
												   + (index+1) + ", TERMINATING COMPILER.\n");
								System.exit(1);
							}
						}
						else
						{
							read = true;
						}
						for(int ii = 0; ii < tokenArray.length; ii++)
						{
							if(operator.equals(tokenArray[ii]))
							{
								if(outputVec.size() == index)
								{
									outputVec.add(index, "");
								}
								outputVec.setElementAt((outputVec.get(index) + ii + " * "), index);
								if(storedInput.size() == index)
								{	
									storedInput.add(index, "");
								}
								storedInput.setElementAt((storedInput.get(index) + operator), index);
							}
						}					
											
					break;
					
					//SPACE
					case 4 :
						if(storedInput.size() == index)
						{
							storedInput.add(index, "");
						}
						storedInput.setElementAt((storedInput.get(index) + " "), index);
						read = true;
					break;
					
					//TAB
					case 5 :
						if(storedInput.size() == index)
						{
							storedInput.add(index, "");
						}
						storedInput.setElementAt((storedInput.get(index) + "   "), index);
						read = true;				
					break;
					//NEW LINE
					case 6:	
						if(outputVec.size() == index)
						{
							outputVec.add(index, "");
						}
						if(storedInput.size() != index)
						{
							outputVec.setElementAt((outputVec.get(index) + "27 * " ), index);
							index++;
							read = true;						
						}
						else
						{
							storedInput.add(index, "");
							read = true;
						}								
					break;					
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
		}
	}//END PARSE
	
	/*************************************************
	*	Stores the identifier in indentifierArray.
	*	@author - Ryan Patterson
	*	@date - 6/4/10
	**************************************************/
	private int StoreIdentifier (String identifier)
	{
		int hashCode;		
		hashCode = Hash(identifier);
		
		if (identifierArray[hashCode] == null)
		{
			identifierArray[hashCode] = identifier;
		}
		else if (identifierArray[hashCode] != identifier)
		{
			int loopCount;		
			loopCount = hashCode;
		
			//loops through and inserts identifier in first empty space or
			// it finds the identifier already present, or the array is full
			while (loopCount < 500)
			{
				if (identifierArray[loopCount] == null)
				{
					identifierArray[loopCount] = identifier;
				}
				else if (identifierArray[loopCount].equals(identifier))
				{
					loopCount = 599;
				}
				loopCount++;
			}
			if (loopCount == 600)
			{
				loopCount = 0;			
				while (loopCount < hashCode)
				{
					if (identifierArray[loopCount] == null)
					{
						identifierArray[loopCount] = identifier;
					}
					else if (identifierArray[loopCount] == identifier)
					{
						loopCount = 599;
					}
					loopCount++;
				}
				
				if (loopCount == hashCode)
				{
					//System.out.println("The identifier array is full.");
				}
				hashCode = loopCount; 				
			}
			        
		}
		return hashCode;
	}//END STOREIDENTIFIER
      
	/*********************
	*	Hash function for parsing
	*	@author - Ryan Patterson
	*	@date - 6/3/10
	**********************/
	private int Hash (String token)
	{
		int code;		
		code = 1;
		int ii;
		
		for (ii = 0; ii < token.length(); ii++)
		{
			code = code + token.charAt(ii);
		}
		code = code % 500;
		
		return code;
	}//END HASH
	
	/**************************
	 *	Prints storedInput and outputVec to screen
	 *	@authour - Geoff Wellington
	 *	@date - 06/04/10
	 ***************************/	 
	 void Out()
	 {
	 	int ii;
	 	for(ii = 0; ii < index; ii++)
	 	{
	 		//skips printing blank lines
	 		if(!outputVec.get(ii).equals("27 * "))
	 		{
	 			System.out.println("\nProgram line number: " + (ii+1));
	 			System.out.println("The input statement is: " + storedInput.get(ii));
	 			System.out.println("The tokens are: " + outputVec.get(ii));
	 		}
	 		
	 	}
	 }//END OUT
	 
	 /**************************
	 *	Prints outputVec to file
	 *	@authour - Geoff Wellington
	 *	@date - 06/15/10
	 ***************************/
	 void OutToFile()
	 {
	 	try
	 	{
	 		FileWriter writer = new FileWriter ("tokens.txt");
		 	int ii;
		 	for(ii = 0; ii <= index; ii++)
		 	{
		 		writer.write(outputVec.get(ii));
		  	}
		  	writer.flush();
		 	writer.close();
	 	}
	 	catch(FileNotFoundException fnfe)
		{
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		}
		catch(IOException ioe)
		{
			System.err.println("IOException: " + ioe.getMessage());
		}
	 	
	 }//END OUTTOFILE
}
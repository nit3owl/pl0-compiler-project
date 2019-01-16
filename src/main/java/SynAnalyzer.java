import java.util.*;
import java.io.*;

/**
 * @(#)SynAnalyzer.java
 *
 *
 * @author Geoff Wellington
 * @version 1.00
 * @date 06/19/10
 */

public class SynAnalyzer
{
	private Scanner scanner;
	private String token;
	private String tokenArray[];
	private int line = 1;
	private boolean foundErr;
	private boolean foundPeriod;
	private boolean done;
	private int lastErr;

	/******************
	*	Constructor for SynAnalyzer, initalizes Scanner object and tokenArray
	*	@author - Geoff Wellington
	*	@date - 06/19/10
	*******************/
    public SynAnalyzer(File source)
    {
    	try
    	{
    		scanner = new Scanner (source);
    		//Search for pattern " * " as delim
    		scanner.useDelimiter("\\s\\*\\s");
    	}
    	catch(FileNotFoundException fnfe)
		{
			System.err.println("FileNotFoundException: " + fnfe.getMessage());
		}
		catch(IOException ioe)
		{
			System.err.println("IOException: " + ioe.getMessage());
		}

		//Used to produce useful error messages
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

		foundErr = false;
		foundPeriod = false;
		done = false;
		lastErr = -1;

    }//END CONSTRUCTOR

    /******************
	*	Analyze method calls block, beings syntax analysis
	*	@author - Geoff Wellington
	*	@date - 06/19/10
	*******************/
    public void Analyze()
    {
		//starts searching syntax
		while(!done)
		{
			getToken();
			block();
		}
		//checks to make sure the program ends with a .
		if(!foundPeriod)
		{
			System.err.println("ERROR ON LINE " + line + ": EXPECTED . BEFORE EOF");
			foundErr = true;
		}
		//checks to see if their were errors and prints accordingly
		if(foundErr)
		{
			System.out.println("***SYNTAX ANALYSIS COMPLETE WITH ERRORS***");
		}
		else
		{
			System.out.println("***SYNTAX ANALYSIS COMPLETE***");
		}

    }//END ANALYZE

    /******************
	*	Sets variable token to the next token input from file; skips newline and
	*	prints to screen if EOF found
	*	@author - Geoff Wellington
	*	@date - 06/19/10
	*******************/
    private void getToken()
    {
		token = scanner.next();
		while(token.equals("27"))
    	{
    		line++;
    		token = scanner.next();
    	}
    	if(token.equals("9"))
    	{
    		foundPeriod = true;
    		token = scanner.next();
    		if(token.equals("28"))
	    	{
	    		System.out.println("***END OF TOKENS***");
	    		done = true;
	    	}
	    	else
	    	{
	    		System.err.println("ERROR: FOUND TOKENS AFTER . BUT EXPECTED EOF");
	    		System.exit(1);
	    	}
    	}
    	else if(token.equals("28"))
    	{
    		System.out.println("***END OF TOKENS***");
	    	done = true;
    	}

    }//END GETTOKEN

    /******************
	*	Checks to see that the token passed in is the same as the current
	*   token
	*	@author - Geoff Wellington
	*	@date - 06/19/10
	*******************/
    private boolean checkToken(String input)
    {
    	boolean result;
    	if (token.equals(input))
    	{
	        getToken();
	        result = true;
    	}
    	else
    	{
    		result = false;
    	}
    	return result;
    }//END CHECKTOKEN

    /**************************
	 *	Checks to see if the input string is an identifier
	 *	@authour - Geoff Wellington
	 *	@date - 06/16/10
	 ***************************/
    private boolean checkIdent(String input)
    {
    	boolean result = false;
    	if(input.length() >= 2)
    	{
    		if(input.charAt(0) == '0' && input.charAt(1) == ' ')
    		{
    			getToken();
    			result = true;
    		}
    	}
    	else
    	{
    		result = false;
    	}
    	return result;
    }//END CHECKIDENT

    /**************************
	 *	Checks to see if the input string is a number
	 *	@authour - Geoff Wellington
	 *	@date - 06/16/10
	 ***************************/
    private boolean checkNum(String input)
    {
    	boolean result = false;

    	if(input.length() >= 2)
    	{
    		if(input.charAt(0) == '1' && input.charAt(1) == ' ')
    		{
    			getToken();
    			result = true;
    		}
    	}
    	else
    	{
    		result = false;
    	}
    	return result;
    }//END CHECKNUM

    /******************
	*	Checks to see if the token passed in is what was expected
	*   Calls error if token was not expected
	*	@author - Geoff Wellington
	*	@date - 06/19/10
	*******************/
    private boolean expect(String input)
    {
    	boolean result = false;
    	if (checkToken(input))
    	{
    		result = true;
    	}
    	else
    	{
    		result = false;
    		error(input);
    	}
    	return result;
    }//END EXPECT

    /******************
	*	Prints out error messages and halts the program
	*	@author - Geoff Wellington
	*	@date - 06/19/10
	*******************/
    private void error(String input)
    {
    	if(line == lastErr)
    	{
    		;
    	}
    	else
    	{
    		System.err.print("ERROR ON LINE " + line + ": ");
    		if(input.equals("ident"))
	    	{
	    		if(checkNum(token))
	    		{
	    			 System.err.println("EXPECTED IDENTIFIER BUT FOUND NUMBER");
	    		}
	    		else
	    		{
	    			System.err.println("EXPECTED IDENTIFIER BUT FOUND " + tokenArray[Integer.parseInt(token)]);
	    		}
	    	}
	    	else if(input.equals("num"))
	    	{
	    		if(checkIdent(token))
	    		{
	    			System.err.println("EXPECTED NUMBER BUT FOUND IDENTIFIER");
	    		}
	    		else
	    		{
	    			System.err.println("EXPECTED NUMBER ON LINE BUT FOUND "	+ tokenArray[Integer.parseInt(token)]);
	    		}
	    	}
	    	else if(input.equals("factor"))
	    	{
	    		System.err.println("EXPECTED IDENTIFIER, NUMBER, OR ( BUT FOUND " + tokenArray[Integer.parseInt(token)]);
	    	}
	    	else if(input.equals("condition"))
	    	{
	    		if(checkIdent(token))
	    		{
	    			System.err.println("EXPECTED = <> < > <= >= BUT FOUND IDENTIFIER");
	    		}
	    		else if (checkNum(token))
	    		{
	    			System.err.println("EXPECTED = <> < > <= >= BUT FOUND NUMBER");
	    		}
	    		else
	    		{
	    			System.err.println("EXPECTED = <> < > <= >= BUT FOUND "	+ tokenArray[Integer.parseInt(token)]);
	    		}
	    	}
	    	else if(checkIdent(token))
	    	{
	    		System.err.println("EXPECTED " +  tokenArray[Integer.parseInt(input)] + " BUT FOUND IDENTIFIER");
	    	}
	    	else if(checkNum(token))
	    	{
	    		System.err.println("ERROR: EXPECTED " +  tokenArray[Integer.parseInt(input)] + " ON LINE " + line
	    							+" BUT FOUND NUMBER");
	    	}
	    	else
	    	{
	    		System.err.println("EXPECTED " + tokenArray[Integer.parseInt(input)] + " BUT FOUND " + tokenArray[Integer.parseInt(token)]);
	    	}
    	}
      	lastErr = line;
    	foundErr = true;
    }//END ERROR

    /******************************
     * Checks block syntax
     * @author - Geoff Wellington
     * @date - 06/19/10
     ****************************/
    private void block()
    {
    	//check for constant
	    if (checkToken("30"))
	    {
	        do
	        {
				if(!checkIdent(token))
					error("ident");
	            expect("26");			//check for =
	            if(!checkNum(token))
	            	error("num");
	        }
	        while (checkToken("7"));	//check for ,
	        expect("5");				//check for ;
	    }
	    //check for var
	    if (checkToken("29"))
	    {
	        do
	        {
	            if(!checkIdent(token))
	            	error("ident");
	        }
	        while (checkToken("7"));	//check for ,
	        expect("5");				//check for ;
	    }
	    //check for procedure
	    while (checkToken("32"))
	    {
	        if(!checkIdent(token))
	        	error("ident");
	        expect("5");				//check for ;
	        block();
	        expect("5");				//check for ;
	    }
    	statement();
	}//END BLOCK

	/******************************
     * Checks statement syntax
     * @author - Geoff Wellington
     * @date - 06/19/10
     ****************************/
	private void statement()
	{
		//check for identifier
		if (checkIdent(token))
	    {
	        expect("8");				//check for :=
	        expression();
	    }
	    //check for call
	    else if (checkToken("31"))
	    {
	        if(!checkIdent(token))
	        	error("ident");
	    }
	    //check for begin
	    else if (checkToken("3"))
	    {
	        do
	        {
	            statement();
	        }
	        while (checkToken("5"));	//check for ;
	        expect("4");				//check for end
	    }
	    //check for if
	    else if (checkToken("10"))
	    {
	        condition();
	        expect("11");				//check for then
	        statement();
	    }
	    //check for while
	    else if (checkToken("14"))
	    {
	        condition();
	        expect("25");				//check for do
	        statement();
    	}
	}//END STATEMENT

	/******************************
     * Checks condition syntax
     * @author - Geoff Wellington
     * @date - 06/19/10
     ****************************/
     private void condition()
     {
     	if (checkToken("33")) 			//check for odd ... what the hell is that?
     	{
        	expression();
	    }
	    else
	    {
	        expression();
	        //check to see if token is = <> < > <= >=
	        if (token.equals("26") || token.equals("40") || token.equals("41") || token.equals("42")
	        	|| token.equals("43") || token.equals("44"))
	        {
	            getToken();
	            expression();
	        }
	        //calls error for condition, then drops out
	        else
	        {
	        	error("condition");
	        }
	    }
     }//END CONDITION

     /******************************
     * Checks expression syntax
     * @author - Geoff Wellington
     * @date - 06/19/10
     ****************************/
     private void expression()
     {
     	//check to see if token is a + or -
     	if(token.equals("19") || token.equals("20"))
     	{
     		getToken();
     	}

        term();

        //check to see if token is a + or -
    	while(token.equals("19") || token.equals("20"))
    	{
	        getToken();
	        term();
    	}
     }//END EXPRESSION

     /******************************
     * Checks term syntax
     * @author - Geoff Wellington
     * @date - 06/19/10
     ****************************/
     private void term()
     {
     	factor();
     	//check to see if token is * or /
    	while(token.equals("21") || token.equals("22"))
    	{
	        getToken();
	        factor();
    	}
     }//END TERM

     /******************************
     * Checks factor syntax
     * @author - Geoff Wellington
     * @date - 06/19/10
     ****************************/
     private void factor()
     {
     	if (checkToken("23")) 		//check for (
	    {
	        expression();
	        expect("24");				//check for )
	    }
	    else if(!checkIdent(token) && !checkNum(token))
	    {
	    	error("factor");
	    }
     }//END FACTOR
}
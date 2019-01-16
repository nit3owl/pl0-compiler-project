/*******************************
 *	Main class, runs compiler
 *	@author - Geoff Wellington
 *	@date - 06/03/10
 ******************************/

import java.util.*;
import java.io.*;

public class main
{
	public static void main(String[] args)
	{
		File source;
		source = new File(args[0]);
		LexAnalyzer lex = new LexAnalyzer();
		lex.Analyze(source);
		lex.Out();
		System.out.println();
		lex.OutToFile();

		source = new File("tokens.txt");
		SynAnalyzer syn = new SynAnalyzer(source);
		syn.Analyze();

	}
}
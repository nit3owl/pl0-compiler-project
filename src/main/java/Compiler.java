import java.io.File;
import java.lang.invoke.MethodHandles;

/*******************************
 *	Main class, runs compiler
 *  @author - Geoff Wellington
 *	@date - 06/03/10
 ******************************/
public class Compiler {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.printf("Usage: java %s <filename>", MethodHandles.lookup().lookupClass().getSimpleName());
            System.exit(1);
        }

        File source = new File(args[0]);
        LexAnalyzer lex = new LexAnalyzer();
        lex.analyze(source);
        lex.out();
        System.out.println();
        lex.outToFile();

        source = new File("tokens.txt");
        SynAnalyzer syn = new SynAnalyzer(source);
        syn.analyze();
    }
}
package cn.misection.cvac;

import cn.misection.cvac.ast.Ast;
import cn.misection.cvac.codegen.ByteCodeGenerator;
import cn.misection.cvac.codegen.TranslatorVisitor;
import cn.misection.cvac.config.Macro;
import cn.misection.cvac.optimize.Optimizer;
import cn.misection.cvac.parser.Parser;
import cn.misection.cvac.semantic.SemanticVisitor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Mengxu on 2017/1/4.
 */
public class CvaCompiler
{
    private static final String ORDINARY = "-o";

    private static final String ORDINARY_0 = "-o0";

    private static final String ORDINARY_1 = "-o1";

    private static final String ORDINARY_2 = "-o2";

    private static final String ORDINARY_3 = "-o3";

    private static final String PEEK_VERSION = "-v";

    private static final String COMPILE_TO_CLASS = "-c";

    private static final String COMPILE_TO_IL = "-i";

    private static final int NORMAL_EXIT_STATUS = 0;

    private static final int ERROR_EXIT_STATUS = 1;


    public static final String CURRENT_SHELL_PATH = System.getProperty("user.dir");

    private static final String DEBUG_FILE = "res/cvasrc/debug.cva";

    private static final String DEBUG_IL_DIR = String.format("%s/debug/il", CURRENT_SHELL_PATH);

    private static final String DEBUG_CLASS_DIR = String.format("%s/debug/classes", CURRENT_SHELL_PATH);


    public static void main(String[] args)
    {
        String fName = null;
        if (Macro.RELEASE)
        {
            fName = release(args);
        }
        if (Macro.DEBUG)
        {
            fName = DEBUG_FILE;
        }
        InputStream fStream = null;
        try
        {
            fStream = new BufferedInputStream(new FileInputStream(fName));
        }
        catch (FileNotFoundException e)
        {
            System.out.printf("Cannot find the file: %s%n", fName);
            System.exit(ERROR_EXIT_STATUS);
        }
        geneCode(fStream);
    }

    private static void geneCode(InputStream fStream)
    {
        Parser parser = new Parser(fStream);
        Ast.Program.T prog = parser.parse();

        doCheck(prog);

        Optimizer optimizer = new Optimizer();
        optimizer.optimize(prog);

        TranslatorVisitor translator = new TranslatorVisitor();
        translator.visit(prog);

        ByteCodeGenerator generator = new ByteCodeGenerator();
        generator.visit(translator.prog);

        doMkDIrs();

        // ascii instructions to binary file
        jasmin.Main.main(new String[] {String.format("%s/%s.il", DEBUG_IL_DIR, translator.prog.mainClass.id)});

        for (cn.misection.cvac.codegen.ast.Ast.Class.ClassSingle cla : translator.prog.classes)
        {
            jasmin.Main.main(new String[] {String.format("%s/%s.il", DEBUG_IL_DIR, cla.id)});
        }
    }

    private static void doCheck(Ast.Program.T prog)
    {
        SemanticVisitor checker = new SemanticVisitor();
        checker.visit(prog);

        // if the program is correct, we generate code for it
        if (!checker.isOK())
        {
            System.err.println("ERROE: check failed");
            System.exit(ERROR_EXIT_STATUS);
        }
    }

    private static String release(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Hello, this is a simple compiler!");
            System.out.println("Please input the file name which you want to compile");
            System.exit(NORMAL_EXIT_STATUS);
        }
        return args[0];
    }

    private static boolean mkDirs(String dirPath)
    {
        File dir = new File(dirPath);
        if (dir.exists())
        {
            if (dir.isFile())
            {
                System.err.println("ERROR: 当前路径下存在同名文件, 请清除后再编译!");
                System.exit(ERROR_EXIT_STATUS);
            }
            return true;
        }
        else
        {
            return dir.mkdir();
        }
    }

    private static void doMkDIrs()
    {
        if (!mkDirs(DEBUG_CLASS_DIR))
        {
            System.err.printf("mkdir %s failed!\n", DEBUG_CLASS_DIR);
        }
        if (mkDirs(DEBUG_IL_DIR))
        {
            System.err.printf("mkdir %s failed\n", DEBUG_IL_DIR);
        }
    }
}

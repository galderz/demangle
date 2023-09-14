///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import model.JavaMethodBuilder;
import model.NameBuilder;
import model.Namespace;
import model.NamespaceBuilder;
import model.Symbol;
import model.SymbolBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
    name = "demangler"
    , mixinStandardHelpOptions = true
    , version = "demangler 0.1"
    , description = "demangler made with jbang")
class demangle implements Callable<Integer>
{
    @Parameters(
        arity = "0"
        , description = "Text input to demangle"
        , index = "0"
    )
    private String input;

    public static void main(String... args)
    {
        int exitCode = new CommandLine(new demangle()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call()
    {
        final Demangler demangler = new Demangler();
        if (input != null)
        {
            System.out.println(demangler.demangle(input));
            return 0;
        }

        String line;
        while ((line = System.console().readLine()) != null)
        {
            System.out.println(demangler.demangle(line));
        }
        return 0;
    }

    static final class Demangler
    {
        String demangle(String input)
        {
            StringBuilder output = new StringBuilder();
            StringBuilder mangled = new StringBuilder();
            SymbolBuilder<?> builder = null;

            for (int i = 0; i < input.length(); i++)
            {
                final char c = input.charAt(i);

                if ("_Z".contentEquals(mangled) && Character.isDigit(c))
                {
                    builder = new NameBuilder();
                }

                if ("_ZN".contentEquals(mangled))
                {
                    builder = new NamespaceBuilder();
                }

                if (builder instanceof NamespaceBuilder && builder.get() instanceof Namespace ns && 'J' == c)
                {
                    builder = new JavaMethodBuilder(ns);
                }

                mangled.append(c);

                if (builder != null)
                {
                    builder.withChar(c);
                }
            }

            Symbol symbol;
            if (builder != null && (symbol = builder.get()) != null)
            {
                symbol.accept(output);
                mangled.setLength(0);
            }
            else if (!mangled.isEmpty())
            {
                output.append(mangled);
            }

            return output.toString();
        }
    }
}

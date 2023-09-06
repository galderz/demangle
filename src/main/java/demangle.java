///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(
    name = "demangler"
    , mixinStandardHelpOptions = true
    , version = "demangler 0.1"
    , description = "demangler made with jbang")
class demangle implements Callable<Integer>
{
    @Parameters(
        index = "0"
        , description = "Text input to demangle"
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
        System.out.println(demangler.demangle(input));
        return 0;
    }

    static final class Demangler
    {
        final StringBuilder output = new StringBuilder();
        final StringBuilder mangled = new StringBuilder();
        final StringBuilder unknown = new StringBuilder();
        MangledSymbol symbol;

        String demangle(String input)
        {
            for (int i = 0; i < input.length(); i++)
            {
                final char c = input.charAt(i);

                if (Character.isDigit(c))
                {
                    if ("_Z".contentEquals(unknown))
                    {
                        symbol = new MangledName();
                        unknown.setLength(0);
                    }

                    symbol.addDigit(c - '0');
                    continue;
                }

                if (symbol != null && symbol.addChar(c))
                {
                    continue;
                }

                unknown.append(c);
            }

            if (!mangled.isEmpty())
            {
                output.append(mangled);
            }
            else if (symbol != null && symbol.isComplete())
            {
                output.append(symbol.demangle());
            }
            else
            {
                output.append(unknown);
            }

            return output.toString();
        }
    }

    private sealed interface MangledSymbol permits MangledName
    {
        boolean isComplete();

        boolean addChar(char c);

        void addDigit(int digit);

        String demangle();
    }

    private static final class MangledName implements MangledSymbol
    {
        final StringBuilder name = new StringBuilder();
        int length;

        @Override
        public boolean isComplete()
        {
            return name.length() == length;
        }

        @Override
        public boolean addChar(char c)
        {
            if (length > 0)
            {
                name.append(c);
                return true;
            }

            return false;
        }

        @Override
        public void addDigit(int i)
        {
            if (name.isEmpty())
            {
                length = 10 * length + i;
            }
            else
            {
                name.append(i);
            }
        }

        @Override
        public String demangle()
        {
            return name.toString();
        }
    }
}

///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

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
        MangledSymbol symbol;

        String demangle(String input)
        {
            for (int i = 0; i < input.length(); i++)
            {
                final char c = input.charAt(i);

                if ("_Z".contentEquals(mangled))
                {
                    symbol = new MangledName();
                }

                if ("_ZN".contentEquals(mangled))
                {
                    symbol = new MangledNamespace();
                }

                mangled.append(c);

                if (Character.isDigit(c))
                {
                    symbol.addDigit(c - '0');
                    continue;
                }

                if (symbol != null)
                {
                    symbol.addChar(c);
                }
            }

            if (symbol != null && symbol.isComplete())
            {
                output.append(symbol.demangle());
                mangled.setLength(0);
            }
            else if (!mangled.isEmpty())
            {
                output.append(mangled);
            }

            return output.toString();
        }
    }

    private sealed interface MangledSymbol permits
        MangledName
        , MangledNamespace
    {
        boolean isComplete();

        default boolean notComplete()
        {
            return !isComplete();
        }

        void addChar(char c);

        void addDigit(int digit);

        String demangle();
    }

    private static final class MangledNamespace implements MangledSymbol
    {
        final MangledName prefix = new MangledName();
        final MangledName name = new MangledName();
        boolean isComplete;

        @Override
        public boolean isComplete()
        {
            return isComplete;
        }

        @Override
        public void addChar(char c)
        {
            if (prefix.notComplete())
            {
                prefix.addChar(c);
            }
            else if (name.notComplete())
            {
                name.addChar(c);
            }
            else if ('E' == c)
            {
                isComplete = true;
            }
        }

        @Override
        public void addDigit(int digit)
        {
            if (prefix.notComplete())
            {
                prefix.addDigit(digit);
            }
            else
            {
                name.addDigit(digit);
            }
        }

        @Override
        public String demangle()
        {
            return String.format("%s::%s"
                , prefix.demangle()
                , name.demangle()
            );
        }
    }

    private static final class MangledName implements MangledSymbol
    {
        StringBuilder name;
        int length;

        @Override
        public boolean isComplete()
        {
            return name != null && name.length() == length;
        }

        @Override
        public void addChar(char c)
        {
            if (length > 0)
            {
                if (name == null)
                {
                    name = new StringBuilder(length);
                }

                name.append(c);
            }
        }

        @Override
        public void addDigit(int i)
        {
            if (name != null && (name.capacity() - name.length()) > 0)
            {
                name.append(i);
            }
            else
            {
                length = 10 * length + i;
            }
        }

        @Override
        public String demangle()
        {
            return name.toString();
        }
    }
}

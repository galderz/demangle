///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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
            MangledSymbol symbol = null;

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

                if (symbol instanceof MangledNamespace mn && symbol.isComplete() && 'J' == c)
                {
                    symbol = new MangledJavaMethod(mn);
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
        , MangledParameter
        , MangledPrimitive
        , MangledJavaMethod
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

    private static final class MangledParameter implements MangledSymbol
    {
        MangledSymbol symbol;

        @Override
        public boolean isComplete()
        {
            return symbol != null && symbol.isComplete();
        }

        @Override
        public void addChar(char c)
        {
            if (symbol == null)
            {
                if ('P' == c)
                {
                    symbol = new MangledName();
                    return; // Don't add the marker to the name
                }
                else
                {
                    symbol = new MangledPrimitive();
                }
            }

            symbol.addChar(c);
        }

        @Override
        public void addDigit(int digit)
        {
            symbol.addDigit(digit);
        }

        @Override
        public String demangle()
        {
            if (symbol instanceof MangledName)
            {
                return String.format("%s*", symbol.demangle());
            }

            return symbol.demangle();
        }
    }

    private static final class MangledPrimitive implements MangledSymbol
    {
        String demangled;

        @Override
        public boolean isComplete()
        {
            return demangled != null;
        }

        @Override
        public void addChar(char c)
        {
            switch (c)
            {
                case 'a' ->
                    demangled = "signed char"; // todo can we demangle to byte?
                case 'b' ->
                    demangled = "bool"; // todo can me demangle to boolean?
                case 'd' ->
                    demangled = "double";
                case 'f' ->
                    demangled = "float";
                case 'i' ->
                    demangled = "int";
                case 'l' ->
                    demangled = "long";
                case 's' ->
                    demangled = "short";
                case 't' ->
                    demangled = "unsigned short"; // todo can we demangle to char?
                case 'v' ->
                    demangled = "void";
                default ->
                    throw new IllegalArgumentException(String.format(
                        "Illegal argument for primitive parameter: %c"
                        , c
                    ));
            }
        }

        @Override
        public void addDigit(int digit)
        {
            throw new IllegalStateException("A digit should not be added to a primitive");
        }

        @Override
        public String demangle()
        {
            return demangled;
        }
    }

    private static final class MangledJavaMethod implements MangledSymbol
    {
        final MangledNamespace namespace;
        final List<MangledParameter> parameters = new ArrayList<>();
        final MangledParameter returnParam = new MangledParameter();
        MangledParameter currentParam = new MangledParameter();
        boolean initialized;

        private MangledJavaMethod(MangledNamespace namespace)
        {
            this.namespace = namespace;
        }

        @Override
        public boolean isComplete()
        {
            if (currentParam.isComplete())
            {
                appendParameter();
                return returnParam.isComplete();
            }

            return false;
        }

        @Override
        public void addChar(char c)
        {
            if ('J' == c && !initialized)
            {
                initialized = true;
                return;
            }

            if (returnParam.notComplete())
            {
                returnParam.addChar(c);
            }
            else if (currentParam.notComplete())
            {
                currentParam.addChar(c);
            }
            else
            {
                appendParameter();
                currentParam.addChar(c);
            }
        }

        private void appendParameter()
        {
            parameters.add(currentParam);
            currentParam = new MangledParameter();
        }

        @Override
        public void addDigit(int digit)
        {
            if (returnParam.notComplete())
            {
                returnParam.addDigit(digit);
            }
            else
            {
                currentParam.addDigit(digit);
            }
        }

        @Override
        public String demangle()
        {
            return String.format(
                "%s %s(%s)"
                , returnParam.demangle()
                , namespace.demangle()
                , parameters.size() == 1 && "void".equals(parameters.get(0).demangle())
                    ? ""
                    : parameters.stream()
                        .map(MangledParameter::demangle)
                        .collect(Collectors.joining(", "))
            );
        }
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

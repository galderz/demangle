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
        final StringBuilder name = new StringBuilder();
        int num;
        String prefix;
        List<String> parameters = new ArrayList<>();

        String demangle(String input)
        {
            for (int i = 0; i < input.length(); i++)
            {
                final char c = input.charAt(i);
//                if ('_' == c || 'Z' == c || 'N' == c)
//                {
//                    mangled.append(c);
//                    continue;
//                }

                if (name.isEmpty() && Character.isDigit(c))
                {
                    num = 10 * num + (c - '0');
                    continue;
                }

                if (num > 0)
                {
                    name.append(c);
                    num--;
                    continue;
                }

                unknown.append(c);
            }

            if (!mangled.isEmpty())
            {
                output.append(mangled);
            }
            else if (!name.isEmpty())
            {
                output.append(name);
            }
            else
            {
                output.append(unknown);
            }

            return output.toString();
        }
    }
}

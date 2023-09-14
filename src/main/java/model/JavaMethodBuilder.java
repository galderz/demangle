package model;

import java.util.ArrayList;
import java.util.List;

public final class JavaMethodBuilder implements SymbolBuilder<JavaMethod>
{
    private final Namespace namespace;
    private final List<Parameter> parameters = new ArrayList<>();
    private ParameterBuilder paramBuilder = new ParameterBuilder();
    private ParameterBuilder returnBuilder;

    public JavaMethodBuilder(Namespace namespace)
    {
        this.namespace = namespace;
    }

    @Override
    public void withChar(char c)
    {
        if ('J' == c && returnBuilder == null)
        {
            returnBuilder = new ParameterBuilder();
            return;
        }

        if (returnBuilder.get() == null)
        {
            returnBuilder.withChar(c);
        }
        else if (paramBuilder.get() == null)
        {
            paramBuilder.withChar(c);
        }
        else
        {
            parameters.add(paramBuilder.get());
            paramBuilder = new ParameterBuilder();
            paramBuilder.withChar(c);
        }
    }

    @Override
    public JavaMethod get()
    {
        Parameter parameter = paramBuilder.get();
        if (parameter == null)
        {
            return null;
        }

        parameters.add(paramBuilder.get());

        return new JavaMethod(
            namespace
            , returnBuilder.get()
            , parameters
        );
    }
}

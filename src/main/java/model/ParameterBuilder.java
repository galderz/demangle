package model;

public final class ParameterBuilder implements SymbolBuilder<Parameter>
{
    private SymbolBuilder<? extends Parameter> builder;

    @Override
    public void withChar(char c)
    {
        if (builder == null)
        {
            if ('P' == c)
            {
                builder = new TypeBuilder();
                return;
            }
            else
            {
                builder = new PrimitiveBuilder();
            }
        }

        builder.withChar(c);
    }

    @Override
    public Parameter get()
    {
        if (builder != null)
        {
            return builder.get();
        }

        return null;
    }
}

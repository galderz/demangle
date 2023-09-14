package model;

public final class TypeBuilder implements SymbolBuilder<Parameter>
{
    private final NameBuilder builder = new NameBuilder();

    @Override
    public void withChar(char c)
    {
        builder.withChar(c);
    }

    @Override
    public Parameter get()
    {
        final Name name = builder.get();
        if (name != null)
        {
            return new Type(name);
        }

        return null;
    }
}

package model;

public final class NamespaceBuilder implements SymbolBuilder<Namespace>
{
    private final NameBuilder prefixBuilder = new NameBuilder();
    private final NameBuilder nameBuilder = new NameBuilder();
    private boolean isComplete;

    @Override
    public void withChar(char c)
    {
        if (prefixBuilder.get() == null)
        {
            prefixBuilder.withChar(c);
        }
        else if (nameBuilder.get() == null)
        {
            nameBuilder.withChar(c);
        }
        else if ('E' == c)
        {
            isComplete = true;
        }
    }

    @Override
    public Namespace get()
    {
        if (isComplete)
        {
            return new Namespace(prefixBuilder.get(), nameBuilder.get());
        }

        return null;
    }
}

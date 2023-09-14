package model;

public final class PrimitiveBuilder implements SymbolBuilder<Primitive>
{
    private Primitive primitive;

    @Override
    public void withChar(char c)
    {
        // todo use switch assignment?
        switch (c)
        {
            case 'a' ->
                primitive = new Primitive.SignedChar(); // todo can we demangle to byte?
            case 'b' ->
                primitive = new Primitive.Bool(); // todo can me demangle to boolean?
            case 'd' ->
                primitive = new Primitive.Double();
            case 'f' ->
                primitive = new Primitive.Float();
            case 'i' ->
                primitive = new Primitive.Int();
            case 'l' ->
                primitive = new Primitive.Long();
            case 's' ->
                primitive = new Primitive.Short();
            case 't' ->
                primitive = new Primitive.UnsignedShort(); // todo can we demangle to char?
            case 'v' ->
                primitive = new Primitive.Void();
            default ->
                throw new IllegalArgumentException(String.format(
                    "Illegal argument for primitive parameter: %c"
                    , c
                ));
        }
    }

    @Override
    public Primitive get()
    {
        return primitive;
    }
}

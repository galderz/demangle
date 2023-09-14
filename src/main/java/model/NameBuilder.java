package model;

public final class NameBuilder implements SymbolBuilder<Name>
{
    private char[] name;
    private int length;
    private int count;

    @Override
    public void withChar(char c)
    {
        if (Character.isDigit(c))
        {
            if (name == null)
            {
                length = 10 * length + (c - '0');
            }
            else
            {
                append(c);
            }
        }
        else
        {
            if (name == null)
            {
                name = new char[length];
            }

            append(c);
        }
    }

    @Override
    public Name get()
    {
        if (length > 0 && count == length)
        {
            return new Name(name);
        }

        return null;
    }

    private void append(char c)
    {
        name[count++] = c;
    }
}

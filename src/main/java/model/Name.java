package model;

public record Name(char[] name) implements Symbol
{
    @Override
    public void accept(StringBuilder output)
    {
        output.append(name);
    }
}

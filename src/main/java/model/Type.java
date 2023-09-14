package model;

public record Type(Name name) implements Parameter
{
    @Override
    public void accept(StringBuilder output)
    {
        name.accept(output);
        output.append("*");
    }
}

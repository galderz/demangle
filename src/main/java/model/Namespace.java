package model;

public record Namespace
(
    Name prefix
    , Name name
)
implements Symbol
{
    @Override
    public void accept(StringBuilder output)
    {
        output.append(prefix.name())
            .append("::")
            .append(name.name());
    }
}

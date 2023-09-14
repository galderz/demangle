package model;

import java.util.List;

public record JavaMethod
(
    Namespace namespace
    , Parameter returnParam
    , List<Parameter> parameters
)
implements Symbol
{
    @Override
    public void accept(StringBuilder output)
    {
        returnParam.accept(output);
        output.append(" ");
        namespace.accept(output);

        if (parameters.size() == 1 && parameters.get(0) instanceof Primitive.Void)
        {
            output.append("()");
        }
        else
        {
            output.append("(");
            for (int i = 0; i < parameters.size(); i++)
            {
                parameters.get(i).accept(output);
                if (i != parameters.size() - 1)
                {
                    output.append(", ");
                }
            }
            output.append(")");
        }
    }
}

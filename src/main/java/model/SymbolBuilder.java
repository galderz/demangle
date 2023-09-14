package model;

import java.util.function.Supplier;

public sealed interface SymbolBuilder<S extends Symbol>
    extends
        Supplier<S>
    permits
        NameBuilder
        , NamespaceBuilder
        , JavaMethodBuilder
        , ParameterBuilder
        , PrimitiveBuilder
        , TypeBuilder
{
    void withChar(char c);
}

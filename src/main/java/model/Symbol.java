package model;

import java.util.function.Consumer;

/**
 * Symbol =
 *   Name(char[] name)
 *   | Namespace(Name prefix, Name name)
 *   | JavaMethod(Namespace namespace, Parameter returnParam, Parameter* parameters)
 * Parameter = Type(Name name) | Primitive
 * Primitive = SignedChar | Bool | Double | Float | Int | Long | Short | UnsignedShort | Void
 */
public sealed interface Symbol
    extends
        Consumer<StringBuilder>
    permits
        Name
        , Namespace
        , JavaMethod
        , Parameter
{}

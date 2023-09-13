package model;

/**
 * Symbol =
 *   Name(char[] name)
 *   | Namespace(char[] package, char[] name)
 *   | JavaMethod(Namespace namespace, Parameter returnParam, Parameter* parameters)
 * Parameter = Type(char[] name) | Primitive
 * Primitive = SignedChar | Bool | Double | Float | Int | Long | Short | UnsignedShort | Void
 */
public sealed interface Symbol permits
    Name
    , Namespace
    , JavaMethod
{}

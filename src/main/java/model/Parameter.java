package model;

public sealed interface Parameter
    extends Symbol
    permits
        Type
        , Primitive
{}

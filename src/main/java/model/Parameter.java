package model;

public sealed interface Parameter permits
    Type
    , Primitive
{}
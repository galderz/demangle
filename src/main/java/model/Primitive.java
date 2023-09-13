package model;

public sealed interface Primitive extends Parameter
{
    record SignedChar() implements Primitive {}
    record Bool() implements Primitive {}
    record Double() implements Primitive {}
    record Float() implements Primitive {}
    record Int() implements Primitive {}
    record Long() implements Primitive {}
    record Short() implements Primitive {}
    record UnsignedShort() implements Primitive {}
    record Void() implements Primitive {}
}

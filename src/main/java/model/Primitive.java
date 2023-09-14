package model;

public sealed interface Primitive extends Parameter
{
    record SignedChar() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("signed char"); // equivalent to Java's byte
        }
    }

    record Bool() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("bool"); // equivalent to Java's boolean
        }
    }

    record Double() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("double");
        }
    }

    record Float() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("float");
        }
    }

    record Int() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("int");
        }
    }

    record Long() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("long");
        }
    }

    record Short() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("short");
        }
    }

    record UnsignedShort() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("unsigned short"); // equivalent to Java's boolean
        }
    }

    record Void() implements Primitive
    {
        @Override
        public void accept(StringBuilder output)
        {
            output.append("void");
        }
    }
}

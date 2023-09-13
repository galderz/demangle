package model;

public record Namespace(
    char[] prefix
    , char[] name
) implements Symbol {}

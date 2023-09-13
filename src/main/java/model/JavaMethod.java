package model;

import java.util.List;

public record JavaMethod(
    Namespace namespace
    , Parameter returnParam
    , List<Parameter> parameters
)implements Symbol {}

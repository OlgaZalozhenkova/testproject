package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OperationType {
    SUPPLY,
    SELLING
}

package com.xost.generator;

public class GeneratorDeserialiser {
    private String fieldName;
    private Integer startIndex;
    private Integer endIndex;

    public GeneratorDeserialiser(String fieldName, Integer startIndex, Integer endIndex) {
        this.fieldName = fieldName;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }
}

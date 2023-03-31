package com.xost.generator;

import java.util.List;

public class GeneratorObject {
    private String objectName;
    private String objectDescription;
    private List<GeneratorDeserialiser> generatorDeserialisers;

    public GeneratorObject() {
    }

    public GeneratorObject(String objectName, String objectDescription, List<GeneratorDeserialiser> generatorDeserialisers) {
        this.objectName = objectName;
        this.objectDescription = objectDescription;
        this.generatorDeserialisers = generatorDeserialisers;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(String objectDescription) {
        this.objectDescription = objectDescription;
    }

    public List<GeneratorDeserialiser> getGeneratorDeserialisers() {
        return generatorDeserialisers;
    }

    public void setGeneratorDeserialisers(List<GeneratorDeserialiser> generatorDeserialisers) {
        this.generatorDeserialisers = generatorDeserialisers;
    }
}

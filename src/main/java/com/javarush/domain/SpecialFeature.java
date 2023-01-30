package com.javarush.domain;

import java.util.Objects;

public enum SpecialFeature {
    TRAILERS("Trailers"),
    COMMENTARIES("Commentaries"),
    DELETED_SCENES("Deleted Scenes"),
    BEHIND_THE_SCENES("Behind the Scenes");

    private final String value;

    SpecialFeature(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SpecialFeature getFeatureByValue(String value){
        if (Objects.isNull(value) || value.isEmpty()){
            return null;
        }
        for (SpecialFeature feature : SpecialFeature.values()){
            if(value.equals(feature.value)){
                return feature;
            }
        }
        return null;
    }
}

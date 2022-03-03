/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

/**
 *
 * @author khouloud
 */
public enum ClassificationArticleEnum {
    A("A"),
    B("B"),
    C("C");
    private final String classification;

    private ClassificationArticleEnum(String classification) {
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }

}

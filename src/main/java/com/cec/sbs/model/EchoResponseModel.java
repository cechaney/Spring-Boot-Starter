package com.cec.sbs.model;

import java.io.Serializable;

public class EchoResponseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

    public EchoResponseModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

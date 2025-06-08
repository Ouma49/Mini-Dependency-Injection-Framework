package com.di.framework.config;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "constructor-arg")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConstructorArg {
    @XmlAttribute
    private String ref;

    @XmlAttribute
    private String value;

    @XmlAttribute
    private String type;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
} 
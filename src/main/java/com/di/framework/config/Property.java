package com.di.framework.config;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String ref;

    @XmlAttribute
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
} 
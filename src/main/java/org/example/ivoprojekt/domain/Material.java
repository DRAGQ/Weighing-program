package org.example.ivoprojekt.domain;

import java.util.Objects;

public class Material {
    private Integer id;
    private String name;
    private Double humidity;
    private Double coefficient;

    public Material() {}

    public Material(Integer id, String name, Double humidity, Double coefficient) {
        this.id = id;
        this.name = name;
        this.humidity = humidity;
        this.coefficient = coefficient;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Objects.equals(name, material.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}

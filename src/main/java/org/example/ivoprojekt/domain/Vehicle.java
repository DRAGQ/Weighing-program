package org.example.ivoprojekt.domain;

import java.util.Objects;

public class Vehicle {
    private Integer id;
    private String identificationNumber;
    private String description;
    private Double tara;

    public Vehicle() {}

    public Vehicle(Integer id, String identificationNumber, String description, Double tara) {
        this.id = id;
        this.identificationNumber = identificationNumber;
        this.description = description;
        this.tara = tara;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTara() {
        return tara;
    }

    public void setTara(Double tara) {
        this.tara = tara;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(identificationNumber, vehicle.identificationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identificationNumber, description, tara);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", description='" + description + '\'' +
                ", tara=" + tara +
                '}';
    }
}
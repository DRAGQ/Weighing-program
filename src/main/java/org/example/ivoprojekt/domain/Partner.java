package org.example.ivoprojekt.domain;

import java.util.Objects;

public class Partner {
    Integer id;
    String name;
    String type;
    String street;
    String township;
    String postcode;
    String businessId;
    String taxId;
    String vat;

    public Partner() {}

    public Partner(Integer id, String name, String type, String street, String township, String postcode, String businessId, String taxId, String vat) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.street = street;
        this.township = township;
        this.postcode = postcode;
        this.businessId = businessId;
        this.taxId = taxId;
        this.vat = vat;
        //tu este boolien hodnoty ci je dodavatel a odoberatel
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Partner partner = (Partner) o;
        return Objects.equals(name, partner.name) && Objects.equals(type, partner.type) && Objects.equals(street, partner.street) && Objects.equals(township, partner.township) && Objects.equals(postcode, partner.postcode) && Objects.equals(businessId, partner.businessId) && Objects.equals(taxId, partner.taxId) && Objects.equals(vat, partner.vat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, street, township, postcode, businessId, taxId, vat);
    }

    @Override
    public String toString() {
        return "Partner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", street='" + street + '\'' +
                ", township='" + township + '\'' +
                ", postalCode='" + postcode + '\'' +
                ", businessId='" + businessId + '\'' +
                ", taxId='" + taxId + '\'' +
                ", vat='" + vat + '\'' +
                '}';
    }
}

package org.example.ivoprojekt.controller.utill;

public enum PartnerType {
    SUPPLIER,
    CUSTOMER,
    BOTH,
    USER;

    public static PartnerType fromDatabase(String value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
            return PartnerType.valueOf(value.toUpperCase());
    }
}

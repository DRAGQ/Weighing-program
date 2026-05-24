package org.example.ivoprojekt.api.response;

public class DialVehicleResponse {
    private Integer id;
    private String number;
    private String description;

    public DialVehicleResponse(Integer id, String number, String description) {
        this.id = id;
        this.number = number;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }
}

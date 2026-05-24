package org.example.ivoprojekt.api.response;

public class WeightTaraResponse {
    private final Integer id;
    private final String identificationNumber;
    private final Double tara;

    public WeightTaraResponse(Integer id, String identificationNumber, Double tara) {
        this.id = id;
        this.identificationNumber = identificationNumber;
        this.tara = tara;
    }

    public Integer getId() {
        return id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public Double getTara() {
        return tara;
    }
}

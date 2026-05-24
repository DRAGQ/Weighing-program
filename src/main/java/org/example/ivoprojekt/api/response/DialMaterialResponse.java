package org.example.ivoprojekt.api.response;

public class DialMaterialResponse {
    private final Integer id;
    private final String name;

    public DialMaterialResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

package com.meusboleto.backend.DTO;

public class AccessDTO {

    private String token;

    public AccessDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //TODO implementar usuário e liberações (authorities)
}

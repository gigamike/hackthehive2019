package com.gigabytes.freebee.videocall.models;

import java.util.Objects;

public class OpenTokSessionResponse {
    private String apiKey;
    private String sessionId;
    private String token;

    public String getApiKey() {
        return apiKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenTokSessionResponse that = (OpenTokSessionResponse) o;
        return Objects.equals(apiKey, that.apiKey) &&
                Objects.equals(sessionId, that.sessionId) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKey, sessionId, token);
    }

    @Override
    public String toString() {
        return "OpenTokSessionResponse{" +
                "apiKey='" + apiKey + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

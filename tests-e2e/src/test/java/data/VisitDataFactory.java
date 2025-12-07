package data;

import model.Country;
import model.CreateVisitReq;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VisitDataFactory {

    public static CreateVisitReq validVisit(UUID userId, Country country, int rating) {
        return new CreateVisitReq(userId, country, rating);
    }

    public static Map<String, Object> visitWithoutUserId(Country country, int rating) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("country", country);
        body.put("rating", rating);
        return body;
    }

    public static Map<String, Object> visitWithoutCountry(UUID userId, int rating) {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("rating", rating);
        return body;
    }

    public static Map<String, Object> visitWithoutRating(UUID userId, Country country) {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("country", country);
        return body;
    }

    public static Map<String, Object> visitWithInvalidCountry(UUID userId, String country, int rating) {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("country", country);
        body.put("rating", rating);
        return body;
    }

    public static Map<String, Object> visitWithInvalidCountry(UUID userId, Country country, int rating) {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("country", country);
        body.put("rating", rating);
        return body;
    }

    public static CreateVisitReq visitWithInvalidRating(UUID userId, Country country, int rating) {
        return new CreateVisitReq(userId, country, rating);
    }
}

package tests;

import api.VisitApi;
import data.VisitDataFactory;
import model.Country;
import model.CreateVisitReq;
import model.ErrorJson;
import model.Visit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spec.Spec;

import java.util.Map;
import java.util.UUID;

import static model.Country.Cuba;
import static org.junit.jupiter.api.Assertions.*;

public class AddTest extends BaseTest{
    VisitApi api = new VisitApi(Spec.specReq(), Spec.specRes());
    private final static UUID userId = UUID.fromString("0e356b1b-04e7-463a-9afd-10d0bf3cc691");
    private final static UUID userIdInvalid = UUID.fromString("0e356b1b-04e7-463a-9afd-10d0bf3cc690");
    private final static Country China = Country.China;
    private final static Country USA = Country.USA;
    private final static Country Mexico = Country.Mexico;
    private final static Country My = Country.My;


    @Test
    @Tag("positive")
    @DisplayName("Добавление корректного визита")
    void addVisit() {
        int rating = 5;
        CreateVisitReq body = VisitDataFactory.validVisit(userId, Country.China, rating);
        Visit visit  = api.addVisit201(body, Visit.class);

        Assertions.assertAll(
                () -> assertEquals(userId, visit.userId()),
                () -> assertFalse(visit.id().toString().isEmpty()),
                () -> assertEquals(China, visit.country()),
                () -> assertEquals(rating, visit.rating()),
                () -> assertFalse(visit.date().isEmpty()),
                () -> assertFalse(visit.archived())
        );
    }

    @Test
    void addVisitNotExistsUserId() {
        int rating = 5;
        CreateVisitReq body = VisitDataFactory.validVisit(userIdInvalid, Country.China, rating);
        ErrorJson error = api.addVisit(body, 404, ErrorJson.class);

        Assertions.assertAll(
                () -> assertEquals( "country: Not Found", error.type()),
                () -> assertEquals("Not Found", error.title()),
                () -> assertEquals(404, error.status()),
                () -> assertEquals("User not found: " + userIdInvalid, error.detail()),
                () -> assertEquals("/api/visits", error.instance())
        );
    }

    @Test
    void addVisitErrorRatingLessMinValue() {
        int rating = 0;
        CreateVisitReq body = VisitDataFactory.validVisit(userId, Country.China, rating);
        ErrorJson error = api.addVisit(body, 400, ErrorJson.class);

        Assertions.assertAll(
                () -> assertEquals( "country: Bad request", error.type()),
                () -> assertEquals("Bad Request", error.title()),
                () -> assertEquals(400, error.status()),
                () -> assertEquals("Rating must be between 1 and 10", error.detail()),
                () -> assertEquals("/api/visits", error.instance())
        );
    }

    @Test
    void addVisitErrorRatingMoreMaxValue() {
        int rating = 999999999;
        CreateVisitReq body = VisitDataFactory.validVisit(userId, Country.China, rating);
        ErrorJson error = api.addVisit(body, 400, ErrorJson.class);

        Assertions.assertAll(
                () -> assertEquals( "country: Bad request", error.type()),
                () -> assertEquals("Bad Request", error.title()),
                () -> assertEquals(400, error.status()),
                () -> assertEquals("Rating must be between 1 and 10", error.detail()),
                () -> assertEquals("/api/visits", error.instance())
        );
    }

    @Test
    void addVisitErrorCountryNotExistsInEnum() {
        Map<String, Object> body = VisitDataFactory.visitWithInvalidCountry(userId, Country.My, 5);
        ErrorJson error = api.addVisit(body, 400, ErrorJson.class);

        Assertions.assertAll(
                () -> assertEquals( "country: Bad request", error.type()),
                () -> assertEquals("Bad Request", error.title()),
                () -> assertEquals(400, error.status()),
                () -> assertEquals("Invalid country", error.detail()),
                () -> assertEquals("/api/visits", error.instance())
        );
    }

    @Test
    void addVisitErrorWithCountryString() {
        Map<String, Object> body = VisitDataFactory.visitWithInvalidCountry(userId, "China1", 5);
        ErrorJson error = api.addVisit(body, 400, ErrorJson.class);

        Assertions.assertAll(
                () -> assertEquals( "country: Bad request", error.type()),
                () -> assertEquals("Bad Request", error.title()),
                () -> assertEquals(400, error.status()),
                () -> assertEquals("Invalid country", error.detail()),
                () -> assertEquals("/api/visits", error.instance())
        );
    }

    @Test
    void addVisitErrorWithoutUserId() {
        Map<String, Object> body = VisitDataFactory.visitWithoutUserId(Cuba, 5);
        ErrorJson error = api.addVisit(body, 400, ErrorJson.class);

        Assertions.assertAll(
                () -> assertEquals( "country: Bad request", error.type()),
                () -> assertEquals("Bad Request", error.title()),
                () -> assertEquals(400, error.status()),
                () -> assertEquals("userID is required", error.detail()),
                () -> assertEquals("/api/visits", error.instance())
        );
    }

    @Test
    void addVisitErrorWithoutRating() {
        Map<String, Object> body = VisitDataFactory.visitWithoutRating(userId, Cuba);
        ErrorJson error = api.addVisit(body, 400, ErrorJson.class);

        Assertions.assertAll(
                () -> assertEquals( "country: Bad request", error.type()),
                () -> assertEquals("Bad Request", error.title()),
                () -> assertEquals(400, error.status()),
                () -> assertEquals("Rating is required", error.detail()),
                () -> assertEquals("/api/visits", error.instance())
        );
    }

    @Test
    void addVisitErrorWithoutCountry() {
        Map<String, Object> body = VisitDataFactory.visitWithoutCountry(userId, 5);
        ErrorJson error = api.addVisit(body, 400, ErrorJson.class);

        Assertions.assertAll(
                () -> assertEquals( "country: Bad request", error.type()),
                () -> assertEquals("Bad Request", error.title()),
                () -> assertEquals(400, error.status()),
                () -> assertEquals("country is required", error.detail()),
                () -> assertEquals("/api/visits", error.instance())
        );
    }
}

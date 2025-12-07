package api;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import model.Country;
import model.CreateVisitReq;
import model.Visit;
import spec.Spec;

import java.util.*;

import static io.restassured.RestAssured.given;

public class VisitApi {

    private final RequestSpecification reqSpec;
    private final ResponseSpecification resSpec;
    private final static String ADD_VISIT_ENDPOINT = "/visits";

    public VisitApi(RequestSpecification reqSpec, ResponseSpecification resSpec) {
        this.reqSpec = reqSpec;
        this.resSpec = resSpec;
    }

    public <T> T addVisit201(CreateVisitReq body, Class<T> responseClass) {
        return given(Spec.specReq())
                .body(body)
                .when()
                .post(ADD_VISIT_ENDPOINT)
                .then()
                .spec(Spec.specRes())
                .statusCode(201)
                .extract().as(responseClass);
    }

    public <T> T addVisit(CreateVisitReq body, int expectedStatus, Class<T> responseClass) {
        return given(Spec.specReq())
                .body(body)
                .when()
                .post(ADD_VISIT_ENDPOINT)
                .then()
                .spec(Spec.specRes())
                .statusCode(expectedStatus)
                .extract().as(responseClass);
    }


    public <T> T addVisit(Map<String, Object> body, int expectedStatus, Class<T> responseModel) {
        return given(Spec.specReq())
                .body(body)
                .when()
                .post(ADD_VISIT_ENDPOINT)
                .then()
                .spec(Spec.specRes())
                .statusCode(expectedStatus)
                .extract().as(responseModel);
    }

    public List<Visit> addVisits(UUID userId, int countVisit) {
        List<Visit> visits = new ArrayList<>();
        List<Country> countries = Arrays.stream(Country.values()).toList();
        Random random = new Random();

        for (int i = 0; i < countVisit; i++) {
            Country country = countries.get(random.nextInt(countries.size()));
            int rating = random.nextInt(1, 11);
            Visit visit = addVisit201(new CreateVisitReq(userId, country, rating), Visit.class);
            visits.add(visit);
        }
        return visits;
    }
}

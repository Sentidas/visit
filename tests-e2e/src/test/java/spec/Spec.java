package spec;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Spec {

    public static RequestSpecification specReq() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.METHOD)
                .log(LogDetail.URI)
                .log(LogDetail.PARAMS)
                .log(LogDetail.COOKIES)
                .build();
    }

    public static ResponseSpecification specRes() {
        return new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .build();
    }
}

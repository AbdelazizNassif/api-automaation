package apiObjects;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class WhoAmIApi {

    RequestSpecification whoAmIApiDriver;
    String whoAmIApiEndpoint = "cp_internal/whoami" ;
    String token;

    public WhoAmIApi(RequestSpecification loginApiDriver, String token) {
        this.whoAmIApiDriver = loginApiDriver;
        this.token = token;
    }

    public ValidatableResponse sendWhoIamRequest_validToken () {
        return whoAmIApiDriver
                        .contentType(ContentType.JSON)
                        .header("authorization", "Bearer " + token)
                        .when()
                        .get(whoAmIApiEndpoint).then();
    }

    public ValidatableResponse sendWhoIamRequest_invalidToken () {
        return whoAmIApiDriver
                .contentType(ContentType.JSON)
                .header("authorization", "Bearer invalidtoken")
                .header("x-requested-with", "XMLHttpRequest")
                .header("Host", "pay2.foodics.dev")
                .when()
                .get(whoAmIApiEndpoint).then();
    }

    public ValidatableResponse sendWhoIamRequest_missingAuthorizationHeader () {
        return whoAmIApiDriver
                .contentType(ContentType.JSON)
                .header("x-requested-with", "XMLHttpRequest")
                .header("Host", "pay2.foodics.dev")
                .when()
                .get(whoAmIApiEndpoint).then();
    }

    public Response sendWhoIamRequest_withWrongHostHeader () {
        return whoAmIApiDriver
                .contentType(ContentType.JSON)
                .header("authorization", "Bearer " + token)
                .header("Host", "wronghost.dev")
                .when()
                .get(whoAmIApiEndpoint);
    }
}

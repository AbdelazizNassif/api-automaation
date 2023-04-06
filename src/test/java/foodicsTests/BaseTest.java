package foodicsTests;

import apiObjects.LoginApi;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import static filesReaders.ReadFromFiles.getPropertyByKey;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;

public class BaseTest {

    protected String token;
    private RequestSpecification loginApiDriver;

    @BeforeClass
    public void setupPreconditions_loginToFoodicsApp ()
    {
        loginApiDriver = RestAssured.given().baseUri(getPropertyByKey("environment.properties", "APP_URL"));
        LoginApi loginApi = new LoginApi(loginApiDriver);
        Response loginResponse = loginApi.loginToApp();
        JsonPath jp = loginResponse.jsonPath();
        token = jp.get("token");
    }



}

package foodicsTests;

import apiObjects.WhoAmIApi;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static filesReaders.ReadFromFiles.getPropertyByKey;

public class WhoAmIApiTest extends BaseTest{

    RequestSpecification whoAmIApiDriver;
    WhoAmIApi whoAmIApi;

    @BeforeMethod
    public void setupPreconditions ()
    {
        whoAmIApiDriver = RestAssured.given().baseUri(getPropertyByKey("environment.properties", "APP_URL"));
        whoAmIApi = new WhoAmIApi(whoAmIApiDriver,token);
    }
    @Test
    public void testWhoIamRequest_validToken () {
        ValidatableResponse whoAmIResponse = whoAmIApi.sendWhoIamRequest_validToken();

        whoAmIResponse.statusCode(200)
                        .header("Content-Type", "application/json")
                        .body("user.email", Matchers.equalTo(getPropertyByKey("environment.properties", "EMAIL")))
                        .body("user.id", Matchers.notNullValue())
                        .body("user.name", Matchers.equalTo("Test Foodics"))
                        .body("user.is_active", Matchers.equalTo(true))
                        .body("user.last_login",
                            Matchers.containsString(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                        .body("user.updated_at",
                            Matchers.containsString(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                        .time(Matchers.lessThan(1000L));
    }

    @Test
    public void testWhoIamRequest_invalidToken ()
    {
        ValidatableResponse whoAmIResponse = whoAmIApi.sendWhoIamRequest_invalidToken();

        whoAmIResponse.statusCode(401)
                        .header("Content-Type", "application/json")
                        .time(Matchers.lessThan(1000L))
                        .body("message" , Matchers.equalTo("Unauthenticated."))
                        .time(Matchers.lessThan(1000L));
    }

    @Test
    public void testWhoIamRequest_missingAuthorizationHeader ()
    {
        ValidatableResponse whoAmIResponse = whoAmIApi.sendWhoIamRequest_missingAuthorizationHeader();

        whoAmIResponse.statusCode(401)
                        .header("Content-Type", "application/json")
                        .time(Matchers.lessThan(1000L))
                        .body("message" , Matchers.equalTo("Unauthenticated."))
                        .time(Matchers.lessThan(1000L));
    }

    @Test
    public void testWhoIamRequest_withWrongHostHeader ()
    {
        Response whoAmIResponse = whoAmIApi.sendWhoIamRequest_withWrongHostHeader();

        whoAmIResponse.then()
                .statusCode(403)
                .header("Content-Type", "text/html")
                .time(Matchers.lessThan(1000L));

        Assert.assertTrue(whoAmIResponse.asString().contains("403 Forbidden"),
                "The response should contain 403 Forbidden as the host header has wrong value");
    }
}

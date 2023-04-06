package apiObjects;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import static filesReaders.ReadFromFiles.getPropertyByKey;

public class LoginApi {

    RequestSpecification loginApiDriver;
    String loginApiEndpoint = "cp_internal/login" ;
    public LoginApi(RequestSpecification loginApiDriver) {
        this.loginApiDriver = loginApiDriver;
    }

    public Response loginToApp ()
    {
        JSONObject loginBody = new JSONObject();
        loginBody.put("email" , getPropertyByKey("environment.properties", "EMAIL"));
        loginBody.put("password" , getPropertyByKey("environment.properties", "PASSWORD"));


        return loginApiDriver
                .contentType(ContentType.JSON)
                .body(loginBody).log().all()
                .when()
                .post(loginApiEndpoint);
    }
}

package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;
import utilities.ConfigReader;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegistrationApiNegativeTestsSteps {

    private static final String BASE_URL = ConfigReader.getProperty("baseUrl");
    private String registerEndpoint = ConfigReader.getProperty("registerEndpoint");
    private String registeredEmail = ConfigReader.getProperty("registeredEmail");
    private String password = ConfigReader.getProperty("password");
    private String weakPassword = ConfigReader.getProperty("weakPassword");
    private String emailConfirmationUrl = ConfigReader.getProperty("emailConfirmationUrl");
    private int languageID = 0;
    private Response response;

    @Given("the base URL is set as BonApp")
    public void the_base_url_is_set_as_bonapp() {
        System.out.println("Base Url :" + BASE_URL);
    }

    @Given("the API endpoint is register")
    public void the_api_endpoint_is_register() {
        System.out.println("Endpoint :" + registerEndpoint);
    }

    @When("I send a POST request with a registered email")
    public void i_send_a_post_request_with_a_registered_email() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", registeredEmail);
        requestBody.put("password", password);
        requestBody.put("languageId", languageID);
        requestBody.put("emailConfirmationUrl", emailConfirmationUrl);

        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + registerEndpoint);
    }

    @When("I send a POST request with a weak password")
    public void i_send_a_post_request_with_a_weak_password() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "testusertesting@test.com");
        requestBody.put("password", weakPassword);
        requestBody.put("languageId", languageID);
        requestBody.put("emailConfirmationUrl", emailConfirmationUrl);

        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + registerEndpoint);
    }

    @When("I send a POST request with an invalid email format")
    public void i_send_a_post_request_with_an_invalid_email_format() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "invalid-email");
        requestBody.put("password", password);
        requestBody.put("languageId", languageID);
        requestBody.put("emailConfirmationUrl", emailConfirmationUrl);
        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + registerEndpoint);
    }

    @When("I send a POST request with a missing confirmation URL")
    public void i_send_a_post_request_with_a_missing_confirmation_url() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "testusertesting@test.com");
        requestBody.put("password", password);
        requestBody.put("languageId", languageID);
        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + registerEndpoint);
    }

    @When("I send a POST request with a missing email field")
    public void i_send_a_post_request_with_a_missing_email_field() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("password", password);
        requestBody.put("languageId", languageID);
        requestBody.put("emailConfirmationUrl", emailConfirmationUrl);
        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + registerEndpoint);
    }

    @When("I send a POST request with a missing password field")
    public void i_send_a_post_request_with_a_missing_password_field() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", "testusertesting@test.com");
        requestBody.put("languageId", languageID);
        requestBody.put("emailConfirmationUrl", emailConfirmationUrl);
        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + registerEndpoint);

    }

    @Then("the response should contain the error message {string}")
    public void the_response_should_contain_the_error_message(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Then("response status code should be {int}")
    public void response_status_code_should_be(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }
}



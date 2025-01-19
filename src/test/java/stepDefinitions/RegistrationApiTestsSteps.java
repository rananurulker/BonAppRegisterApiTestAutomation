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

public class RegistrationApiTestsSteps {

    private static final String BASE_URL = ConfigReader.getProperty("baseUrl");
    private static String apiEndpoint = "/api/account/register";
    private static String registeredEmail = "rananurulker@gmail.com";
    private static String password = "Ra123456!";
    private static String newPassword = "Ar123456!";
    private static String weakPassword = "weak";
    private static String emailConfirmationUrl = "https://bonaapp-dev-afhjdsgqcuc6fxch.z02.azurefd.net/login";
    private static int languageID = 0;
    private static String randomEmail;
    private static String mailID;
    private static String userId;
    private static String code;
    private static String resetCode;
    private static String mailServerUrl = ConfigReader.getProperty("mailosaurBaseUrl") + "/api/messages?server=" + ConfigReader.getProperty("SERVER_ID");
    private static String mailAuthToken = "Bearer " + ConfigReader.getProperty("mailAuth");
    private Response response;

    @Given("the base URL is set as BonApp")
    public void the_base_url_is_set_as_bonapp() {
        System.out.println("Base Url :" + BASE_URL);
    }

    @Given("the API endpoint is register")
    public void the_api_endpoint_is_register() {
        System.out.println("Endpoint :" + apiEndpoint);
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
                .post(BASE_URL + apiEndpoint);
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
                .post(BASE_URL + apiEndpoint);
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
                .post(BASE_URL + apiEndpoint);
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
                .post(BASE_URL + apiEndpoint);
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
                .post(BASE_URL + apiEndpoint);
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
                .post(BASE_URL + apiEndpoint);

    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("the response should contain the error message {string}")
    public void the_response_should_contain_the_error_message(String expectedMessage) {
        String actualMessage = response.jsonPath().getString("message");
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Given("I generate a random email address")
    public void iGenerateARandomEmailAddress() {
        String randomString = java.util.UUID.randomUUID().toString().substring(0, 10);
        randomEmail = randomString + ConfigReader.getProperty("domain");
    }

    @When("I send a POST request to {string} with the valid data")
    public void iSendAPOSTRequestToWithTheFollowingDetails(String registerEndpoint) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", randomEmail);
        requestBody.put("password", password);
        requestBody.put("languageId", languageID);
        requestBody.put("emailConfirmationUrl", emailConfirmationUrl);

        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + registerEndpoint);

    }

    @Given("I receive the emails from mailosaur server and extract userID and code")
    public void iReceiveTheEmailsFromMailosaurServerAndExtractUserIDandCode() throws InterruptedException {
        Thread.sleep(5000);
        response = given()
                .header("Authorization", mailAuthToken)
                .when()
                .get(mailServerUrl);
        Assert.assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();

        JSONObject jsonData = new JSONObject(responseBody);

        JSONArray items = jsonData.optJSONArray("items");

        if (items != null && !items.isEmpty()) {
            mailID = items.getJSONObject(0).optString("id");
        } else {
            System.out.println("No items found in the response.");
        }

        String getMailUrl = ConfigReader.getProperty("mailosaurBaseUrl") + "/api/messages/" + mailID;
        System.out.println("get specific mail url: " + getMailUrl);
        response = given()
                .header("Authorization", mailAuthToken)
                .when()
                .get(getMailUrl);
        Assert.assertEquals(200, response.getStatusCode());

        JSONObject jsonDataMail = new JSONObject(response.getBody().asString());

        String link = jsonDataMail.getJSONObject("html").getJSONArray("links").getJSONObject(0).getString("href");
        System.out.println("Extracted link: " + link);

        String extractedUserId = getQueryParam(link, "userId");
        String extractedCode = getQueryParam(link, "code");

        userId = extractedUserId;
        code = extractedCode;

        System.out.println(userId);
        System.out.println(code);

        response = given()
                .header("Authorization", mailAuthToken)
                .when()
                .delete(mailServerUrl);

        Assert.assertEquals(204, response.getStatusCode());

    }

    @When("I send a POST request to {string} with query parameters userID and code")
    public void iSendAPOSTRequestToWithQueryParameters(String confirmEndpoint) {
        String confirmUrl = BASE_URL + confirmEndpoint + "?userID=" + userId + "&code=" + code;
        System.out.println("confirmUrl :" + confirmUrl);
        response = given()
                .when()
                .post(confirmUrl);
    }

    @When("I send a POST request to {string} for resending confirmation email")
    public void iSendAPOSTRequestToForResendingConfirmationEmail(String sendConfirmation) {
        String sendConfirmationUrl = BASE_URL + sendConfirmation + "/" + languageID + "?email=" + randomEmail + "&emailConfirmationUrl=" + emailConfirmationUrl;
        System.out.println("sendconfirmUrl :" + sendConfirmationUrl);
        response = given()
                .when()
                .post(sendConfirmationUrl);

    }

    @When("I send a POST request to {string} for password change")
    public void iSendAPOSTRequestToForPasswordChange(String changePasswordRequest) {

        response = given()
                .header("Authorization", mailAuthToken)
                .when()
                .delete(mailServerUrl);
        Assert.assertEquals(204, response.getStatusCode());

        String changePasswordRequestUrl = BASE_URL + changePasswordRequest;
        System.out.println("changePasswordRequestUrl: " + changePasswordRequestUrl);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", randomEmail);
        requestBody.put("languageId", languageID);
        requestBody.put("ConfirmChangePasswordUrl", emailConfirmationUrl);

        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(changePasswordRequestUrl);
    }

    @When("I send a POST request to {string} with the reset code")
    public void iSendAPOSTRequestToWithTheResetCode(String changePassword) throws InterruptedException {
        Thread.sleep(5000);

        Thread.sleep(5000);
        response = given()
                .header("Authorization", mailAuthToken)
                .when()
                .get(mailServerUrl);
        Assert.assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();

        JSONObject jsonData = new JSONObject(responseBody);

        JSONArray items = jsonData.optJSONArray("items");

        if (items != null && !items.isEmpty()) {
            mailID = items.getJSONObject(0).optString("id");
        } else {
            System.out.println("No items found in the response.");
        }

        String getMailUrl = ConfigReader.getProperty("mailosaurBaseUrl") + "/api/messages/" + mailID;
        System.out.println("get specific mail url: " + getMailUrl);
        response = given()
                .header("Authorization", mailAuthToken)
                .when()
                .get(getMailUrl);
        Assert.assertEquals(200, response.getStatusCode());

        JSONObject jsonDataMail = new JSONObject(response.getBody().asString());

        String link = jsonDataMail.getJSONObject("html").getJSONArray("links").getJSONObject(0).getString("href");
        System.out.println("Extracted link: " + link);

        resetCode = getQueryParam(link, "code");

        System.out.println("Reset code: " + resetCode);

        response = given()
                .header("Authorization", mailAuthToken)
                .when()
                .delete(mailServerUrl);

        Assert.assertEquals(204, response.getStatusCode());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", randomEmail);
        requestBody.put("resetCode", resetCode);
        requestBody.put("newPassword", newPassword);

        response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(BASE_URL + changePassword);

    }


    public static String getQueryParam(String url, String param) {
        String regex = "[?&]" + param + "=([^&]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

}



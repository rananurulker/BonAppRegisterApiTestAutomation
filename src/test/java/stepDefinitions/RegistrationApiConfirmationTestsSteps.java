package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import utilities.ConfigReader;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class RegistrationApiConfirmationTestsSteps {
    private static final String BASE_URL = ConfigReader.getProperty("baseUrl");
    private String registerEndpoint = ConfigReader.getProperty("registerEndpoint");
    private String password = ConfigReader.getProperty("password");
    private String newPassword = ConfigReader.getProperty("newPassword");
    private String emailConfirmationUrl = ConfigReader.getProperty("emailConfirmationUrl");
    private int languageID = 0;
    private static String randomEmail;
    private static String mailID;
    private static String userId;
    private static String code;
    private static String resetCode;
    private String mailServerUrl = ConfigReader.getProperty("mailosaurBaseUrl") + "/api/messages?server=" + ConfigReader.getProperty("SERVER_ID");
    private String mailAuthToken = "Bearer " + ConfigReader.getProperty("mailAuth");
    private Response response;

    @Given("I generate a random email address")
    public void iGenerateARandomEmailAddress() {
        String randomString = java.util.UUID.randomUUID().toString().substring(0, 10);
        randomEmail = randomString + ConfigReader.getProperty("domain");
    }

    @When("I send a POST request to register endpoint with the valid data")
    public void iSendAPOSTRequestToRegisterEndpointWithTheValidData() {
        System.out.println("random email: " + randomEmail);
        System.out.println("password : " + password);
        System.out.println("languageID" + languageID);
        System.out.println("registerendpoint: " + registerEndpoint);
        System.out.println("confirmationUrl: " + emailConfirmationUrl);
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
        response = given()
                .when()
                .post(confirmUrl);
    }

    @When("I send a POST request to {string} for resending confirmation email")
    public void iSendAPOSTRequestToForResendingConfirmationEmail(String sendConfirmation) {
        String sendConfirmationUrl = BASE_URL + sendConfirmation + "/" + languageID + "?email=" + randomEmail + "&emailConfirmationUrl=" + emailConfirmationUrl;
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

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    public static String getQueryParam(String url, String param) {
        String regex = "[?&]" + param + "=([^&]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }
}

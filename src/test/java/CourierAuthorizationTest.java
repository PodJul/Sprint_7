import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Courier;
import pojo.CourierId;

public class CourierAuthorizationTest {
    Courier courier = new Courier("rusalka", "hvost", "Ariel");
    Courier courierForLogIn = new Courier("rusalka", "hvost");

    public CourierAuthorizationTest() {
    }

    @Before
    public void setUp() {
        Specification.installSpec(Specification.requestSpec("http://qa-scooter.praktikum-services.ru", "api/v1"), Specification.responseSpec());
    }

    @Before
    public void createNewCourier() {
        RestAssured.given().body(this.courier).when().post("courier", new Object[0]).then().statusCode(201);
    }

    @After
    public void deleteCourier() {
        CourierId idReq = RestAssured.given().body(this.courierForLogIn).post("courier/login", new Object[0]).then().extract().response().as(CourierId.class);
        RestAssured.given().delete("courier/{:id}", new Object[]{idReq.getId()}).then().statusCode(200);
    }

    @Test
    @DisplayName("Check statusCode of courier/login")
    public void checkCourierCanLogIn() {
        Response response = this.sendPostRequestCourierLogin();
        this.checkIdNotNullValue(response);
    }

    @Step("Send POST request to /api/courier/login")
    public Response sendPostRequestCourierLogin() {
        Response response = (Response)RestAssured.given().body(this.courierForLogIn).when().post("courier/login", new Object[0]);
        return response;
    }

    @Step("Check id not null value and statusCode")
    public void checkIdNotNullValue(Response response) {
        response.then().body("id", Matchers.notNullValue(), new Object[0]).and().statusCode(200);
    }

    @Test
    @DisplayName("LogIn without required fields and check statusCode")
    public void LogInWithoutRequiredFieldsAndCheckResponse() {
        Response response = this.sendPostRequestWithEmptyFields();
        this.checkStatusCode(response);
    }

    @Step("Send POST request with empty fields to /api/courier/login")
    public Response sendPostRequestWithEmptyFields() {
        Courier courierWithoutRequiredFields = new Courier("", "");
        Response response = (Response)RestAssured.given().body(courierWithoutRequiredFields).when().post("courier/login", new Object[0]);
        return response;
    }

    @Step("Check statusCode")
    public void checkStatusCode(Response response) {
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("LogIn without required fields and check message")
    public void LogInWithoutRequiredFieldsAndCheckMessage() {
        Response response = this.sendPostRequestWithEmptyFields();
        this.checkMessage(response);
    }

    @Step("Check message")
    public void checkMessage(Response response) {
        response.then().assertThat().body("message", Matchers.equalTo("Недостаточно данных для входа"), new Object[0]);
    }

    @Test
    @DisplayName("LogIn without login and check response")
    public void LogInWithoutLoginAndCheckResponse() {
        Response response = sendPostRequestWithoutLogin();
        checkResponse400(response);
    }

    @Step("Send POST request without login to /api/courier/login")
    public Response sendPostRequestWithoutLogin() {
        Courier courierWithoutLogin = new Courier("", "hvost");
        Response response = RestAssured.given().body(courierWithoutLogin).when().post("courier/login", new Object[0]);
        return response;
    }

    @Step("Check message and statusCode")
    public void checkResponse400(Response response) {
        response.then().assertThat().body("message", Matchers.equalTo("Недостаточно данных для входа"), new Object[0]).and().statusCode(400);
    }

    @Test
    @DisplayName("LogIn without password and check response")
    public void LogInWithoutPasswordAndCheckResponse() {
        Response response = this.sendPostRequestWithoutPassword();
        this.checkResponse400(response);
    }

    @Step("Send POST request without password to /api/courier/login")
    public Response sendPostRequestWithoutPassword() {
        Courier courierWithoutPassword = new Courier("rusalka", "");
        Response response = RestAssured.given().body(courierWithoutPassword).when().post("courier/login", new Object[0]);
        return response;
    }

    @Test
    @DisplayName("LogIn with wrong login and check response")
    public void LogInWithWrongLoginAndCheckResponse() {
        Response response = this.sendPostRequestWithWrongLogin();
        this.checkResponse404(response);
    }

    @Step("Send POST request without wrong login to /api/courier/login")
    public Response sendPostRequestWithWrongLogin() {
        Courier courierWithWrongLogin = new Courier("shark", "hvost", "");
        Response response = RestAssured.given().body(courierWithWrongLogin).when().post("courier/login", new Object[0]);
        return response;
    }

    @Step("Check message and statusCode")
    public void checkResponse404(Response response) {
        response.then().assertThat().body("message", Matchers.equalTo("Учетная запись не найдена"), new Object[0]).and().statusCode(404);
    }

    @Test
    @DisplayName("LogIn with wrong password and check response")
    public void LogInWithWrongPasswordAndCheckResponse() {
        Response response = this.sendPostRequestWithWrongPassword();
        this.checkResponse404(response);
    }

    @Step
    public Response sendPostRequestWithWrongPassword() {
        Courier courierWithWrongPassword = new Courier("rusalka", "wings", "");
        Response response = RestAssured.given().body(courierWithWrongPassword).when().post("courier/login", new Object[0]);
        return response;
    }
}

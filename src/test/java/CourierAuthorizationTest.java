import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Courier;
import pojo.CourierId;
import static io.restassured.RestAssured.given;

public class CourierAuthorizationTest {
    Courier courier = new Courier("rusalka", "hvost", "Ariel");
    Courier courierForLogIn = new Courier("rusalka", "hvost");

    public CourierAuthorizationTest() {
    }

    @Before
    public void setUp() {
    Specification.installSpec(Specification.requestSpec("http://qa-scooter.praktikum-services.ru", "api/v1"), Specification.responseSpec());
    given().body(courier).when().post("courier").then().statusCode(201);
    }

    @After
    public void deleteCourier() {
    CourierId idReq = given().body(courierForLogIn).post("courier/login").then().extract().response().as(CourierId.class);
    given().delete("courier/{:id}", idReq.getId()).then().statusCode(200);
    }

    @Test
    @DisplayName("Check statusCode of courier/login")
    public void checkCourierCanLogIn() {
        Response response = sendPostRequestCourierLogin();
        checkIdNotNullValue(response);
    }

    @Step("Send POST request to /api/courier/login")
    public Response sendPostRequestCourierLogin() {
        return given().body(courierForLogIn).when().post("courier/login");
    }

    @Step("Check id not null value and statusCode")
    public void checkIdNotNullValue(Response response) {
        response.then().body("id", Matchers.notNullValue()).and().statusCode(200);
    }

    @Test
    @DisplayName("LogIn without required fields and check statusCode")
    public void LogInWithoutRequiredFieldsAndCheckResponse() {
        Response response = sendPostRequestWithEmptyFields();
        checkStatusCode(response);
    }

    @Step("Send POST request with empty fields to /api/courier/login")
    public Response sendPostRequestWithEmptyFields() {
        Courier courierWithoutRequiredFields = new Courier("", "");
        return given().body(courierWithoutRequiredFields).when().post("courier/login");

    }

    @Step("Check statusCode")
    public void checkStatusCode(Response response) {
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("LogIn without required fields and check message")
    public void LogInWithoutRequiredFieldsAndCheckMessage() {
        Response response = sendPostRequestWithEmptyFields();
        checkMessage(response);
    }

    @Step("Check message")
    public void checkMessage(Response response) {
        response.then().assertThat().body("message", Matchers.equalTo("Недостаточно данных для входа"));
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
        return given().body(courierWithoutLogin).when().post("courier/login");
    }

    @Step("Check message and statusCode")
    public void checkResponse400(Response response) {
        response.then().assertThat().body("message", Matchers.equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    @Test
    @DisplayName("LogIn without password and check response")
    public void LogInWithoutPasswordAndCheckResponse() {
        Response response = sendPostRequestWithoutPassword();
        checkResponse400(response);
    }

    @Step("Send POST request without password to /api/courier/login")
    public Response sendPostRequestWithoutPassword() {
        Courier courierWithoutPassword = new Courier("rusalka", "");
        return given().body(courierWithoutPassword).when().post("courier/login");
    }

    @Test
    @DisplayName("LogIn with wrong login and check response")
    public void LogInWithWrongLoginAndCheckResponse() {
        Response response = sendPostRequestWithWrongLogin();
        checkResponse404(response);
    }

    @Step("Send POST request without wrong login to /api/courier/login")
    public Response sendPostRequestWithWrongLogin() {
        Courier courierWithWrongLogin = new Courier("shark", "hvost", "");
        return given().body(courierWithWrongLogin).when().post("courier/login");
    }

    @Step("Check message and statusCode")
    public void checkResponse404(Response response) {
        response.then().assertThat().body("message", Matchers.equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    @Test
    @DisplayName("LogIn with wrong password and check response")
    public void LogInWithWrongPasswordAndCheckResponse() {
        Response response = sendPostRequestWithWrongPassword();
        checkResponse404(response);
    }

    @Step
    public Response sendPostRequestWithWrongPassword() {
        Courier courierWithWrongPassword = new Courier("rusalka", "wings", "");
        return given().body(courierWithWrongPassword).when().post("courier/login");
    }
}

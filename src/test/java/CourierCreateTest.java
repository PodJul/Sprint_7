import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import pojo.Courier;
import org.junit.Test;
import pojo.CourierId;


import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;
public class CourierCreateTest {
    @Before

    public void setUp() {
        Specification.installSpec(Specification.requestSpec("http://qa-scooter.praktikum-services.ru","api/v1"),Specification.responseSpec());
    }
    Courier courier=new Courier("rusalka","hvost","Ariel");
    Courier courierForLogIn=new Courier("rusalka","hvost");


    @After

    public void deleteCourier() {

        CourierId idReq = given()

                .body(courierForLogIn)
                .post("courier/login")
                .then()
                .statusCode(200)
                .extract().response().as(CourierId.class);

        given().delete("courier/{:id}",idReq.getId())
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Create new courier and check statusCode")
    public void createNewCourierAndCheckResponse(){
        Response response=sendPostRequestCourier();
        checkBodyAndStatusCode(response);}

    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(){
        Response response =given().body(courier)
                .when()
                .post ("courier");
        return response;}

    @Step("Check body and statusCode")
    public void checkBodyAndStatusCode(Response response) {

        response.then().body("ok",is(true))
                .and().statusCode(201);}

    @Test
    @DisplayName("Create double courier and check statusCode")
    public void createDoubleCourierAndCheckResponse(){
        Response response=sendPostRequestCourier();
        checkBodyAndStatusCode(response);
        Response newResponse=sendDoublePostRequestCourier();
        checkStatusCode409 (newResponse);
    }

    @Step("Send double POST request to /api/v1/courier")
    public Response sendDoublePostRequestCourier(){
        Response newResponse =given().body(courier)
                .when()
                .post ("courier");
        return newResponse;}

    @Step("Check statusCode 409")
    public void checkStatusCode409 (Response newResponse) {

        newResponse.then().statusCode(409);

    }
    // этот тест будет падать из-за бага: Текстовое сообщение не соответствует документации
    @Test
    @DisplayName("Create double courier and check message")
    public void createDoubleCourierAndCheckErrorMessage(){
        Response response=sendPostRequestCourier();
        checkBodyAndStatusCode(response);
        Response newResponse=sendDoublePostRequestCourier();
        checkMessage409 (newResponse);}
    @Step("Check message 409")
    public void checkMessage409 (Response newResponse) {

        newResponse.then().assertThat().body ("message",equalTo("Этот логин уже используется"));

    }
    // этот тест будет падать из-за @After: так как курьер не создан, то нечего и удалять
    @Test
    @DisplayName("Create courier without required fields and check statusCode")
    public void createCourierWithoutRequiredFieldsAndCheckResponse() {
        Response response=sendPostRequestCourierWithEmptyRequiredFields();
        checkStatusCode400(response);}


    @Step("Send POST request to /api/courier with empty required fields")
    public Response sendPostRequestCourierWithEmptyRequiredFields(){
        Courier courierWithoutRequiredFields = new Courier("", "", "");
        Response response =given()
                .body(courierWithoutRequiredFields)
                .when()
                .post("courier");
        return response;}

    @Step("Check statusCode 400")
    public void checkStatusCode400(Response response) {

        response.then().statusCode(400);}

    // этот тест будет падать из-за @After: так как курьер не создан, то нечего и удалять
    @Test
    @DisplayName("Create courier without required fields and check message")
    public void createCourierWithoutRequiredFieldsAndCheckMessage() {
        Response response=sendPostRequestCourierWithEmptyRequiredFields();
        checkMessage400(response);}
    @Step("Check message 400")
    public void checkMessage400(Response response) {

        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    // этот тест будет падать из-за @After: так как курьер не создан, то нечего и удалять
    @Test
    @DisplayName("Create courier without login and check response")
    public void createCourierWithoutLoginAndCheckResponse() {
        Response response=sendPostRequestCourierWithoutLogin();
        checkResponse(response);}

    @Step("Send POST request to /api/courier without login")
    public Response sendPostRequestCourierWithoutLogin(){
        Courier courierWithoutLogin = new Courier("", "hvost", "Ariel");
        Response response =given()
                .body(courierWithoutLogin)
                .when()
                .post("courier");
        return response;}

    @Step("Check response")
    public void checkResponse (Response response) {

        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);}

    // этот тест будет падать из-за @After: так как курьер не создан, то нечего и удалять
    @Test
    @DisplayName("Create courier without password and check response")
    public void createCourierWithoutPasswordAndCheckResponse() {
        Response response=sendPostRequestCourierWithoutPassword();
        checkResponse(response);}

    @Step("Send POST request to /api/courier without password")
    public Response sendPostRequestCourierWithoutPassword(){
        Courier courierWithoutPassword = new Courier("rusalka", "", "Ariel");
        Response response =given()
                .body(courierWithoutPassword)
                .when()
                .post("courier");
        return response;}

    // этот тест будет падать из-за бага: в документации поле firstName не отмечено как необязательное,
    // однако фактически курьер создается
    @Test
    @DisplayName("Create courier without first name and check response")
    public void createCourierWithoutFirstNameAndCheckResponse() {
        Response response=sendPostRequestCourierWithoutFirstName();
        checkResponse(response);}

    @Step("Send POST request to /api/courier without first name")
    public Response sendPostRequestCourierWithoutFirstName(){
        Courier courierWithoutFirstName = new Courier("rusalka", "hvost", "");
        Response response =given()
                .body(courierWithoutFirstName)
                .when()
                .post("courier");
        return response;}
}

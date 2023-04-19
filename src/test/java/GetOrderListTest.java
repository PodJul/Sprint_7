import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import pojo.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetOrderListTest {
    @Before

    public void setUp() {
        Specification.installSpec(Specification.requestSpec("http://qa-scooter.praktikum-services.ru", "api/v1"), Specification.responseSpec());
    }

    Courier courier = new Courier("nezabudka", "2309", "Julia");
    Courier courierForLogIn = new Courier("nezabudka", "2309");


    @Test
    @DisplayName("Get list of orders and check orderId")
    public void getCourierOrderListAndCheckResponse() {
        // создаем курьера
        Response response =given().body(courier)
                .when()
                .post ("courier");
        response.then().body("ok",is(true))
                .and().statusCode(201);
        // авторизируемся, чтобы узнать id курьера
        CourierId idReq = given()
                .body(courierForLogIn)
                .post("courier/login")
                .then()
                .statusCode(200)
                .and()
                .body("id", notNullValue())
                .extract().response().as(CourierId.class);
        // создаем заказ, узнаем трек-номер
        Order order = new Order("Smirnov", "Ivan", "Moscow,Kremlin", "2", "+79990009999", 1, "2023-06-01", "no comments", new String[]{
                "BLACK"});
        OrderTrack track = given()
                .body(order)
                .when()
                .post("orders")
                .then()
                .body("track", notNullValue())
                .and().statusCode(201)
                .extract().response().as(OrderTrack.class);
        int orderTrack = track.getTrack();
        // узнаем id заказа по трек-номеру
        OrderId orderRes = given()
                .param("t", orderTrack)
                .when()
                .get("orders/track")
                .then()
                .statusCode(200)
                .and()
                .body("id", anything())
                .extract().response().as(OrderId.class);
        int orderId = orderRes.getOrder().getId();

        //принимаем заказ курьером
        given()
                .param("courierId", idReq.getId())
                .when()
                .put("orders/accept/{:id}", orderId)
                .then()
                .statusCode(200);


        // запрос списка заказов курьера, проверка
        int listRes = given()
                .param("courierId", idReq.getId())
                .when()
                .get("orders")
                .then().assertThat().statusCode(200).and()
                .body("orders", notNullValue()).extract().body().path("orders[0].id");


        assertThat("заказа нет в списке курьера", listRes,equalTo(orderId));

        // удаление курьера
        given().delete("courier/{:id}",idReq.getId())
                .then()
                .statusCode(200);

    }
}

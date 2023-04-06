import io.qameta.allure.junit4.DisplayName;
import pojo.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.OrderTrack;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private String[] color;
    @Before

    public void setUp() {
        Specification.installSpec(Specification.requestSpec("http://qa-scooter.praktikum-services.ru","api/v1"),Specification.responseSpec());

    }
    public CreateOrderTest(String[] color)  {
        this.color = color;

    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"",""}},
                {new String[]{"BLACK","GREY"}},

        };}

    @Test
    @DisplayName("Create oder and check statusCode")
    public void createNewOrderAndCheckResponse() {

        Order order = new Order("Smirnov","Ivan","Moscow,Kremlin","2","+79990009999",1,"2023-06-01","no comments",color);
        OrderTrack response = given()
                .body(order)
                .when()
                .post ("orders")
                .then()
                .body("track",notNullValue())
                .and().statusCode(201)
                .extract().response().as(OrderTrack.class);


        int track = response.getTrack();
        given()
                .param("track",track)
                .put("orders/cancel")
                .then()
                .statusCode(200);

    }
}

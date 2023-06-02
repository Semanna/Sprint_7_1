import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public class OrdersListTests {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void shouldCreateCourier() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?limit=10&page=0")
                .then()
                .assertThat()
                .statusCode(200)
                .body("orders", hasSize(greaterThan(0)));
    }
}

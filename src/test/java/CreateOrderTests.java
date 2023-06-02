import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;

@RunWith(value = Parameterized.class)
public class CreateOrderTests {

    private List<String> color;

    public CreateOrderTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GRAY")},
                {List.of()}
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание заказа")
    public void shouldCreateOrder() {
        Order order = Order.random();

        order.setColor(color);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .assertThat().statusCode(201)
                .body("track", not(blankOrNullString()));
    }
}

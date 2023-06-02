
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierLogin;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;


public class LogInCourierTests {

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Успешная авторизация")
    public void shouldLoginCourier() {
        Courier courier = Courier.random();
        CourierLogin courierLogin = CourierLogin.of(courier);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat()
                .statusCode(201);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .assertThat().statusCode(200)
                .body("id", not(blankOrNullString()));
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Неуспешная авторизация без пароля")
    public void shouldNotLoginCourierWithoutPassword() {
        CourierLogin courierLogin = new CourierLogin();

        courierLogin.setPassword("");
        courierLogin.setLogin("login");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Неуспешная авторизация без логина")
    public void shouldNotLoginCourierWithoutLogin() {
        CourierLogin courierLogin = new CourierLogin();

        courierLogin.setPassword("pass");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Неуспешная авторизация под несуществующим пользователем")
    public void shouldNotLoginWhenCourierDoesNotExist() {
        Courier courier = Courier.random();
        CourierLogin courierLogin = CourierLogin.of(courier);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .assertThat().statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}

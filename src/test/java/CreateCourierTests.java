import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import model.Courier;
import model.CourierLogin;
import model.LoginResponse;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTests {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Курьер создан")
    public void shouldCreateCourier() {
        Courier courier = Courier.random();

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

        deleteCourier(courier);
    }

    @Test
    @DisplayName("Неуспешное создание курьера, который уже есть в системе")
    public void shouldNotCreateSameCourier() {
        Courier courier = Courier.random();

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat().statusCode(201);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat().statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        deleteCourier(courier);
    }

    @Test
    @DisplayName("Неуспешное создание курьера без логина")
    public void shouldNotCreateCourierWithoutLogin() {
        Courier courier = Courier.random();

        courier.setLogin(null);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Неуспешное создание курьера без пароля")
    public void shouldNotCreateCourierWithoutPassword() {
        Courier courier = Courier.random();

        courier.setPassword(null);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    private void deleteCourier(Courier courier) {
        CourierLogin courierLogin = CourierLogin.of(courier);

        LoginResponse loginResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .assertThat().statusCode(200)
                .extract().as(LoginResponse.class);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .delete("/api/v1/courier/" + loginResponse.getId())
                .then()
                .assertThat().statusCode(200);
    }
}

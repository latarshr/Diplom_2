package steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import pojo.CreateOrder;

import java.util.List;

public class Order extends Base {
    private static final String INGREDIENTS = "ingredients";
    private static final String ORDERS = "orders";

    @Step("Получение списка ингридиентов")
    public static List<String> getIngredients() {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .when()
                .get(INGREDIENTS)
                .then().log().ifError()
                .extract().path("data._id");
    }

    @Step("Создание заказа с авторизацией")
    public static ValidatableResponse createOrderWithAuthorization(CreateOrder ingredients, String accessToken) {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .body(ingredients)
                .when()
                .post(ORDERS)
                .then()
                .log().ifError();
    }

    @Step("Создание заказа без авторизации")
    public static ValidatableResponse createOrderWithOutAuthorization(CreateOrder ingredients) {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .when()
                .post(ORDERS)
                .then()
                .log().ifError();
    }
    @Step("Получение заказа пользователя без авторизации")
    public static ValidatableResponse getOrderWithOutAuthorization() {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .when()
                .get(ORDERS)
                .then()
                .log().ifError();
    }
    @Step("Получение заказа пользователя с авторизацией")
    public static ValidatableResponse getOrderWithAuthorization(String accessToken) {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS)
                .then()
                .log().ifError();
    }
}

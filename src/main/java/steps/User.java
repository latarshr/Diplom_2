package steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import pojo.CreateUser;
import pojo.LoginUser;

public class User extends Base {
    private static final String AUTH = "auth";
    private static final String AUTH_REGISTER = AUTH + "/register";
    private static final String AUTH_LOGIN = AUTH + "/login";
    private static final String AUTH_USER = AUTH + "/user";


    @Step("Создание пользователя")
    public static ValidatableResponse createNewUser(CreateUser body) {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .body(body)
                .when()
                .post(AUTH_REGISTER)
                .then().log().ifError();
    }

    @Step("Авторизация пользователя")
    public static ValidatableResponse userAuthorization(LoginUser body) {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .body(body)
                .when()
                .post(AUTH_LOGIN)
                .then().log().ifError();
    }

    @Step("Удаление пользователя")
    public static ValidatableResponse deleteUser(String accessToken) {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(AUTH_USER)
                .then().log().ifError();
    }

    @Step("Изменение данных пользователя с авторизацией")
    public static ValidatableResponse updateDataOfUser(CreateUser user, String accessToken) {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then()
                .log().ifError();
    }

    @Step("Изменение данных пользователя без авторизации")
    public static ValidatableResponse updateWithoutAuth(CreateUser user) {
        return RestAssured.given()
                .spec(getDefaultRequestSpec())
                .body(user)
                .when()
                .patch(AUTH_USER)
                .then()
                .log().ifError();
    }

}

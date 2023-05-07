package user;

import com.github.javafaker.Faker;
import generator.CreateUserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import pojo.CreateUser;
import pojo.LoginUser;
import steps.User;

import static org.hamcrest.core.IsEqual.equalTo;

public class LoginUserTest {
    private static final String AUTHORIZATION_ERROR_401 = "email or password are incorrect";
    ValidatableResponse response;
    ValidatableResponse loginResponse;
    CreateUser request;


    @Test
    @DisplayName("Авторизация пользователя с валидными данными")
    @Description("Ожидание ответа 200")
    public void authorizationUserSuccessful() {
        request = CreateUserGenerator.getRandomNewUserGenerator();
        response = User.createNewUser(request);
        response.assertThat()
                .statusCode(200)
                .and()
                .assertThat()
                .body("success", equalTo(true));

        loginResponse = User.userAuthorization(new LoginUser(request.getEmail(), request.getPassword()));

        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    @Description("Ожидание ответа 401")
    public void authorizationUserNoValidUser() {
        Faker faker = Faker.instance();
        loginResponse = User.userAuthorization(new LoginUser(faker.internet().emailAddress(), faker.number().toString()));

        loginResponse.assertThat()
                .statusCode(401)
                .and()
                .assertThat().body("message", equalTo(AUTHORIZATION_ERROR_401));
    }

    @After
    @DisplayName("Удаление созданного пользователя")
    public void deleteData() {
        if (response != null) {
            response = User.deleteUser(response.extract().path("accessToken").toString());
            response.assertThat()
                    .statusCode(202)
                    .and()
                    .assertThat()
                    .body("success", equalTo(true));
        }
    }
}

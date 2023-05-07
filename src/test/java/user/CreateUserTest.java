package user;

import generator.CreateUserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import pojo.CreateUser;
import steps.User;

import static org.hamcrest.core.IsEqual.equalTo;

public class CreateUserTest {
    private static final String REGISTER_ERROR_403_ALREADY = "User already exists";
    private static final String REGISTER_ERROR_403_REQUIRED = "Email, password and name are required fields";
    ValidatableResponse response;
    CreateUser request;

    @Test
    @DisplayName("Создание пользователя с валидными данными")
    @Description("Ожидание ответа 200")
    public void createUserAllCorrectParameters() {
        request = CreateUserGenerator.getRandomNewUserGenerator();
        response = User.createNewUser(request);
        response.assertThat().statusCode(200).and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Ожидание ответа 403")
    public void createTwoIdenticalUser() {
        request = CreateUserGenerator.getRandomNewUserGenerator();
        response = User.createNewUser(request);
        response.assertThat().statusCode(200).and().assertThat().body("success", equalTo(true));

        ValidatableResponse errorResponse = User.createNewUser(request);
        errorResponse.assertThat().statusCode(403).and().assertThat().body("message", equalTo(REGISTER_ERROR_403_ALREADY));
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Ожидание ответа 403")
    public void createUserWithoutPassword() {
        request = CreateUserGenerator.getRandomNewUserGeneratorWithoutPassword();
        response = User.createNewUser(request);
        response.assertThat().statusCode(403).and().assertThat().body("message", equalTo(REGISTER_ERROR_403_REQUIRED));
    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Ожидание ответа 403")
    public void createUserWithoutEmail() {
        request = CreateUserGenerator.getRandomNewUserGeneratorWithoutEmail();
        response = User.createNewUser(request);
        response.assertThat().statusCode(403).and().assertThat().body("message", equalTo(REGISTER_ERROR_403_REQUIRED));
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Ожидание ответа 403")
    public void createUserWithoutName() {
        request = CreateUserGenerator.getRandomNewUserGeneratorWithoutName();
        response = User.createNewUser(request);
        response.assertThat().statusCode(403).and().assertThat().body("message", equalTo(REGISTER_ERROR_403_REQUIRED));
    }

    @After
    @DisplayName("Удаление созданного пользователя")
    public void deleteData() {
        if (response.extract().body().path("success").equals(true)) {
            response = User.deleteUser(response.extract().path("accessToken").toString());
            response.assertThat()
                    .statusCode(202)
                    .and()
                    .assertThat()
                    .body("success", equalTo(true));
        }
    }
}
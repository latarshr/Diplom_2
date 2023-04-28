package user;

import com.github.javafaker.Faker;
import generator.CreateUserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CreateUser;
import pojo.LoginUser;
import steps.User;

import static org.hamcrest.core.IsEqual.equalTo;

public class UpdateUserDataTest {
    public static final String AUTH_ERROR_401 = "You should be authorised";
    ValidatableResponse response;
    ValidatableResponse loginResponse;
    CreateUser request;
    Faker faker;
    CreateUser updateDataUser;

    @Before
    @DisplayName("Создание пользователя с валидными данными")
    @Description("Ожидание ответа 200")
    public void createUser() {
        request = CreateUserGenerator.getRandomNewUserGenerator();
        response = User.createNewUser(request);
        response.assertThat().statusCode(200).and().assertThat().body("success", equalTo(true));
        faker = Faker.instance();
        loginResponse = User.userAuthorization(new LoginUser(request.getEmail(), request.getPassword()));

        loginResponse.assertThat()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение 'email' пользователя с авторизацией")
    @Description("Ожидание ответа 200")
    public void updateUserEmailWithAuthorization() {
        updateDataUser = CreateUser.editEmailOfUser();
        ValidatableResponse updateDataOfUser = User.updateDataOfUser(updateDataUser, response.extract().path("accessToken").toString());

        updateDataOfUser.assertThat()
                .statusCode(200)
                .and()
                .body("user.email", equalTo(updateDataUser.getEmail()));
    }

    @Test
    @DisplayName("Изменение 'name' пользователя с авторизацией")
    @Description("Ожидание ответа 200")
    public void updateUserNameWithAuthorization() {
        updateDataUser = CreateUser.editNameOfUser();
        ValidatableResponse updateDataOfUser = User.updateDataOfUser(updateDataUser, response.extract().path("accessToken").toString());

        updateDataOfUser.assertThat()
                .statusCode(200)
                .and()
                .body("user.name", equalTo(updateDataUser.getName()));
    }

    @Test
    @DisplayName("Изменение 'password' пользователя с авторизацией")
    @Description("Ожидание ответа 200")
    public void updateUserPasswordWithAuthorization() {
        updateDataUser = CreateUser.editPasswordOfUser();
        ValidatableResponse updateDataOfUser = User.updateDataOfUser(updateDataUser, response.extract().path("accessToken").toString());

        updateDataOfUser.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение 'name' пользователя без авторизацией")
    @Description("Ожидание ответа 401")
    public void updateUserNameWithoutAuthorization() {
        updateDataUser = CreateUser.editNameOfUser();
        ValidatableResponse updateDataOfUser = User.updateWithoutAuth(updateDataUser);

        updateDataOfUser.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo(AUTH_ERROR_401));
    }

    @Test
    @DisplayName("Изменение 'name' пользователя без авторизацией")
    @Description("Ожидание ответа 401")
    public void updateUserEmailWithoutAuthorization() {
        updateDataUser = CreateUser.editEmailOfUser();
        ValidatableResponse updateDataOfUser = User.updateWithoutAuth(updateDataUser);

        updateDataOfUser.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo(AUTH_ERROR_401));
    }

    @Test
    @DisplayName("Изменение 'name' пользователя без авторизацией")
    @Description("Ожидание ответа 401")
    public void updateUserPasswordWithoutAuthorization() {
        updateDataUser = CreateUser.editPasswordOfUser();
        ValidatableResponse updateDataOfUser = User.updateWithoutAuth(updateDataUser);

        updateDataOfUser.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo(AUTH_ERROR_401));
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

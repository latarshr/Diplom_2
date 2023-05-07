package pojo;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import lombok.Data;

@Data
public class CreateUser {
    private String accessToken;
    private String email;
    private String password;
    private String name;

    public CreateUser() {
    }
    public CreateUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Step("Пользователь для смены почты")
    public static CreateUser editEmailOfUser() {
        return new CreateUser(Faker.instance().internet().emailAddress(), null,
                null);
    }

    @Step("Пользователь для смены имени")
    public static CreateUser editNameOfUser() {
        return new CreateUser(null, null,
                Faker.instance().name().firstName());
    }

    @Step("Пользователь для смены пароля")
    public static CreateUser editPasswordOfUser() {
        return new CreateUser(null, Faker.instance().internet().password(),
                null);
    }
}

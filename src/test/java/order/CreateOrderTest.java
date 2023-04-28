package order;

import generator.CreateUserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CreateOrder;
import pojo.CreateUser;
import steps.Order;
import steps.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class CreateOrderTest {
    private static final String INGREDIENT_ERROR_400 = "Ingredient ids must be provided";
    ValidatableResponse response;
    ValidatableResponse order;
    CreateUser request;
    CreateOrder createOrder;


    @Before
    @DisplayName("Создание пользователя с валидными данными")
    @Description("Ожидание ответа 200")
    public void createUser() {
        request = CreateUserGenerator.getRandomNewUserGenerator();
        response = User.createNewUser(request);
        response.assertThat().statusCode(200).and().assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингредиентами")
    @Description("Ожидание ответа 200")
    public void createOrderWithAuthorization() {
        List<String> ingredientsRequest = Order.getIngredients();
        createOrder = new CreateOrder(ingredientsRequest);
        order = Order.createOrderWithAuthorization(createOrder, response.extract().path("accessToken").toString());
        List<String> ingredientsResponse = order.extract().path("order.ingredients._id");
        order.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        assertTrue("Списки ингредиентов не совпадают", Objects.equals(ingredientsRequest, ingredientsResponse));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с ингредиентами")
    @Description("Ожидание ответа 200")
    public void createOrderWithOutAuthorization() {
        List<String> ingredientsRequest = Order.getIngredients();
        createOrder = new CreateOrder(ingredientsRequest);
        order = Order.createOrderWithOutAuthorization(createOrder);
        List<String> ingredientsResponse = order.extract().path("order.ingredients._id");
        order.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        assertNull("Созадан заказ без авторизации", ingredientsResponse);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    @Description("Ожидание ответа 400")
    public void createOrderWithAuthorizationAndWithOutIngredients() {
        createOrder = new CreateOrder(null);
        order = Order.createOrderWithAuthorization(createOrder, response.extract().path("accessToken").toString());
        order.assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo(INGREDIENT_ERROR_400));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    @Description("Ожидание ответа 400")
    public void createOrderWithOutAuthorizationAndWithOutIngredients() {
        createOrder = new CreateOrder(null);
        order = Order.createOrderWithOutAuthorization(createOrder);

        order.assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo(INGREDIENT_ERROR_400));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и c невалидными ингредиентами")
    @Description("Ожидание ответа 500")
    public void createOrderWithOutAuthorizationAndWithNoValidIngredients() {
        createOrder = new CreateOrder(Arrays.asList("noValid1", "noValid2"));
        order = Order.createOrderWithOutAuthorization(createOrder);
        order.assertThat()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и c невалидными ингредиентами")
    @Description("Ожидание ответа 500")
    public void createOrderWithAuthorizationAndWithNoValidIngredients() {
        createOrder = new CreateOrder(Arrays.asList("noValid1", "noValid2"));
        order = Order.createOrderWithAuthorization(createOrder, response.extract().path("accessToken").toString());
        order.assertThat()
                .statusCode(500);
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

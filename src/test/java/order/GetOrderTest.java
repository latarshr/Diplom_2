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

import java.util.List;
import java.util.Objects;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrderTest {
    private static final String GET_ORDER_ERROR_401 = "You should be authorised";
    ValidatableResponse response;
    ValidatableResponse order;
    CreateUser request;
    CreateOrder createOrder;

    @Before
    @DisplayName("Создание пользователя/создание заказа")
    @Description("Ожидание ответа 200")
    public void createUser() {
        request = CreateUserGenerator.getRandomNewUserGenerator();
        response = User.createNewUser(request);
        response.assertThat().statusCode(200).and().assertThat().body("success", equalTo(true));
        createOrder = new CreateOrder(Order.getIngredients());
        order = Order.createOrderWithAuthorization(createOrder, response.extract().path("accessToken").toString());
        order.assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Ожидание ответа 401")
    public void getOrderWithOutAuthorization() {
        order = Order.getOrderWithOutAuthorization();
        order.assertThat()
                .statusCode(401)
                .and()
                .body("message", equalTo(GET_ORDER_ERROR_401));
    }

    @Test
    @DisplayName("Получение списка заказов с авторизацией")
    @Description("Ожидание ответа 200")
    public void getOrderWithAuthorization() {
        String createOrderId = order.extract().path("order._id");
        order = Order.getOrderWithAuthorization(response.extract().path("accessToken").toString());
        List<String> getOrderId = order.extract().path("orders._id");
        order.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        assertTrue(getOrderId.contains(createOrderId));;
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

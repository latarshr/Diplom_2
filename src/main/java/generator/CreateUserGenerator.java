package generator;

import com.github.javafaker.Faker;
import pojo.CreateUser;

public class CreateUserGenerator {
    public static CreateUser getRandomNewUserGenerator() {
        CreateUser createUser = new CreateUser();
        Faker faker = Faker.instance();
        createUser.setEmail(faker.internet().emailAddress());
        createUser.setPassword(faker.number().toString());
        createUser.setName(faker.name().firstName());
        return createUser;
    }
    public static CreateUser getRandomNewUserGeneratorWithoutPassword() {
        CreateUser createUser = new CreateUser();
        Faker faker = Faker.instance();
        createUser.setEmail(faker.internet().emailAddress());
        createUser.setPassword(null);
        createUser.setName(faker.name().firstName());
        return createUser;
    }

    public static CreateUser getRandomNewUserGeneratorWithoutEmail() {
        CreateUser createUser = new CreateUser();
        Faker faker = Faker.instance();
        createUser.setEmail(null);
        createUser.setPassword(faker.number().toString());
        createUser.setName(faker.name().firstName());
        return createUser;
    }
    public static CreateUser getRandomNewUserGeneratorWithoutName() {
        CreateUser createUser = new CreateUser();
        Faker faker = Faker.instance();
        createUser.setEmail(faker.internet().emailAddress());
        createUser.setPassword(faker.number().toString());
        createUser.setName(null);
        return createUser;
    }
}


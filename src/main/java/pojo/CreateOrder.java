package pojo;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrder {
    private List<String> ingredients;

    public CreateOrder(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}

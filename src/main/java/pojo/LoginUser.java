package pojo;

import lombok.Data;

@Data
public class LoginUser {
    private String email;
    private String password;

    public LoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

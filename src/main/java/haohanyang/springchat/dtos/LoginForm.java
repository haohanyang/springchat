package haohanyang.springchat.dtos;


import org.hibernate.validator.constraints.Length;

public class LoginForm {

    @Length(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Length(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
package haohanyang.springchat.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class RegistrationForm {
    @NotBlank
    @Length(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

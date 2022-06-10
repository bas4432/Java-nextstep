package bong.lines.basic.handler.Model;

public class LoginUserDTO {
    String name;
    String email;
    String id;
    String password;

    public LoginUserDTO(String name, String email, String id ,String password){
        this.name = name;
        this.email = email;
        this.id = id;
        this.password =password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package model;

public class CourierLogin {
    private String login;
    private String password;

    public static CourierLogin of(Courier courier) {
        CourierLogin courierLogin = new CourierLogin();

        courierLogin.password = courier.getPassword();
        courierLogin.login = courier.getLogin();

        return courierLogin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

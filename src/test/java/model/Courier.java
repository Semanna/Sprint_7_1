package model;

import java.util.Random;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    private final static Random random = new Random();

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public static Courier random() {
        Courier courier = new Courier();

        int number = random.nextInt(10000000);
        courier.firstName = "Bob" + number;
        courier.login = "Bob" + number;
        courier.password = "pass" + number;

        return courier;
    }


}

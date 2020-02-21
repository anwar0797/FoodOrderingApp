package uk.ac.mmu.foodorderingapp.Model;


public class User {
    private String Username;
    private String Password;
    private String Phone;
    private String secureCode;


    public User() {

    }

    public User(String username, String password, String secureCode) {
        Username = username;
        Password = password;
        this.secureCode = secureCode;

    }


    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

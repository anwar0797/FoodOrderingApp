package uk.ac.mmu.foodorderingapp.Model;



public class User {
    private String Username;
    private String Password;
    private String Phone;

    public User() {

    }

    public User(String username, String password, String phone) {
        Username = username;
        Password = password;
        Phone = phone;
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

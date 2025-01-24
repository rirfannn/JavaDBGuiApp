package view_user;

public class User {
    private String userID;
    private String username;
    private String email;
    private String password;
    private int age;
    private String gender;
    private String country;
    private String phoneNumber;
    private String role;

    // Constructor
    public User(String userID, String username, String email, String password, int age, String gender, String country, String phoneNumber, String role) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }
}


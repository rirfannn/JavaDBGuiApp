package view_register;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view_login.LoginScene;
import view_user.User;

import java.sql.*;

import databaseconnection.*;

public class RegisterScene extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Menubar and Navigation
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Navigation");
        MenuItem loginMenuItem = new MenuItem("Login");
        MenuItem registerMenuItem = new MenuItem("Register");
        registerMenuItem.setDisable(true); // Disable "Register" since we're on Register Scene
        loginMenuItem.setOnAction(e -> {
            LoginScene loginScene = new LoginScene();
            try {
                loginScene.start(primaryStage); // Reuse the same stage
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        menu.getItems().addAll(loginMenuItem, registerMenuItem);
        menuBar.getMenus().add(menu);

        // Labels and Inputs
        Label lblTitle = new Label("REGISTER");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label lblUserID = new Label("User ID:");
        TextField txtUserID = new TextField();
        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        Label lblConfirmPassword = new Label("Confirm Password:");
        PasswordField txtConfirmPassword = new PasswordField();
        Label lblAge = new Label("Age:");
        Spinner<Integer> spnAge = new Spinner<>(1, 100, 13);
        Label lblGender = new Label("Gender:");
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton rbMale = new RadioButton("Male");
        RadioButton rbFemale = new RadioButton("Female");
        rbMale.setToggleGroup(genderGroup);
        rbFemale.setToggleGroup(genderGroup);
        Label lblCountry = new Label("Country:");
        ComboBox<String> cbCountry = new ComboBox<>();
        cbCountry.getItems().addAll("Indonesia", "Malaysia", "Singapore");
        cbCountry.getSelectionModel().selectFirst();
        Label lblPhoneNumber = new Label("Phone Number:");
        TextField txtPhoneNumber = new TextField();
        CheckBox chkTerms = new CheckBox("I agree to the terms and conditions.");
        Button btnRegister = new Button("Register");
        Label lblAlreadyHaveAccount = new Label("Already have an account?");
        Label lblSignIn = new Label("Sign In");
        lblSignIn.setStyle("-fx-text-fill: blue; -fx-cursor: hand;");

        // Layout for Gender
        HBox genderBox = new HBox(10, rbMale, rbFemale);
        genderBox.setAlignment(Pos.CENTER_LEFT);

        // GridPane for Form Layout
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(lblUserID, 0, 0);
        gridPane.add(txtUserID, 1, 0);
        gridPane.add(lblUsername, 0, 1);
        gridPane.add(txtUsername, 1, 1);
        gridPane.add(lblEmail, 0, 2);
        gridPane.add(txtEmail, 1, 2);
        gridPane.add(lblPassword, 0, 3);
        gridPane.add(txtPassword, 1, 3);
        gridPane.add(lblConfirmPassword, 0, 4);
        gridPane.add(txtConfirmPassword, 1, 4);
        gridPane.add(lblAge, 0, 5);
        gridPane.add(spnAge, 1, 5);
        gridPane.add(lblGender, 0, 6);
        gridPane.add(genderBox, 1, 6);
        gridPane.add(lblCountry, 0, 7);
        gridPane.add(cbCountry, 1, 7);
        gridPane.add(lblPhoneNumber, 0, 8);
        gridPane.add(txtPhoneNumber, 1, 8);
        gridPane.add(chkTerms, 1, 9);

        // Sign In Link with Additional Text
        HBox signInBox = new HBox(5, lblAlreadyHaveAccount, lblSignIn);
        signInBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, menuBar, lblTitle, gridPane, btnRegister, signInBox);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        btnRegister.setOnAction(e -> {
            String userID = txtUserID.getText();
            String username = txtUsername.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();
            int age = spnAge.getValue();
            String gender = rbMale.isSelected() ? "Male" : rbFemale.isSelected() ? "Female" : "";
            String country = cbCountry.getValue();
            String phoneNumber = txtPhoneNumber.getText();
            boolean isTermsChecked = chkTerms.isSelected();

            // Validasi form
            String errorMsg = validateForm(username, email, password, confirmPassword, age, phoneNumber, isTermsChecked);
            if (!errorMsg.isEmpty()) {
                showError(errorMsg);
            } else {
                // Buat objek User
                User user = new User(userID, username, email, password, age, gender, country, phoneNumber, "User");
                try (Connection conn = DBHelper.getConnection()) {
                    String query = "INSERT INTO msuser VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, user.getUserID());
                    ps.setString(2, user.getUsername());
                    ps.setString(3, user.getEmail());
                    ps.setString(4, user.getPassword());
                    ps.setInt(5, user.getAge());
                    ps.setString(6, user.getGender());
                    ps.setString(7, user.getCountry());
                    ps.setString(8, user.getPhoneNumber());
                    ps.setString(9, user.getRole());

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created successfully!");
                        LoginScene loginScene = new LoginScene();
                        loginScene.start(primaryStage);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to create an account!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to database!");
                }
            }
        });

        // Sign In Label Event
        lblSignIn.setOnMouseClicked(e -> {
            LoginScene loginScene = new LoginScene();
            try {
                loginScene.start(primaryStage); // Reuse the same stage
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.setTitle("Dv.CO | Register");
        primaryStage.show();
    }

    private String validateForm(String username, String email, String password, String confirmPassword, int age, String phoneNumber, boolean isTermsChecked) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
            return "All fields must be filled!";
        }
        if (username.length() < 3 || username.length() > 15) {
            return "Username must be between 3-15 characters.";
        }
        if (!email.endsWith("@gmail.com")) {
            return "Email must end with '@gmail.com'.";
        }
        if (!password.matches("[a-zA-Z0-9]+")) {
            return "Password must be alphanumeric.";
        }
        if (!password.equals(confirmPassword)) {
            return "Confirm Password must match Password.";
        }
        if (age <= 13) {
            return "Age must be older than 13 years.";
        }
        if (phoneNumber.length() >= 15) {
            return "Phone Number must be less than 15 characters.";
        }
        if (!isTermsChecked) {
            return "You must agree to the terms and conditions.";
        }
        return "";
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package view_login;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view_admin.AdminHomeScene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import databaseconnection.DBHelper;
import view_register.RegisterScene; // Import RegisterScene
import view_home.HomeScene; // Import HomeScene

public class LoginScene extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Membuat komponen
        Label lblTitle = new Label("Login");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();

        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();

        Button btnLogin = new Button("Login");
        MenuBar menuBar = new MenuBar();
        Menu menuNavigation = new Menu("Navigation");
        MenuItem menuRegister = new MenuItem("Register");
        menuNavigation.getItems().add(menuRegister);
        menuBar.getMenus().add(menuNavigation);

        Label lblSignupMessage = new Label("Don't have an account?");
        lblSignupMessage.setStyle("-fx-font-size: 12px;");

        Label lblSignup = new Label("Sign Up!");
        lblSignup.setStyle("-fx-font-size: 12px; -fx-text-fill: blue; -fx-cursor: hand;");

        HBox hboxSignup = new HBox(lblSignupMessage, lblSignup);
        hboxSignup.setSpacing(5);
        hboxSignup.setAlignment(Pos.CENTER_RIGHT);

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.add(lblEmail, 0, 0);
        gridPane.add(txtEmail, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(txtPassword, 1, 1);
        gridPane.add(btnLogin, 1, 2);
        gridPane.add(hboxSignup, 1, 3);

        VBox root = new VBox(menuBar, lblTitle, gridPane);
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(10);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dv.CO Login");
        primaryStage.show();

        // Event Handler for login
        btnLogin.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Email and Password must be filled!");
                return;
            }

            try (Connection conn = DBHelper.getConnection()) {
                String query = "SELECT * FROM msuser WHERE email = ? AND password = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + email + "!");

                    if (role.equals("Admin")) {
                        AdminHomeScene adminHomeScene = new AdminHomeScene();
                        adminHomeScene.start(new Stage());
                        primaryStage.close();
                    } else {
                        HomeScene homeScene = new HomeScene();
                        homeScene.start(new Stage());
                        primaryStage.close();
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Wrong email or password!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to database!");
            }

        });

        // Event handler for sign up
        lblSignup.setOnMouseClicked(e -> {
            txtEmail.clear();
            txtPassword.clear();
            RegisterScene registerScene = new RegisterScene();
            try {
                registerScene.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        menuRegister.setOnAction(e -> {
            txtEmail.clear();
            txtPassword.clear();
            RegisterScene registerScene = new RegisterScene();
            try {
                registerScene.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

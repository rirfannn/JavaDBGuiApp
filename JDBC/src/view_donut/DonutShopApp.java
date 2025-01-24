package view_donut;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view_home.HomeScene;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonutShopApp extends Application {

    private ListView<String> donutListView;
    private Label welcomeLabel, activeDonutLabel, donutNameLabel, donutDescLabel, donutPriceLabel;
    private MenuBar menuBar;
    private Menu fileMenu;
    private MenuItem logoutMenuItem;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Donut Shop");

        // Create components
        createComponents();

        // Set up the layout
        BorderPane layout = new BorderPane();

        // Set the ListView
        layout.setLeft(createListView());

        // Set up the center content (Donut details)
        VBox detailsBox = new VBox(10);
        detailsBox.getChildren().addAll(welcomeLabel, activeDonutLabel, donutNameLabel, donutDescLabel, donutPriceLabel);
        layout.setCenter(detailsBox);

        // Set up the menu bar
        layout.setTop(menuBar);

        // Create scene and set stage
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createComponents() {
        welcomeLabel = new Label("Hello, User");
        activeDonutLabel = new Label("Active Donut:");
        donutNameLabel = new Label("Name: ");
        donutDescLabel = new Label("Description: ");
        donutPriceLabel = new Label("Price: ");

        // Create menu bar with navigation items
        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        logoutMenuItem = new MenuItem("Logout");
        logoutMenuItem.setOnAction(e -> logout());
        fileMenu.getItems().add(logoutMenuItem);
        menuBar.getMenus().add(fileMenu);
    }

    private ListView<String> createListView() {
        donutListView = new ListView<>();
        
        // Load donuts from database
        List<Donut> donuts = loadDonutsFromDatabase();
        for (Donut donut : donuts) {
            donutListView.getItems().add(donut.getName());
        }

        donutListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateDonutDetails(donuts, newValue);
        });

        return donutListView;
    }

    private List<Donut> loadDonutsFromDatabase() {
        List<Donut> donuts = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DonutName, DonutDescription, DonutPrice FROM msdonut")) {

            while (resultSet.next()) {
                String name = resultSet.getString("DonutName");
                String description = resultSet.getString("DonutDescription");
                double price = resultSet.getDouble("DonutPrice");
                donuts.add(new Donut(name, description, price));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return donuts;
    }
    
 // Tambahkan method untuk mengirim data donut ke HomeScene
    private void sendDonutDataToHomeScene(String donutName) {
        HomeScene homeScene = new HomeScene();
        homeScene.updateDonutDetails(donutName); // Kirim nama donut ke HomeScene
    }

    
    private void updateDonutDetails(List<Donut> donuts, String donutName) {
        for (Donut donut : donuts) {
            if (donut.getName().equals(donutName)) {
                donutNameLabel.setText("Name: " + donut.getName());
                donutDescLabel.setText("Description: " + donut.getDescription());
                donutPriceLabel.setText("Price: Rp " + donut.getPrice());
                break;
            }
        }
    }

    private void logout() {
        System.out.println("Logout.");
    }

    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dvco", "root", "Adiguna98"); // Ganti dengan username dan password MySQL kamu
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Donut class representing each donut
    class Donut {
        private String name;
        private String description;
        private double price;

        public Donut(String name, String description, double price) {
            this.name = name;
            this.description = description;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public double getPrice() {
            return price;
        }
    }
}

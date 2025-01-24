package view_home;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import veiw_cart.CartScene;
import view_login.LoginScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HomeScene extends Application {

    private ListView<String> donutListView;
    private Label welcomeLabel;
    private Label donutNameLabel, donutDescriptionLabel, donutPriceLabel;
    private Spinner<Integer> quantitySpinner;
    private Button addToCartButton;
    private Label quantityLabel;
    private MenuBar menuBar;
    private Menu menuUser, menuLogout;
    private MenuItem cartMenuItem, logoutMenuItem;
    private ObservableList<String> donutList;
    private ObservableList<Donut> cart;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Inisialisasi data donut
        donutList = FXCollections.observableArrayList(
            "Chocolate Donut", 
            "Strawberry Donut", 
            "Glazed Donut", 
            "Blueberry Donut", 
            "Matcha Donut", 
            "Vanilla Sprinkle Donut", 
            "Peanut Butter Donut", 
            "Caramel Donut", 
            "Coconut Donut", 
            "Red Velvet Donut"
        );

        // Inisialisasi komponen UI
        donutListView = new ListView<>(donutList);
        welcomeLabel = new Label("Hello, User");
        donutNameLabel = new Label("Donut Name:");
        donutDescriptionLabel = new Label("Description:");
        donutPriceLabel = new Label("Price:");
        quantityLabel = new Label("Quantity:");
        addToCartButton = new Button("Add to Cart");
        quantitySpinner = new Spinner<>(1, 999, 1);
        cart = FXCollections.observableArrayList(); // Inisialisasi cart

        // Menu Bar dengan User dan Logout menu
        menuBar = new MenuBar();

        // Menu User dengan Home dan Cart
        menuUser = new Menu("Hello, User");
        MenuItem homeMenuItem = new MenuItem("Home");
        cartMenuItem = new MenuItem("Go to Cart");
        menuUser.getItems().addAll(homeMenuItem, cartMenuItem);

        // Menu Logout
        menuLogout = new Menu("Logout");
        logoutMenuItem = new MenuItem("Logout");
        menuLogout.getItems().add(logoutMenuItem);

        // Tambahkan menu ke MenuBar
        menuBar.getMenus().addAll(menuUser, menuLogout);

        // Layout utama
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                menuBar,
                welcomeLabel,
                donutListView,
                donutNameLabel,
                donutDescriptionLabel,
                donutPriceLabel,
                quantityLabel,
                quantitySpinner,
                addToCartButton
        );

        // Mengelola pilihan donut
        donutListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateDonutDetails(newValue);
        });

        // Mengelola "Go to Cart" menu item
        cartMenuItem.setOnAction(e -> {
            goToCartScene(primaryStage);
        });

        // Mengelola "Logout" menu item
        logoutMenuItem.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Logout", "You have been logged out.");
            LoginScene loginScene = new LoginScene();
            loginScene.start(primaryStage);  // Menampilkan LoginScene
        });

        // Mengelola tombol "Add to Cart"
        addToCartButton.setOnAction(e -> {
            String selectedDonut = donutListView.getSelectionModel().getSelectedItem();
            int quantity = quantitySpinner.getValue();

            if (selectedDonut != null) {
                addDonutToCart(selectedDonut, quantity);
                showAlert(Alert.AlertType.INFORMATION, "Added to Cart", selectedDonut + " x" + quantity + " added to cart.");
            } else {
                showAlert(Alert.AlertType.WARNING, "No Donut Selected", "Please select a donut to add to your cart.");
            }
        });

        // Scene setup
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Home Scene");
        primaryStage.show();
    }

    public void updateDonutDetails(String donutName) {
        if (donutName != null) {
            String description = "Description of " + donutName;
            double price = 2.99;  // Contoh harga
            donutNameLabel.setText("Donut Name: " + donutName);
            donutDescriptionLabel.setText("Description: " + description);
            donutPriceLabel.setText("Price: $" + price);
            quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
            addToCartButton.setDisable(false);
        } else {
            donutNameLabel.setText("Donut Name:");
            donutDescriptionLabel.setText("Description:");
            donutPriceLabel.setText("Price:");
            quantitySpinner.getValueFactory().setValue(1);
            addToCartButton.setDisable(true);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addDonutToCart(String name, int quantity) {
        Donut donut = new Donut(name, quantity);
        cart.add(donut);  // Menambahkan donut ke dalam cart
    }

    // Class Donut
    public static class Donut {
        private String name;
        private int quantity;

        public Donut(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void increaseQuantity(int quantity) {
            this.quantity += quantity;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Donut donut = (Donut) obj;
            return name.equals(donut.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return name + " x" + quantity;
        }
    }

    // Pergi ke Cart Scene
    private void goToCartScene(Stage primaryStage) {
        CartScene cartScene = new CartScene(cart);  // Mengirimkan daftar donut beserta kuantitas
        cartScene.start(primaryStage);  // Menampilkan CartScene
    }

}

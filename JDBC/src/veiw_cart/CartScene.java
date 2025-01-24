package veiw_cart;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.collections.*;
import javafx.beans.property.*;
import view_home.HomeScene;

public class CartScene extends Application {
    private TableView<CartItem> cartTableView;
    private Label totalPriceLabel;
    private Button checkoutButton, backButton;
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private ObservableList<HomeScene.Donut> cart;
    
    
    
    // Constructor with parameter
    public CartScene(ObservableList<HomeScene.Donut> cart) {
        this.cart = cart;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Setup UI components
        Label cartLabel = new Label("Your Cart");
        totalPriceLabel = new Label("Total Price: $0.00");
        cartTableView = new TableView<>();

        // Table Columns for cart details
        TableColumn<CartItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<CartItem, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        TableColumn<CartItem, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(cellData -> {
            double total = cellData.getValue().getPrice() * cellData.getValue().getQuantity();
            return new SimpleDoubleProperty(total).asObject();
        });

        cartTableView.getColumns().addAll(nameColumn, priceColumn, quantityColumn, totalColumn);

        // Load cart items from HomeScene
        loadCartItems();

        checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> checkout());

        // Back button to go back to HomeScene
        backButton = new Button("Back to Home");
        backButton.setOnAction(e -> goBackToHome(primaryStage));

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(cartLabel, cartTableView, totalPriceLabel, checkoutButton, backButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setTitle("Cart Scene");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadCartItems() {
        cartItems.clear(); // Clear any previous cart items
        if (cart.isEmpty()) {
            totalPriceLabel.setText("Your cart is empty!");
        } else {
            // Loop through the cart and add the donut to cartItems list
            for (HomeScene.Donut donut : cart) {
                cartItems.add(new CartScene.CartItem(donut.getName(), 2.99, donut.getQuantity()));  // Example price
            }
            updateTotalPrice();
        }
        cartTableView.setItems(cartItems);
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        totalPriceLabel.setText("Total Price: $" + total);
    }

    private void checkout() {
        // Implement checkout functionality
        showAlert(Alert.AlertType.INFORMATION, "Checkout", "Thank you for your purchase!");
    }
    
    private String generateTransactionId() {
        int randomNum = (int) (Math.random() * 1000); // Angka random antara 0-999
        return String.format("TR%03d", randomNum);
    }


    private void goBackToHome(Stage primaryStage) {
        // Go back to HomeScene
        primaryStage.close();
        HomeScene homeScene = new HomeScene();
        homeScene.start(primaryStage);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // CartItem class for displaying cart details in table
    public static class CartItem {
        private StringProperty name;
        private DoubleProperty price;
        private IntegerProperty quantity;

        public CartItem(String name, double price, int quantity) {
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleDoubleProperty(price);
            this.quantity = new SimpleIntegerProperty(quantity);
        }

        public StringProperty nameProperty() {
            return name;
        }

        public DoubleProperty priceProperty() {
            return price;
        }

        public IntegerProperty quantityProperty() {
            return quantity;
        }

        public String getName() {
            return name.get();
        }

        public double getPrice() {
            return price.get();
        }

        public int getQuantity() {
            return quantity.get();
        }
    }
}

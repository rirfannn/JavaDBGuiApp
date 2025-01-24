package view_admin;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminHomeScene extends Application {

    private TableView<Donut> tableView = new TableView<>();
    private TextField nameField = new TextField();
    private TextField priceField = new TextField();
    private TextArea descriptionArea = new TextArea();
    private ObservableList<Donut> donuts = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        // TableView Setup
        TableColumn<Donut, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Donut, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Donut, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Donut, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.getColumns().addAll(idColumn, nameColumn, descriptionColumn, priceColumn);
        tableView.setItems(donuts);

        // Add Listener for TableView Selection
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                descriptionArea.setText(newSelection.getDescription());
                priceField.setText(String.valueOf(newSelection.getPrice()));
            }
        });

        // Input Fields
        nameField.setPromptText("Donut Name");
        priceField.setPromptText("Donut Price");
        descriptionArea.setPromptText("Donut Description");
        descriptionArea.setPrefRowCount(3);

        // Buttons
        Button addButton = new Button("Add Donut");
        addButton.setOnAction(e -> addDonut());

        Button updateButton = new Button("Update Donut");
        updateButton.setOnAction(e -> updateDonut());

        Button deleteButton = new Button("Delete Donut");
        deleteButton.setOnAction(e -> deleteDonut());

        // Layout
        GridPane inputPane = new GridPane();
        inputPane.setHgap(10);
        inputPane.setVgap(10);
        inputPane.setPadding(new Insets(10));
        inputPane.add(new Label("Name:"), 0, 0);
        inputPane.add(nameField, 1, 0);
        inputPane.add(new Label("Price:"), 0, 1);
        inputPane.add(priceField, 1, 1);
        inputPane.add(new Label("Description:"), 0, 2);
        inputPane.add(descriptionArea, 1, 2, 2, 1);
        inputPane.add(addButton, 0, 3);
        inputPane.add(updateButton, 1, 3);
        inputPane.add(deleteButton, 2, 3);

        // Menu Bar
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Options");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> stage.close());
        menu.getItems().add(logoutItem);
        menuBar.getMenus().add(menu);

        // Main Layout
        VBox layout = new VBox(menuBar, tableView, inputPane);
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("Admin Home Scene");
        stage.setScene(scene);
        stage.show();
    }

    private void addDonut() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        String priceText = priceField.getText();

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            String id = "DN" + (donuts.size() + 1); // Generate unique ID
            Donut newDonut = new Donut(id, name, description, price);

            // Add to ObservableList
            donuts.add(newDonut);

            // Simpan ke database atau file (contoh)
            saveDonutToDatabase(newDonut);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Donut added successfully!");
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Price must be a number!");
        }
    }

    // Fungsi untuk menyimpan ke database
    private void saveDonutToDatabase(Donut donut) {
        // Implementasikan koneksi ke database atau logika penyimpanan
        System.out.println("Donut disimpan ke database: " + donut.getName());
    }


    private void updateDonut() {
        Donut selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No donut selected!");
            return;
        }

        String name = nameField.getText();
        String description = descriptionArea.getText();
        String priceText = priceField.getText();

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);

            // Update pada ObservableList
            selected.setName(name);
            selected.setDescription(description);
            selected.setPrice(price);

            // Simpan perubahan ke database
            updateDonutInDatabase(selected);

            tableView.refresh(); // Refresh tampilan TableView
            showAlert(Alert.AlertType.INFORMATION, "Success", "Donut updated successfully!");
            clearFields();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Price must be a number!");
        }
    }

    // Fungsi untuk mengupdate database
    private void updateDonutInDatabase(Donut donut) {
        // Implementasikan logika update ke database
        System.out.println("Donut diperbarui di database: " + donut.getName());
    }


    private void deleteDonut() {
        Donut selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No donut selected!");
            return;
        }

        // Hapus dari ObservableList
        donuts.remove(selected);

        // Hapus dari database
        deleteDonutFromDatabase(selected);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Donut deleted successfully!");
    }

    // Fungsi untuk menghapus dari database
    private void deleteDonutFromDatabase(Donut donut) {
        // Implementasikan logika penghapusan dari database
        System.out.println("Donut dihapus dari database: " + donut.getName());
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nameField.clear();
        priceField.clear();
        descriptionArea.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Donut Class
    public static class Donut {
        private SimpleStringProperty id;
        private SimpleStringProperty name;
        private SimpleStringProperty description;
        private SimpleDoubleProperty price;

        public Donut(String id, String name, String description, double price) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.description = new SimpleStringProperty(description);
            this.price = new SimpleDoubleProperty(price);
        }

        public String getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getDescription() {
            return description.get();
        }

        public void setDescription(String description) {
            this.description.set(description);
        }

        public double getPrice() {
            return price.get();
        }

        public void setPrice(double price) {
            this.price.set(price);
        }
    }
}


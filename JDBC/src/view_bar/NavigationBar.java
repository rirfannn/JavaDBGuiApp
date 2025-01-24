package view_bar;

import javafx.application.Application;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import veiw_cart.CartScene;
import view_home.HomeScene;
import view_login.LoginScene;

public class NavigationBar{
	

    public static MenuBar createNavigationBar(Stage primaryStage, String currentScene) {
        Menu menu = new Menu("Menu");

        MenuItem homeMenuItem = new MenuItem("Home");
        MenuItem cartMenuItem = new MenuItem("Cart");
        MenuItem logoutMenuItem = new MenuItem("Logout");

        if ("Home".equals(currentScene)) {
            homeMenuItem.setDisable(true);
        } else if ("Cart".equals(currentScene)) {
            cartMenuItem.setDisable(true);
        }

        homeMenuItem.setOnAction(e -> {
            HomeScene homeScene = new HomeScene();
            homeScene.start(primaryStage);
        });

        cartMenuItem.setOnAction(e -> {
            CartScene cartScene = new CartScene(null);
            cartScene.start(primaryStage);
        });

        logoutMenuItem.setOnAction(e -> {
            LoginScene loginScene = new LoginScene();
            loginScene.start(primaryStage);
        });

        menu.getItems().addAll(homeMenuItem, cartMenuItem, logoutMenuItem);
        return new MenuBar(menu);
    }
}


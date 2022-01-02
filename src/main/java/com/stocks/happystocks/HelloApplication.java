package com.stocks.happystocks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        HappyStocksController controller = new HappyStocksController();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
        Parent root = loader.load(getClass().getResource("HappyStocks.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("HappyStocks!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}





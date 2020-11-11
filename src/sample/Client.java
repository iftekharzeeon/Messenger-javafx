package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import netUtil.NetworkUtil;

public class Client extends Application {
    @Override
    public void start(Stage primaryStage){
        try {
            //NetworkUtil networkUtil = new NetworkUtil("localhost", 9990);
            new Login(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
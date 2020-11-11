package sample;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import netUtil.NetworkUtil;
import java.io.IOException;

public class Option {
    private Stage stage;
    private Login client;
    private NetworkUtil networkUtil;

    public void groupChat(ActionEvent event) {
        stage.close();
        client.groupChat();

    }

    public void directMessage(ActionEvent event) throws IOException {
        stage.close();
//        Thread t = new Thread(new TaskThread());
//        t.start();
//        t.interrupt();
        //Login.activeClients = true;
        client.activeClients();
    }

    public void setClient(Login login) {
        this.client = login;
    }

    public void setNetworkUtil(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import netUtil.NetworkUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;


public class ActiveClients {

    private Stage stage;
    private Login client;
    private NetworkUtil networkUtil;
    private ArrayList<String> temp = new ArrayList<>();


    @FXML
    public ListView<String> listView;

    public void backButton(ActionEvent event) throws IOException {
        //Login.activeClients = false;
        stage.close();
        client.option();
    }

    public void refreshButton(ActionEvent event) {
        networkUtil.write("R");
        Object o = networkUtil.read();
        System.out.println(o);
        if (o instanceof ArrayList) {
            temp = (ArrayList<String>) o;
            for (String s : temp) {
                if (!listView.getItems().contains(s)) {
                    listView.getItems().add(s);
                }
            }
        }
        else {
            //networkUtil.write("R");
            ArrayList<String> temp = (ArrayList<String>) networkUtil.read();
            System.out.println(temp);
            for (String s : temp) {
                if (!listView.getItems().contains(s)) {
                    listView.getItems().add(s);
                }
            }
        }
    }

    public void setListView(ArrayList<String> temp) {
        this.temp = temp;
        //for (String s : temp) {
            //listView.setItems((ObservableList<String>) temp);
//            if (!listView.getItems().contains(s)) {
//                listView.getItems().add(s);
//            }
        //}
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

    public void startChat(ActionEvent event) {
        //Login.activeClients = false;
        stage.close();
        client.directMessage(listView.getSelectionModel().getSelectedItem());
    }
}

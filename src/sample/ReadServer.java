package sample;

import netUtil.NetworkUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class ReadServer implements Runnable {
    private NetworkUtil networkUtil;

    public ReadServer(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        Thread thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                boolean loginSuccessful = false;
                boolean alreadyLoggedIn = false;
                String loginMessage = (String) networkUtil.read();
                if (loginMessage != null) {
                    String[] message = loginMessage.split("#");
                    if (message[0].equals("L")) {
                        String username = message[1].split(" ")[0];
                        String password = message[1].split(" ")[1];
                        for (NetworkUtil net : ClientInformation.information.keySet()) {
                            if (username.equals(ClientInformation.information.get(net))) {
                                networkUtil.write("exist");
                                alreadyLoggedIn = true;
                                break;
                            }
                        }
                        if (!alreadyLoggedIn) {
                            BufferedReader is = new BufferedReader(new FileReader("user.txt"));
                            String line;
                            while ((line = is.readLine()) != null) {
                                String[] userInfo = line.split(" ");
                                if (username.equals(userInfo[0]) && password.equals(userInfo[1])) {
                                    ClientInformation.information.put(networkUtil, username);
                                    loginSuccessful = true;
                                    networkUtil.write("success");
                                }
                            }
                            is.close();
                            if (!loginSuccessful) {
                                networkUtil.write("failed");
                            }
                        }
                    }

                    else if (message[0].equals("G")) {
                        String text = message[1];
                        String name = ClientInformation.information.get(networkUtil);
                        String receivedMessage = name + ": " + text;
                        for (NetworkUtil netUtil : ClientInformation.information.keySet()) {
                            if (netUtil == networkUtil) {
                                netUtil.write("You: " + text);
                            }
                            else {
                                netUtil.write(receivedMessage);
                            }
                        }
                    }
                    else if (message[0].equals("S")) {
                        BufferedWriter os = new BufferedWriter(new FileWriter("user.txt"));
                        os.write(message[1] + "\n");
                        networkUtil.write("saved");
                        os.close();
                    }
                    else if (message[0].equals("D")) {
                        String receiver = message[1];
                        String text = message[2];
                        if (ClientInformation.information.containsValue(receiver)) {
                            networkUtil.write("You: " + text);
                            for (NetworkUtil net : ClientInformation.information.keySet()) {
                                if (receiver.equals(ClientInformation.information.get(net))) {
                                    net.write(text);
                                }
                            }
                        } else {
                            networkUtil.write("[CLIENT LEFT THE CHAT]");
                        }
                    }
                    else if (message[0].equals("R")) {
                        ArrayList<String> temp = new ArrayList<>(ClientInformation.information.values());
                        networkUtil.write(temp);
                    }
                    else if (message[0].equals("B")) {
                        ClientInformation.information.remove(networkUtil);
                        break;
                    }
                    else if (message[0].equals("DB")) {
                        String receiver = message[1];
                        String text = message[2];
                        networkUtil.write("You: " + text);
//                        if (ClientInformation.information.containsValue(receiver)) {
//                            for (NetworkUtil net : ClientInformation.information.keySet()) {
//                                if (receiver.equals(ClientInformation.information.get(net))) {
//                                    net.write(text);
//                                }
//                            }
//                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
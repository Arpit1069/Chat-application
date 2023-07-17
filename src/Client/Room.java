package Client;

import Client.Controller.*;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static Client.Controller.loggedInUser;
import static Client.Controller.users;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Room extends Thread implements Initializable {
    @FXML
    public Label clientName;
    @FXML
    public Button chatBtn;
    @FXML
    public Pane chat;
    @FXML
    public TextField msgField;
    @FXML
    public TextArea msgRoom;
    @FXML
    public Label online;
    @FXML
    public Label fullName;
    @FXML
    public Label email;
    @FXML
    public Label phoneNo;
    @FXML
    public Label gender;
    @FXML
    public Pane profile;
    @FXML
    public Button profileBtn;
    @FXML
    public TextField fileChoosePath;
    @FXML
    public ImageView proImage;
    @FXML
    public Circle showProPic;
    private FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false, toggleProfile = false;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    public void connectSocket() {
        try {
            socket = new Socket("192.168.1.3", 8889);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setProfile2() {
        try {
            String url = "jdbc:mysql://localhost:3306/java"; // replace with your MySQL database URL
            String username = "root"; // replace with your MySQL username
            String password = "ashviper$26"; // replace with your MySQL password
    
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
    
            // Connect to MySQL database
            conn = DriverManager.getConnection(url, username, password);
            // Prepare SQL query
            String sql = "SELECT message FROM message";
            stmt = conn.prepareStatement(sql);
    
            // Execute query and fetch results
            rs = stmt.executeQuery();
    
            // Clear previous messages in the TextArea
            msgRoom.clear();
    
            // Loop through the result set and append messages to the TextArea
            while (rs.next()) {
                String message = rs.getString("message");
                msgRoom.appendText(message + "\n");
            }
    
            // Close database resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            String url = "jdbc:mysql://localhost:3306/java"; // replace with your MySQL database URL
            String username = "root"; // replace with your MySQL username
            String password = "ashviper$26"; // replace with your MySQL password
    
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
    
            // Connect to MySQL database
            conn = DriverManager.getConnection(url, username, password);
            // Prepare SQL query
            String sql = "SELECT * FROM message";
            stmt = conn.prepareStatement(sql);
    
            // Execute query and fetch results
            rs = stmt.executeQuery();
    
            while (rs.next()) {
                String sender = rs.getString("sender");
                String message = rs.getString("message");
                String time = rs.getString("timestamp");
                String msg = sender + ": " + message+ "("+ time +")";
                msgRoom.appendText(msg + "\n");
            }
            
            while (true) {
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println(cmd);
                StringBuilder fulmsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }
                System.out.println(fulmsg);
                if (cmd.equalsIgnoreCase(Controller.username + ":")) {
                    continue;
                } else if (fulmsg.toString().equalsIgnoreCase("bye")) {
                    break;
                }
                msgRoom.appendText(msg +"\n");
            }
            rs.close();
            stmt.close();
            conn.close();
            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleProfileBtn(ActionEvent event) {
        if (event.getSource().equals(profileBtn) && !toggleProfile) {
            new FadeIn(profile).play();
            profile.toFront();
            chat.toBack();
            toggleProfile = true;
            toggleChat = false;
            profileBtn.setText("Back");
            setProfile();
        } else if (event.getSource().equals(profileBtn) && toggleProfile) {
            new FadeIn(chat).play();
            chat.toFront();
            toggleProfile = false;
            toggleChat = false;
            profileBtn.setText("Profile");
        }
    }

    public void setProfile() {
      
                fullName.setText(Controller.username);
                fullName.setOpacity(1);
                email.setText(Controller.email);
                email.setOpacity(1);
                phoneNo.setText(Controller.phoneNo);
                gender.setText(Controller.gender);
            
        
    }

    // public void setProfile1() {
    //     String url = "jdbc:mysql://localhost:3306/java"; // replace with your MySQL database URL
    //     String username = "root"; // replace with your MySQL username
    //     String password = "ashviper$26"; // replace with your MySQL password

    //     Connection conn = null;
    //     PreparedStatement stmt = null;
    //     ResultSet rs = null;

    //     try {
    //         // Connect to MySQL database
    //         conn = DriverManager.getConnection(url, username, password);
    //         // Prepare SQL query
    //         String sql = "SELECT full_name, email, phone_number, gender FROM users WHERE name = ?";
    //         stmt = conn.prepareStatement(sql);
    //         stmt.setString(1, Controller.username);

    //         // Execute query and fetch results
    //         rs = stmt.executeQuery();

    //         if (rs.next()) {
    //             System.out.println(rs.getString("full_name"));
    //             // Set profile information in UI
    //             fullName.setText(rs.getString("full_name"));
    //             fullName.setOpacity(1);
    //             email.setText(rs.getString("email"));
    //             email.setOpacity(1);
    //             phoneNo.setText(rs.getString("phone_number"));
    //             gender.setText(rs.getString("gender"));
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     } finally {
    //         // Close database resources
    //         try {
    //             if (rs != null)
    //                 rs.close();
    //             if (stmt != null)
    //                 stmt.close();
    //             if (conn != null)
    //                 conn.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    public void handleSendEvent(MouseEvent event) {
        send();
        for (User user : users) {
            System.out.println(user.name);
        }
    }

    public void send() {
        String msg = msgField.getText();
        writer.println(Controller.username + ": " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Me: " + msg + "\n");
        msgField.setText("");
        if (msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    // Changing profile pic

    public boolean saveControl = false;

    public void chooseImageButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        fileChoosePath.setText(filePath.getPath());
        saveControl = true;
    }

    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    public void saveImage() {
        if (saveControl) {
            try {
                BufferedImage bufferedImage = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                proImage.setImage(image);
                showProPic.setFill(new ImagePattern(image));
                saveControl = false;
                fileChoosePath.setText("");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProPic.setStroke(Color.valueOf("#90a4ae"));
        Image image;
        if (Controller.gender.equalsIgnoreCase("Male")) {
            image = new Image("icons/user.png", false);
        } else {
            image = new Image("icons/female.png", false);
            proImage.setImage(image);
        }
        showProPic.setFill(new ImagePattern(image));
        clientName.setText(Controller.username);
        connectSocket();
    }
}
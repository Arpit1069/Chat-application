package Client;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import Server.ClientHandler;
import Server.Server;
import animatefx.animation.FadeIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    @FXML
    public Pane pnSignIn;
    @FXML
    public Pane pnSignUp;
    @FXML
    public Button btnSignUp;
    @FXML
    public Button getStarted;
    @FXML
    public ImageView btnBack;
    @FXML
    public TextField regName;
    @FXML
    public TextField regPass;
    @FXML
    public TextField regEmail;
    @FXML
    public TextField regFirstName;
    @FXML
    public TextField regPhoneNo;
    @FXML
    public RadioButton male;
    @FXML
    public RadioButton female;
    @FXML
    public Label controlRegLabel;
    @FXML
    public Label success;
    @FXML
    public Label goBack;
    @FXML
    public TextField userName;
    @FXML
    public TextField passWord;
    @FXML
    public Label loginNotifier;
    @FXML
    public Label nameExists;
    @FXML
    public Label checkEmail;
    public static String username, password, gender,email,phoneNo;
    public static ArrayList<User> loggedInUser = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<User>();

    // public void registration1() {
    //     if (!regName.getText().equalsIgnoreCase("")
    //             && !regPass.getText().equalsIgnoreCase("")
    //             && !regEmail.getText().equalsIgnoreCase("")
    //             && !regFirstName.getText().equalsIgnoreCase("")
    //             && !regPhoneNo.getText().equalsIgnoreCase("")
    //             && (male.isSelected() || female.isSelected())) {
    //         String phoneNumber = regPhoneNo.getText();
    //         String email = regEmail.getText();

    //         // Validate phone number: must be 10 digits
    //         if (phoneNumber.matches("\\d{10}")) {
    //             // Validate email address
    //             if (email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
    //                 try {
    //                     // Your database insert code here
    //                     Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root",
    //                             "ashviper$26");
    //                     PreparedStatement stmt = conn.prepareStatement(
    //                             "INSERT INTO users (username,password, email, full_name, phone_number, gender) VALUES (?, ?, ?, ?, ?,?)");
    //                     stmt.setString(1, regName.getText());
    //                     stmt.setString(2, regPass.getText());
    //                     stmt.setString(3, regEmail.getText());
    //                     stmt.setString(4, regFirstName.getText());
    //                     stmt.setString(5, phoneNumber);
    //                     stmt.setString(6, male.isSelected() ? "Male" : "Female");
    //                     int rows = stmt.executeUpdate();
    //                     if (rows > 0) {
    //                         goBack.setOpacity(1);
    //                         success.setOpacity(1);
    //                         makeDefault();
    //                         if (controlRegLabel.getOpacity() == 1) {
    //                             controlRegLabel.setOpacity(0);
    //                         }
    //                         if (nameExists.getOpacity() == 1) {
    //                             nameExists.setOpacity(0);
    //                         }
    //                     } else {
    //                         controlRegLabel.setOpacity(1);
    //                         setOpacity(success, goBack, nameExists, checkEmail);
    //                     }
    //                     conn.close();

    //                     // Rest of the code
    //                 } catch (SQLException e) {
    //                     // Handle SQLException
    //                     if (e.getErrorCode() == 1062) { // Duplicate entry error code
    //                         nameExists.setOpacity(1);
    //                         setOpacity(success, goBack, controlRegLabel, checkEmail);
    //                     } else {
    //                         e.printStackTrace();
    //                     }
    //                 }
    //             } else {
    //                 // Show alert for invalid email address
    //                 Alert alert = new Alert(AlertType.ERROR);
    //                 alert.setTitle("Error");
    //                 alert.setHeaderText(null);
    //                 alert.setContentText("Please enter a valid email address.");
    //                 alert.showAndWait();
    //             }
    //         } else {
    //             // Show alert for invalid phone number
    //             Alert alert = new Alert(AlertType.ERROR);
    //             alert.setTitle("Error");
    //             alert.setHeaderText(null);
    //             alert.setContentText("Please enter a 10-digit phone number.");
    //             alert.showAndWait();
    //         }
    //     } else {
    //         // Show alert for incomplete form
    //         Alert alert = new Alert(AlertType.ERROR);
    //         alert.setTitle("Error");
    //         alert.setHeaderText(null);
    //         alert.setContentText("Please fill in all the required fields.");
    //         alert.showAndWait();
    //     }
    // }

    public void registration() {
    if (!regName.getText().equalsIgnoreCase("")
            && !regPass.getText().equalsIgnoreCase("")
            && !regEmail.getText().equalsIgnoreCase("")
            && !regFirstName.getText().equalsIgnoreCase("")
            && !regPhoneNo.getText().equalsIgnoreCase("")
            && (male.isSelected() || female.isSelected())) {
        String phoneNumber = regPhoneNo.getText();
        String email = regEmail.getText();

        // Validate phone number: must be 10 digits
        if (phoneNumber.matches("\\d{10}")) {
            // Validate email address
            if (email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
                try {
                    // Your database insert code here
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root",
                            "ashviper$26");

                    // Check if username already exists
                    PreparedStatement checkStmt = conn.prepareStatement(
                            "SELECT * FROM users WHERE username = ?");
                    checkStmt.setString(1, regName.getText());
                    ResultSet checkResult = checkStmt.executeQuery();
                    if (checkResult.next()) {
                        // Username already taken, show alert
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Username already taken. Please choose a different username.");
                        alert.showAndWait();
                        return; // Return without inserting new user
                    }

                    // Insert new user
                    PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO users (username,password, email, full_name, phone_number, gender) VALUES (?, ?, ?, ?, ?,?)");
                    stmt.setString(1, regName.getText());
                    stmt.setString(2, regPass.getText());
                    stmt.setString(3, regEmail.getText());
                    stmt.setString(4, regFirstName.getText());
                    stmt.setString(5, phoneNumber);
                    stmt.setString(6, male.isSelected() ? "Male" : "Female");
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        goBack.setOpacity(1);
                        success.setOpacity(1);
                        makeDefault();
                        if (controlRegLabel.getOpacity() == 1) {
                            controlRegLabel.setOpacity(0);
                        }
                        if (nameExists.getOpacity() == 1) {
                            nameExists.setOpacity(0);
                        }
                    } else {
                        controlRegLabel.setOpacity(1);
                        setOpacity(success, goBack, nameExists, checkEmail);
                    }
                    conn.close();

                    // Rest of the code
                } catch (SQLException e) {
                    // Handle SQLException
                    e.printStackTrace();
                }
            } else {
                // Show alert for invalid email address
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid email address.");
                alert.showAndWait();
            }
        } else {
            // Show alert for invalid phone number
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a 10-digit phone number.");
            alert.showAndWait();
        }
    } else {
        // Show alert for incomplete form
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please fill in all the required fields.");
        alert.showAndWait();
    }
}

    private void setOpacity(Label a, Label b, Label c, Label d) {
        if (a.getOpacity() == 1 || b.getOpacity() == 1 || c.getOpacity() == 1 || d.getOpacity() == 1) {
            a.setOpacity(0);
            b.setOpacity(0);
            c.setOpacity(0);
            d.setOpacity(0);
        }
    }

    private void setOpacity(Label controlRegLabel, Label checkEmail, Label nameExists) {
        controlRegLabel.setOpacity(0);
        checkEmail.setOpacity(0);
        nameExists.setOpacity(0);
    }

    private boolean checkUser(String username) {
        for (User user : users) {
            if (user.name.equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEmail(String email) {
        for (User user : users) {
            if (user.email.equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    private void makeDefault() {
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
        regFirstName.setText("");
        regPhoneNo.setText("");
        male.setSelected(true);
        setOpacity(controlRegLabel, checkEmail, nameExists);
    }

    public void login() {
        username = userName.getText();
        password = passWord.getText();
        boolean login = false;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "ashviper$26");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                login = true;
                User loggedInUser = new User();
                loggedInUser.name = rs.getString("username");
                loggedInUser.password = rs.getString("password");
                loggedInUser.email = rs.getString("email");
                loggedInUser.fullName = rs.getString("full_name");
                loggedInUser.phoneNo = rs.getString("phone_number");
                loggedInUser.gender = rs.getString("gender");
                Controller.loggedInUser.add(loggedInUser);
                System.out.println(loggedInUser.name);
                gender = loggedInUser.gender;
                email = loggedInUser.email;
                phoneNo = loggedInUser.phoneNo;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (login) {
            changeWindow();
        } else {
            loginNotifier.setOpacity(1);
        }
    }

    // public void login() {
    // username = userName.getText();
    // password = passWord.getText();
    // boolean login = false;
    // try {
    // Connection conn =
    // DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root",
    // "ashviper$26");
    // PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE
    // username = ? AND password = ?");
    // stmt.setString(1, username);
    // stmt.setString(2, password);
    // ResultSet rs = stmt.executeQuery();
    // if (rs.next()) {
    // login = true;
    // User loggedInUser = new User(rs.getString("username"),
    // rs.getString("password"), rs.getString("email"), rs.getString("full_name"),
    // rs.getString("phone_number"), rs.getString("gender"));
    // this.loggedInUser.add(loggedInUser);
    // System.out.println(loggedInUser.name);
    // gender = loggedInUser.gender;
    // }
    // conn.close();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // if (login) {
    // changeWindow();
    // } else {
    // loginNotifier.setOpacity(1);
    // }
    // }

    // public void login() {
    // username = userName.getText();
    // password = passWord.getText();
    // boolean login = false;
    // for (User x : users) {
    // if (x.name.equalsIgnoreCase(username) &&
    // x.password.equalsIgnoreCase(password)) {
    // login = true;
    // loggedInUser.add(x);
    // System.out.println(x.name);
    // gender = x.gender;
    // break;
    // }
    // }
    // if (login) {
    // changeWindow();
    // } else {
    // loginNotifier.setOpacity(1);
    // }
    // }
    @FXML

    public void changeWindow() {
        try {
            Stage stage = (Stage) userName.getScene().getWindow();
            Parent root = FXMLLoader.load(this.getClass().getResource("Room.fxml"));
            stage.setScene(new Scene(root, 330, 560));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(btnSignUp)) {
            new FadeIn(pnSignUp).play();
            pnSignUp.toFront();
        }
        if (event.getSource().equals(getStarted)) {
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }
        loginNotifier.setOpacity(0);
        userName.setText("");
        passWord.setText("");
    }
    
    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource() == btnBack) {
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
    }
}


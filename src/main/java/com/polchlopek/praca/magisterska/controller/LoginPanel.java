package com.polchlopek.praca.magisterska.controller;

import com.polchlopek.praca.magisterska.DAO.LoginDAO;
import com.polchlopek.praca.magisterska.DAO.PersonDAO;
import com.polchlopek.praca.magisterska.DTO.LoggedInUser;
import com.polchlopek.praca.magisterska.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

public class LoginPanel {

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label checkCorrectPassLabel;

    @FXML
    public void checkLogin(ActionEvent event) throws IOException {

        Parent mainWindow = FXMLLoader.load(getClass().getResource("/view/mainWindow.fxml"));
        Scene mainWindowScene = new Scene(mainWindow);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainWindowScene);
        window.show();

        String login = loginTextField.getText().trim();
        PersonDAO personDAO = new PersonDAO();
        User user = personDAO.getPerson("damian");
        LoggedInUser.getInstance().setLoggedInUSer(user);

//        PersonDAO personDAO = new PersonDAO();
//        String login = loginTextField.getText().trim();
//        String password = passwordField.getText().trim();
//        User user = personDAO.getPerson(login);
//
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String passwordFromDb = user.getPassword().substring(8, user.getPassword().length());
//
//        if(user != null &&
//                passwordEncoder.matches(password, passwordFromDb )) {
//
//            LoginDAO log = new LoginDAO();
//            log.addLogin(user);
//            LoggedInUser.getInstance().setLoggedInUSer(user);
//
//            Parent mainWindow = FXMLLoader.load(getClass().getResource("/view/mainWindow.fxml"));
//            Scene mainWindowScene = new Scene(mainWindow);
//            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
//            window.setScene(mainWindowScene);
//            window.show();
//        }
//        else{
//            checkCorrectPassLabel.setText("Incorrect pass or login !!!");
//        }


    }


}

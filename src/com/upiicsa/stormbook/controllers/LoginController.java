package com.upiicsa.stormbook.controllers;

import java.io.IOException;
import java.sql.SQLException;

import com.upiicsa.stormbook.DatabaseConnection;
import com.upiicsa.stormbook.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class LoginController {
	@FXML
	private Stage loginWindow;
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordPasswordField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private CheckBox showPasswordCheckBox;
	
	@FXML
	private void onTypeHiddenPassword() {
		passwordTextField.setText(passwordPasswordField.getText());
	}
	
	@FXML
	private void onTypeShownPassword() {
		passwordPasswordField.setText(passwordTextField.getText());
	}
	
	@FXML
	private void onTogglePasswordVisibility() {	
		if (showPasswordCheckBox.isSelected()) {
			passwordTextField.setVisible(true);
			passwordPasswordField.setVisible(false);
		} else {
			passwordPasswordField.setVisible(true);
			passwordTextField.setVisible(false);
		}
	}
	
	@FXML
	private void onLogin() {
		try {
			var connection = new DatabaseConnection();
			String query = "SELECT username FROM user WHERE username = ? AND password = ?;";
			
			String username = usernameTextField.getText();
			String password = passwordPasswordField.getText();
			
			if (username.isEmpty() || password.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				
				error.setHeaderText("Campos no requisitados o incompletos");
				error.setContentText("No puede dejar campos vacíos.");
				error.showAndWait();
				
				return;
			}
			
			var resultSet = connection.executeQuery(query, username, password);
			
			if (resultSet.next()) {
				User user = new User();
				user.setUsername(resultSet.getString(1));
				
				connection.disconnect();
				
				loginWindow.close();
				
				FXMLLoader root = new FXMLLoader(getClass().getResource("/views/MainWindow.fxml"));
				Stage mainWindow = root.load();
				
				mainWindow.show();
			} else {
				Alert error = new Alert(AlertType.ERROR);
				
				error.setHeaderText("Error al intentar iniciar sesión");
				error.setContentText("Es posible que el usuario o la contraseña sean incorrectos.");
				error.showAndWait();
			}
		}  catch (SQLException e) {
			Alert error = new Alert(AlertType.ERROR);
			
			error.setHeaderText("Error al intentar iniciar sesión");
			error.setContentText("Es posible que el usuario o la contraseña sean incorrectos.");
			error.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

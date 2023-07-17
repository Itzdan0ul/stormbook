package com.upiicsa.stormbook.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.upiicsa.stormbook.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class SignUpController {
	@FXML
	private Stage signUpWindow;
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
	private void onCreateUser() {
		try {
			var connection = new DatabaseConnection();
			String query = "INSERT INTO user (username, password) VALUES (?, ?);";
			
			String username = usernameTextField.getText();
			String password = passwordPasswordField.getText();
			
			if (username.isEmpty() || password.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				
				error.setHeaderText("Campos no requisitados o incompletos");
				error.setContentText("No puede dejar campos vacíos.");
				error.showAndWait();
				
				return;
			}
			
			Pattern pattern = Pattern.compile(".{8,}");
	        Matcher isPasswordCorrectly = pattern.matcher(password);
			
			if (!isPasswordCorrectly.matches()) {
				Alert error = new Alert(AlertType.ERROR);
				
				error.setHeaderText("Longitud de la contraseña inadecuado");
				error.setContentText("La contraseña debe estar conformada por al menos ocho caracteres.");
				error.showAndWait();
				
				return;
			}
			
			connection.executeNonQuery(query, username, password);
			
			Alert success = new Alert(AlertType.INFORMATION);
			success.setHeaderText("¡Usuario creado con éxito!");
			success.setContentText("Tu cuenta de usuario ha sido creada correctamente");
			success.showAndWait();
			
			connection.disconnect();
			
			signUpWindow.close();
			
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/LoginWindow.fxml"));
			Stage loginUpWindow = root.load();
			
			loginUpWindow.show();
		}  catch (SQLException e) {
			Alert error = new Alert(AlertType.ERROR);
			
			error.setHeaderText("Error al intentar crear el usuario.");
			error.setContentText("Es posible que el usuario o la contraseña sean incorrectos.");
			error.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

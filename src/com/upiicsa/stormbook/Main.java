package com.upiicsa.stormbook;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			var connection = new DatabaseConnection();
			String query = "SELECT id FROM user;";
			
			var resultSet = connection.executeQuery(query);
			
			if (resultSet.next()) {
				FXMLLoader root = new FXMLLoader(getClass().getResource("/views/LoginWindow.fxml"));
				Stage loginWindow = root.load();
				
				loginWindow.show();
			} else {
				FXMLLoader root = new FXMLLoader(getClass().getResource("/views/SignUpWindow.fxml"));
				Stage signUpWindow = root.load();
				
				signUpWindow.show();
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

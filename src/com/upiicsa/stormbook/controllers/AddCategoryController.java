package com.upiicsa.stormbook.controllers;

import java.sql.SQLException;
import com.upiicsa.stormbook.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class AddCategoryController {
	@FXML
	private Stage addCategoryWindow;
	@FXML
	private TextField categoryNameTextField;
	
	@FXML
	private void onCancel() {
		addCategoryWindow.close();
	}
	
	@FXML
	private void onAddCategory() {
		try {
			var connection = new DatabaseConnection();
			String query = "INSERT INTO category (`name`) VALUES (?);";
			
			String name = categoryNameTextField.getText();
			
			if (name.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error al intentar agregar la categoría");
				error.setContentText("No puedes dejar el campo del nombre de la categoría vacío.");
				error.showAndWait();
				
				return;
			}
			
			connection.executeNonQuery(query, name);
			
			Alert success = new Alert(AlertType.INFORMATION);
			success.setHeaderText("Categoría agregada");
			success.setContentText("La categoría " + name + " se agregó correctamente.");
			success.showAndWait();
			
			categoryNameTextField.setText("");
			
			connection.disconnect();
		} catch (SQLException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setHeaderText("Error al agregar la categoría");
			error.setContentText("¡Vaya! Parece que ya hay una categoría con ese nombre. Probablemete sea su gemela malvada.");
			error.showAndWait();
		}
	}
}

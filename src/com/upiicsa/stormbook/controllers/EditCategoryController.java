package com.upiicsa.stormbook.controllers;

import java.sql.SQLException;
import java.util.Date;
import com.upiicsa.stormbook.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class EditCategoryController {
	@FXML
	private Stage editCategoryWindow;
	@FXML
	private TextField categoryNameTextField;
	public static String selectedCategory;
	
	@FXML
	private void onCancel() {
		editCategoryWindow.close();
	}
	
	@FXML
	private void onShowing() {
		String oldName = selectedCategory;
		categoryNameTextField.setText(oldName);
	}
	
	@FXML
	private void onEditCategory() {
		try {
			var connection = new DatabaseConnection();
			String query = "UPDATE category SET name = ?, updated_at = ? WHERE name = ?;";
			
			String oldName = selectedCategory;
			String name = categoryNameTextField.getText();
			
			if (name.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setHeaderText("Error al intentar agregar la categoría");
				error.setContentText("No puedes dejar el campo del nombre de la categoría vacío.");
				error.showAndWait();
				
				return;
			}
			
			connection.executeNonQuery(query, name, new Date(), oldName);
			
			Alert success = new Alert(AlertType.INFORMATION);
			success.setHeaderText("Categoría actualizada");
			success.setContentText("La categoría " + name + " se actualizó correctamente correctamente.");
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

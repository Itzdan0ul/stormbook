package com.upiicsa.stormbook.controllers;

import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.upiicsa.stormbook.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
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
public class EditBookController {
	@FXML
	private Stage editBookWindow;
	@FXML
	private TextField isbnTextField;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField authorTextField;
	@FXML
	private TextField publisherTextField;
	@FXML
	private TextField pagesTextField;
	@FXML
	private ChoiceBox<String> categoryNameChoiceBox;
	
	public static String isbn;
	public static String name;
	public static String author;
	public static String publisher;
	public static int pages;
	public static String categoryName;
	
	@FXML
	private void onShowing() {
		try {
	        var connection = new DatabaseConnection();
	        String query = "SELECT name FROM category;";
	        
	        var resultSet = connection.executeQuery(query);
	        
	        
	        if (resultSet.next()) {
	            ObservableList<String> categories = FXCollections.observableArrayList();
	            
	            do {
	                categories.add(resultSet.getString(1));
	            } while (resultSet.next());
	            
	            categoryNameChoiceBox.setItems(categories);
	        }

	        
	        connection.disconnect();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (java.lang.RuntimeException e) { }
		
		isbnTextField.setText(isbn);
		nameTextField.setText(name);
		authorTextField.setText(author);
		publisherTextField.setText(publisher);
		pagesTextField.setText("" + pages);
		categoryNameChoiceBox.getSelectionModel().select(categoryName);
	}
	
	@FXML
	private void onCancel() {
		editBookWindow.close();
	}
	
	@FXML
	private void onEdit() throws NumberFormatException, SQLException {
		Alert errorAlert = new Alert(AlertType.ERROR);
		Alert successAlert = new Alert(AlertType.INFORMATION);
		
		var connection = new DatabaseConnection();
		String query = "UPDATE book SET isbn = ?, name = ?, author = ?, publisher = ?, pages = ?, category_name = ?, updated_at = ? WHERE isbn = ?;";
		
		String oldIsbn = isbn;
		String isbn = isbnTextField.getText();
		String name = nameTextField.getText();
		String author = authorTextField.getText();
		String publisher = publisherTextField.getText();
		String pages = pagesTextField.getText();
		String categoryName = categoryNameChoiceBox.getValue();
		
		String verifyIsbnRegex = "^\\d{13}$";
		String verifyPagesRegex = "\\d+$";
        
        Pattern patternIsbn = Pattern.compile(verifyIsbnRegex);
        Matcher isIsbnCorrectly = patternIsbn.matcher(isbn);
		
		if (isbn.isEmpty() || name.isEmpty() || author.isEmpty() 
				|| publisher.isEmpty() || pagesTextField.getText().isEmpty()
				|| categoryName.isEmpty()
			) {
			
			errorAlert.setTitle("??? ok ???");
			errorAlert.setHeaderText("Error al requisitar los campos.");
			errorAlert.setContentText("Si sabes que los campos se rellenan, ¿verdad?");

			errorAlert.showAndWait();
			
			return;
		}
		
		if (!isIsbnCorrectly.matches()) {
			errorAlert.setTitle("??? ok ???");
			errorAlert.setHeaderText("Error en el ISBN");
			errorAlert.setContentText("EL ISBN debe estar compuesto de 13 numeros.");

			errorAlert.showAndWait();
			
			return;
		}
		
		Pattern patternpages = Pattern.compile(verifyPagesRegex);
        Matcher isPagesCorrectly = patternpages.matcher(pages);
		
		if (!isPagesCorrectly.matches()) {
			errorAlert.setTitle("Error al insertar el número de páginas.");
			errorAlert.setHeaderText("Error en el numero de paginas.");
			errorAlert.setContentText("El número de paginas debe estar compuesto por numeros.");

			errorAlert.showAndWait();
			
			return;
		}
		
		connection.executeNonQuery(query,
				isbn,
				name,
				author,
				publisher,
				Integer.parseInt(pages),
				categoryName,
				new Date(),
				oldIsbn);
		
        successAlert.setTitle("¡Libro se ha actualizado correctamente!");
        successAlert.setHeaderText("Se ha actualizado el libro.");
        successAlert.setContentText("El libro " + name +  " se ha actualizado correctamente.");
        successAlert.showAndWait();
        
        editBookWindow.close();
        
        connection.disconnect();
	}
}

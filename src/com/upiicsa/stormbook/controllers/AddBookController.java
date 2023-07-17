package com.upiicsa.stormbook.controllers;

import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.upiicsa.stormbook.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class AddBookController {
	@FXML
	private Stage addBookWindow;
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
	
	@FXML
	private void onSelectCategory() {
	    try {
	        var connection = new DatabaseConnection();
	        String query = "SELECT name FROM category;";
	        
	        var resultSet = connection.executeQuery(query);
	        
	        if (!resultSet.next()) {
	            Alert error = new Alert(AlertType.WARNING);
	            error.setHeaderText("No se encontraron categorías");
	            error.setContentText("¡Vaya...! Parece que no tienes categorias agregadas.");
	            
	            Optional<ButtonType> answer = error.showAndWait();
	            
	            if (answer.get() == ButtonType.OK) {
	                addBookWindow.close();
	            }
	            
	            return;
	        }
	        
	        if (categoryNameChoiceBox.getItems().isEmpty()) {
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
	}
	
	@FXML
	private void onCancel() {
		addBookWindow.close();
	}
	
	@FXML
	private void onAdd() throws SQLException {
		Alert errorAlert = new Alert(AlertType.ERROR);
		Alert successAlert = new Alert(AlertType.INFORMATION);
		
		var connection = new DatabaseConnection();
		String query = "INSERT INTO book (isbn, name, author, publisher, pages, category_name) VALUES (?, ?, ?, ?, ?, ?);";
		
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
				categoryName);
		
        successAlert.setTitle("¡Libro agregado correctamente!");
        successAlert.setHeaderText("Se ha agregado el libro.");
        successAlert.setContentText("El libro " + name +  " se ha agregado correctamente.");
        successAlert.showAndWait();
        
        isbnTextField.setText("");
        nameTextField.setText("");
        authorTextField.setText("");
        publisherTextField.setText("");
        pagesTextField.setText("");
        
        connection.disconnect();
	}
}

package com.upiicsa.stormbook.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import com.upiicsa.stormbook.models.Book;
import com.upiicsa.stormbook.models.Category;
import com.upiicsa.stormbook.models.Loan;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;
import com.upiicsa.stormbook.DatabaseConnection;
import com.upiicsa.stormbook.App;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class MainController extends App {
	@FXML
	private Stage mainWindow;
	@FXML
	private TableView<Book> booksTableView;
	@FXML
	private TableView<Category> categoryTableView;
	@FXML
	private TableView<Loan> loansTableView;
	@FXML
	private TextField searchBookTextField;
	@FXML
	private TextField searchLoanTextField;
	@FXML
	private TabPane layoutTabPane;
	@FXML
	private MenuItem newBookMenuItem;
	@FXML
	private MenuItem editBookMenuItem;
	@FXML
	private MenuItem removeBookMenuItem;
	@FXML
	private MenuItem newCategoryMenuItem;
	@FXML
	private MenuItem editCategoryMenuItem;
	@FXML
	private MenuItem removeCategoryMenuItem;
	@FXML
	private MenuItem newLoanMenuItem;
	private Book selectedBook = new Book();
	private Category selectedCategory = new Category();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			onLoadBooks();
			onLoadCategories();
			onLoadLoans();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void onLoadBooks() throws SQLException {
		var connection = new DatabaseConnection();
	    String query = "SELECT * FROM book;";
	    
    	var resultSet = connection.executeQuery(query);
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        
        while (resultSet.next()) {
            Book book = new Book();
            
            book.setIsbn(resultSet.getString(1));
            book.setName(resultSet.getString(2));
            book.setAuthor(resultSet.getString(3));
            book.setPublisher(resultSet.getString(4));
            book.setPages(resultSet.getInt(5));
            book.setCategoryName(resultSet.getString(6));
            book.setCreatedAt(resultSet.getDate(7));
            book.setUpdatedAt(resultSet.getDate(8));
            
            bookList.add(book);
        }
        
		booksTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("isbn"));
		booksTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
		booksTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
		booksTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("publisher"));
		booksTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("pages"));
		booksTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("categoryName"));
		booksTableView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("createdAt"));
		booksTableView.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
	
		
		booksTableView.setItems(bookList);
	}
	
	private void onLoadCategories() {
		try {
			var connection = new DatabaseConnection();
		    String query = "SELECT category.`name`, COUNT(book.category_name) AS books, category.created_at, category.updated_at FROM category LEFT OUTER JOIN book ON category.`name` = category_name GROUP BY category.`name`;";
		    
	    	var resultSet = connection.executeQuery(query);
	        ObservableList<Category> categoryList = FXCollections.observableArrayList();
	        
	        while (resultSet.next()) {
	            Category category = new Category();
	            
	            category.setName(resultSet.getString(1));
	            category.setBooks(resultSet.getInt(2));
	            category.setCreatedAt(resultSet.getDate(3));
	            category.setUpdatedAt(resultSet.getDate(4));
	            
	            categoryList.add(category);
	        }
	        
	        categoryTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
	        categoryTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("books"));
	        categoryTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("createdAt"));
	        categoryTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

			categoryTableView.setItems(categoryList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void onLoadLoans() {
		try {
			var connection = new DatabaseConnection();
		    String query = "SELECT folio, return_date, state, student_enrollment, `student`.`name`, grade, `group`, book_isbn, loan.created_at, loan.updated_at FROM loan JOIN student ON student.enrollment = loan.student_enrollment;";
		    
	    	var resultSet = connection.executeQuery(query);
	        ObservableList<Loan> loans = FXCollections.observableArrayList();
	        
	        
	        while (resultSet.next()) {
	            Loan loan = new Loan();
	            
	            loan.setFolio(resultSet.getString(1));
	            loan.setReturnDate(resultSet.getDate(2));
	            loan.setState(resultSet.getString(3));
	            loan.setStudentEnrollment(resultSet.getString(4));
	            loan.setStudentName(resultSet.getString(5));
	            loan.setStudentGrade(resultSet.getString(6));
	            loan.setStudentGroup(resultSet.getString(7));
	            loan.setBookIsbn(resultSet.getString(8));
	            loan.setCreatedAt(resultSet.getDate(9));
	            loan.setUpdatedAt(resultSet.getDate(10));

	            loans.add(loan);
	        }
	        
	        loansTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("folio"));
	        loansTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("returnDate"));
	        loansTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("state"));
	        loansTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("studentEnrollment"));
	        loansTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("studentName"));
	        loansTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("studentGrade"));
	        loansTableView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("studentGroup"));
	        loansTableView.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("bookIsbn"));
	        loansTableView.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("createdAt"));
	        loansTableView.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

	        loansTableView.setItems(loans);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onChangeTab() {
		if (layoutTabPane.getTabs().get(0).isSelected()) {
			newBookMenuItem.setVisible(true);
			editBookMenuItem.setVisible(true);
			removeBookMenuItem.setVisible(true);
			
			newCategoryMenuItem.setVisible(false);
			editCategoryMenuItem.setVisible(false);
			removeCategoryMenuItem.setVisible(false);
			
			newLoanMenuItem.setVisible(false);
			
			return;
		}
		
		if (layoutTabPane.getTabs().get(1).isSelected()) {
			newCategoryMenuItem.setVisible(true);
			editCategoryMenuItem.setVisible(true);
			removeCategoryMenuItem.setVisible(true);
			
			newBookMenuItem.setVisible(false);
			editBookMenuItem.setVisible(false);
			removeBookMenuItem.setVisible(false);
			
			newLoanMenuItem.setVisible(false);
			
			return;
		} 
		
		if (layoutTabPane.getTabs().get(2).isSelected()) {
			newLoanMenuItem.setVisible(true);
			
			newBookMenuItem.setVisible(false);
			editBookMenuItem.setVisible(false);
			removeBookMenuItem.setVisible(false);
			
			newCategoryMenuItem.setVisible(false);
			editCategoryMenuItem.setVisible(false);
			removeCategoryMenuItem.setVisible(false);
			
			return;
		}
	}
	
	@FXML
	private void onSearchBook() {
		try {
	    	var connection = new DatabaseConnection();
		    String query = "SELECT * FROM book WHERE name LIKE ? OR isbn LIKE ?;";
		    
	    	var resultSet = connection.executeQuery(query, searchBookTextField.getText() + '%', searchBookTextField.getText() + '%');
	        ObservableList<Book> bookList = FXCollections.observableArrayList();
	        
	        while (resultSet.next()) {
	            Book book = new Book();
	            
	            book.setIsbn(resultSet.getString(1));
	            book.setName(resultSet.getString(2));
	            book.setAuthor(resultSet.getString(3));
	            book.setPublisher(resultSet.getString(4));
	            book.setPages(resultSet.getInt(5));
	            book.setCategoryName(resultSet.getString(6));
	            book.setCreatedAt(resultSet.getDate(7));
	            book.setUpdatedAt(resultSet.getDate(8));
	            
	            bookList.add(book);
	        }
	        
	        connection.disconnect();
	        
	      	booksTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("isbn"));
	      	booksTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
	      	booksTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
	      	booksTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("publisher"));
	      	booksTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("pages"));
	      	booksTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("categoryName"));
	      	booksTableView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("createdAt"));
	      	booksTableView.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
	      	
	      	
	        booksTableView.setItems(bookList);
	    } catch (SQLException e) {
	    	System.err.print(e.getMessage());
	    }
	}
	
	@FXML
	private void onAddNewBook(ActionEvent e) throws IOException {
		FXMLLoader root = new FXMLLoader(getClass().getResource("/views/AddBookWindow.fxml"));
		Stage addBookWindow = root.load();
		
		addBookWindow.initModality(Modality.WINDOW_MODAL);
		addBookWindow.initOwner(mainWindow);
		addBookWindow.showAndWait();
	}
	
	@FXML
	private void onSelectTableBook() {
		try {
			String isbn = booksTableView.getSelectionModel().getSelectedItem().getIsbn();
			String name = booksTableView.getSelectionModel().getSelectedItem().getName();
			String author = booksTableView.getSelectionModel().getSelectedItem().getAuthor();
			String publisher = booksTableView.getSelectionModel().getSelectedItem().getPublisher();
			int pages = booksTableView.getSelectionModel().getSelectedItem().getPages();
			String categoryName = booksTableView.getSelectionModel().getSelectedItem().getCategoryName();
			
			selectedBook.setIsbn(isbn);
			selectedBook.setName(name);
			selectedBook.setAuthor(author);
			selectedBook.setPublisher(publisher);
			selectedBook.setPages(pages);
			selectedBook.setCategoryName(categoryName);
		} catch (java.lang.RuntimeException e) { }
	}
	
	@FXML
	private void onEditBook() {
		if (selectedBook.getIsbn() == null || selectedBook.getIsbn().equals("")) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Editar libro");
			error.setHeaderText("Error al intentar editar un libro.");
			error.setContentText("Primero tienes que seleccionar la fila del libro que deseas actualizar.");
			error.showAndWait();
			
			return;
		}
		
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/EditBookWindow.fxml"));
			Stage editCategoryWindow = root.load();
			
			EditBookController.isbn = selectedBook.getIsbn();
			EditBookController.name = selectedBook.getName();
			EditBookController.author = selectedBook.getAuthor();
			EditBookController.publisher = selectedBook.getPublisher();
			EditBookController.pages = selectedBook.getPages();
			EditBookController.categoryName = selectedBook.getCategoryName();
			
			editCategoryWindow.initModality(Modality.WINDOW_MODAL);
			editCategoryWindow.initOwner(mainWindow);
			editCategoryWindow.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onRemoveBook() throws SQLException {
		if (selectedBook.getIsbn() == null || selectedBook.getIsbn().equals("")) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Borrar libro");
			error.setHeaderText("Error al intentar borrar un libro.");
			error.setContentText("Primero tienes que seleccionar la fila del libro que deseas borrar.");
			error.showAndWait();
			
			return;
		}
		
		Alert question = new Alert(AlertType.CONFIRMATION);
		question.setTitle("¿Borrar libro?");
		question.setHeaderText("¿Borrar libro?");
		question.setContentText("¿Estás seguro de que quieres realizar esta acción? No podrás revertirla.");
		
		Optional<ButtonType> answer = question.showAndWait();
		
		if (answer.get() == ButtonType.OK) {
			var connection = new DatabaseConnection();
			String query = "DELETE FROM book WHERE isbn = ?;";
			
			connection.executeNonQuery(query, selectedBook.getIsbn());
			
			Alert success = new Alert(AlertType.INFORMATION);
			success.setTitle("¡Libro eliminado correctamente!");
			success.setHeaderText("Libro eliminado");
	        success.setContentText("El libro se ha eliminado correctamente.");
	        success.showAndWait();
	        
	        connection.disconnect();
	        
	        onLoadBooks();
		} else if (answer.get() == ButtonType.CANCEL) {
			return;
		}
	}
	
	@FXML
	private void onAddNewCategory() {
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/AddCategoryWindow.fxml"));
			Stage addCategoryWindow = root.load();
			
			addCategoryWindow.initModality(Modality.WINDOW_MODAL);
			addCategoryWindow.initOwner(mainWindow);
			addCategoryWindow.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onSelectTableCategory() {
		try {
			String name = categoryTableView.getSelectionModel().getSelectedItem().getName();
			int books = categoryTableView.getSelectionModel().getSelectedItem().getBooks();
			
			selectedCategory.setName(name);
			selectedCategory.setBooks(books);
		} catch (java.lang.RuntimeException e) { }
	}
	
	@FXML
	private void onEditCategory() {
		if (selectedCategory.getName() == null || selectedCategory.getName().equals("")) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Editar categoría");
			error.setHeaderText("Error al intentar editar una categoría.");
			error.setContentText("Primero tienes que seleccionar la fila de la categoría que deseas actualizar.");
			error.showAndWait();
			
			return;
		}
		
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/EditCategoryWindow.fxml"));
			Stage editCategoryWindow = root.load();
			
			EditCategoryController.selectedCategory = selectedCategory.getName();
			
			editCategoryWindow.initModality(Modality.WINDOW_MODAL);
			editCategoryWindow.initOwner(mainWindow);
			editCategoryWindow.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onRemoveCategory() throws SQLException {
		if (selectedCategory.getName() == null || selectedCategory.getName().equals("")) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Borrar categoría");
			error.setHeaderText("Error al intentar borrar una categoría.");
			error.setContentText("Primero tienes que seleccionar la fila de la categoría que deseas borrar.");
			error.showAndWait();
			
			return;
		}
		
		Alert question = new Alert(AlertType.CONFIRMATION);
		question.setTitle("¿Borrar categoría?");
		question.setHeaderText("¿Borrar categoría?");
		question.setContentText("¿Estás seguro de que quieres realizar esta acción? No podrás revertirla.");
		
		Optional<ButtonType> answer = question.showAndWait();
		
		if (answer.get() == ButtonType.OK) {
			var connection = new DatabaseConnection();
			String query = "DELETE FROM category WHERE name = ?;";
			
			connection.executeNonQuery(query, selectedCategory.getName());
			
			Alert success = new Alert(AlertType.INFORMATION);
			success.setTitle("¡Categoría eliminado correctamente!");
			success.setHeaderText("Categoría eliminado");
	        success.setContentText("La categoría se ha eliminado correctamente.");
	        success.showAndWait();
	        
	        connection.disconnect();
	        
	        onLoadCategories();
		} else if (answer.get() == ButtonType.CANCEL) {
			return;
		}
	}
	
	@FXML
	private void onNewLoan() {
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/views/AddLoanWindow.fxml"));
			Stage addLoanWindow = root.load();
			
			addLoanWindow.initModality(Modality.WINDOW_MODAL);
			addLoanWindow.initOwner(mainWindow);
			addLoanWindow.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onSearchLoan() {
		try {
			var connection = new DatabaseConnection();
		    String query = "SELECT folio, return_date, state, student_enrollment, `student`.`name`, grade, `group`, book_isbn, loan.created_at, loan.updated_at FROM loan JOIN student ON student.enrollment = loan.student_enrollment WHERE folio LIKE ?;";
		    
		    String folio = searchLoanTextField.getText();
		    
	    	var resultSet = connection.executeQuery(query, (folio + '%'));
	        ObservableList<Loan> loans = FXCollections.observableArrayList();
	        
	        
	        while (resultSet.next()) {
	            Loan loan = new Loan();
	            
	            loan.setFolio(resultSet.getString(1));
	            loan.setReturnDate(resultSet.getDate(2));
	            loan.setState(resultSet.getString(3));
	            loan.setStudentEnrollment(resultSet.getString(4));
	            loan.setStudentName(resultSet.getString(5));
	            loan.setStudentGrade(resultSet.getString(6));
	            loan.setStudentGroup(resultSet.getString(7));
	            loan.setBookIsbn(resultSet.getString(8));
	            loan.setCreatedAt(resultSet.getDate(9));
	            loan.setUpdatedAt(resultSet.getDate(10));

	            loans.add(loan);
	        }
	        
	        loansTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("folio"));
	        loansTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("returnDate"));
	        loansTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("state"));
	        loansTableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("studentEnrollment"));
	        loansTableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("studentName"));
	        loansTableView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("studentGrade"));
	        loansTableView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("studentGroup"));
	        loansTableView.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("bookIsbn"));
	        loansTableView.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("createdAt"));
	        loansTableView.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

	        loansTableView.setItems(loans);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onRefresh() {
		try {
			onLoadBooks();
			onLoadCategories();
			onLoadLoans();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onExit() {
		System.exit(0);
	}
	
	@FXML
	private void onGetHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Stormbook");
        alert.setContentText("Esta aplicación es un proyecto escolar de la materia de Programación Orientada a Objetos, "
        		+ "de alumnos de la carrera de Ingeniería en Informática, "
        		+ "de la Unidad Profesional Interdisciplinaria de Ingeniería y Ciencias Sociales y Administrativas, "
						+ "del Instituto Politécnico Nacional."
						+ "\n\n" + "La licencia de este proyecto es MIT.");

        alert.showAndWait();
	}
}

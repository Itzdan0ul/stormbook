package com.upiicsa.stormbook.controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.upiicsa.stormbook.DatabaseConnection;
import com.upiicsa.stormbook.FolioBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class AddLoanController {
	@FXML
	private Stage addLoanWindow;
	@FXML
	private TextField enrollmentTextField;
	@FXML
	private TextField studentNameTextField;
	@FXML
	private ComboBox<String> gradesComboBox;
	@FXML
	private ComboBox<String> groupsComboBox;
	@FXML
	private ComboBox<String> booksComboBox;
	@FXML
	private TextField bookNameTextField;
	@FXML
	private DatePicker returnDatePicker;

	@FXML
	private void onShowing() {
		ObservableList<String> grades = FXCollections.observableArrayList("1°", "2°", "3°");
		ObservableList<String> groups = FXCollections.observableArrayList("A", "B", "C", "D", "E");

		gradesComboBox.setItems(grades);
		groupsComboBox.setItems(groups);

		try {
			var connection = new DatabaseConnection();
			String query = "SELECT isbn FROM book;";

			var resultSet = connection.executeQuery(query);

			if (!resultSet.next()) {
				Alert error = new Alert(AlertType.WARNING);
				error.setHeaderText("No se encontraron categorías");
				error.setContentText("¡Vaya...! Parece que no tienes categorias agregadas.");

				Optional<ButtonType> answer = error.showAndWait();

				if (answer.get() == ButtonType.OK) {
					addLoanWindow.close();
				}

				return;
			}

			if (booksComboBox.getItems().isEmpty()) {
				ObservableList<String> categories = FXCollections.observableArrayList();

				do {
					categories.add(resultSet.getString(1));
				} while (resultSet.next());

				booksComboBox.setItems(categories);
			}

			connection.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (java.lang.RuntimeException e) {
		}
	}

	@FXML
	private void onChangeData() {
		try {
			var connection = new DatabaseConnection();
			String query = "SELECT `name` FROM book WHERE isbn = ?;";

			var resultSet = connection.executeQuery(query, booksComboBox.getValue());

			while (resultSet.next()) {
				bookNameTextField.setText(resultSet.getString(1));
			}

			connection.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (java.lang.RuntimeException e) {
		}
	}

	@FXML
	private void onGenerate() {
		try {
			var connection = new DatabaseConnection();
			String query = "INSERT INTO student (`enrollment`, `name`, `grade`, `group`) VALUES (?, ?, ?, ?);";
			String secondQuery = "INSERT INTO loan (`folio`, `return_date`, `student_enrollment`, `book_isbn`) VALUES (?, ?, ?, ?);";

			String enrollment = enrollmentTextField.getText();
			String studentName = studentNameTextField.getText();
			String grade = gradesComboBox.getValue();
			String group = groupsComboBox.getValue();

			String folio = FolioBuilder.makeFolio();
			LocalDate returnDate = returnDatePicker.getValue();
			String isbn = booksComboBox.getValue();

			if (enrollment.isEmpty() || studentName.isEmpty() || grade.isEmpty() || group.isEmpty()
					|| returnDate == null || isbn.isEmpty()) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("??? ok ???");
				error.setHeaderText("Error al requisitar los campos.");
				error.setContentText("Si sabes que los campos se rellenan, ¿verdad?");

				error.showAndWait();
				return;
			}

			String verifyEnrollmentRegex = "^\\d{10}$";

			Pattern pattern = Pattern.compile(verifyEnrollmentRegex);
			Matcher isEnrrollmentCorrectly = pattern.matcher(enrollment);

			if (!isEnrrollmentCorrectly.matches()) {
				Alert error = new Alert(AlertType.ERROR);

				error.setTitle("??? ok ???");
				error.setHeaderText("Error en la matrícula del estudiante.");
				error.setContentText("La matrícula debe estar compuesta por 10 numeros.");

				error.showAndWait();

				return;
			}

			LocalDate today = LocalDate.now();

			if (returnDate.isBefore(today)) {
				Alert error = new Alert(AlertType.ERROR);

				error.setTitle("??? ok ???");
				error.setHeaderText("Error en la fecha de devolució del libro.");
				error.setContentText("La fecha no puede ser a menor hoy. Aún no se puede viajar al pasado.");

				error.showAndWait();

				return;
			}

			connection.executeNonQuery(query, enrollment, studentName, grade, group);
			connection.executeNonQuery(secondQuery, folio, returnDate, enrollment, isbn);
			
			Alert success = new Alert(AlertType.INFORMATION);

			success.setTitle("¡Préstamo generado!");
			success.setHeaderText("Préstamo generado correctamente.");
			success.setContentText("El préstamo del libro " + bookNameTextField.getText() + " para " + studentName + " se ha generado correctamente. La fecha prevista para la devolución es " + returnDate);

			success.showAndWait();
			
			connection.disconnect();
			
			addLoanWindow.close();
		} catch (SQLException e) {
			Alert question = new Alert(AlertType.CONFIRMATION);

			question.setTitle("Matrícula ay registrada");
			question.setHeaderText("Matrícula previamente registrada..");
			question.setContentText("Parece que ya existe un estudiante con esa matrícula. El registro se llevará acabo reutilizando los datos previamente registrados. ¿Desea proseguir?");

			Optional<ButtonType> answer = question.showAndWait();
			
			if (answer.get() == ButtonType.OK) {
				try {
					var connection = new DatabaseConnection();
					String secondQuery = "INSERT INTO loan (`folio`, `return_date`, `student_enrollment`, `book_isbn`) VALUES (?, ?, ?, ?);";

					String enrollment = enrollmentTextField.getText();
					String studentName = studentNameTextField.getText();
					String folio = FolioBuilder.makeFolio();
					LocalDate returnDate = returnDatePicker.getValue();
					String isbn = booksComboBox.getValue();

					if (enrollment.isEmpty() || returnDate == null || isbn.isEmpty()) {
						Alert error = new Alert(AlertType.ERROR);
						error.setTitle("??? ok ???");
						error.setHeaderText("Error al requisitar los campos.");
						error.setContentText("Si sabes que los campos se rellenan, ¿verdad?");

						error.showAndWait();
						return;
					}

					String verifyEnrollmentRegex = "^\\d{10}$";

					Pattern pattern = Pattern.compile(verifyEnrollmentRegex);
					Matcher isEnrrollmentCorrectly = pattern.matcher(enrollment);

					if (!isEnrrollmentCorrectly.matches()) {
						Alert error = new Alert(AlertType.INFORMATION);

						error.setTitle("??? ok ???");
						error.setHeaderText("Error en la matrícula del estudiante.");
						error.setContentText("La matrícula debe estar compuesta por 10 numeros.");

						error.showAndWait();

						return;
					}

					LocalDate today = LocalDate.now();

					if (returnDate.isBefore(today)) {
						Alert error = new Alert(AlertType.ERROR);

						error.setTitle("??? ok ???");
						error.setHeaderText("Error en la fecha de devolució del libro.");
						error.setContentText("La fecha no puede ser menor a hoy. Aún no se puede viajar al pasado.");

						error.showAndWait();

						return;
					}

					connection.executeNonQuery(secondQuery, folio, returnDate, enrollment, isbn);
					
					Alert success = new Alert(AlertType.INFORMATION);

					success.setTitle("¡Préstamo generado!");
					success.setHeaderText("Préstamo generado correctamente.");
					success.setContentText("El préstamo del libro " + bookNameTextField.getText() + " para " + studentName + " se ha generado correctamente. La fecha prevista para la devolución es " + returnDate);

					success.showAndWait();

					connection.disconnect();
					
					addLoanWindow.close();
				} catch (SQLException e2) { }
				
			} else if (answer.get() == ButtonType.CANCEL) {
				return;
			}
		}
	}

	@FXML
	private void onCancel() {
		addLoanWindow.close();
	}
}

module Stormbook {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	
	opens com.upiicsa.stormbook to javafx.graphics, javafx.fxml;
	opens com.upiicsa.stormbook.controllers to javafx.fxml;
	opens com.upiicsa.stormbook.models to javafx.fxml, javafx.base;
}

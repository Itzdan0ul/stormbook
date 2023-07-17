package com.upiicsa.stormbook;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class DatabaseConnection {
	private Connection connection = null;
	
	public DatabaseConnection() throws SQLException {
		final String source = "jdbc:mysql://127.0.0.1:3306/stormbook?useSSL=false";
		final String user = "[your_user]";
		final String password = "[your_password]";
		
		try {
			connection = DriverManager.getConnection(source, user, password);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Método para hacer inserciones, actualizaciones o eliminaciones con y sin parametros.
	 * @param query
	 * 	La consulta SQL a realizar.
	 * @param parameters
	 * 	Los datos a insertar, modificar o eliminar.
	 */
	public void executeNonQuery(String query, Object... parameters) throws SQLException {
		var statement = connection.prepareStatement(query);
		
		if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
        }
		
		statement.executeUpdate();
	}
	
	/**
	 * Método para obtener los registros con y sin parametros.
	 * @param query
	 * 	La consulta SQL a realizar.
	 * @param parameters
	 * 	Los datos a insertar, modificar o eliminar.
	 * @return Retornar los registros basados en la consulta.
	 */
	public ResultSet executeQuery(String query, Object... parameters) throws SQLException {
		var statement = connection.prepareStatement(query);
		
		if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
        }
		
		return statement.executeQuery();
	}
	
	public void disconnect() throws SQLException {
		connection.close();
	}
}

package it.infocamere.sipert.checkUserPermissions.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import it.infocamere.sipert.checkUserPermissions.db.DBConnect;
import it.infocamere.sipert.checkUserPermissions.db.dto.GenericResultsDTO;
import it.infocamere.sipert.checkUserPermissions.db.dto.SchemaDTO;

public class GenericDAO {

	public boolean testConnessioneOK (SchemaDTO schemDTO) {
		
		SchemaDTO schemaDB = schemDTO;
		
		try {
			Connection conn = DBConnect.getConnectionByHost(schemaDB);
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Errore nella connessione", e);
		}
	}

	public boolean testConnessioneOKbyTNS (SchemaDTO schemDTO) {
		
		SchemaDTO schemaDB = schemDTO;
		
		try {
			Connection conn = DBConnect.getConnectionByTNS(schemaDB);
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Errore nella connessione", e);
		}
	}	
	
	public GenericResultsDTO executeQuery(SchemaDTO schemDTO, String queryDB, List<Object> pars) {
		
		
		GenericResultsDTO results = new GenericResultsDTO();
		
		results.setSchema(schemDTO.getSchemaUserName());
		
		SchemaDTO schemaDB = schemDTO;

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnect.getConnectionByTNS(schemaDB);
			
			if (pars != null) {
				preparedStatement = toSqlPreparedStatement(conn, queryDB, pars);
			} else {
				preparedStatement = conn.prepareStatement(queryDB);	
			}

			rs = preparedStatement.executeQuery();

			List<LinkedHashMap<String, Object>> listLinkedHashMap = convertResultSetToListOfLinkedHashMap(rs);
			
			results.setListLinkedHashMap(listLinkedHashMap);
			
		} catch (SQLSyntaxErrorException e) {
			throw new RuntimeException(e.toString(), e);
		} catch (SQLException e) {
			throw new RuntimeException("Errore nell'esecuzione della query: " + e.toString() , e);
		} 
		
		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

		return results;
	}
	
	public java.sql.PreparedStatement toSqlPreparedStatement(Connection con, String queryDB, List<Object> pars) throws RuntimeException {
		
		java.sql.PreparedStatement stmt = null;
		
		try {
			
			 stmt = con.prepareStatement(queryDB);
			 
			 for (int i = 0; pars != null && i < pars.size(); i++) {
				 
				 Object par = pars.get(i);
				 
				 if (par instanceof java.util.Date)
					 par = new java.sql.Date(((java.util.Date)par).getTime());
				 
				 stmt.setObject(i+1, par);
				 
			 }
			 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return stmt;
	}

	public List<LinkedHashMap<String,Object>> convertResultSetToListOfLinkedHashMap(ResultSet rs) throws SQLException {
		
	    ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    List<LinkedHashMap<String,Object>> list = new ArrayList<LinkedHashMap<String,Object>>();
	  
	    while (rs.next()) {
	    	LinkedHashMap<String,Object> row = new LinkedHashMap<String, Object>(columns);
	        
	        for(int i=1; i<=columns; ++i) {
	            row.put(md.getColumnName(i),rs.getObject(i));
	        }
	        list.add(row);
	    }

	    return list;
	}
	
}


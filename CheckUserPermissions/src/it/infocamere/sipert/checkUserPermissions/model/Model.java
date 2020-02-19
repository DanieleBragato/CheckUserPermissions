package it.infocamere.sipert.checkUserPermissions.model;

import java.util.List;

import it.infocamere.sipert.checkUserPermissions.db.dao.GenericDAO;
import it.infocamere.sipert.checkUserPermissions.db.dto.GenericResultsDTO;
import it.infocamere.sipert.checkUserPermissions.db.dto.SchemaDTO;

public class Model {
	
//	private List<SchemaDTO> schemi;
	
//	public List<SchemaDTO> getSchemi(File fileSchemiXLS, boolean reload) throws ErroreFileSchemiNonTrovato, ErroreColonneFileXlsSchemiKo {
//		
//		if (this.schemi == null || reload) {
//			SchemiManager schemiManager = new SchemiManager();
//			this.schemi = schemiManager.getListSchemi(fileSchemiXLS) ;
//
//			//System.out.println("Trovati " +  this.schemi.size() + " schemi");
//		}
//
//		return this.schemi ;
//
//	}
	
	public boolean testConnessioneDB(SchemaDTO schemaDB) {
		
		GenericDAO genericDAO = new GenericDAO();
		
		return genericDAO.testConnessioneOK(schemaDB);
		
	}
	
	public boolean testConnessioneDBbyTNS(SchemaDTO schemaDB) {
		
		GenericDAO genericDAO = new GenericDAO();
		
		return genericDAO.testConnessioneOKbyTNS(schemaDB);
		
	}	
	
	public GenericResultsDTO runQuery(SchemaDTO schema, String queryDB, List<Object> pars) {
		
		GenericResultsDTO risultati = null;
		
		GenericDAO genericDAO = new GenericDAO();
		risultati = genericDAO.executeQuery(schema, queryDB, pars);
		
		return risultati;
	}
}

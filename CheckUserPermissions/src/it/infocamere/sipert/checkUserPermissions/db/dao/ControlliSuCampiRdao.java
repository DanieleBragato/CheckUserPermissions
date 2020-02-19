package it.infocamere.sipert.checkUserPermissions.db.dao;

public class ControlliSuCampiRdao {

    public String getSql() {
    	
    	String sql = "" + 
    	"select COUNT(*)  from controlli_su_campi where ut_code= ? and appl_code='ER' and operando=TO_CHAR(TO_NUMBER(?),'FM0000') ";
    	
    	return sql;
    }	
	
}

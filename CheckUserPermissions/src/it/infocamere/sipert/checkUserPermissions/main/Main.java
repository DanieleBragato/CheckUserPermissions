package it.infocamere.sipert.checkUserPermissions.main;

import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import it.infocamere.sipert.checkUserPermissions.db.dao.ControlliSuCampiRdao;
import it.infocamere.sipert.checkUserPermissions.db.dto.GenericResultsDTO;
import it.infocamere.sipert.checkUserPermissions.db.dto.SchemaDTO;
import it.infocamere.sipert.checkUserPermissions.model.Model;
import it.infocamere.sipert.checkUserPermissions.util.xml.ParseXmlForSetLogFileName;


public class Main {

	private static String nomeUtenteOracle;
	private static String passwordUtenteOracle;
	private static String codiceAzienda;
	private static String UtCode;
	private static String logFile;
	private static String IdConnessione;

	private static Logger log;

	public static void main(String[] args) {

		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (i == 0) nomeUtenteOracle = args[i].toString();
				if (i == 1) passwordUtenteOracle = args[i].toString();
				if (i == 2) codiceAzienda = args[i].toString();
				if (i == 3) UtCode = args[i].toString();
				if (i == 4) logFile = args[i].toString();
				if (i == 5) IdConnessione = args[i].toString();
			}
		} else {
			System.out.println("ERRORE: Non ci sono PARAMETRI DI INPUT !!");
		}

		if ((nomeUtenteOracle == null || (nomeUtenteOracle != null && nomeUtenteOracle.length() < 1))
				|| (passwordUtenteOracle == null || (passwordUtenteOracle != null && passwordUtenteOracle.length() < 1))
				|| (codiceAzienda == null || (codiceAzienda != null && codiceAzienda.length() < 1))
				|| (UtCode == null || (UtCode != null && UtCode.length() < 1))
				|| (logFile == null || (logFile != null && logFile.length() < 1))
				|| (IdConnessione == null || (IdConnessione != null && IdConnessione.length() < 1))) {
			System.out.println("ERRORE: Parametri in input Non completi/valorizzati !!");
			System.out.println("Elaborazione Conclusa con Errori: EXIT !! ");
			return;
		}

		// load configuration File in XML format for logging (log4j)
		URL url = Main.class.getResource("/Log4j.xml");
		if (logFile != null && logFile.length() > 0) {
			// setup nome file dei log da parametro di input
			ParseXmlForSetLogFileName parseXmlForSetLogFileName = new ParseXmlForSetLogFileName();
			InputStream is = parseXmlForSetLogFileName.setLogFileName(logFile, url);
			DOMConfigurator configurator = new DOMConfigurator();
			configurator.doConfigure(is, LogManager.getLoggerRepository());			
		}

		log = Logger.getLogger(Main.class);
		log.info("Inizio elaborazione");
		log.info("Elenco dei parametri di elaborazione:");
		log.info("   Parametro nr 1 - Nome utente Oracle = " + nomeUtenteOracle);
		log.info("   Parametro nr 2 - la Password utente Oracle è presente ");
		log.info("   Parametro nr 3 - Codice azienda = " + codiceAzienda);
		log.info("   Parametro nr 4 - UT_CODE = " + UtCode);
		log.info("   Parametro nr 5 - log file = " + logFile);
		log.info("   Parametro nr 6 - IdConnessione = " + IdConnessione);
		
		Model model = new Model();
		
		if (!model.testConnessioneDBbyTNS(getSchemaDataBase())) {
			log.error("ERRORE: Test di connessione al data base con TNS NON COMPLETATO CORRETTAMENTE !! ");
			log.error("Elaborazione Conclusa con Errori: EXIT !! ");
			return;
		}		

		GenericResultsDTO controlliSuCampiRRisultatiDTO;
		ControlliSuCampiRdao controlliSuCampiRdao = new ControlliSuCampiRdao();
		
    	List<Object> pars = new ArrayList<Object>();
    	pars.add(UtCode);
    	pars.add(codiceAzienda);    	

		controlliSuCampiRRisultatiDTO = model.runQuery(getSchemaDataBase(), controlliSuCampiRdao.getSql(), pars);


		if (controlliSuCampiRRisultatiDTO != null && controlliSuCampiRRisultatiDTO.getListLinkedHashMap() != null) {
			log.info("Query sulla tabella controlli_su_campi >> COUNT = " + controlliSuCampiRRisultatiDTO.getListLinkedHashMap().get(0).get("COUNT(*)").toString() );
			if (controlliSuCampiRRisultatiDTO.getListLinkedHashMap().get(0).get("COUNT(*)").toString() != null ) {
				if ("0".equals(controlliSuCampiRRisultatiDTO.getListLinkedHashMap().get(0).get("COUNT(*)").toString())) {
					System.out.println("N");
				} else {
					System.out.println("S");
					log.info("Elaborazione conclusa correttamente !!");
				}
			} else {
				System.out.println("N");
				log.error("ERRORE: QUERY sulla tabella controlli_su_campi >> Nessuna riga trovata !! ");
				log.error("Elaborazione conclusa con ERRORI !!");				
			}
		} else {
			System.out.println("N");
			log.error("ERRORE: QUERY sulla tabella controlli_su_campi >> Nessuna riga trovata !! ");
			log.error("Elaborazione conclusa con ERRORI !!");
		}

	}


	private static SchemaDTO getSchemaDataBase() {

		SchemaDTO schemaDTO = new SchemaDTO();

		schemaDTO.setSchemaUserName(nomeUtenteOracle);
		schemaDTO.setPassword(passwordUtenteOracle);
//		schemaDTO.setDbName("ORAXAP");
//		schemaDTO.setPort("1523");
//		schemaDTO.setHostServerURL("lxidbpena01.intra.infocamere.it");
		schemaDTO.setIdConnessione(IdConnessione);
		return schemaDTO;

	}

}
package peerToPeerAct4;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Config {

	public Statement state = null;
	public ResultSet resultSet = null;
	public PreparedStatement preparedState = null;
	public String sqlRequest = "";//Requ�te SQL
	public String toSend = "";
	public String getAnn = "";
	public String getMyAnn = "";
	public int idNewAnnonce = 0;
	
	//Variables concernant les informations des annonces
	public String nom = "";
	public double prix = 0;
	public String descriptif = "";
	public String domaine = "";
	
	public int codeUpdate = 0;
	public int codeUpdateBdd = 0;
	public String annonceExists = "";
	
	public int codeDelete = 0;//Code de l'annonce � supprimer (entr�e au clavier)
	public int codeDeleteBdd = 0;//Code de l'annonce � supprimer (r�cup�r�e de la bdd)
	
	//Variables concernant les informations de l'utilisateur (entr�es au clavier)
		public String name = "";
		public String prenom = "";
		public String login = "";
		public String password = "";
		public String connexionSuccess = "";
		public String loginBdd = "";
		public String passwordBdd = "";
		public int idClient = 0;
	
	public Config() {
		// TODO Auto-generated constructor stub
	}

}

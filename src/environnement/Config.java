/*
 * Titre du TP :		Gestionnaire d'annonces Version 2
 * 
 * Date : 				23/11/2019
 * 
 * Nom : 				AGHARMIOU
 * Pr�nom :				Tanina
 * Num�ro �tudiant : 	21961776
 * email : 				20185597@etud.univ-evry.fr
 *  
 * Nom : 				HAMOUCHE
 * Pr�nom :				Nassila
 * Num�ro �tudiant : 	21967736
 * email : 				nassilahamouche@gmail.com
 * 
 * 
 * */

package environnement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * Cette classe permet de regrouper les variables globales utilis�es dans les autres classes
 * En d'autre termes, elle met en place l'environnement de travail
 */
public class Config {

	public Statement state = null;
	public ResultSet resultSet = null;
	public PreparedStatement preparedState = null;
	public String sqlRequest = "";// Requ�te SQL
	public String toSend = "";
	public String getAnn = "";
	public String getMyAnn = "";
	public int idNewAnnonce = 0;

	// Variables concernant les informations des annonces
	public String nom = "";
	public double prix = 0;
	public String descriptif = "";
	public String domaine = "";

	public int codeUpdate = 0;
	public int codeUpdateBdd = 0;
	public String annonceExists = "";

	public int codeDelete = 0;// Code de l'annonce � supprimer (entr�e au clavier)
	public int codeDeleteBdd = 0;// Code de l'annonce � supprimer (r�cup�r�e de la bdd)

	// Variables concernant les informations de l'utilisateur (entr�es au clavier)
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

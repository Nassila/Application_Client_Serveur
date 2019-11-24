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
import java.sql.SQLException;
import java.sql.Statement;

import connexion.Connexion;

/*
 * Cette classe regroupe un ensemble d'outils utilis�s dans les autres classes
 * 
 */
public class Outils {
	public static int idc = 0;

	public static String getAnnonces() {
		Config c = new Config();
		try {

			// Connexion avec la base de donn�es
			c.state = Connexion.connexionBD().createStatement();

			// Requ�te SQL
			c.sqlRequest = "SELECT * FROM `annonces`";

			// Ex�cution de la requ�te
			c.resultSet = c.state.executeQuery(c.sqlRequest);

			if (c.resultSet != null) {

				while (c.resultSet.next()) { // Envoi des annonces � partir de la base de donn�es au demandeur
					c.getAnn = c.getAnn + c.resultSet.getInt("id") + "&" + c.resultSet.getString("nom") + "&"
							+ c.resultSet.getString("domaine") + "&" + c.resultSet.getInt("prix") + "&"
							+ c.resultSet.getString("descriptif") + "&" + c.resultSet.getInt("refCl") + "|";

				}

				c.toSend = "CONFIRMED/" + c.getAnn.substring(0, c.getAnn.length() - 1);// envoie les annonces sauf le
																						// dernier "|"

				// printWrite.println("DISPLAYENDED");//Envoie au client que toutes les annonces
				// ont �t� affich�es
			} else {
				c.toSend = "DENIED/Aucune annonce disponibles !";
			}
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		return c.toSend;

	}

	public static String getMyAnnonces(int id) {
		Config c = new Config();
		try {

			// Connexion avec la base de donn�es
			c.state = Connexion.connexionBD().createStatement();

			// Requ�te SQL
			c.sqlRequest = "SELECT * FROM `annonces` WHERE `refCL` = " + id + ";";

			// Ex�cution de la requ�te
			c.resultSet = c.state.executeQuery(c.sqlRequest);

			if (c.resultSet != null) {

				while (c.resultSet.next()) { // Envoi des annonces � partir de la base de donn�es au demandeur
					c.getMyAnn = c.getMyAnn + c.resultSet.getInt("id") + "&" + c.resultSet.getString("nom") + "&"
							+ c.resultSet.getString("domaine") + "&" + c.resultSet.getInt("prix") + "&"
							+ c.resultSet.getString("descriptif") + "&" + c.resultSet.getInt("refCl") + "|";

				}

				c.toSend = "CONFIRMED/" + c.getMyAnn.substring(0, c.getMyAnn.length() - 1);// envoie les annonces sauf
																							// le dernier "|"

			} else {
				c.toSend = "DENIED/Aucune annonce disponibles !";
			}
		} catch (SQLException e1) {

			e1.printStackTrace();

		}
		return c.toSend;

	}

	public static String newAnnonce(String nom, String domaine, double prix, String des, int id) {
		Config c = new Config();

		c.nom = nom;
		c.domaine = domaine;
		c.prix = prix;
		c.descriptif = des;

		try {

			// Requ�te SQL
			c.sqlRequest = "INSERT INTO `annonces` (`nom`, `domaine`, `prix`, `descriptif`, `refCL`) VALUES ('" + c.nom
					+ "', '" + c.domaine + "', '" + c.prix + "', '" + c.descriptif + "' , '" + id + "');";

			// Connexion avec la base de donn�es
			c.preparedState = Connexion.connexionBD().prepareStatement(c.sqlRequest, Statement.RETURN_GENERATED_KEYS);
			// Ex�cution de la requ�te
			c.preparedState.execute();

			ResultSet resultSet = c.preparedState.getGeneratedKeys();

			if (resultSet.next()) {
				c.idNewAnnonce = resultSet.getInt(1);
			}

			c.toSend = "[SERVEUR] : CONFIRMED/";

		} catch (SQLException e1) {
			// e1.printStackTrace();
			c.toSend = "[SERVEUR] : DENIED/" + e1;
		}

		return c.toSend;
	}

	public static String updateAnnonce(int codeUp, String nom, String domaine, double prix, String des, int id) {
		Config c = new Config();

		c.codeUpdate = codeUp;
		c.nom = nom;
		c.domaine = domaine;
		c.prix = prix;
		c.descriptif = des;

		try {

			// Connexion avec la base de donn�es
			c.state = Connexion.connexionBD().createStatement();

			// Requ�te SQL
			c.sqlRequest = "SELECT * FROM `annonces`";

			// Ex�cution de la requ�te
			c.resultSet = c.state.executeQuery(c.sqlRequest);

			while (c.resultSet.next()) {// Parcours de la bdd � la recherche du code de l'annonce � modifier

				c.codeUpdateBdd = c.resultSet.getInt("id");

				if (c.codeUpdateBdd == c.codeUpdate) {// Test sur l'existance de l'annonce

					c.annonceExists = "EXIST";
					break;
				} else {
					c.annonceExists = "NOTEXIST";
				}
				// Dans tout les cas, le serveur envoie au client une r�ponse

			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if (c.annonceExists.equals("EXIST")) {// Si l'annonce existe

			// Le serveur envoie au client l'annoce � modifier
			try {
				// Connexion avec la base de donn�es
				c.state = Connexion.connexionBD().createStatement();

				PreparedStatement update = Connexion.connexionBD().prepareStatement(
						"UPDATE `annonces` SET `nom` = ?, `domaine`= ?,  `prix`= ?,  `descriptif`= ? WHERE `id` = "
								+ c.codeUpdate + " and `refCL` = " + id + ";");

				update.setString(1, c.nom);
				update.setString(2, c.domaine);
				update.setDouble(3, c.prix);
				update.setString(4, c.descriptif);

				int okUpd = update.executeUpdate();

				if (okUpd == 1) {
					c.toSend = "[SERVEUR] : CONFIRMED/";
				} else
					c.toSend = "[SERVEUR] : ERROR/";
				// call executeUpdate to execute our sql update statement

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {// Si l'annonce n'existe pas
			c.toSend = "\n[SERVEUR] : DENIED/annonce inexistante !";// envoi fin
		}

		return c.toSend;

	}

	public static String deleteAnnonce(int codeDel, int id) {
		Config c = new Config();

		// Lecture des donn�es re�ues du client
		c.codeDelete = codeDel;
		int okDel = 0;
		// Recup�ration du num�ro de l'annoce � supprimer

		try {
			// Connexion avec la base de donn�es
			c.state = Connexion.connexionBD().createStatement();

			// Requ�te SQL
			c.sqlRequest = "SELECT * FROM `annonces`";

			// Ex�cution de la requ�te
			c.resultSet = c.state.executeQuery(c.sqlRequest);

			while (c.resultSet.next()) {// Parcours de la bdd � la recherche du code de l'annonce � supprimer

				c.codeDeleteBdd = c.resultSet.getInt("id");

				if (c.codeDeleteBdd == c.codeDelete) {

					// Connexion avec la base de donn�es
					c.state = Connexion.connexionBD().createStatement();

					// Requ�te SQL
					c.sqlRequest = "DELETE FROM `annonces` WHERE id = " + c.codeDelete + " and `refCL` = " + id + " ;";

					// Ex�cution de la requ�te
					okDel = c.state.executeUpdate(c.sqlRequest);

					break;

				} else {
					okDel = 0;
				}

			}

			if (okDel == 0) {
				c.toSend = "[SERVEUR] : DENIED/L'annonce n'existe pas ! ";
			} else {
				c.toSend = "[SERVEUR] : CONFIRMED/Annonce supprim� avec succ�s !";
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		return c.toSend;
	}

	public static String connect(String login, String passWord) {
		Config c = new Config();

		try {
			// Cr�ation de la connexion avec la base de donn�es
			c.state = Connexion.connexionBD().createStatement();

			// Requ�te SQL
			c.sqlRequest = "SELECT * FROM `clients`";

			// Ex�cution de la requ�te SQL
			c.resultSet = c.state.executeQuery(c.sqlRequest);

			while (c.resultSet.next()) {// Parcours de la base de donn�es

				c.loginBdd = c.resultSet.getString("username"); // login client
				c.passwordBdd = c.resultSet.getString("password");// password client

				if ((c.loginBdd.equals(login)) && (c.passwordBdd.equals(passWord))) {// Test sur le login et le mot de
																						// passe saisis
					c.name = c.resultSet.getString("nom");
					c.prenom = c.resultSet.getString("prenom");
					idc = c.resultSet.getInt("id");// ID client
					c.connexionSuccess = "CONNECTED";// Connexion r�ussie
					break;

				}
			} // End While BDD

			if (c.connexionSuccess.equals("CONNECTED")) {// Si le client est connect�, la while de validit� est termin�e
				c.getAnn = getAnnonces();
				c.getAnn = c.getAnn.substring(11, c.getAnn.length());
				return (c.connexionSuccess + "/" + c.name + "," + c.prenom + "," + login + "," + passWord + "/"
						+ c.getAnn + "-" + idc);
			} else {// Sinon, le serveur attend la saisie des bon identifiants
				return ("DENIED/Erreur dans le login ou le mot de passe !");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return (c.connexionSuccess + "/" + c.name + "," + c.prenom + "," + login + "," + passWord + "/" + c.getAnn + "-"
				+ idc);
	}

	public static String newuser(String name, String prenom, String login, String password) {
		Config c = new Config();
		try {

			// Requ�te SQL
			c.sqlRequest = "INSERT INTO `clients` (`nom`, `prenom`, `username`, `password`) VALUES ('" + name + "', '"
					+ prenom + "', '" + login + "', '" + password + "');";

			// Ex�cution de la requ�te
			// state.executeUpdate(sqlRequest);

			// Connexion avec la base de donn�es
			c.preparedState = Connexion.connexionBD().prepareStatement(c.sqlRequest, Statement.RETURN_GENERATED_KEYS);
			// Ex�cution de la requ�te
			c.preparedState.execute();

			c.resultSet = c.preparedState.getGeneratedKeys();

			if (c.resultSet.next()) {// Parcours de la base de donn�es
				idc = c.resultSet.getInt(1);// ID client

			}
			c.getAnn = getAnnonces();
			c.getAnn = c.getAnn.substring(11, c.getAnn.length());

			return ("CONFIRMED" + "/" + name + "," + prenom + "," + login + "," + password + "/" + c.getAnn + "-"
					+ idc);

		} catch (SQLException e1) {
			// e1.printStackTrace();
			return ("[SERVEUR] : DENIED/" + e1);
		}

	}

	// SocketAddress
	public static int getInfoClient(int idAnn) {
		Config c = new Config();
		int idC = 0;
		try {

			// Connexion avec la base de donn�es
			c.state = Connexion.connexionBD().createStatement();

			// Requ�te SQL
			c.sqlRequest = "SELECT `refCL` FROM `annonces` WHERE `id` = " + idAnn + ";";

			// Ex�cution de la requ�te
			c.resultSet = c.state.executeQuery(c.sqlRequest);

			if (c.resultSet != null) {

				while (c.resultSet.next()) { // Envoi du num client
					idC = c.resultSet.getInt("refCl");

				}

			}

		} catch (SQLException e1) {

			e1.printStackTrace();

		}

		return idC;

	}

}

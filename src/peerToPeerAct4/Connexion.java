
/*
 * Titre du TP :		Gestionnaire d'annonces Version 1
 * 
 * Date : 				31/10/2019
 * 
 * Nom : 				HAMOUCHE
 * Pr�nom :				Nassila
 * Num�ro �tudiant : 	21967736
 * email : 				nassilahamouche@gmail.com
 * 
 * Nom : 				AGHARMIOU
 * Pr�nom :				Tanina
 * Num�ro �tudiant : 	21961776
 * email : 				20185597@etud.univ-evry.fr
 *  
 * 
 * Remarques : Lire le ReadMe.pdf inclus dans le zip
 * 
 * */
package peerToPeerAct4;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connexion {
	public static Connection connexionBD() {

		//creer une connexion avec la base de donn�es
		
		java.sql.Connection cn = null;
		

		try {

			Class.forName("com.mysql.jdbc.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bdd","root","");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cn;
	}
}

/*
 *  Titre du TP :		Gestionnaire d'annonces Version securis�
 * 
 * Date : 				08/12/2019
 * 
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

package connexion;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * Cette classe met en place la connexion avec la base de donn�es MySQL
 * 
 */
public class Connexion {
	public static Connection connexionBD() {

		// creer une connexion avec la base de donn�es

		java.sql.Connection cn = null;

		try {

			Class.forName("com.mysql.jdbc.Driver");
			cn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/bdd", "root", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return cn;
	}
}

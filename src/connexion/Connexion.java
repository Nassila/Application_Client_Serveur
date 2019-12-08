/*
 *  Titre du TP :		Gestionnaire d'annonces Version securisé
 * 
 * Date : 				08/12/2019
 * 
 * 
 * Nom : 				AGHARMIOU
 * Prénom :				Tanina
 * Numéro étudiant : 	21961776
 * email : 				20185597@etud.univ-evry.fr
 *  
 * Nom : 				HAMOUCHE
 * Prénom :				Nassila
 * Numéro étudiant : 	21967736
 * email : 				nassilahamouche@gmail.com
 * 
 * 
 * */

package connexion;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * Cette classe met en place la connexion avec la base de données MySQL
 * 
 */
public class Connexion {
	public static Connection connexionBD() {

		// creer une connexion avec la base de données

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

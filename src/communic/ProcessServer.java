/*
 * Titre du TP :		Gestionnaire d'annonces Version 2
 * 
 * Date : 				23/11/2019
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
package communic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import environnement.Outils;
import main.Server;

/*
 * Cette classe permet la gestion de plusieurs clients pour le gestionnaire d'annonces.
 * Avec utilisation des threads
 */
public class ProcessServer extends Thread {

	public Socket socketClient;
	SocketAddress AdresseIPClient;
	int id = 0;
	String[] info = null;
	List<String> infosClient = Server.infosClient;// récupère les infos sur les clients connectés

	public ProcessServer(Socket socket, SocketAddress AdresseIP) {// Constrcuteur de la classe Process qui prend en
																	// argument une socket cliente
		super();
		this.socketClient = socket;// Récupération de la socket cliente
		this.AdresseIPClient = AdresseIP;// recuperer l'adresse du client connecté
	}

	@SuppressWarnings("unlikely-arg-type")
	public void run() {

		try {
			// Initialisation des stream de lecture et d'écriture
			InputStream inputStream = null;
			InputStreamReader inputStreamRead = null;
			BufferedReader buffRead = null;
			OutputStream outputStream = null;
			PrintWriter printWrite = null;

			// Préparation des stream de communication entre le serveur et ses clients
			inputStream = socketClient.getInputStream();
			inputStreamRead = new InputStreamReader(inputStream);
			buffRead = new BufferedReader(inputStreamRead);
			outputStream = socketClient.getOutputStream();
			printWrite = new PrintWriter(outputStream, true);

			String ip = socketClient.getRemoteSocketAddress().toString();// Récupréation de l'adresse IP du client
																			// connecté
			System.out.println("Connexion du client IP : " + ip);// Affichage des informations concernant le client
																	// connecté

			// Initialisation des variables
			String request = "";

			String[] splitCnxSucs = null;
			String connexionSuccess = "";// Etat de la connexion avec la base de données
			String[] demandeConnexion = buffRead.readLine().split("/");
			String cs = "";
			int numRandomPort = 0;

			if ((demandeConnexion[0].equals("CONNECT"))) {
				// Etat de la connexion avec la base de données

				while (!cs.equals("CONNECTED")) {// Test sur la validité des identifiants saisis

					connexionSuccess = Outils.connect(demandeConnexion[1], demandeConnexion[2]);
					cs = connexionSuccess.substring(0, 9);

					if (cs.equals("CONNECTED")) {
						splitCnxSucs = connexionSuccess.split("-");
						try {
							id = Integer.parseInt(splitCnxSucs[1]);// id int

							info = AdresseIPClient.toString().split(":");
							numRandomPort = (int) (1000 + Math.random() * (4000 - 1000));
							while (true) {
								if (!infosClient.contains(numRandomPort)) {
									infosClient.add(info[0]);// ajout de l'IP
									infosClient.add("" + numRandomPort);// ajout du port
									infosClient.add(splitCnxSucs[1]);// ajout de l'id du client

									break;
								} else {// génération d'un port aléatoire
									numRandomPort = (int) (1000 + Math.random() * (4000 - 1000));
								}

							}
							printWrite.println(splitCnxSucs[0] + ":" + numRandomPort);

						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					} else {
						printWrite.println("DENIED/Erreur dans le login ou le mot de passe !");
						demandeConnexion = buffRead.readLine().split("/");
					}

				} // End While test de validité

			} else if (demandeConnexion[0].equals("NEWUSER")) {

				while (!cs.equals("CONFIRMED")) {

					connexionSuccess = Outils.newuser(demandeConnexion[1], demandeConnexion[2], demandeConnexion[3],
							demandeConnexion[4]);
					cs = connexionSuccess.substring(0, 9);

					if (cs.equals("CONFIRMED")) {
						splitCnxSucs = connexionSuccess.split("-");

						try {
							id = Integer.parseInt(splitCnxSucs[1]);// id int
							printWrite.println(splitCnxSucs[0]);
							info = AdresseIPClient.toString().split(":");

							while (true) {
								if (!infosClient.contains(numRandomPort)) {
									infosClient.add(info[0]);
									infosClient.add("" + numRandomPort);
									infosClient.add(splitCnxSucs[1]);

									break;
								} else {
									numRandomPort = (int) (1000 + Math.random() * (4000 - 1000));
								}

							}

						} catch (NumberFormatException e) {
							e.printStackTrace();
						}

					} else {
						printWrite.println("DENIED/Erreur!");
						demandeConnexion = buffRead.readLine().split("/");
					}
				}

			}

			while (!socketClient.isClosed()) {// Tant que la connexion avec le client n'est pas perdue

				request = buffRead.readLine();// Recupération la requête cliente

				String[] demande = request.split("/");
				// Lecture des données reçues du client

				switch (demande[0].toUpperCase()) {// Traitement la requête selon le cas

				case "GETANNONCES": {// Affichage des annonces disponibles

					printWrite.println(Outils.getAnnonces());// envoie les annonces sauf le dernier "|"

					break;
				} // End GETANNONCES

				case "GETMYANNONCES": {// Affichage des annonces disponibles

					printWrite.println(Outils.getMyAnnonces(id));

					break;
				} // End GETMYANNONCES

				case "NEWANNONCE": { // Ajout d'une annonce

					try {
						Double.parseDouble(demande[3]);
						printWrite.println(Outils.newAnnonce(demande[1].toLowerCase(), demande[2].toLowerCase(),
								Double.valueOf(demande[3]), demande[4].toLowerCase(), id));

					} catch (NumberFormatException e) {
						// not a double
						printWrite.println("Erreur dans le prix saisi !");
					}

					break;
				} // End NEWANNONCE

				case "UPDATEANNONCE": {// Modification d'une annonce

					try {
						Integer.parseInt(demande[1]);// n° int

						try {
							Double.parseDouble(demande[4]);// prix double
							printWrite.println(
									Outils.updateAnnonce(Integer.parseInt(demande[1]), demande[2].toLowerCase(),
											demande[3], Double.valueOf(demande[4]), demande[5].toLowerCase(), id));

						} catch (NumberFormatException e) {

							printWrite.println("Erreur dans le prix saisi !");
						}

					} catch (NumberFormatException e) {
						// not a double
						printWrite.println("Erreur dans le code de l'annonce !");
					}

					break;
				} // End UPDATEANNONCE

				case "DELETEANNONCE": {// Suppression d'une annonce

					try {
						Integer.parseInt(demande[1]);// n° int

						printWrite.println(Outils.deleteAnnonce(Integer.parseInt(demande[1]), id));
					} catch (NumberFormatException e) {
						// not a double
						printWrite.println("Erreur dans le code de l'annonce !");

					}

					break;
				} // End DELETEANNONCE

				case "INFO": {// Infos sur une annonce

					int idAnn = Integer.parseInt(demande[1]);
					int idCl = Outils.getInfoClient(idAnn);

					String PORT = " ";
					String IP = " ";

					String idd = Integer.toString(idCl);
					boolean clientConnecte = false;
					int indexCl = 0;

					while (indexCl < infosClient.size()) {
						if (infosClient.get(indexCl).equals(idd)) {
							PORT = infosClient.get(indexCl - 1);
							IP = infosClient.get(indexCl - 2);
							clientConnecte = true;
							printWrite.println("CONFIRM/" + idAnn + IP + "/" + PORT);
							break;

						}
						indexCl++;
					}

					if (!clientConnecte) {
						printWrite.println("DENIED/le client n'est pas connecté");
					}

					break;

				}

				case "EXIT": {// Quitter la conversation

					printWrite.println("[SERVEUR] : Au revoir, à bientôt !");// Message de fin de communication

					// Fermeture des streams
					printWrite.close();
					buffRead.close();
					socketClient.close();

					break;
				} // End EXIT

				default: {
					break;
				}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
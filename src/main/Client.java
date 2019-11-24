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
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import communic.serverCl.ServerClient;

/*
 * Cette classe est notre classe Client
 * Celui-ci peut int�ragir avec le gestionnaire d'annonces en console interne
 * ou bien communiquer avec d'autres clients via une console externe d�di�e � l'�change de messages
 * 
 */

public class Client {

	public Client() {
		try {// 192.168.43.196

			Socket socket = new Socket("127.0.0.1", 1229);

			// Pour lire les messages du serveur
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStRead = new InputStreamReader(inputStream);
			BufferedReader buffRead = new BufferedReader(inputStRead);

			// Pour �crire et envoyer des messages au serveur
			OutputStream outputStream = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(outputStream, true);

			// line est une variable qui sert � recuperer la saisie du client en console
			String line = "";
			final Scanner scanner = new Scanner(System.in);
			boolean close = false;

			String connexionSucces = "";// Pour la verification de la validit� des identifiants
			String[] req = null;

			// Tant que connection_succes ne re�oit pas CONNECTED de la part du serveur la
			// saisie des identifiants est redemand�e
			while (!connexionSucces.equals("CONNECTED") || !connexionSucces.equals("CONFIRMED")) {

				System.out.println("Saisissez votre demande : ");
				line = scanner.nextLine();// r�cup�re la saisie du user
				pw.println(line);// envoie la demande de connexion au serveur

				String msgSuccess = buffRead.readLine();

				connexionSucces = msgSuccess.substring(0, 9);// le message envoy� par le serveur

				System.err.println("\n" + msgSuccess + "\n");
				if (connexionSucces.equals("CONNECTED") || connexionSucces.equals("CONFIRMED")) {// Si le client est
																									// connect�
					String[] splitSuccess = msgSuccess.split("\\:");// on destructure le message re�u
					int Port = Integer.parseInt(splitSuccess[1]);// pour r�cup�rer le port

					ServerClient window = new ServerClient(Port);// on appelle la classe ServerClient pour ouvrir
																	// l'interface de conversation
					window.frame.setVisible(true);
					window.start();

					break;
				}

			}

			// Affichage du menu d'accueil
			System.out.println("\n***** Menu ***** \n");
			System.out.println("Display toutes les annonces : GETANNONCES");
			System.out.println("Display mes annonces : 		  GETMYANNONCES");
			System.out.println("Poster une annonce :          NEWANNONCE/nom/domaine/prix/descriptif");
			System.out.println("Update une annonce :          UPDATEANNONCE/n�/nom/domaine/prix/descriptif");
			System.out.println("Delete une annonce :          DELETEANNONCE/n�");
			System.out.println("Infos sur une annonce :       INFO/n�");
			System.out.println("Contact un client:         	  CONTACT");
			System.out.println("Quitter :                     EXIT\n");

			System.out.println("Choisissez une operation dans le menu.\n");

			String[] choix = { "GETANNONCES", "GETMYANNONCES", "NEWANNONCE", "UPDATEANNONCE", "DELETEANNONCE", "INFO" };

			line = "";
			while (!line.equals("EXIT") && !socket.isClosed() && !close) {

				System.out.print("[CLIENT] : ");
				line = scanner.nextLine();

				req = line.split("/");
				@SuppressWarnings("unused")
				int Port = 0;
				String[] splitRepSever = null;
				String[] splitRep = null;
				String linec = "";

				pw.println(line);

				if (Arrays.asList(choix).contains(req[0].toUpperCase())) {
					System.out.println(req[0]);

					String repServer = buffRead.readLine();
					System.out.println(repServer);
					splitRepSever = repServer.split("/");

					if (splitRepSever[0].equals("CONFIRM")) {

						Port = Integer.parseInt(splitRepSever[3]);
						System.out.print("[CLIENT] : ");
						linec = scanner.nextLine();
						splitRep = linec.split("/");

						if (splitRep[0].toUpperCase().equals("CONTACT")) {
							System.out.println("in contact");

							// on ouvre une interface d'�change de messages
							communic.client.ClientClient window = new communic.client.ClientClient(splitRepSever[2],
									splitRepSever[3]);
							window.frame.setVisible(true);
							window.start();
						}

					}
				}

				else {

					if (req[0].toUpperCase().equals("EXIT")) {
						System.out.println(buffRead.readLine());
						close = true;
						scanner.close();
						socket.close();

					}

					if (!close) {
						System.out.print("\n[CLIENT] : ");
						line = scanner.nextLine();
						req = line.split("/");
					} else {
						System.exit(0);
						break;
					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Client c = new Client();
	}

}

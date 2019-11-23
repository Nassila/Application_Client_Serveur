package peerToPeerAct4;

/*
 * Titre du TP :		Gestionnaire d'annonces Version 1
 * 
 * Date : 				31/10/2019
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
 * Remarques : Lire le ReadMe.pdf inclus dans le zip
 * 
 * */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import com.serverCl.ServeurClient;

public class Client {
	private Object frame;

	// public static int idc = 0;
	public Client() {
		try {// 192.168.43.196
				// 192.168.43.62
			Socket socket = new Socket("127.0.0.1", 1229);

			// Pour lire les messages du serveur
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStRead = new InputStreamReader(inputStream);
			BufferedReader buffRead = new BufferedReader(inputStRead);

			// Pour écrire et envoyer des messages au serveur
			OutputStream outputStream = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(outputStream, true);

			// line est une variable qui sert à recuperer la saisie du client en console
			String line = "";
			Scanner scanner = new Scanner(System.in);
			boolean close = false;

			String connexionSucces = "";// Pour la verification de la validité des identifiants
			String[] req = null;

			// Tant que connection_succes ne reçoit pas CONNECTED de la part du serveur la
			// saisie des identifiants est redemandée
			while (!connexionSucces.equals("CONNECTED") || !connexionSucces.equals("CONFIRMED")) {

				System.out.println("Saisissez votre demande : ");
				line = scanner.nextLine();// récupère la saisie du user
				pw.println(line);// envoie la demande de connexion au serveur

				String cs = buffRead.readLine();

				connexionSucces = cs.substring(0, 9);// le message envoyé par le serveur

				System.err.println("\n" + cs + "\n");
				if (connexionSucces.equals("CONNECTED") || connexionSucces.equals("CONFIRMED")) {// Si le client est
					String[] tab = cs.split("\\:");
					int Port = Integer.parseInt(tab[1]);// connecté, la

					ServeurClient window = new ServeurClient(Port);
					window.frame.setVisible(true);
					window.start();

					// new ProcessClient(Port).start(); // while de validité
					// est terminée
					break;
				}

			}

			// Affichage du menu d'accueil
			System.out.println("\n***** Menu ***** \n");
			System.out.println("Display toutes les annonces : GETANNONCES");
			System.out.println("Display mes annonces : 		GETMYANNONCES");
			System.out.println("Poster une annonce :          NEWANNONCE/nom/domaine/prix/descriptif");
			System.out.println("Update une annonce :          UPDATEANNONCE/n°/nom/domaine/prix/descriptif");
			System.out.println("Delete une annonce :          DELETEANNONCE/n°");
			System.out.println("Quitter :                     EXIT\n");

			System.out.println("Choisissez une operation dans le menu.\n");

			String[] choix = { "GETANNONCES", "GETMYANNONCES", "NEWANNONCE", "UPDATEANNONCE", "DELETEANNONCE", "INFO" };

			// while (!socket.isClosed()) {
			line = "";
			while (!line.equals("EXIT") && !socket.isClosed() && !close) {

				System.out.print("[CLIENT] : ");
				line = scanner.nextLine();
				// System.out.println("ecrit ====== "+line);
				req = line.split("/");
				int Port = 0;
				String[] tab = null;
				String[] x = null;
				String[] y = null;
				String linec = "";
				boolean ok = false;
				int p = 0;
				// do {

				pw.println(line);

				if (Arrays.asList(choix).contains(req[0].toUpperCase())) {
					System.out.println(req[0]);

//						System.out.println("OK ========"+ok);

					String a = buffRead.readLine();
					System.out.println(a);
					x = a.split("/");

					if (x[0].equals("CONFIRM")) {
						Port = Integer.parseInt(x[3]);
						System.out.print("[CLIENT] : ");
						linec = scanner.nextLine();
						y = linec.split("/");

						if (y[0].toUpperCase().equals("CONTACT")) { //
							System.out.println("in contact"); // tab = buffRead.readLine().split("/");

							// new ContactClient(x[2], x[3]).start();
							com.client.ClientClient window = new com.client.ClientClient(x[2], x[3]);
							window.frame.setVisible(true);
							window.start();

						}

					}
				}

				else {

					if (req[0].toUpperCase().equals("EXIT")) {
						System.out.println(buffRead.readLine());
						close = true;
						socket.close();
					}

					if (!close) {
						System.out.print("\n[CLIENT] : ");
						line = scanner.nextLine();
						req = line.split("/");
					} else {
						scanner.close();
						socket.close();
						break;
					}

				}

				// } while (!close);
			}
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Client c = new Client();
	}

}

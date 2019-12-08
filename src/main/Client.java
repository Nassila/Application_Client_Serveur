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
package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

import communic.client.ClientClient;
import communic.serverCl.ServerClient;
import environnement.Outils;

/*
 * Cette classe est notre classe Client
 * Celui-ci peut intéragir avec le gestionnaire d'annonces en console interne
 * ou bien communiquer avec d'autres clients via une console externe dédiée à l'échange de messages
 * 
 */

public class Client {
	// static ServerSocket serverSocket;
	PrivateKey clePriveA = null;
	PublicKey clePublicB = null;
	ServerClient window1 = null;
	ServerSocket serverScocket;
	String Port = "";

	@SuppressWarnings("static-access")
	public Client() throws NoSuchAlgorithmException, InvalidKeySpecException {
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
			final Scanner scanner = new Scanner(System.in);
			boolean close = false;

			String connexionSucces = "";// Pour la verification de la validité des identifiants
			String[] req = null;
			int pt = 0;

			// Tant que connection_succes ne reçoit pas CONNECTED de la part du serveur la
			// saisie des identifiants est redemandée
			while (!connexionSucces.equals("CONFIRMED")) {

				// Génération de la paire de clés
				KeyPair paireCles = Outils.genererCles(2048);
				clePriveA = paireCles.getPrivate();
				PublicKey clePubliqueA = paireCles.getPublic();

				// converting public key to byte
				byte[] byte_pubkey = clePubliqueA.getEncoded();
				// converting byte to String
				String str_key = Base64.getEncoder().encodeToString(byte_pubkey);

				System.out.println("********* MENU ********* \n ");
				System.out.println("CONNEXION : CONNECT/login/mot de passe \n ");
				System.out.println("INSCRIPTION : NEWUSER/nom/prenom/login/mot de passe \n ");
				System.out.println("Saisissez votre demande \n ");
				line = scanner.nextLine();// récupère la saisie du user
				pw.println(line);// envoie la demande de connexion au serveur

				String msgSuccess = buffRead.readLine();

				connexionSucces = msgSuccess.substring(0, 9);// le message envoyé par le serveur

				System.err.println("\n" + msgSuccess + "\n");
				if (connexionSucces.equals("CONFIRMED")) {// Si le client est
															// connecté
					String[] splitSuccess = msgSuccess.split("\\:");// on destructure le message reçu

					serverScocket = new ServerSocket(0);
					Port = Integer.toString(serverScocket.getLocalPort());

					pw.println("SEND/" + Port + "/" + str_key);

					window1 = new ServerClient(clePriveA, serverScocket);// on appelle la classe
					// ServerClient pour
					// l'interface de conversation

					window1.frame.setVisible(true);

					window1.start();

					break;
				}

			}

			// Affichage du menu d'accueil
			System.out.println("\n***** Menu ***** \n");
			System.out.println("Display toutes les annonces : GETANNONCES");
			System.out.println("Display mes annonces : 		  GETMYANNONCES");
			System.out.println("Poster une annonce :          NEWANNONCE/nom/domaine/prix/descriptif");
			System.out.println("Update une annonce :          UPDATEANNONCE/n°Annonce/nom/domaine/prix/descriptif");
			System.out.println("Delete une annonce :          DELETEANNONCE/n°Annonce");
			System.out.println("Infos sur une annonce :       INFO/n°Annonce");
			System.out.println("Clé du destinataire:          KEYS/n°Client");
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
					// System.out.println(req[0]);

					String repServer = buffRead.readLine();
					System.out.println(repServer);
					splitRepSever = repServer.split("/", 5);

					if (splitRepSever[0].equals("CONFIRMEDP2P")) {

						Port = Integer.valueOf((String) splitRepSever[3]);
						// converting string to Bytes
						System.out.println(splitRepSever[4]);
						byte[] byte_pubkey = Base64.getDecoder().decode(splitRepSever[4]);

						// converting it back to public key
						try {
							X509EncodedKeySpec ks = new X509EncodedKeySpec(byte_pubkey);
							KeyFactory kf = KeyFactory.getInstance("RSA");
							clePublicB = kf.generatePublic(ks);

						} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
							System.err.println(e);

						}

						System.out.print("[CLIENT] : ");
						linec = scanner.nextLine();
						splitRep = linec.split("/");

						if (splitRep[0].toUpperCase().equals("CONTACT")) {
							// on ouvre une interface d'échange de messages
							ClientClient window = new ClientClient(splitRepSever[2], splitRepSever[3], clePublicB,
									clePriveA);
							window.frame.setVisible(true);
							window.start();
						}

					}

				} else if (req[0].toUpperCase().equals("KEY")) {
					String[] clecle = buffRead.readLine().split("/", 2);

					System.out.println(clecle[1]);
					byte[] byte_pubkey = Base64.getDecoder().decode(clecle[1]);

					// converting it back to public key
					try {
						X509EncodedKeySpec ks = new X509EncodedKeySpec(byte_pubkey);
						KeyFactory kf = KeyFactory.getInstance("RSA");
						PublicKey clePublic = kf.generatePublic(ks);
						window1.setkeypub(clePublic);

					} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
						System.err.println(e);

					}

				} else

				if (req[0].toUpperCase().equals("EXIT")) {
					System.out.println(buffRead.readLine());
					close = true;
					scanner.close();
					socket.close();

				} else {
					System.out.println(buffRead.readLine());
				}

				if (!close) {
					System.out.print("\n Souhaitez vous faire autre chose \n");

				} else {
					System.exit(0);
					break;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		Client c = new Client();
	}

}

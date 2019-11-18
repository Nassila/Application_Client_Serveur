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
 * 
 * Remarques : Lire le ReadMe.pdf inclus dans le zip
 * 
 * */
package peerToPeerAct1;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


class Process extends Thread{



	private Socket socketClient;
	SocketAddress AdresseIPClient;
	int id = 0;
	String[] info= null;
	//Object[] infosClient = null;
	List<String> infosClient = Server.infosClient;




	public Process(Socket socket, SocketAddress AdresseIP) {//Constrcuteur de la classe Process qui prend en argument une socket cliente
		super();
		this.socketClient = socket;//Récupération de la socket cliente
	    this.AdresseIPClient = AdresseIP;//recuperer l'adresse du client connecté
	}

	public void run() {

		try {
			//Initialisation des stream de lecture et d'écriture
			InputStream inputStream = null;
			InputStreamReader inputStreamRead = null;
			BufferedReader buffRead = null;
			OutputStream outputStream = null;
			PrintWriter printWrite = null;

			//Préparation des stream de communication entre le serveur et ses clients
			inputStream = socketClient.getInputStream();
			inputStreamRead = new InputStreamReader(inputStream);
			buffRead= new BufferedReader(inputStreamRead);
			outputStream = socketClient.getOutputStream();
			printWrite = new PrintWriter(outputStream, true);

			String ip = socketClient.getRemoteSocketAddress().toString();//Récupréation de l'adresse IP du client connecté
			System.out.println("Connexion du client IP : "+ ip);//Affichage des informations concernant le client connecté

			//Initialisation des variables
			String request ="";
			
			String[] t = null;
			String connexionSuccess = "";//Etat de la connexion avec la base de données
			String[] demandeConnexion = buffRead.readLine().split("/");
			String cs ="";

			if ((demandeConnexion[0].equals("CONNECT"))) {
				//Etat de la connexion avec la base de données

				while (!cs.equals("CONNECTED")) {//Test sur la validité des identifiants saisis

					connexionSuccess = Outils.connect(demandeConnexion[1], demandeConnexion[2]);
					cs = connexionSuccess.substring(0, 9);

					if (cs.equals("CONNECTED")) {
						t = connexionSuccess.split("-");
						try {
							id = Integer.parseInt(t[1]);//id int
							printWrite.println(t[0]);
							info = AdresseIPClient.toString().split(":");
							//infosClient.add(info[0]+"/"+info[1]+"/"+id);
							infosClient.add(info[0]);
							infosClient.add(info[1]);
							infosClient.add(t[1]);
							//printWrite.println(infosClient);
						
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						printWrite.println("DENIED/Erreur dans le login ou le mot de passe !");
						demandeConnexion = buffRead.readLine().split("/");
					}


				}//End While test de validité
				

			}else if (demandeConnexion[0].equals("NEWUSER")) {

				while (!cs.equals("CONFIRMED")) {

					connexionSuccess= Outils.newuser(demandeConnexion[1],demandeConnexion[2], demandeConnexion[3], demandeConnexion[4]);
					cs = connexionSuccess.substring(0, 9);

					if (cs.equals("CONFIRMED")) {
						t = connexionSuccess.split("-");

						try {
							id = Integer.parseInt(t[1]);//id int
							printWrite.println(t[0]);
							info = AdresseIPClient.toString().split(":");
							//infosClient.add(info[0]+"/"+info[1]+"/"+id);
							infosClient.add(info[0]);
							infosClient.add(info[1]);
							infosClient.add(t[1]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}    

					}else {
						printWrite.println("DENIED/Erreur!");
						demandeConnexion = buffRead.readLine().split("/");
					}
				}

			}

			while (!socketClient.isClosed()) {//Tant que la connexion avec le client n'est pas perdue

				request = buffRead.readLine();//Recupération la requête cliente

				String[] demande = request.split("/");
				//Lecture des données reçues du client

				switch(demande[0].toUpperCase()) {//Traitement la requête selon le cas

				case "GETANNONCES" : {//Choix 1 : Affichage des annonces disponibles

					printWrite.println(Outils.getAnnonces()) ;//envoie les annonces sauf le dernier "|"

					break;
				}//End GETANNONCES

				case "GETMYANNONCES" : {//Choix 2 : Affichage des annonces disponibles

					printWrite.println(Outils.getMyAnnonces(id)) ;

					break;
				}//End GETMYANNONCES

				case "NEWANNONCE" : { //Choix 3 : Ajout d'une annonce

					//	Outils.newAnnonce(nom, domaine, prix, descriptif);

					try {
						Double.parseDouble(demande[3]);
						printWrite.println(Outils.newAnnonce(demande[1].toLowerCase(), demande[2].toLowerCase(), Double.valueOf(demande[3]), demande[4].toLowerCase(), id));

					}
					catch(NumberFormatException e)
					{
						//not a double
						printWrite.println("Erreur dans le prix saisi !");
					}


					break;
				}//End NEWANNONCE

				case "UPDATEANNONCE" : {//Choix 4 : Modification d'une annonce

					try {
						Integer.parseInt(demande[1]);//n° int

						try {
							Double.parseDouble(demande[4]);//prix double
							printWrite.println(Outils.updateAnnonce(Integer.parseInt(demande[1]), demande[2].toLowerCase(), demande[3], Double.valueOf(demande[4]), demande[5].toLowerCase(), id));
						
						} catch (NumberFormatException e) {

							printWrite.println("Erreur dans le prix saisi !");
						}

					}
					catch(NumberFormatException e)
					{
						//not a double
						printWrite.println("Erreur dans le code de l'annonce !");
					}



					break;
				}//End UPDATEANNONCE

				case "DELETEANNONCE" : {//Choix 5 : Suppression d'une annonce

					try {
						Integer.parseInt(demande[1]);//n° int

						printWrite.println(Outils.deleteAnnonce(Integer.parseInt(demande[1]), id));
					}
					catch(NumberFormatException e)
					{
						//not a double
						printWrite.println("Erreur dans le code de l'annonce !");

					}

					break;
				}//End DELETEANNONCE

				case "EXIT" : {//Choix 6 : Quitter la conversation

					printWrite.println("[SERVEUR] : Au revoir, à bientôt !");//Message de fin de communication

					//Fermeture des streams
					printWrite.close();
					buffRead.close();
					socketClient.close();// Fermeture de la socket cliente

					break;
				}//End EXIT
				
				case "INFO" : {
					int idan = Integer.parseInt(demande[1]);  
					int idC = Outils.getInfoClient(idan);
					String PORT =" ";
					String IP =" ";
					String idd = Integer.toString(idC); 
					
					for (int i = 0; i < infosClient.size(); i++) {
						 if (infosClient.get(i).equals(idd)) {
							PORT = infosClient.get(i-1);
							IP = infosClient.get(i-2);
							break;
						}
					}
					
					
					printWrite.println("CONFIRM/"+idan+IP+"/"+PORT);
					
					break;		
					
					}

				default : {//Un choix autre que 1..6 a été demandé
					System.out.println("[SERVEUR] : try again ...");
					break;
				}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}	
}
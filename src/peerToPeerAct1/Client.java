package peerToPeerAct1;

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

import java.io.*;
import java.net.*;
import java.util.*;


public class Client {
	//public static int idc = 0;
	public Client(){
		try {//192.168.43.196
			//192.168.43.62
			Socket socket = new Socket("127.0.0.1", 1229);

			//Pour lire les messages du serveur
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStRead = new InputStreamReader(inputStream);
			BufferedReader buffRead = new BufferedReader(inputStRead);

			//Pour écrire et envoyer des messages au serveur
			OutputStream outputStream = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(outputStream, true);


			// line est une variable qui sert à recuperer la saisie du client en console
			String line = "";
			Scanner scanner = new Scanner(System.in);
			boolean close = false;

			String connexionSucces = "";//Pour la verification de la validité des identifiants
			String[] req = null;

			//Tant que connection_succes ne reçoit pas CONNECTED de la part du serveur la saisie des identifiants est redemandée
			while (!connexionSucces.equals("CONNECTED") || !connexionSucces.equals("CONFIRMED")) {

				System.out.println("Saisissez votre demande : ");
				line = scanner.nextLine();//récupère la saisie du user
				pw.println(line);//envoie la demande de connexion au serveur

				String cs = buffRead.readLine();

				connexionSucces = cs.substring(0 , 9);//le message envoyé par le serveur

				System.err.println("\n" + cs +"\n");
				if (connexionSucces.equals("CONNECTED") || connexionSucces.equals("CONFIRMED")) {//Si le client est connecté, la while de validité est terminée
					break;
				}

			} 


			//Affichage du menu d'accueil
			System.out.println("\n***** Menu ***** \n");
			System.out.println("Display toutes les annonces : GETANNONCES");
			System.out.println("Display mes annonces : 		GETMYANNONCES");
			System.out.println("Poster une annonce :          NEWANNONCE/nom/domaine/prix/descriptif");
			System.out.println("Update une annonce :          UPDATEANNONCE/n°/nom/domaine/prix/descriptif");
			System.out.println("Delete une annonce :          DELETEANNONCE/n°");
			System.out.println("Quitter :                     EXIT\n");

			System.out.println("Choisissez une operation dans le menu.\n");

			String[] choix = {"GETANNONCES", "GETMYANNONCES", "NEWANNONCE", "UPDATEANNONCE", "DELETEANNONCE" , "INFO", "REC"};

			while (!socket.isClosed()) {

				System.out.print("[CLIENT] : ");
				line = scanner.nextLine();
				//	System.out.println("ecrit ====== "+line);
				req = line.split("/");
				int Port = 0;
				String[] tab= null;
				String[] x = null;
				String[] y = null;
				String linec =  "";
				boolean ok = false;
				int p = 0;
				do {

					pw.println(line);


					if(Arrays.asList(choix).contains(req[0].toUpperCase())) {
						System.out.println(req[0]);

						if(req[0].toUpperCase().equals("REC")) {
							ok = true;
						}

//						System.out.println("OK ========"+ok);

						
						
						if(ok) {
							//	printWrite.println("OK contact me") ;//envoie les annonces sauf le dernier "|"
							//tab = buffRead.readLine().split("/");
							Socket socket1 = new Socket("127.0.0.1", 1254);
						//	System.out.println("port ="+Port);
							System.out.println("ok u can connect with me");
							
							//Pour lire les messages du serveur
							InputStream inputStreamCL = socket1.getInputStream();
							InputStreamReader inputStReadCL = new InputStreamReader(inputStreamCL);
							BufferedReader buffReadCL = new BufferedReader(inputStReadCL);
							OutputStream outputStreamCL = socket1.getOutputStream();
							PrintWriter pwCL = new PrintWriter(outputStreamCL, true);
							String str = "";
							while(!(str = buffReadCL.readLine()).equals("A")) {
								System.out.print("[RECEIVED] : "+str + "\n");
								System.out.print("[SEND] : ");
								linec = scanner.nextLine();
								pwCL.println(linec);
								if (linec.equals("A")) {
									socket1.close();
									break;
								}
							}
							ok = false;
						}
						else {
							
						
						String a = buffRead.readLine();
						System.out.println(a);
						x = a.split("/");
						
						if (x[0].equals("CONFIRM")) {
						Port = Integer.parseInt(x[3]);
						System.out.print("[CLIENT] : ");
							linec = scanner.nextLine();
							y = linec.split("/");

							if(y[0].toUpperCase().equals("CONTACT")) {
							//	System.out.println("in contact");
							//	tab = buffRead.readLine().split("/");
								
								//	System.out.println(buffRead.readLine() + "\n");
								ServerSocket serverSocket1 = new ServerSocket(1254);
							//	System.out.println("port ="+Port);
								//	System.out.println("Connexion du client IP : "+ ipp);//Affichage des informations concernant le client connecté
								Socket socketCL = serverSocket1.accept();
								
								System.out.println("connected with client");
								//Pour écrire et envoyer des messages au CLIENT
								OutputStream outputStreamCL = socketCL.getOutputStream();
								PrintWriter pwCL = new PrintWriter(outputStreamCL, true);
								InputStream inputStreamCL = socketCL.getInputStream();
								InputStreamReader inputStReadCL = new InputStreamReader(inputStreamCL);
								BufferedReader buffReadCL = new BufferedReader(inputStReadCL);

								while(!linec.equals("A")) {
									System.out.print("[SEND] : ");
									linec = scanner.nextLine();
									pwCL.println(linec);
									System.out.print("[RECEIVED] : "+buffReadCL.readLine());
									if (linec.equals("A")) {
										socketCL.close();
										break;
									}
								}

							}
							
						}
					}
						
					}
					else {

						if(req[0].toUpperCase().equals("EXIT")) {
							System.out.println(buffRead.readLine());
							close = true;
							socket.close();
						}
						else {
							if(req[0].toUpperCase().equals("CONNECTUSER")) {

							}




						}

					}

					if(!close) {
						System.out.print("\n[CLIENT] : ");
						line = scanner.nextLine();
						req = line.split("/");
					}
					else {
						scanner.close();
					}

				}
				while (!close);
			}


		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	public static void main(String[] args) {
		Client c = new Client();
	}

}

/*
 * Titre du TP :		Gestionnaire d'annonces Version 1
 * 
 * Date : 				31/10/2019
 * 
 * Nom : 				HAMOUCHE
 * Prénom :				Nassila
 * Numéro étudiant : 	21967736
 * email : 				nassilahamouche@gmail.com
 * 
 * Nom : 				AGHARMIOU
 * Prénom :				Tanina
 * Numéro étudiant : 	21961776
 * email : 				20185597@etud.univ-evry.fr
 *  
 * 
 * Remarques : Lire le ReadMe.pdf inclus dans le zip
 * 
 * */

package peerToPeerAct4;

import java.io.*;
import java.net.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Server extends Thread{
	static List<String> infosClient =new ArrayList<String>();
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(1229);//Création d'une socket côté serveur sur le port 1229

			while(true) {
				Socket clientSocket = serverSocket.accept();//Création d'une socket côté client
				SocketAddress AdresseIPClient = clientSocket.getRemoteSocketAddress();//recuperer l'adresse du client 
				
				new ProcessServer(clientSocket, AdresseIPClient).start();//Pour chaque client connecté au serveur, une instance de classe Process est évoquée
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().start();//Démarrage du serveur, prêt à recevoir des connexions clientes
	}

}

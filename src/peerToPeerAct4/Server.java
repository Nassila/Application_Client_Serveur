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
			ServerSocket serverSocket = new ServerSocket(1229);//Cr�ation d'une socket c�t� serveur sur le port 1229

			while(true) {
				Socket clientSocket = serverSocket.accept();//Cr�ation d'une socket c�t� client
				SocketAddress AdresseIPClient = clientSocket.getRemoteSocketAddress();//recuperer l'adresse du client 
				
				new ProcessServer(clientSocket, AdresseIPClient).start();//Pour chaque client connect� au serveur, une instance de classe Process est �voqu�e
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().start();//D�marrage du serveur, pr�t � recevoir des connexions clientes
	}

}

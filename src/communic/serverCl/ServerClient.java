/*
 *  Titre du TP :		Gestionnaire d'annonces Version securisé
 * 
 * Date : 				08/12/2019
 * 
 * 
 * 
 * Nom : 				HAMOUCHE
 * Prénom :				Nassila
 * Numéro étudiant : 	21967736
 * email : 				nassilahamouche@gmail.com
 * 
 * 
 * Nom : 				AGHARMIOU
 * Prénom :				Tanina
 * Numéro étudiant : 	21961776
 * email : 				20185597@etud.univ-evry.fr
 *  
 * 
 * */

package communic.serverCl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import environnement.Outils;

/*
 * Recepteur
 * Cette classe permet la communication entre deux ou plusieurs clients
 * Elle contient une SeverSocket qui acceptera une connexion cliente
 * 
 */
public class ServerClient extends Thread {

	public static JFrame frame;
	private static JTextField textField;
	private static JPanel panelCenter;
	private static JScrollPane scrollPane;
	private static JTextArea textArea;
	ServerSocket serverSocket;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */

	public void run() {

		String message = "";
		try {

			socket = serverSocket.accept();
			dataOut = new DataOutputStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());
			String out = "";
			while (!out.equals("EXIT")) {
				message = dataIn.readUTF();

				// dechiffremnt
				out = Outils.dechiffrement(prv, message);

				textArea.setText(textArea.getText().trim() + "\n" + out);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 * 
	 */

	static Socket socket;
	static DataInputStream dataIn;
	static DataOutputStream dataOut;
	static PrivateKey prv;
	static PublicKey pub;

	@SuppressWarnings("static-access")
	public ServerClient(PrivateKey prv, ServerSocket serverSocket) {
		this.prv = prv;
		this.serverSocket = serverSocket;
		initialize();

	}

	public void setkeypub(PublicKey pubb) {
		this.pub = pubb;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private static void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Serveur Client recepteur");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelsouth = new JPanel();
		frame.getContentPane().add(panelsouth, BorderLayout.SOUTH);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (textField.getText().equals("EXIT")) {
					try {
						String msg = "Aurevoir";
						// Chiffrement du message
						String out = Outils.chiffrement(pub, msg);
						dataOut.writeUTF(out);

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});
		panelsouth.add(textField);
		textField.setColumns(10);

		JButton btnEnvoyer = new JButton("envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String messageOut = "";
				try {
					messageOut = textField.getText().trim();

					// Chiffrement du message
					String out = Outils.chiffrement(pub, messageOut);

					dataOut.writeUTF(out);
					textField.setText("");

				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		panelsouth.add(btnEnvoyer);

		panelCenter = new JPanel();
		frame.getContentPane().add(panelCenter, BorderLayout.CENTER);

		textArea = new JTextArea();
		textArea.setRows(12);
		textArea.setColumns(35);
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		panelCenter.add(scrollPane);

	}

}

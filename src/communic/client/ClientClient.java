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
package communic.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
 * Emetteur
 * Cette classe permet la communication entre deux ou plusieurs clients
 * Elle contient une socket cliente qui demandera la connexion à ServerClient
 * 
 */
public class ClientClient extends Thread {

	public JFrame frame;
	static String IpAdress = "";
	static int Port = 0;

	private JTextField textField;
	private JPanel panelCenter;
	private JScrollPane scrollPane;
	private static JTextArea textArea;

	/**
	 * Launch the application.
	 */

	static Socket socket;
	static DataInputStream dataIn;
	static DataOutputStream dataOut;
	static PublicKey pub;
	static PrivateKey prv;

	public void run() {

		String message = "";
		try {
			socket = new Socket(IpAdress, Port);// socket cliente

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
	 */
	@SuppressWarnings("static-access")
	public ClientClient(String Ip, String port, PublicKey pub, PrivateKey prv) {
		this.prv = prv;
		this.pub = pub;
		this.IpAdress = Ip;
		this.Port = Integer.parseInt(port);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Client emetteur");
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
					frame.dispose();
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
					e2.printStackTrace();
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

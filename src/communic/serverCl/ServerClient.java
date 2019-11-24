/*
 * Titre du TP :		Gestionnaire d'annonces Version 2
 * 
 * Date : 				23/11/2019
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Cette classe permet la communication entre deux ou plusieurs clients
 * Elle contient une SeverSocket qui acceptera une connexion cliente
 * 
 */
public class ServerClient extends Thread {

	public JFrame frame;
	private JTextField textField;
	private JPanel panelCenter;
	private JScrollPane scrollPane;
	private static JTextArea textArea;
	static int Port = 0;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public void run() {

		String message = "";
		try {
			serverSocket = new ServerSocket(Port);// socket serveur
			socket = serverSocket.accept();
			dataOut = new DataOutputStream(socket.getOutputStream());
			dataIn = new DataInputStream(socket.getInputStream());
			while (!message.equals("EXIT")) {
				message = dataIn.readUTF();
				textArea.setText(textArea.getText().trim() + "\n" + message);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 * 
	 */

	static ServerSocket serverSocket;
	static Socket socket;
	static DataInputStream dataIn;
	static DataOutputStream dataOut;

	@SuppressWarnings("static-access")
	public ServerClient(int port) {
		this.Port = port;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
						dataOut.writeUTF("ClientServeur : Aurevoir");
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

					dataOut.writeUTF("ClientServeur : " + messageOut);
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

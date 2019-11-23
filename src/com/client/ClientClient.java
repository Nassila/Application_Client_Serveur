package com.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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

	public void run() {
		/*
		 * EventQueue.invokeLater(new Runnable() { public void run() { try { // Client
		 * window = new Client(); // window.frame.setVisible(true); } catch (Exception
		 * e) { e.printStackTrace(); } } });
		 */
		String message = "";
		try {
			socket = new Socket(IpAdress, Port);

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
	 */
	public ClientClient(String Ip, String port) {
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
		panelsouth.add(textField);
		textField.setColumns(10);

		JButton btnEnvoyer = new JButton("envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String messageOut = "";
				try {
					messageOut = textField.getText().trim();
					dataOut.writeUTF("Client : " + messageOut);
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

		scrollPane = new JScrollPane(textArea);
		panelCenter.add(scrollPane);

	}

}

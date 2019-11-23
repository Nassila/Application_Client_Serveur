package com.serverCl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ServeurClient extends Thread {

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
		/*
		 * EventQueue.invokeLater(new Runnable() {
		 * 
		 * public void run() {
		 * 
		 * try { // ServeurClient window = new ServeurClient(Port); //
		 * window.frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); }
		 * } });
		 */
		String message = "";
		try {
			serverSocket = new ServerSocket(Port);
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

	public ServeurClient(int port) {
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

		scrollPane = new JScrollPane(textArea);
		panelCenter.add(scrollPane);

	}

}

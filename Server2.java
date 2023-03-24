//Create Frame For Server to See the Sended and Received Message.

import java.net.Socket;
import java.net.ServerSocket;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Server2 implements ActionListener
{
	static ServerSocket ss;
	static Socket clientSocket;
	DataOutputStream dout;
	static DataInputStream din;
	static String inputData,outputData;
	static JFrame f;
	static JScrollPane js1,js2;
	JButton send,exit;
	JLabel message;
	static JTextArea data;
	static JPanel chatPanel,typePanel,mainPanel;
	static Font font=new Font("calibri",Font.BOLD,30);
	
	Server2(String title)
	{
		f=new JFrame(title);

		try
		{
			ss=new ServerSocket(2222);
			clientSocket=ss.accept();
		}
		catch(IOException ie)
		{
			System.out.println(ie);
		}
	}
	void addComponents()
	{
		mainPanel=new JPanel();
		mainPanel.setBounds(5,5,650,550);
		mainPanel.setLayout(new GridLayout(1,1));
		
		chatPanel =new JPanel();
		chatPanel.setLayout(new GridLayout(100,1));
		chatPanel.setBackground(Color.GRAY);
		
		js1=new JScrollPane(chatPanel);
		mainPanel.add(js1);

		typePanel=new JPanel();
		typePanel.setBounds(5,560,650,600);
		typePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		typePanel.setBackground(Color.RED);
		
		data=new JTextArea(5,35);
		data.setToolTipText("Type Mesage To Send...");
		send=new JButton("Send");
		send.setPreferredSize(new Dimension(100,30));
		send.addActionListener(this);
		exit=new JButton("Exit");
		exit.setPreferredSize(new Dimension(100,30));
		exit.addActionListener(this);

		typePanel.add(data);
		typePanel.add(send);
		typePanel.add(exit);

		f.add(mainPanel);
		f.add(typePanel);
		f.setSize(680,730);
		f.setLayout(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(true);
		f.setVisible(true);
	}
	static void receiveData()
	{
		try
		{
			while(true)
			{
				din=new DataInputStream(clientSocket.getInputStream());
				inputData=din.readUTF();
				JPanel pnl=new JPanel();
				pnl.setLayout(new FlowLayout(FlowLayout.LEFT));
				pnl.setBackground(Color.GRAY);
				JLabel lbl=new JLabel(inputData);
				lbl.setForeground(Color.GREEN);
				lbl.setFont(font);
				pnl.add(lbl);
				chatPanel.add(pnl);
				data.setText(" ");				
				if(inputData.equalsIgnoreCase("bye")&& outputData.equalsIgnoreCase("bye"))
				{
					System.out.println(outputData+" "+inputData);
					f.dispose();
					break;
				}
			}
			ss.close();
		}
		catch(IOException ie)
		{}
	}
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			if(ae.getSource()==send)
			{
				outputData=data.getText();
				JPanel pnl=new JPanel();
				pnl.setLayout(new FlowLayout(FlowLayout.RIGHT));
				pnl.setBackground(Color.GRAY);
				JLabel lbl=new JLabel(outputData);
				lbl.setFont(font);
				lbl.setForeground(Color.GREEN);
				pnl.add(lbl);
				chatPanel.add(pnl);
				dout=new DataOutputStream(clientSocket.getOutputStream());
				dout.writeUTF(outputData);
				data.setText("");
			}
			else if(ae.getSource()==exit)
			{
				f.dispose();
			}
		}
		catch(IOException ie){}
	}
	public static void main(String args[])
	{
		Server2 s = new Server2("Server");
		s.addComponents();
		Server2.receiveData();
	}
}
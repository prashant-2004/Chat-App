//Create Frame For Server to See the Sended and Received Message.

import java.util.Vector;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class Client2 implements ActionListener
{
	static Socket client;
	static DataInputStream din;
	DataOutputStream dout;
	static String inputData,outputData;
	static JFrame f;
	JScrollPane js;
	JButton send,exit;
	static JTextArea data;
	JLabel message;
	static JPanel chatPanel,typePanel,mainPanel;
	Vector<JLabel> chatting=new Vector<JLabel>(); 
	static Font font=new Font("calibri",Font.BOLD,30);
	
	Client2(String title)
	{
		f=new JFrame(title);

		try
		{
			client=new Socket("localhost",2222);
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
		
		js=new JScrollPane(chatPanel);
		mainPanel.add(js);

		typePanel=new JPanel();
		typePanel.setBounds(5,560,650,600);
		typePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		typePanel.setBackground(Color.GREEN);
		
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
		f.setLocation(680,5);
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
				din=new DataInputStream(client.getInputStream());
				inputData=din.readUTF();
				JPanel pnl=new JPanel();
				pnl.setBackground(Color.GRAY);
				pnl.setLayout(new FlowLayout(FlowLayout.LEFT));	
				JLabel lbl=new JLabel(inputData);
				lbl.setFont(font);
				lbl.setForeground(Color.GREEN);
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
			client.close();
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
				pnl.setBackground(Color.GRAY);
				pnl.setLayout(new FlowLayout(FlowLayout.RIGHT));
				JLabel lbl=new JLabel(outputData);
				lbl.setForeground(Color.GREEN);
				lbl.setFont(font);
				pnl.add(lbl);
				chatPanel.add(pnl);
				dout=new DataOutputStream(client.getOutputStream());
				dout.writeUTF(outputData);
				data.setText("");
			}	
			else if(ae.getSource()==exit)
			{
				f.dispose();
			}
		}
		catch(IOException ie)
		{}
	}
	public static void main(String args[])
	{
		Client2 c = new Client2("Client");
		c.addComponents();
		Client2.receiveData();
	}
}
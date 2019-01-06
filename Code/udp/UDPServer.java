package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.net.InetAddress;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc = null;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		try
		{
			pacSize = 1024;
			while(true)
			{
				pacData = new byte[pacSize];
				pac = new DatagramPacket(pacData, pacSize);
				recvSoc.setSoTimeout(30000);
				recvSoc.receive(pac);
				processMessage(new String(pac.getData()));
			}
		} catch (SocketException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try
		{
			msg = new MessageInfo(data.trim());
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(msg.messageNum == 0)
		{
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
			System.out.println("Buffer successfully initialised");
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		System.out.println("Message " + (msg.messageNum+1) + " received");

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == totalMessages - 1)
		{
			System.out.println("The final expected message has arrived");
			int missing_messages = 0;
			for(int i = 0; i < totalMessages; i++)
			{
				if(receivedMessages[i] != 1)
					{
						System.out.println("Message " + (i+1) + " was lost!");
						missing_messages++;
					}
			}
			System.out.println(missing_messages + " messages were lost in total!");
			System.out.println(msg.totalMessages + " messages were sent by the client!");
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try
		{
			recvSoc = new DatagramSocket(rp);
		} catch (SocketException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		try
		{
			System.out.println("Successfully instantiated server with IP address: ");
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		server.run();
	}

}

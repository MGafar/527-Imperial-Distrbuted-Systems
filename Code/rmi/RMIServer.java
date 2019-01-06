package rmi;

import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.RMISecurityManager;
import java.net.InetAddress;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private static int port = 1099;
	private static String hostname = "146.169.53.13";

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException
	{
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
			int missing_messages = 0;
			for(int i = 0; i < totalMessages; i++)
			{
				if(receivedMessages[i] != 1)
					{
						System.out.println("Message " + (i+1) + "was lost!");
						missing_messages++;
					}
			}
			System.out.println(missing_messages + " messages were lost in total!");
			System.out.println(msg.totalMessages + " messages were sent by the client!");
		}
	}


	public static void main(String[] args)
	{

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if(System.getSecurityManager() == null)
		{
			System.setSecurityManager(new RMISecurityManager());
			System.out.println("Security manager successfully initialised");
		} else
			System.out.println("A security manager already exists");

		// TO-DO: Instantiate the server class
		try
		{
			rmis = new RMIServer();
			System.out.println("Successfully instantiated server with IP address: ");
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		}catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// TO-DO: Bind to RMI registry
			String urlServer = new String("rmi://" + hostname + ":" + port + "/RMIServer");
    	rebindServer(port, rmis);
	}

	protected static void rebindServer(int port, RMIServer server)
	{

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		boolean already_created = false;
		try
		{
			LocateRegistry.createRegistry(port);
			System.out.println("Successfully created registry");
		} catch (Exception e)
		{
			already_created = true;
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		try
		{
			if(already_created)
			{
				System.out.println("Attempting to get exisiting registry");
				LocateRegistry.getRegistry(port);
			}

			// TO-DO:
			// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
			// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
			// expects different things from the URL field.

			Naming.rebind("RMIServer", server);
			System.out.println("Successfully rebinded registry");
			System.out.println("Server is operational!");
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

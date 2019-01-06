/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.RMISecurityManager;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	private static int port = 1234;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer

		// TO-DO: Log receipt of the message

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages

	}


	public static void main(String[] args) {

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
			RMIServer server = new RMIServer();
			System.out.println("Server successfully instantiated");
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

		// TO-DO: Bind to RMI registry

	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		Registry registry = null;
		try
		{
			registry = LocateRegistry.createRegistry(port);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			try
			{
				System.out.println("Unable to create registry, using getRegistry()");
				registry = LocateRegistry.getRegistry(port);
			} catch (Exception er)
			{
				System.out.println(e.getMessage());
			}
		}

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
	}
}

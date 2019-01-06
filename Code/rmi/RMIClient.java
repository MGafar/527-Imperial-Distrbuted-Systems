package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if(System.getSecurityManager() == null)
		{
			System.setSecurityManager(new RMISecurityManager());
			System.out.println("Security manager successfully initialised");
		} else
			System.out.println("A security manager already exists");

		// TO-DO: Bind to RMIServer
		boolean server_is_active = false;
		try
		{
			iRMIServer = (RMIServerI) Naming.lookup(urlServer);
			server_is_active = true;
		} catch (MalformedURLException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (NotBoundException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (RemoteException e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// TO-DO: Attempt to send messages the specified number of times
		if(server_is_active)
		{
			System.out.println("Attempting to pass message!");
			try
			{
				for(int i = 0; i < numMessages; i++)
				{
					MessageInfo message = new MessageInfo(numMessages, i);
					iRMIServer.receiveMessage(message);
				}
			} catch (RemoteException e)
			{
					System.out.println(e.getMessage());
					e.printStackTrace();
			} finally
			{
				System.exit(0);
			}
		}
	}
}

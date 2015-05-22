package Interfaz;

import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Sender implements Runnable 
{
	private final static String QUEUE_NAME = "CF";

	public static void main(String[] argv) throws java.io.IOException 
	{
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
		factory.setPort(5672);
		factory.setUsername("tu");
		factory.setPassword("tu");
		
		Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    String message = "hola hola";
	    //channel.queueDeclare("SC", false, false, false, null);
		
	    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
	    System.out.println(" [x] Sent '" + message + "'");
	    
	    channel.close();
    	connection.close();
  	}
	
	public Sender( )
	{
		
	}
	
	public boolean enviarMensaje(String mensaje) throws IOException
	{
		boolean ya = false;
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
		factory.setPort(5672);
		factory.setUsername("tu");
		factory.setPassword("tu");
		
		Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    //channel.queueDeclare("SC", false, false, false, null);
	    channel.basicPublish("", QUEUE_NAME, null, mensaje.getBytes());
	    
	    System.out.println(" [x] Sent '" + mensaje + "'");
	    channel.close();
    	connection.close();
    	return ya;
	}

	@Override
	public void run() 
	{
		try 
		{
			this.main(null);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
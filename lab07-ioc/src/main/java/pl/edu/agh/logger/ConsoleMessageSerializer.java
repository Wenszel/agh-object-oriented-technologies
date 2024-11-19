package pl.edu.agh.logger;


public class ConsoleMessageSerializer  implements IMessageSerializer
{
	
	public ConsoleMessageSerializer()
	{
	}

	@Override
	public void serializeMessage(String message) {
		System.out.println(message);
	}
	
	
	
}

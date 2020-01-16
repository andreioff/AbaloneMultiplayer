import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;



public class AbaloneClient {

	private Socket serverSock;
	private BufferedReader in;
	private BufferedWriter out;
	private AbaloneTUI view;
	
	
	public AbaloneClient() {
		view = new AbaloneTUI(this);
	}
}

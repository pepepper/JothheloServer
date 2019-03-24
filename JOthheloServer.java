import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class JOthheloServer{

	public static void main(String[] args) {
		try {
			ArrayList<Game> games=new ArrayList<Game>();
			ArrayList<Acception> queue=new ArrayList<Acception>();
			ServerSocket ss =new ServerSocket(45451);
			ss.setReuseAddress(true);
			while(true) {
				Socket sc=ss.accept();
				Acception temp=new Acception(sc, games);
				temp.start();
				queue.add(temp);
				for(Acception server:queue) {
					if(server.isAlive()==false)server=null;
				}
				queue.removeAll(Collections.singleton(null)); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}


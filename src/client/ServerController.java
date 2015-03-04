package client;

import java.util.HashMap;
import java.util.Iterator;

public class ServerController {
	private HashMap<String,String> map = new HashMap<String,String>();
	
	public synchronized String put(String name, String age) {
		String person = name + "," + age;
		String response = map.put(name, person);
		if(response==null) {
			response = person + " lagrad";
		} else {
			response += " ersatt med " + person;
		}
		return response;
	}
	
	public synchronized String get(String name) {
		String response = map.get(name);
		if(response==null) {
			response = name + " är okänd";
		}
		return response;
	}

	public synchronized String list() {
		Iterator<String> iter = map.keySet().iterator();
		String response = "";
		while(iter.hasNext()) {
			response += map.get(iter.next()) + "\n";
		}
		return response;
	}
	
	public synchronized String remove(String name) {
		String response = map.remove(name);
		if(response==null) {
			response = name + " är okänd";
		}
		return response;
	}
	
	public static void main(String[] args) {
		ServerController controller = new ServerController();
		AgeServerA serverA = new AgeServerA(controller,3440);
	}
}

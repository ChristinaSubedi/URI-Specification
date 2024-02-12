package uri.implementation;

import uri.Host;

import java.util.regex.*;

// TODO implement this class or another implementation of Host
public class HostImplementation implements Host {

	private String hostnotip;


	public HostImplementation(String host) {
		
		// TODO implement this
		this.hostnotip=host;
		}
	@Override
	public String toString() {
		return this.hostnotip;
	}

}

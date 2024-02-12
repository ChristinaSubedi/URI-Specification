package uri.implementation;

import uri.IPv4Address;

// TODO implement this class or another Implementation of IPv4Address
public class IPv4AddressImplementation extends HostImplementation implements IPv4Address {

	private byte[] iparray=new byte [4];
	private String[] iparraystring;
	private String ipstring;

	public IPv4AddressImplementation(String host) {
		super(host);
		this.ipstring=host;
		iparraystring=(host.split("\\."));
		int counter=0;
		for (String ipInString:iparraystring){
			int temp=Integer.parseInt(ipInString);
			iparray[counter]=(byte) temp;
			counter +=1;



		}
	}

	@Override
	public byte[] getOctets() {
		
		return iparray;
	}

	@Override
	public String toString() {
		return this.ipstring;
	}

}

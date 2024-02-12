package uri.implementation;

import uri.Host;
import uri.Uri;
import java.util.regex.*;


// TODO implement this class or another implementation of Uri
public class UriImplementation implements Uri {

	public String scheme;
	public String query;
	public String userinfo;
	public String host;
	public String path; 

	public Boolean queryfound;
	public Boolean userinfofound;
	public Boolean hostfound;
	public Boolean pathfound;

	@Override
	public String getScheme() {
		return this.scheme;
	}

	@Override
	public String getQuery() {
		if (queryfound){
			return this.query;
		}
		return null;
	}

	@Override
	public String getUserInfo() {
		if (userinfofound){
			return this.userinfo;
		}
		return null;
	}

	@Override
	public Host getHost() {
		
		if (hostfound){
			String [] octetsplit;
			Boolean octetfound=true;
			while (true){
				if (!(Pattern.matches("([\\.]|[0-9])*", host))){

					octetfound=false;
					break;
				} else {
					octetsplit=host.split("\\.");
	
					if (octetsplit.length!=4){

						octetfound=false;
						break;
					} else {
						for (String i:octetsplit){
							if (i.length()>3){
								octetfound=false;
	
								break;
							} else {
								int temp=Integer.parseInt(i);
								if (temp>255){
									octetfound=false;
									break;
								}
							}
						}
					}
				}
				
				break;
			}

			if (octetfound){
				IPv4AddressImplementation result=new IPv4AddressImplementation(host);
				return result;
				} else {
					HostImplementation result=new HostImplementation(host);
					return result;
				}
			

		} else {
			HostImplementation result=new HostImplementation("");
			return result;
		}
	}

	@Override
	public String getPath() {
		if (pathfound){
			return this.path;
		} else {
			return "";
		}
	}

}

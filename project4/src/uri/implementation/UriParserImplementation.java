package uri.implementation;

import uri.Uri;
import uri.UriParser;

import java.util.regex.*;

public class UriParserImplementation implements UriParser {
	private String uristring;
	private String scheme;
	private String userinfo;
	private String host;
	private String query;
	private String path;
	Boolean userinfofound = false;
	Boolean hostfound = false;
	Boolean queryfound = false;
	Boolean pathfound = false;

	public void setParser(String uristring) {
		this.uristring = uristring;
	}

	@Override
	public Uri parse() {

		while (true) {
			String[] schemesplit;

			if (!Pattern.matches("[a-zA-Z][\\w]*:[//][//][^~]*", this.uristring)) {
				return null;
			} else {

				schemesplit = (this.uristring).split("://", 2);
				this.scheme = schemesplit[0];
				if (schemesplit[1] == "") {
					break;
				}

			}

			// Check for userinfo

			String[] userinfosplit;
			String afterscheme = schemesplit[1];
			String afteruserinfo;

			if (Pattern.matches("@.*", afterscheme)) {
				userinfofound = true;
				this.userinfo = "";

				afteruserinfo = afterscheme.replaceFirst("(?:@)+", "");

				if (afteruserinfo == "") {

					break;
				}

			} else if (Pattern.matches(".*@.*", afterscheme)) {
				userinfofound = true;

				userinfosplit = (afterscheme).split("@");
				if (userinfosplit.length > 2) {

					return null;
				} else {
					this.userinfo = userinfosplit[0];
					if (!Pattern.matches("((\\w|[\\.]|[\\:]|\\%[0-9A-Fa-f]{2})*)", this.userinfo)) {

						return null;
					}

					if (userinfosplit.length == 1) {

						break;
					}

					afteruserinfo = userinfosplit[1];

				}

			} else {

				afteruserinfo = afterscheme;
			}

			String[] hostsplit;
			String afterhost;
			String afterpath;

			if (Pattern.matches("[^([\\/]|[\\?])].*", afteruserinfo)) {
				this.hostfound = true;
				// there is a host
				if (afteruserinfo.contains("/")) {
					this.pathfound = true;
					// there is a path
					hostsplit = afteruserinfo.split("/", 2);
					this.host = hostsplit[0];
					if (hostsplit.length == 2) {

						afterhost = hostsplit[1];
						findpath("/" + afterhost);
					} else {
						this.path = "/";

					}
				} else if (afteruserinfo.contains("\\?")) {
					this.queryfound = true;
					hostsplit = afteruserinfo.split("\\?", 2);
					this.host = hostsplit[0];
					if (hostsplit.length == 2) {
						this.queryfound = true;
						afterpath = hostsplit[1];
						this.query = afterpath;

					} else {
						this.query = "";

					}

				} else {
					this.host = afteruserinfo;

				}

			} else {

				afterhost = afteruserinfo;
				if (afterhost.startsWith("/")) {
					this.pathfound = true;
					findpath(afterhost);
				} else {
					afterpath = afterhost;
					this.queryfound = true;
					this.query = afterhost.replaceFirst("\\?", "");
				}

			}

			break;

		}
		UriImplementation result = new UriImplementation();

		result.scheme = this.scheme;

		if (userinfofound) {
			result.userinfofound = true;
			result.userinfo = this.userinfo;
		} else {
			result.userinfofound = false;
		}

		if (this.hostfound) {
			if (!Pattern.matches("((\\w|[\\.]|\\%[0-9A-Fa-f]{2})*)", this.host)) {
				return null;
			} else {
				result.hostfound = true;
				result.host = this.host;

			}
		} else {
			result.hostfound = false;
		}

		if (this.pathfound) {
			if (!Pattern.matches("((\\w|[\\.]|[\\/]|\\%[0-9A-Fa-f]{2})*)", this.path)) {
				return null;
			} else {
				result.pathfound = true;
				result.path = this.path;

			}

		} else {
			result.pathfound = false;
		}

		if (this.queryfound) {
			if (!Pattern.matches("((\\w|[\\.]|[&]|[=]|\\%[0-9A-Fa-f]{2})*)", this.query)) {
				return null;
			} else {
				result.queryfound = true;
				result.query = this.query;
			}

		} else {
			result.queryfound = false;
		}

		return result;

	}

	private void findpath(String input) {
		this.pathfound = true;
		String[] pathsplit;

		if (!(input.contains("?"))) {
			this.path = input;
		} else {
			this.queryfound = true;
			pathsplit = input.split("\\?", 2);
			this.path = pathsplit[0];
			if (pathsplit.length == 1) {
				this.query = "";
			} else {
				this.query = pathsplit[1];
			}

		}

	}

}

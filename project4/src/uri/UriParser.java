package uri;

/**
 * A parser for URIs according to the specification in {@link Uri}.
 *
 * This interface will be overwritten with the original version when testing.
 */
public interface UriParser {

	/**
	 * @return the parsed {@link Uri} instance or null when the string is not a
	 *         valid {@link Uri} with regards to the grammar given in
	 *         {@link Uri}
	 */
	public Uri parse();

}

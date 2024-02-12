package uri;

/**
 * Represents a host as identified by the non-terminal "host" in the {@link Uri} grammar.
 *
 * This interface will be overwritten with the original version when testing.
 */
public interface Host {

	/**
	 * @return the parsed "host" name or a normalized version for {@link IPv4Address}
	 */
	@Override
	public String toString();

}

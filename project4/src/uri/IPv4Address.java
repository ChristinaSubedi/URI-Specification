package uri;

/**
 * An IPv4 address represented via byte octets.
 *
 * This interface will be overwritten with the original version when testing.
 */
public interface IPv4Address extends Host {

	/**
	 * @return a length 4 byte array of the IPs components, bytes must be
	 *         interpreted as unsigned for correct values
	 */
	public byte[] getOctets();

	/**
	 * @return "a.b.c.d" where a-d are numbers from 0 to 255 without leading
	 *         zeros
	 */
	@Override
	public String toString();

}

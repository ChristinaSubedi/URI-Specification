package uri;

/**
 *
 * Represents a fully encoded URI for a subset and variant of RFC 3986.
 * <p>
 * The following grammar defines the URIs represented by this class and parsed
 * by {@link UriParser}. Each rule is of the form
 * {@code non-terminal = expression}. Expressions are constructed with:
 * <ul>
 * <li>literals enclosed by {@code "} quotation marks.</li>
 * <li>alternatives separated by slashes ({@code /}).<br>
 * The leftmost has highest priority and the rightmost alternative the least.
 * </li>
 * <li>groups enclosed by parentheses.</li>
 * <li>optional groups enclosed by square brackets ([]).</li>
 * <li>literal ranges such as {@code "0"-"4"}, which is equivalent to
 * {@code ("0"/"1"/"2"/"3"/"4")}.</li>
 * <li>0 or more repetitions of an expression marked by an asterisk {@code *}.
 * </li>
 * </ul>
 *
 * <p>
 * Semicolons ({@code ;}) indicate a comment for the rest of the line.
 *
 * See <a href=https://de.wikipedia.org/wiki/Angereicherte_Backus-Naur-Form>
 * wikipedia (German)</a> or
 * <a href=https://en.wikipedia.org/wiki/Augmented_Backus%E2%80%93Naur_form>
 * wikipedia (English)</a> for more explanation. Note that we use "a"-"z" and
 * "0"-"9" and similar ranges here, which are not an official part of the ABNF
 * RFC, but their meaning remains clear.
 *
 * <pre>
 * {@code
 * URI           = scheme ":" hierarchical [ "?" query ]
 *
 * hierarchical  = "//" authority path
 *
 * scheme        = ALPHA *( ALPHA / DIGIT )
 *
 * authority     = [ userinfo "@" ] host
 * userinfo      = *( pchar / ":" )
 * host          = IPv4address / reg-name
 *
 * IPv4address   = dec-octet "." dec-octet "." dec-octet "." dec-octet
 *
 * dec-octet     = ["0" ["0"]] DIGIT      ; 000-009 with optional leading zeros
 *               / ["0"] "1"-"9" DIGIT    ; 010-099
 *               / "1" DIGIT DIGIT        ; 100-199
 *               / "2" "0"-"4" DIGIT      ; 200-249
 *               / "25" "0"-"5"           ; 250-255
 *
 * reg-name      = *pchar
 *
 * path          = *( "/" *pchar )        ; begins with "/" or is empty
 *
 * query         = *( pchar / "&" / "=" )
 *
 * pchar         = unreserved / pct-encoded
 * unreserved    = ALPHA / DIGIT / "."
 * pct-encoded   = "%" HEXDIGIT HEXDIGIT
 *
 * ALPHA         = "A"-"Z" / "a"-"z"
 * DIGIT         = "0"-"9"
 * HEXDIGIT      = DIGIT / "A"-"F" / "a"-"f"
 * }
 * </pre>
 *
 *
 * This interface will be overwritten with the original version when testing.
 */
public interface Uri {

	/**
	 * @return the result of parsing the non-terminal "scheme" in the grammar,
	 *         must not be {@code null}
	 */
	public String getScheme();

	/**
	 * @return the result of parsing the non-terminal "query" in the grammar or
	 *         {@code null} if the URI did not contain a query part
	 */
	public String getQuery();

	/**
	 * @return the result of parsing the non-terminal "userinfo" in the grammar
	 *         or {@code null} if the URI did not contain user information
	 */
	public String getUserInfo();

	/**
	 * @return the {@link Host} (non-terminal "reg-name") or {@link IPv4Address}
	 *         (non-terminal "IPv4address", has precedence over "reg-name"),
	 *         must not be {@code null}
	 */
	public Host getHost();

	/**
	 * @return the result of parsing the non-terminal "path" in the grammar,
	 *         must not be {@code null}
	 */
	public String getPath();

}

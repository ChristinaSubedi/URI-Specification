package uri.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.beans.Transient;

import org.junit.Test;

import uri.Host;
import uri.Uri;
import uri.IPv4Address;
import uri.UriParserFactory;

/**
 * This class provides a very simple example of how to write tests for this
 * project.
 * You can implement your own tests within this class or any other class within
 * this package.
 * Tests in other packages will not be run and considered for completion of the
 * project.
 */
public class SimpleTests {

	/**
	 * Helper function to determine if the given host is an instance of
	 * {@link IPv4Address}.
	 *
	 * @param host the host
	 * @return {@code true} if the host is an instance of {@link IPv4Address}
	 */
	public boolean isIPv4Address(Host host) {
		return host instanceof IPv4Address;
	}

	/**
	 * Helper function to retrieve the byte array representation of a given host
	 * which must be an instance of {@link IPv4Address}.
	 *
	 * @param host the host
	 * @return the byte array representation of the IPv4 address
	 */
	public byte[] getIPv4Octets(Host host) {
		if (!isIPv4Address(host))
			throw new IllegalArgumentException("host must be an IPv4 address");
		return ((IPv4Address) host).getOctets();
	}

	@Test
	public void testNonNull() {
		assertNull(UriParserFactory.create(null));
		assertNotNull(UriParserFactory.create("scheme://").parse());
	}

	@Test
	public void testNegativeSimple() {
		assertNull(UriParserFactory.create("").parse());
	}

	@Test
	public void testIPv4AddressSimple() {
		Host host = UriParserFactory.create("scheme://1.2.3.4").parse().getHost();
		assertTrue("host must be an IPv4 address", isIPv4Address(host));
	}

	@Test
	public void testScheme() {
		assertNotNull(UriParserFactory.create("scheme://g::ht::T.@").parse());
		assertNotNull(UriParserFactory.create("scheme://host///").parse());
		assertNull("The url is empty", UriParserFactory.create("").parse());
		assertNull("The scheme started with a digit", UriParserFactory.create("9scheme://1.2.3.4").parse());
		assertNull("The scheme has two .//", UriParserFactory.create("9scheme://1.2.3.4.//").parse());
		assertNull("The scheme has an illegal character", UriParserFactory.create("s^cheme://1.2.3.4").parse());
		assertNull("The scheme has an illegal character only", UriParserFactory.create("^://1.2.3.4").parse());
		assertNull("The scheme has an illegal character", UriParserFactory.create("s@cheme://1.2.3.4").parse());
		assertNull("The scheme did not end with a colon", UriParserFactory.create("scheme//1.2.3.4").parse());
		Uri uri = UriParserFactory.create("htt9p://").parse();
		assertEquals("htt9p", uri.getScheme());
		assertEquals("",uri.getHost().toString());
		assertEquals("", uri.getPath());
		assertNull(uri.getQuery());
		assertNull(uri.getUserInfo());
	}

	@Test
	public void testHierarchyEmpty() {
		assertNull("The hierarchy has space", UriParserFactory.create("scheme:// ").parse());
		assertNull("The scheme started with a digit", UriParserFactory.create("9scheme://").parse());
		assertNull("The hierarchy did not have backslash", UriParserFactory.create("scheme:1.2.3.4").parse());
		assertNull("The scheme has a single backlash only", UriParserFactory.create("scheme:/1.2.3.4").parse());
		Uri uri = UriParserFactory.create("http://").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("",uri.getHost().toString());
		assertEquals("", uri.getPath());
		assertNull(uri.getQuery());
		assertNull(uri.getUserInfo());

	}

	@Test
	public void testHierarchyUserInfoColon() {
		// assertNull("The userinfo is supposed to be null",
		// UriParserFactory.create("scheme://").parse());
		assertNull("The userinfo has colon space", UriParserFactory.create("scheme://: :").parse());
		assertNull("The userinfo had an invalid character", UriParserFactory.create("scheme://:^llo@").parse());
		assertNull("The scheme started with a digit", UriParserFactory.create("9scheme://:").parse());

		assertEquals("The userinfo is supposed to be empty", "",
				UriParserFactory.create("scheme://@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to have 1 colon", ":",
				UriParserFactory.create("scheme://:@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to have 2 colons", "::",
				UriParserFactory.create("scheme://::@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to have 3 colons", ":::",
				UriParserFactory.create("scheme://:::@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to have 3 colons", ":a.%76:",
				UriParserFactory.create("scheme://:a.%76:@").parse().getUserInfo());

		Uri uri = UriParserFactory.create("http://::@").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("The user info was ::", "::", uri.getUserInfo());
		assertEquals("",uri.getHost().toString());
		assertEquals("", uri.getPath());
		assertNull(uri.getQuery());

		Uri uri2 = UriParserFactory.create("http://:xy%56@").parse();
		assertEquals("http", uri2.getScheme());
		assertEquals("The user info was :", ":xy%56", uri2.getUserInfo());
		assertEquals("",uri2.getHost().toString());
		assertEquals("", uri2.getPath());
		assertNull(uri2.getQuery());
	}

	@Test
	public void testHierarchyUserInfoHex() {
		//assertNull("The userinfo has no digit in hex", UriParserFactory.create("scheme://ab%43").parse());
		assertNull("The userinfo is has 1 digit in hex", UriParserFactory.create("scheme://%3").parse());
		assertNull("The userinfo has invalid char in hex", UriParserFactory.create("scheme://%2^@").parse());
		assertNull("The scheme started with a digit", UriParserFactory.create("9scheme://%23%25").parse());

		assertNull("The userinfo has no digit in 2nd hex", UriParserFactory.create("scheme://%22%@").parse());
		assertNull("The userinfo has 1 right 1 incomplete digit in hex",
				UriParserFactory.create("scheme://%22%2@").parse());
		assertNull("The userinfo has invalid digit in hex", UriParserFactory.create("scheme://%22%2g@").parse());

		assertEquals("The userinfo is supposed to be %52", "%ab.:%52",
				UriParserFactory.create("scheme://%ab.:%52@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be %52%56", "%52%56",
				UriParserFactory.create("scheme://%52%56@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be %52%5a", "%52%5a",
				UriParserFactory.create("scheme://%52%5a@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be %ff", "%ff",
				UriParserFactory.create("scheme://%ff@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be %aa", "%aaxy:",
				UriParserFactory.create("scheme://%aaxy:@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be %fa", "1%fa",
				UriParserFactory.create("scheme://1%fa@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be %0f", "GH%0f",
				UriParserFactory.create("scheme://GH%0f@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be %ff%9a", "1.:O%ff%9al",
				UriParserFactory.create("scheme://1.:O%ff%9al@").parse().getUserInfo());

		Uri uri2 = UriParserFactory.create("http://%22@").parse();
		assertEquals("http", uri2.getScheme());
		assertEquals("The user info was %22", "%22", uri2.getUserInfo());
		assertEquals("",uri2.getHost().toString());
		assertEquals("", uri2.getPath());
		assertNull(uri2.getQuery());

		Uri uri = UriParserFactory.create("http://%22:A%2a.@").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("The user info was %22%2a", "%22:A%2a.", uri.getUserInfo());
		assertEquals("",uri.getHost().toString());
		assertEquals("", uri.getPath());
		assertNull(uri.getQuery());

	}

	@Test
	public void testHierarchyUserInfoUnreserved() {
		assertNull("The userinfo has illegal character", UriParserFactory.create("scheme://^@").parse());
		assertNull("The userinfo has colon and hex", UriParserFactory.create("scheme://b&23@").parse());
		assertNull("The userinfo has illegal character", UriParserFactory.create("scheme://y%5z@").parse());
		assertNull("The userinfo has illegal character", UriParserFactory.create("scheme://xx#@").parse());
		assertNull("The scheme started with a digit", UriParserFactory.create("9scheme://a2b.@").parse());

		assertEquals("The userinfo is supposed to be abc", "abc%54",
				UriParserFactory.create("scheme://abc%54@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be ab.c", "ab.c",
				UriParserFactory.create("scheme://ab.c@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be a2bc", "ab2c",
				UriParserFactory.create("scheme://ab2c@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be .a2bc", ".ab2c",
				UriParserFactory.create("scheme://.ab2c@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be a2bc", "ab2c",
				UriParserFactory.create("scheme://ab2c@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be 23", "23",
				UriParserFactory.create("scheme://23@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be 2.3", "2.3::",
				UriParserFactory.create("scheme://2.3::@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be .", ".%Af",
				UriParserFactory.create("scheme://.%Af@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be ..", ".:J.",
				UriParserFactory.create("scheme://.:J.@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be a", "a",
				UriParserFactory.create("scheme://a@").parse().getUserInfo());
		assertEquals("The userinfo is supposed to be 6", "6",
				UriParserFactory.create("scheme://6@").parse().getUserInfo());

		Uri uri2 = UriParserFactory.create("http://ab.:@").parse();
		assertEquals("http", uri2.getScheme());
		assertEquals("The user info was ab.", "ab.:", uri2.getUserInfo());
		assertEquals("",uri2.getHost().toString());
		assertEquals("", uri2.getPath());
		assertNull(uri2.getQuery());

		Uri uri = UriParserFactory.create("http://a23%5F:t@").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("The user info was a23", "a23%5F:t", uri.getUserInfo());
		assertEquals("",uri.getHost().toString());
		assertEquals("", uri.getPath());
		assertNull(uri.getQuery());

		Uri uri3 = UriParserFactory.create("http://V.23@").parse();
		assertEquals("http", uri3.getScheme());
		assertEquals("The user info was .23", "V.23", uri3.getUserInfo());
		assertEquals("",uri3.getHost().toString());
		assertEquals("", uri3.getPath());
		assertNull(uri3.getQuery());

	}

	@Test
	public void testHostUnreserved() {

		// Empty UserInfo
		assertNull("The host has illegal character", UriParserFactory.create("scheme://^").parse());
		assertNull("The Host has colon and unreserved", UriParserFactory.create("scheme://:a").parse());
		assertNull("The Host has colon", UriParserFactory.create("scheme://:a%42").parse());
		assertNull("The Host has illegal character", UriParserFactory.create("scheme://y^y").parse());
		assertNull("The Host has illegal character", UriParserFactory.create("scheme://xx#").parse());
		assertNull("The scheme started with a digit", UriParserFactory.create("9scheme://a2b.").parse());

		// assertEquals("The Host is supposed to be abc", "abc",
		// UriParserFactory.create("scheme://abc").parse().getHost());
		assertEquals("The Host is supposed to be ab.c", "ab.%43c",
				UriParserFactory.create("scheme://ab.%43c").parse().getHost().toString());
		assertEquals("The Host is supposed to be a2bc", "ab552c",
				UriParserFactory.create("scheme://ab552c").parse().getHost().toString());
		assertEquals("The Host is supposed to be .a2bc", ".ab%552c",
				UriParserFactory.create("scheme://.ab%552c").parse().getHost().toString());
		assertEquals("The Host is supposed to be a2bc", "1.2.3.4",
				UriParserFactory.create("scheme://1.2.3.4").parse().getHost().toString());
		assertEquals("The Host is supposed to be 23", "255.234.255.2",
				UriParserFactory.create("scheme://255.234.255.2").parse().getHost().toString());
				assertEquals("The Host is supposed to be 23", "255.234.256.2",
				UriParserFactory.create("scheme://255.234.256.2").parse().getHost().toString());
		assertEquals("The Host is supposed to be 2.3", "2.3",
				UriParserFactory.create("scheme://2.3").parse().getHost().toString());
		assertEquals("The Host is supposed to be .", ".",
				UriParserFactory.create("scheme://.").parse().getHost().toString());
		assertEquals("The Host is supposed to be ..", ".aXYz.",
				UriParserFactory.create("scheme://.aXYz.").parse().getHost().toString());
		assertEquals("The Host is supposed to be a", "a",
				UriParserFactory.create("scheme://a").parse().getHost().toString());
		assertEquals("The Hostis supposed to be 6", "6",
				UriParserFactory.create("scheme://6").parse().getHost().toString());


		// Empty UserInfo
		Uri uri2 = UriParserFactory.create("http://ab.").parse();
		assertEquals("http", uri2.getScheme());
		assertEquals("Host was ab.", "ab.", uri2.getHost().toString());
		assertNull(uri2.getUserInfo());
		assertEquals("", uri2.getPath());
		assertNull(uri2.getQuery());

		Uri uri = UriParserFactory.create("http://a23").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("Host was a23", "a23", uri.getHost().toString());
		assertNull(uri.getUserInfo());
		assertEquals("", uri.getPath());
		assertNull(uri.getQuery());

		Uri uri3 = UriParserFactory.create("http://x.23").parse();
		assertEquals("http", uri3.getScheme());
		assertEquals("Host was a.23", "x.23", uri3.getHost().toString());
		assertNull(uri3.getUserInfo());
		assertEquals("", uri3.getPath());
		assertNull(uri3.getQuery());

		// Userinfo with valid characters and valid host
		Uri uri4 = UriParserFactory.create("http://%55%9f@x.2%fA3").parse();
		assertEquals("http", uri4.getScheme());
		assertEquals("Host was a.23", "x.2%fA3", uri4.getHost().toString());
		assertEquals("The user info was %55%9f", "%55%9f", uri4.getUserInfo());
		assertEquals("", uri4.getPath());
		assertNull(uri4.getQuery());

		Uri uri5 = UriParserFactory.create("http://z9.a@x.23").parse();
		assertEquals("http", uri5.getScheme());
		assertEquals("Host was a.23", "x.23", uri5.getHost().toString());
		assertEquals("The user info was z9.a", "z9.a", uri5.getUserInfo());
		assertEquals("", uri4.getPath());
		assertNull(uri4.getQuery());

	}

	@Test
	public void testHostHex() {

		// Empty UserInfo
		// assertNull("The host has is too long",
		// UriParserFactory.create("scheme://%9aa").parse());
		assertNull("The Host has a g", UriParserFactory.create("scheme://%9g%5f").parse());
		assertNull("The Host has illegal character", UriParserFactory.create("scheme://%23%33%9z").parse());
		assertNull("The Host has short hex", UriParserFactory.create("scheme://%4%5aa%6e").parse());
		assertNull("The scheme started with a digit", UriParserFactory.create("9scheme://a2b.").parse());

		assertEquals("The Host is supposed to be %5c", "%5cabxY",
				UriParserFactory.create("scheme://%5cabxY").parse().getHost().toString());
		assertEquals("The Host is supposed to be %9a3c", "%9a.%3c",
				UriParserFactory.create("scheme://%9a.%3c").parse().getHost().toString());
		assertEquals("The Host is supposed to be %10", "%10",
				UriParserFactory.create("scheme://%10").parse().getHost().toString());
		assertEquals("The Host is supposed to be %51%6c%4a", "%51%6c%4a",
				UriParserFactory.create("scheme://%51%6c%4a").parse().getHost().toString());
		assertEquals("The Host is supposed to be %42%90", "%42%90",
				UriParserFactory.create("scheme://%42%90").parse().getHost().toString());
		assertEquals("The Host is supposed to be %aa%4f", "%aa%4f",
				UriParserFactory.create("scheme://%aa%4f").parse().getHost().toString());
		assertEquals("The Host is supposed to be %c4", "%C4",
				UriParserFactory.create("scheme://%C4").parse().getHost().toString());
		assertEquals("The Host is supposed to be %cc%88%5c", "%ccllab%88%5c",
				UriParserFactory.create("scheme://%ccllab%88%5c").parse().getHost().toString());
		assertEquals("The Host is supposed to be %d7%ac%50", "%d7AZ98.%ac%50",
				UriParserFactory.create("scheme://%d7AZ98.%ac%50").parse().getHost().toString());



		// Empty UserInfo
		Uri uri2 = UriParserFactory.create("http://ZZ.l%3a%5c").parse();
		assertEquals("http", uri2.getScheme());
		assertEquals("Host was %3a%5c", "ZZ.l%3a%5c", uri2.getHost().toString());
		assertNull(uri2.getUserInfo());
		assertEquals("", uri2.getPath());
		assertNull(uri2.getQuery());


		Uri uri = UriParserFactory.create("http://").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("Host was dc%af", "", uri.getHost().toString());
		assertNull(uri.getUserInfo());
		assertEquals("", uri.getPath());
		assertNull(uri.getQuery());

		Uri uri3 = UriParserFactory.create("http://%55%20").parse();
		assertEquals("http", uri3.getScheme());
		assertEquals("Host was %55%20", "%55%20", uri3.getHost().toString());
		assertNull(uri3.getUserInfo());
		assertEquals("", uri3.getPath());
		assertNull(uri3.getQuery());

		// Userinfo with valid characters and valid hex
		Uri uri4 = UriParserFactory.create("http://%55%9f@%af%34%55").parse();
		assertEquals("http", uri4.getScheme());
		assertEquals("Host was %af%34%55", "%af%34%55", uri4.getHost().toString());
		assertEquals("The user info was %55%9f", "%55%9f", uri4.getUserInfo());
		assertEquals("", uri4.getPath());
		assertNull(uri4.getQuery());

	}

	@Test
	public void testHostOctet() {

		// Empty UserInfo Correct
		Host host = UriParserFactory.create("scheme://1.255.254.255").parse().getHost();
		assertTrue("host must be an IPv4 address", (isIPv4Address(host)));

		Host host2 = UriParserFactory.create("scheme://0.25.5.255").parse().getHost();
		assertTrue("host must be an IPv4 address", (isIPv4Address(host2)));

		Host host3 = UriParserFactory.create("scheme://1.255.009.255").parse().getHost();
		assertTrue("host must be an IPv4 address", (isIPv4Address(host3)));

		Host host4 = UriParserFactory.create("scheme://1.255.009.05").parse().getHost();
		assertTrue("host must be an IPv4 address", (isIPv4Address(host4)));

		Host nothost = UriParserFactory.create("scheme://1.255.254.256").parse().getHost();
		assertTrue("host must be an IPv4 address", !(isIPv4Address(nothost)));

		Host nothost2 = UriParserFactory.create("scheme://1.255.254.0000").parse().getHost();
		assertTrue("host must be an IPv4 address", !(isIPv4Address(nothost2)));

		Host nothost3 = UriParserFactory.create("scheme://1.255.254.255.1").parse().getHost();
		assertTrue("host must be an IPv4 address", !(isIPv4Address(nothost3)));

		Host nothost4 = UriParserFactory.create("scheme://1.255.254").parse().getHost();
		assertTrue("host must be an IPv4 address", !(isIPv4Address(nothost4)));

		Host nothost5 = UriParserFactory.create("scheme://abcde").parse().getHost();
		assertTrue("host must be an IPv4 address", !(isIPv4Address(nothost5)));

		

		Host host5 = UriParserFactory.create("scheme://1.255.254.255").parse().getHost();
		byte [] octet5=getIPv4Octets(host5);
		assertEquals((byte) 1, octet5[0]);
		assertEquals((byte) 255, octet5[1]);
		assertEquals((byte) 254, octet5[2]);
		assertEquals((byte) 255, octet5[3]);
		
		

		Host host6 = UriParserFactory.create("scheme://1.0.0.0").parse().getHost();
		byte [] octet6=getIPv4Octets(host6);
		assertEquals((byte) 1, octet6[0]);
		assertEquals((byte) 0, octet6[1]);
		assertEquals((byte) 0, octet6[2]);
		assertEquals((byte) 0, octet6[3]);

		// Has UserInfo Incorrect

	
		assertNull("The userinfo has illegal character", UriParserFactory.create("scheme://=@1.2.3.4").parse());
		
		// Contains Invalid Userinfo in hex, valid IP Host
		assertNull("The Hex is too long", UriParserFactory.create("scheme://%2za@%3.44.56.92").parse());
		assertNull("The hex has unreserved and hex", UriParserFactory.create("scheme://3tb%l23@1.1.1.01").parse());
		assertNull("The hex has colon, hex and unreserved", UriParserFactory.create("scheme://:a%4j2@3.2.1.0").parse());

	}

	@Test

	public void testPathHex() {

		// Empty UserInfo
		assertNull("The Host has a g", UriParserFactory.create("scheme:///%9g%5f").parse());
		assertNull("The Host has colon ", UriParserFactory.create("scheme:///%:23").parse());
		assertNull("The Host has illegal character", UriParserFactory.create("scheme:///%23%33%9z").parse());
		assertNull("The Host has long hex", UriParserFactory.create("scheme:///%44%5ka%6e").parse());
		assertNull("The Host has short hex", UriParserFactory.create("scheme:///%4%5aa%6e").parse());
		assertNull("The scheme started with a digit", UriParserFactory.create("9scheme:///a2b.").parse());

		assertEquals("The Path is supposed to be %5c", "/Ab./%5c",
				UriParserFactory.create("scheme:///Ab./%5c").parse().getPath());
		assertEquals("The Path is supposed to be %10", "/ay//%10",
				UriParserFactory.create("scheme:///ay//%10").parse().getPath());
		assertEquals("The Path is supposed to be %51%6c%4a", "/%51%6c%4a",
				UriParserFactory.create("scheme:///%51%6c%4a").parse().getPath());
		assertEquals("The Path is supposed to be %42%90", "/%42%90",
				UriParserFactory.create("scheme:///%42%90").parse().getPath());
		assertEquals("The Path is supposed to be %aa%4f", "/%aa%4f",
				UriParserFactory.create("scheme:///%aa%4f").parse().getPath());
		assertEquals("The Path is supposed to be %c4", "/%c4",
				UriParserFactory.create("scheme:///%c4").parse().getPath());
		assertEquals("The Path is supposed to be %cc%88%5c", "/%cc%88%5c",
				UriParserFactory.create("scheme:///%cc%88%5c").parse().getPath());
		assertEquals("The Path is supposed to be %d7%ac%50", "/%d7%ac%50",
				UriParserFactory.create("scheme:///%d7%ac%50").parse().getPath());


		// Userinfo with valid characters and valid hex, but empty host


		// Userinfo with valid characters and valid hex

		Uri uri = UriParserFactory.create("http://xy37@255.99.1.3/%0a/%AF").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("Path was /%0a", "/%0a/%AF", uri.getPath());
		assertEquals("The user info was xyz37", "xy37", uri.getUserInfo());
		assertEquals("255.99.1.3", uri.getHost().toString());
		assertNull(uri.getQuery());

		Uri uri3 = UriParserFactory.create("http://%39%65@%35%ff/%cf%8b%99").parse();
		assertEquals("http", uri3.getScheme());
		assertEquals("Host was %35%ff", "%35%ff", uri3.getHost().toString());
		assertEquals("Path was /%cf%8b%99", "/%cf%8b%99", uri3.getPath());
		assertEquals("The user info was %39%65", "%39%65", uri3.getUserInfo());
		assertNull(uri3.getQuery());
	}

	@Test
	public void PathUnreserved() {

		// Empty UserInfo
		assertNull("The path has illegal character", UriParserFactory.create("scheme:///=.").parse());

		assertNull("The path has colon and hex", UriParserFactory.create("scheme:///3b%33&").parse());
		assertNull("The path has colon, hex and unreserved", UriParserFactory.create("scheme:///%bt42").parse());
		assertNull("The path has illegal character", UriParserFactory.create("scheme:///y+y").parse());

		assertEquals("The Host is supposed to be abc", "/abc//Y",
				UriParserFactory.create("scheme:///abc//Y").parse().getPath());
		assertEquals("The Host is supposed to be ab.c", "/ab.c%43%AC",
				UriParserFactory.create("scheme:///ab.c%43%AC").parse().getPath());
		assertEquals("The Host is supposed to be a2bc", "/ab2c",
				UriParserFactory.create("scheme:///ab2c").parse().getPath());
		assertEquals("The Host is supposed to be .a2bc", "/.ab2c",
				UriParserFactory.create("scheme:///.ab2c").parse().getPath());
		assertEquals("The Host is supposed to be a2bc", "/ab2c",
				UriParserFactory.create("scheme:///ab2c").parse().getPath());
		assertEquals("The Host is supposed to be 23", "/23", UriParserFactory.create("scheme:///23").parse().getPath());
		assertEquals("The Host is supposed to be 2.3", "/2.3",
				UriParserFactory.create("scheme:///2.3").parse().getPath());
		assertEquals("The Host is supposed to be .", "/.", UriParserFactory.create("scheme:///.").parse().getPath());
		assertEquals("The Host is supposed to be ..", "/.//.", UriParserFactory.create("scheme:///.//.").parse().getPath());
		assertEquals("The Host is supposed to be a", "/a", UriParserFactory.create("scheme:///a").parse().getPath());
		assertEquals("The Hostis supposed to be 6", "/6", UriParserFactory.create("scheme:///6").parse().getPath());


		// Userinfo with valid characters and valid hex

		Uri uri = UriParserFactory.create("http://xy37@255.99.1.3/..ABst8%5B/A.").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("Path was /...", "/..ABst8%5B/A.", uri.getPath());
		assertEquals("The user info was xyz37", "xy37", uri.getUserInfo());
		assertEquals("255.99.1.3", uri.getHost().toString());
		assertNull(uri.getQuery());

		Uri uri3 = UriParserFactory.create("http://%39%65@%35%ff/23.76").parse();
		assertEquals("http", uri3.getScheme());
		assertEquals("Host was %35%ff", "%35%ff", uri3.getHost().toString());
		assertEquals("Path was /23.76", "/23.76", uri3.getPath());
		assertEquals("The user info was %39%65", "%39%65", uri3.getUserInfo());
		assertNull(uri.getQuery());
	}

	@Test
	public void QueryUnreserved() {

		// Empty UserInfo
		assertNull("The query has illegal character", UriParserFactory.create("scheme://?/").parse());
		assertNull("The query has illegal character", UriParserFactory.create("scheme://?:").parse());
		assertNull("The query has colon and hex", UriParserFactory.create("scheme://?%zb23").parse());
		assertNull("The query has colon, hex and unreserved", UriParserFactory.create("scheme://?H:a%z2").parse());

		assertEquals("The Host is supposed to be abc", "abc",
				UriParserFactory.create("scheme://?abc").parse().getQuery());
		assertEquals("The Host is supposed to be ab.c", "aB.c",
				UriParserFactory.create("scheme://?aB.c").parse().getQuery());
		assertEquals("The Host is supposed to be .a2bc", ".ab2%5Ac",
				UriParserFactory.create("scheme://?.ab2%5Ac").parse().getQuery());
		assertEquals("The Host is supposed to be a2bc", "ab2c",
				UriParserFactory.create("scheme://?ab2c").parse().getQuery());
		assertEquals("The Host is supposed to be 23", "23", UriParserFactory.create("scheme://?23").parse().getQuery());
		assertEquals("The Host is supposed to be 2.3", "2.3=",
				UriParserFactory.create("scheme://?2.3=").parse().getQuery());
		assertEquals("The Host is supposed to be .", ".&", UriParserFactory.create("scheme://?.&").parse().getQuery());
		assertEquals("The Host is supposed to be ..", "..", UriParserFactory.create("scheme://?..").parse().getQuery());
		assertEquals("The Host is supposed to be a", "a", UriParserFactory.create("scheme://?a").parse().getQuery());
		assertEquals("The Hostis supposed to be 6", "6", UriParserFactory.create("scheme://?6").parse().getQuery());



		// Userinfo with valid characters and valid hex

		Uri uri = UriParserFactory.create("http://xy37@255.99.1.3/...?998s").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("Path was /...", "/...", uri.getPath());
		assertEquals("The user info was xyz37", "xy37", uri.getUserInfo());
		assertEquals("The query was supposed to be 998s", "998s", uri.getQuery());
		assertEquals("255.99.1.3", uri.getHost().toString());

		Uri uri3 = UriParserFactory.create("http://%39%65@%35%ffA/23.76?..=&..").parse();
		assertEquals("http", uri3.getScheme());
		assertEquals("Host was %35%ff", "%35%ffA", uri3.getHost().toString());
		assertEquals("Path was /23.76", "/23.76", uri3.getPath());
		assertEquals("The user info was %39%65", "%39%65", uri3.getUserInfo());
		assertEquals("The query was supposed to be null", "..=&..", uri3.getQuery());

	}

	@Test
	public void QueryHex() {

		// Empty UserInfo
		// assertNull("The query has illegal character",
		// UriParserFactory.create("scheme://?%2aa").parse());
		assertNull("The query has colon and unreserved", UriParserFactory.create("scheme://?%2a%3").parse());
		assertNull("The query has colon and unreserved", UriParserFactory.create("scheme://?%2a%3B?").parse());
		assertNull("The query has colon and hex", UriParserFactory.create("scheme://?%5j7G").parse());
		assertNull("The query has colon and hex", UriParserFactory.create("scheme://?:%5a0%ff%aa").parse());

		assertEquals("The Host is supposed to be abc", "%AA",
				UriParserFactory.create("scheme://?%AA").parse().getQuery());
		assertEquals("The Host is supposed to be ab.c", "aby=%BD%5A",
				UriParserFactory.create("scheme://?aby=%BD%5A").parse().getQuery());
		assertEquals("The Host is supposed to be a2bc", "===%4E=",
				UriParserFactory.create("scheme://?===%4E=").parse().getQuery());
		assertEquals("The Host is supposed to be .a2bc", "&&&",
				UriParserFactory.create("scheme://?&&&").parse().getQuery());
		assertEquals("The Host is supposed to be a2bc", "%55%6A",
				UriParserFactory.create("scheme://?%55%6A").parse().getQuery());
		assertEquals("The Host is supposed to be 23", "=",
				UriParserFactory.create("scheme://?=").parse().getQuery());
		assertEquals("The Host is supposed to be 2.3", "&",
				UriParserFactory.create("scheme://?&").parse().getQuery());
		assertEquals("The Host is supposed to be .", "%AA",
				UriParserFactory.create("scheme://?%AA").parse().getQuery());
		assertEquals("The Host is supposed to be ..", "%56",
				UriParserFactory.create("scheme://?%56").parse().getQuery());
		assertEquals("The Host is supposed to be a", "%66%fa%8D",
				UriParserFactory.create("scheme://?%66%fa%8D").parse().getQuery());
		assertEquals("The Hostis supposed to be 6", "%9A",
				UriParserFactory.create("scheme://?%9A").parse().getQuery());



		// Userinfo with valid characters and valid hex

		Uri uri = UriParserFactory.create("http://xy37@255.99.1.3/...?%55").parse();
		assertEquals("http", uri.getScheme());
		assertEquals("Path was /...", "/...", uri.getPath());
		assertEquals("The user info was xyz37", "xy37", uri.getUserInfo());
		assertEquals("The query was supposed to be 998s", "%55", uri.getQuery());

		Uri uri3 = UriParserFactory.create("http://::@%35%ff/23.76?%Af").parse();
		assertEquals("http", uri3.getScheme());
		assertEquals("Host was %35%ff", "%35%ff", uri3.getHost().toString());
		assertEquals("Path was /23.76", "/23.76", uri3.getPath());
		assertEquals("The user info was ::", "::", uri3.getUserInfo());
		assertEquals("The query was supposed to be null", "%Af", uri3.getQuery());

	}

	

}

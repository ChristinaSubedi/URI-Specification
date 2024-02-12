package prog2.tests.pub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import uri.Uri;
import uri.UriParserFactory;

public class UriParserTests {

	public static String getCategory() {
		return "public";
	}

	public static String getExercise() {
		return "parser";
	}

	@Test
	public void testScheme() {
		Uri uri = UriParserFactory.create("http://").parse();
		assertEquals("http", uri.getScheme());
	}

	@Test
	public void testNull() {
		assertNull(UriParserFactory.create(null));
	}

}


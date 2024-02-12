The Specification

The specification is an adaption and simplification of the Internet standard RFC 3986.1 This standard defines the syntax for those parts of URIs (Uniform Resource Identifier) that are common with all URI schemes.

Examples for URIs are web addresses with the http/https scheme, but also “mailto:a@b.com”, “tel:+49-681-302- 0” and many more schemes. We considerably reduced the syntax of the specification so that essentially only URIs similar to the http scheme are still allowed.

The details of the specification can be found in the JavaDoc comments of the interfaces and classes in the package uri: Uri, IPv4Address, Host, UriParser and UriParserFactory. The to be accepted syntax of the URIs in particular is specified in Uri. The grammar that is used there consists of rules that each define one non-terminal on the left of the equals sign. This non-terminal can take the possible syntactical forms on the right.

A more comprehensive description of this grammar representation with some examples can be found on Wikipedia. We additionally use value ranges for literals in the form of "a"-"f" and "0"-"9".

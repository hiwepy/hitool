package hitool.web;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import hitool.core.lang3.Assert;


/*
 * Utility class for URI encoding and decoding based on RFC 3986.
 * Offers encoding methods for the various URI components.
 *
 * <p>All {@code encode*(String, String)} methods in this class operate in a similar way:
 * <ul>
 * <li>Valid characters for the specific URI component as defined in RFC 3986 stay the same.</li>
 * <li>All other characters are converted into one or more bytes in the given encoding scheme.
 * Each of the resulting bytes is written as a hexadecimal string in the "<code>%<i>xy</i></code>"
 * format.</li>
 * </ul>
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
 */
public abstract class URIUtils {

	/*
	 * Encodes the given URI scheme with the given encoding.
	 * @param scheme the scheme to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded scheme
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeScheme(String scheme, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(scheme, encoding, HierarchicalUriComponents.Type.SCHEME);
	}

	/*
	 * Encodes the given URI authority with the given encoding.
	 * @param authority the authority to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded authority
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeAuthority(String authority, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(authority, encoding, HierarchicalUriComponents.Type.AUTHORITY);
	}

	/*
	 * Encodes the given URI user info with the given encoding.
	 * @param userInfo the user info to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded user info
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeUserInfo(String userInfo, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(userInfo, encoding, HierarchicalUriComponents.Type.USER_INFO);
	}

	/*
	 * Encodes the given URI host with the given encoding.
	 * @param host the host to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded host
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeHost(String host, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(host, encoding, HierarchicalUriComponents.Type.HOST_IPV4);
	}

	/*
	 * Encodes the given URI port with the given encoding.
	 * @param port the port to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded port
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodePort(String port, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(port, encoding, HierarchicalUriComponents.Type.PORT);
	}

	/*
	 * Encodes the given URI path with the given encoding.
	 * @param path the path to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded path
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodePath(String path, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(path, encoding, HierarchicalUriComponents.Type.PATH);
	}

	/*
	 * Encodes the given URI path segment with the given encoding.
	 * @param segment the segment to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded segment
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodePathSegment(String segment, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(segment, encoding, HierarchicalUriComponents.Type.PATH_SEGMENT);
	}

	/*
	 * Encodes the given URI query with the given encoding.
	 * @param query the query to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded query
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeQuery(String query, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(query, encoding, HierarchicalUriComponents.Type.QUERY);
	}

	/*
	 * Encodes the given URI query parameter with the given encoding.
	 * @param queryParam the query parameter to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded query parameter
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeQueryParam(String queryParam, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(queryParam, encoding, HierarchicalUriComponents.Type.QUERY_PARAM);
	}

	/*
	 * Encodes the given URI fragment with the given encoding.
	 * @param fragment the fragment to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded fragment
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeFragment(String fragment, String encoding) throws UnsupportedEncodingException {
		return HierarchicalUriComponents.encodeUriComponent(fragment, encoding, HierarchicalUriComponents.Type.FRAGMENT);
	}

	/*
	 * Encode characters outside the unreserved character set as defined in
	 * <a href="https://tools.ietf.org/html/rfc3986#section-2">RFC 3986 Section 2</a>.
	 * <p>This can be used to ensure the given String will not contain any
	 * characters with reserved URI meaning regardless of URI component.
	 * @param source the string to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded string
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encode(String source, String encoding) throws UnsupportedEncodingException {
		HierarchicalUriComponents.Type type = HierarchicalUriComponents.Type.URI;
		return HierarchicalUriComponents.encodeUriComponent(source, encoding, type);
	}

	// decoding

	/*
	 * Decodes the given encoded source String into an URI. Based on the following rules:
	 * <ul>
	 * <li>Alphanumeric characters {@code "a"} through {@code "z"}, {@code "A"} through {@code "Z"}, and
	 * {@code "0"} through {@code "9"} stay the same.</li>
	 * <li>Special characters {@code "-"}, {@code "_"}, {@code "."}, and {@code "*"} stay the same.</li>
	 * <li>A sequence "{@code %<i>xy</i>}" is interpreted as a hexadecimal representation of the character.</li>
	 * </ul>
	 * @param source the source string
	 * @param encoding the encoding
	 * @return the decoded URI
	 * @throws IllegalArgumentException when the given source contains invalid encoded sequences
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 * @see java.net.URLDecoder#decode(String, String)
	 */
	public static String decode(String source, String encoding) throws UnsupportedEncodingException {
		Assert.notNull(source, "Source must not be null");
		Assert.hasLength(encoding, "Encoding must not be empty");
		int length = source.length();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			int ch = source.charAt(i);
			if (ch == '%') {
				if ((i + 2) < length) {
					char hex1 = source.charAt(i + 1);
					char hex2 = source.charAt(i + 2);
					int u = Character.digit(hex1, 16);
					int l = Character.digit(hex2, 16);
					if (u == -1 || l == -1) {
						throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
					}
					bos.write((char) ((u << 4) + l));
					i += 2;
					changed = true;
				}
				else {
					throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
				}
			}
			else {
				bos.write(ch);
			}
		}
		return (changed ? new String(bos.toByteArray(), encoding) : source);
	}

}

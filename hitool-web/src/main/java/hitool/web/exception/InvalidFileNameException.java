package hitool.web.exception;

/**
 * This exception is thrown in case of an invalid file name.
 * A file name is invalid, if it contains a NUL character.
 * Attackers might use this to circumvent security checks:
 * For example, a malicious user might upload a file with the name
 * "foo.exe\0.png". This file name might pass security checks (i.e.
 * checks for the extension ".png"), while, depending on the underlying
 * C library, it might create a file named "foo.exe", as the NUL
 * character is the string terminator in C.
 */
public class InvalidFileNameException extends RuntimeException {
	
    private static final long serialVersionUID = 7922042602454350470L;
    private final String name;

    /**
     * Creates a new instance.
     * @param pName The file name causing the exception.
     * @param pMessage A human readable error message.
     */
    public InvalidFileNameException(String pName, String pMessage) {
        super(pMessage);
        name = pName;
    }

    /**
     * Returns the invalid file name.
     */
    public String getName() {
        return name;
    }
}

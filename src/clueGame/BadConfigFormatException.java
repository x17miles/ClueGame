package clueGame;


public class BadConfigFormatException extends Exception{
	public BadConfigFormatException() {
		super("Error: Incorrectly formatted config file");
	}
	public BadConfigFormatException(String s) {
		super(s);
	}
}

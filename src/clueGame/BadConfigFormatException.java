package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception{
	public BadConfigFormatException() {
		super("Error: Incorrectly formatted config file");
		logError("Error: Incorrectly formatted config file");
	}
	public BadConfigFormatException(String s) {
		super(s);
		logError(s);
	}
	private void logError(String errorMessage) {
		try {
			PrintWriter outFile = new PrintWriter("errorLog.txt");
			outFile.println(errorMessage);
			outFile.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("error creating error log");
		}
	}
}

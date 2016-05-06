import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Rc4 {

	private static final short sLength = 256;
	private static short[] S;
	private static int[] key = new int[] { 1, 2, 3, 4, 5 };

	// Comment in to encrypt
	private static String inputPath = ".\\src\\King James Bible.txt";
	private static String outputPath = ".\\src\\encrypted.txt";

	public static void main(String[] args) {
		long start = System.nanoTime();

		System.out.println("Starting key scheduling algorithm.");
		keyScheduling();
		System.out.println("Key scheduling finished.");

		File toEncrypt = new File(inputPath);
		StringBuffer plaintext = new StringBuffer();
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(toEncrypt));
			// Iterate over file and add to plaintext buffer
			System.out.println("Reading in file.");
			short temp;
			while ((temp = (short) reader.read()) != -1) {
				plaintext.append((char) temp);
			}
			System.out.println("File read.");

		} catch (IOException e) {
			e.printStackTrace();
		}

		File ciphertext = new File(outputPath);
		try {
			BufferedWriter outWriter = new BufferedWriter(new FileWriter(
					ciphertext));
			System.out.println("Encrypting...");
			outWriter.write(encrypt(plaintext.toString()));
			outWriter.close();
			long end = System.nanoTime();
			long runtimeMS = (end - start) / 1000000; // convert to ms
			System.out.println("Encryption complete in " + (end - start)
					+ "ns (" + runtimeMS + "ms)");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String encrypt(final String plaintext) {
		StringBuffer ciphertext = new StringBuffer();
		char[] chars = plaintext.toCharArray();
		int i = 0, j = 0, k;
		// short temp;
		int length = plaintext.length();
		for (int counter = 0; counter < length; counter++) {
			i = ((i + 1) % S.length);
			j = ((j + S[i]) % S.length);
			S[i] ^= S[j];
			S[j] ^= S[i];
			S[i] ^= S[j];
			k = S[(S[i] + S[j]) % S.length];
			ciphertext.append((char) (chars[counter] ^ k));
		}
		return ciphertext.toString();
	}

	/**
	 * Shifts the S array based on the key.
	 */
	private static void keyScheduling() {
		S = new short[sLength];
		for (int i = 0; i < S.length; i++)
			S[i] = (short) i;
		int j = 0;
		for (int i = 0; i < S.length; i++) {
			j = (j + S[i] + key[i % key.length]) % S.length;
			S[i] ^= S[j];
			S[j] ^= S[i];
			S[i] ^= S[j];
		}
	}
}
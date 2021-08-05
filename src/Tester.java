import edu.duke.FileResource;

import java.io.File;
import java.io.FileReader;

public class Tester {
    FileResource fr = new FileResource();
    String input = fr.asString();
    public void testCaesarCipher() {
        CaesarCipher cc = new CaesarCipher(14);
        String encrypted = cc.encrypt(input);
        System.out.println("Encrypted message is :\n" + encrypted);
        String decrypted = cc.decrypt(encrypted);
        System.out.println("Decrypted message is :\n" + decrypted);
    }

    public void testCaesarCracker() {
        CaesarCracker ccr = new CaesarCracker('a');
        String decrypted = ccr.decrypt(input);
        System.out.println("The Decrypted message is : \t" + decrypted);
    }

    public void testVigenereCipher() {
        int[] key = {17, 14, 12, 4};
        VigenereCipher vc  = new VigenereCipher(key);
        String encrypted = vc.encrypt(input);
        String decrypted = vc.decrypt(input);
        System.out.println("The encrypted message is : \n" + encrypted);
        System.out.println("The decrypted message is : \n" + decrypted);
    }
    
    public static void main(String[] args) {
        Tester tt = new Tester();
        tt.testVigenereCipher();
    }
}


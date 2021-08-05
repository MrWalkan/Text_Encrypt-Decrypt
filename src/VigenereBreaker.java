import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.nio.file.DirectoryStream;
import java.util.*;
import edu.duke.*;

import javax.swing.*;

public class VigenereBreaker {

    FileResource forEncryptedFile;
    DirectoryResource forDictionary;

    public VigenereBreaker() {
        forEncryptedFile = new FileResource();
        forDictionary = new DirectoryResource();
    }
    public String sliceString(String message, int whichSlice, int totalSlices) {
        String slicedMessage = "";
        for (int i = whichSlice; i < message.length(); i += totalSlices) {
            char currCharacter = message.charAt(i);
            slicedMessage += currCharacter;
        }
        return slicedMessage;
    }

    public int[] tryKeyLength(String encrypted, int kLength, char mostCommon) {
        int[] keys = new int[kLength];
        CaesarCracker ccr = new CaesarCracker(mostCommon);
        for (int i = 0; i < kLength; i++) {
            String slicedString = sliceString(encrypted,i,kLength);
            int key = ccr.getKey(slicedString);
            keys[i] = key;
        }
        return keys;
    }



    public HashSet<String> readDictionary(FileResource fr1) {
        HashSet<String> dictionaryData = new HashSet<String>();
        for (String s : fr1.lines()) {
            s = s.toLowerCase();
            dictionaryData.add(s);
        }
        return dictionaryData;
    }

    public int countWords(String message, HashSet<String> dictionary) {
        int count = 0;
        message = message.toLowerCase();
        String[] words = message.split("\\W+");
        for (int i = 0; i < words.length; i++) {
            if (dictionary.contains(words[i])) {
                count++;
            }
        }
        return count;
    }

    public char mostCommonCharIn(HashSet<String> dictionary) {
        HashMap<Character, Integer> stats = new HashMap<Character, Integer>();
        for (String s : dictionary) {
            for (char c : s.toCharArray()) {
                if (!stats.containsKey(c)) {
                    stats.put(c, 1);
                } else {
                    stats.put(c, stats.get(c) + 1);
                }
            }
        }
        int maxValue = 0;
        char mostChar = '\0';
        for (char c : stats.keySet()) {
            int currValue = stats.get(c);
            if (currValue > maxValue) {
                maxValue = currValue;
                mostChar = c;
            }
        }
        return mostChar;
    }

    public void breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages) {
        int maxWord = 0;
        String decrypted = "";
        String languageUsed ="";
        for (String s : languages.keySet()) {
            HashSet<String> specificLang = languages.get(s);
            String decryption = breakForLanguage(encrypted,specificLang);
            int countWord = countWords(decryption, specificLang);
            if (countWord > maxWord) {
                maxWord = countWord;
                decrypted = decryption;
                languageUsed = s;
            }
        }
        System.out.println("The Language was identified as : " + languageUsed);
        System.out.println("The Decrypted message is : \n--------------------------------\n" + decrypted);
    }
    public String breakForLanguage(String encrypted, HashSet<String> dictionary) {
        int maxCount = 0;
        String decryptedMessage ="";
        int[] key1 = {};
        char mostCommon = mostCommonCharIn(dictionary);
        for (int i = 1; i <= 100; i++) {
            int[] key = tryKeyLength(encrypted,i,mostCommon);
            VigenereCipher vc = new VigenereCipher(key);
            String decrypted = vc.decrypt(encrypted);
            int currCount = countWords(decrypted,dictionary);
            if (currCount > maxCount) {
                maxCount = currCount;
                decryptedMessage = decrypted;
                key1 = key;
            }
        }
//        System.out.println("Total valid words : " + maxCount + " out of " + decryptedMessage.split("\\W+").length);
//        System.out.println("The Key is : " + Arrays.toString(key1));
//        System.out.println("The key length is : " + key1.length);
        return decryptedMessage;
    }

    public void breakVigenere () {
        HashMap<String, HashSet<String>> dictionary = new HashMap<String, HashSet<String>>();
        for (File f : forDictionary.selectedFiles()) {
            HashSet<String> temp = readDictionary(new FileResource(f));
            dictionary.put(f.getName(), temp);
            System.out.println(f + " is successfully read!");
        }
        System.out.println(dictionary);
        String encryptedData = forEncryptedFile.asString();
        breakForAllLangs(encryptedData, dictionary);
//        String decryption = breakForLanguage(encryptedData, hashSet);
//        System.out.println("The decrypted message is : \n--------------------------\n" + decryption );

    }

    public static void main(String[] args) {
        VigenereBreaker vcc = new VigenereBreaker();
        vcc.breakVigenere();

//        System.out.println(Arrays.toString(vcc.tryKeyLength(vcc.data, 4, 'e')));

    }
    
}

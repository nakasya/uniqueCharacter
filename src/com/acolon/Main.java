package com.acolon;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * CLI tool
 * The unique characters used in the first argument file is output to the second argument file.
 * Especially for languages that do not use the alphabet.
 * Usage: uniqueCharacter input-file output-file
 * Note:It doesn't include under unicode IGNORED_ASCII_CODE. They must be included by the base-code.
 * 2019-9-10 Ken'ichi Masuta
 */
public class Main {
    private static final char IGNORED_ASCII_CODE = 0x100;
    public static void main(String[] args) {
        if (2 != args.length) {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(1);
        }
        String inputPath = args[0];
        String outputPath = args[1];
        String allText;
        if (null == (allText = readFile(inputPath))) {
            System.exit(2);
        }
        String uniqueText = sort(uniqueCharacters(allText));
        try {
            FileOutputStream outStream = new FileOutputStream(outputPath);
            // Insert UTF-8 BOM
            outStream.write(0xef);
            outStream.write(0xbb);
            outStream.write(0xbf);

            OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(uniqueText);

            buffer.close();
            writer.close();
            outStream.close();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(3);
        }
        System.err.print("Execution was successful.");
    }

    private static String readFile(String inputPath) {
        String ret;
        try {
            ret = new String(Files.readAllBytes(Paths.get(inputPath)));
        }
        catch (IOException e) {
            System.err.print("Can't read the input file:" + inputPath);
            ret = null;
        }
        return ret;
    }

    /**
     * Note:White spaces are included.
     * @param test scanning source text
     * @return unique string
     */
    private static String uniqueCharacters(String test) {
        StringBuilder temp =  new StringBuilder(test.length());
        for(int i=0;i<test.length();i++){
            char c = test.charAt(i);
            // Ignore ASCII for default fonts.
            if (c < IGNORED_ASCII_CODE)
                continue;
            if(temp.indexOf(Character.toString(c))==-1) {
                temp.append(c);
            }
        }
        return temp.toString();
    }

    private static String sort(String str) {
        char []arr = str.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }
}

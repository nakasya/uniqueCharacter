package com.acolon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * CLI tool
 * The unique characters used in the first argument file is output to the second argument file.
 * Usage: uniqueCharacter input-file output-file
 * 2019-9-10 Kenichi Masuta
 */
public class Main {
    public static void main(String[] args) {
        if (2 != args.length) {
            System.err.println("Invalid arguments count:" + args.length);
            System.exit(1);
        }
        String readPath = args[0];
        String writePath = args[1];
        String allText;
        if (null == (allText = readFile(readPath))) {
            System.exit(2);
        }
        String uniqueText = sort(uniqueCharacters(allText));
        try {
            Files.write(Paths.get(writePath), uniqueText.getBytes());
        }catch (IOException e){
            e.printStackTrace();
            System.exit(3);
        }
        System.err.print("Execution was successful.");
    }

    private static String readFile(String readPath) {
        String ret;
        try {
            ret = new String(Files.readAllBytes(Paths.get(readPath)));
        }
        catch (IOException e) {
            e.printStackTrace();
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
            // Ignore control code.
            if (Character.isISOControl(c))
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

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
 * Usage: uniqueCharacter input-file output-file
 * 2019-9-10 Kenichi Masuta
 * TODO:Unicode 0x100未満を含めなくする。
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
            FileOutputStream outstream;
            OutputStreamWriter writer;
            BufferedWriter buffer = null;

            outstream = new FileOutputStream(writePath);
            outstream.write(0xef);
            outstream.write(0xbb);
            outstream.write(0xbf);

            writer = new OutputStreamWriter(outstream, "UTF-8");
            buffer = new BufferedWriter(writer);
            buffer.write(uniqueText);

            buffer.close();
            writer.close();
            outstream.close();
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
            // Ignore ASCII for default fonts.
            if (c < 0x100)
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

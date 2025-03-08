package com.thecrappiest.api.parseit.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thecrappiest.api.parseit.Parseit;
import com.thecrappiest.api.parseit.util.FileMethods;

public class DefaultParser implements Parseit {

    private File toParse;
    private Map<String, String> placeholders = new HashMap<>();

    public DefaultParser(File file) {
        toParse = file;
    }

    @Override
    public void parse() {
        parse(toParse);
    }
    
    public void parse(File file) {
        byte[] data = FileMethods.asBytes(toParse);
        char[] charArray = new char[data.length];
        
        for(int i = 0; i < data.length; i++)
            charArray[i] = (char) Byte.toUnsignedInt(data[i]);
        
        String asString = new String(charArray);
        for(Entry<String, String> ent : placeholders.entrySet())
            asString = asString.replace(ent.getKey(), ent.getValue());
        
        charArray = asString.toCharArray();
        byte[] reconverted = new byte[charArray.length];
        for(int i = 0; i < charArray.length; i++)
            reconverted[i] = (byte) charArray[i];
        
        FileMethods.writeToFile(file, reconverted);
    }
    
    @Override
    public Parseit setPlaceholders(Map<String, String> map) {
        placeholders = map;
        return this;
    }
}
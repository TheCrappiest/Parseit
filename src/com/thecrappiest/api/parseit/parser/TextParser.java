package com.thecrappiest.api.parseit.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thecrappiest.api.parseit.Parseit;
import com.thecrappiest.api.parseit.util.FileMethods;

public class TextParser implements Parseit {

    private File toParse;
    private Map<String, String> placeholders = new HashMap<>();
    
    public TextParser(File file) {
        toParse = file;
    }

    @Override
    public void parse() {
        parse(toParse);
    }

    @Override
    public void parse(File file) {
        if(placeholders == null || placeholders.isEmpty())
            return;
        
        byte[] data = FileMethods.asBytes(toParse);
        
        String asString = new String(data);
        for(Entry<String, String> ent : placeholders.entrySet())
            asString = asString.replace(ent.getKey(), ent.getValue());
        
        FileMethods.writeToFile(file, asString.getBytes());
    }

    @Override
    public Parseit setPlaceholders(Map<String, String> map) {
        placeholders = map;
        return this;
    }
    
}

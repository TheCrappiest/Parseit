package com.thecrappiest.api.parseit;

import java.io.File;
import java.util.Map;

import com.thecrappiest.api.parseit.parser.ClassParser;
import com.thecrappiest.api.parseit.parser.DefaultParser;
import com.thecrappiest.api.parseit.parser.JarParser;
import com.thecrappiest.api.parseit.parser.TextParser;

public interface Parseit {
    
    public void parse();
    public void parse(File file);
    public Parseit setPlaceholders(Map<String, String> map);
    
    public static Parseit of(File file) {
        String name = file.getName();
        String type = name.substring(name.lastIndexOf(".") + 1);
        
        switch(type.toUpperCase()) {
        case "TXT", "YML", "JSON", "LOG":
            return new TextParser(file);
        case "JAR":
            return new JarParser(file);
        case "CLASS":
            return new ClassParser(file);
        default:
            return new DefaultParser(file);
        }
    }
    
}

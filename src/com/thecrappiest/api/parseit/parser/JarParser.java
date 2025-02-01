package com.thecrappiest.api.parseit.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import com.thecrappiest.api.parseit.Parseit;
import com.thecrappiest.api.parseit.util.FileMethods;

public class JarParser implements Parseit {

    private File toParse;
    private Map<String, String> placeholders = new HashMap<>();
    
    public JarParser(File file) {
        toParse = file;
    }

    @Override
    public void parse() {
        parse(toParse);
    }

    @Override
    public void parse(File file) {
        File temp = FileMethods.getTemporary(".jar");
        JarOutputStream output = FileMethods.asJarOutput(temp);
        
        read(FileMethods.asJar(toParse), output);
        FileMethods.overwrite(temp, file);
    }

    @Override
    public Parseit setPlaceholders(Map<String, String> map) {
        placeholders = map;
        return this;
    }

    private void read(JarFile file, JarOutputStream output) {
        file.stream().forEach(entry -> {
            try {
                write(file, entry, output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        try {
            file.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void write(JarFile file, JarEntry ent, JarOutputStream output) throws IOException {
        String name = ent.getRealName();
        File temp = FileMethods.getTemporary(name.substring(name.lastIndexOf(".")));
        FileMethods.writeToFile(temp, readBytesFromEntry(file, ent));
        
        Parseit parser = Parseit.of(temp);
        if(parser != null) {
            parser.setPlaceholders(placeholders).parse();
            
            JarEntry modified = new JarEntry(ent);
            modified.setTime(System.currentTimeMillis());
            output.putNextEntry(modified);
            output.write(FileMethods.asBytes(temp));
        }else {
            output.putNextEntry(ent);
            file.getInputStream(ent).transferTo(output);
        }
    }
    
    private byte[] readBytesFromEntry(JarFile file, JarEntry ent) {
        try {
            InputStream input = file.getInputStream(ent);
            byte[] data = input.readAllBytes();
            input.close();
            
             return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}

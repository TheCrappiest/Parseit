package com.thecrappiest.api.parseit.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class FileMethods {
    
    public static byte[] asBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void writeToFile(File file, byte[] bytes) {
        if(file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
            Files.write(file.toPath(), bytes, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean overwrite(File read, File write) {
        try {
            Files.copy(read.toPath(), write.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static File getTemporary(String suffix) {
        try {
            File file = Files.createTempFile(null, suffix).toFile();
            file.deleteOnExit();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JarFile asJar(File file) {
        try {
            return new JarFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JarOutputStream asJarOutput(File file) {
        try {
            return new JarOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

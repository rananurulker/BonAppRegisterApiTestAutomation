package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader {
    public static Properties properties;

    static {
        String file="configuration.properties";

        try{
            FileInputStream fileInputStream=new FileInputStream(file);
            properties=new Properties();
            properties.load(fileInputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return  properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        String file="configuration.properties";
        properties.setProperty(key, value);
        try {
            OutputStream outputStream = Files.newOutputStream(Paths.get(file));
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

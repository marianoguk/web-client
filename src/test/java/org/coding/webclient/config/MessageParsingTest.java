package org.coding.webclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.File;
import java.nio.file.Files;

public class MessageParsingTest {

    private MessageParser parser = new MessageParser(new ObjectMapper());

    @Test
    public void request(){
        String data = getData("request.txt");
        assertNotNull(data);
        assertNotNull(parser.parse("", data));
    }

    @Test
    public void response(){
        String data = getData("response.txt");
        assertNotNull(data);
        assertNotNull(parser.parse("", data));
    }

    private String getData(String fileName) {
        File file = new File(this.getClass().getClassLoader().getResource(fileName).getFile());

        //File is found
        System.out.println("File Found : " + file.exists());

        try {
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            return null;
        }

    }
}

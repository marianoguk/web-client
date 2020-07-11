package org.coding.webclient.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class MessageParser {

    public static final String HTTP_VERSION = "/1.1";
    private ObjectMapper mapper;

    @Autowired
    public MessageParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String parse(String event, String data) {
        Map<String, String> properties = HTTP_VERSION.startsWith(event) ? response(data): request(data);
        try {
            return mapper.writeValueAsString(properties);
        } catch (JsonProcessingException e) {
            log.error("Error parsing mapping: {}", e.getMessage(), e);
            return "{}";
        }
    }

    private Map<String, String> request(String data) {
        Map<String, String> result = new LinkedHashMap<>();
        String[] parts = data.split("\n");
        Arrays.stream(parts).forEach(item -> {
            int pos = item.indexOf(":");
            if(pos != -1 && !isJson(item)) {
                result.put(item.substring(0, pos),item.substring(pos+1));
            }
        });
        String json = Arrays.stream(parts).filter( it -> isJson(it)).findAny().orElse("");
        result.put("json", json);
        return result;
    }

    private Map<String, String> response(String data) {
        Map<String, String> result = new LinkedHashMap<>();
        int index = data.indexOf("{");

        String[] pairs = (index != -1 ? data.substring(0, index) : "").split("\n");
        Arrays.stream(pairs).forEach( item -> {
            int pos = item.indexOf(":");
            if(pos != -1) {
                result.put(item.substring(0, pos),item.substring(pos+1));
            }
        });

        result.put("json", data.substring(index));
        return result;
    }
    private boolean isJson(String value) {
        return value.contains("{") || value.contains("{") ;
    }


}

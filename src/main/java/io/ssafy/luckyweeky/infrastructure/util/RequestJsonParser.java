package io.ssafy.luckyweeky.infrastructure.util;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RequestJsonParser {
    private static RequestJsonParser instance;
    private final Gson gson;

    private RequestJsonParser() {
        this.gson = new Gson();
    }

    public static RequestJsonParser getInstance() {
        if (instance == null) {
            instance = new RequestJsonParser();
        }
        return instance;
    }


    public JsonObject parse(Part jsonPart) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonPart.getInputStream()));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        return gson.fromJson(jsonBuilder.toString(), JsonObject.class);
    }
}

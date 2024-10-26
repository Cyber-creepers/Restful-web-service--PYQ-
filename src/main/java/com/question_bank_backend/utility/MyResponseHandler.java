package com.question_bank_backend.utility;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class MyResponseHandler {

    public static ResponseEntity<Object> generateResponse(HttpStatus status, boolean isError, String message, Object responseBody) {


        Map<String, Object> map = new HashMap<>();
        try {
            map.put("status", status.value());
            map.put("message", message);
            map.put("responseBody", responseBody);
            map.put("isError", isError);
            return new ResponseEntity<>(map, status);

        } catch (Exception e) {

            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("message", e.getMessage());
            map.put("responseBody", null);
            map.put("isError", false);
            return new ResponseEntity<>(map, status);

        }


    }

}

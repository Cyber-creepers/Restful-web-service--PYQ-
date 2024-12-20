package com.question_bank_backend.utility;

import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class OtpUtil {

    public StringBuilder generateOtp() {
        Random random = new Random();

        int randomNumber = random.nextInt(999999);

        StringBuilder output = new StringBuilder(Integer.toString(randomNumber));

        while (output.length() < 6) {
            output.insert(0, "0");
        }
        return output;
    }


}

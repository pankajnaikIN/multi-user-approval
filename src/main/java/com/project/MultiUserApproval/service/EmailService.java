package com.project.MultiUserApproval.service;

import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.util.Objects;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    public void sendEmail(String toEmail, String subject, String message) {
        OkHttpClient client = new OkHttpClient();

        String jsonPayload = "{"
                + "\"from\": \"" + fromEmail + "\","
                + "\"to\": \"" + toEmail + "\","
                + "\"subject\": \"" + subject + "\","
                + "\"html\": \"" + message + "\""
                + "}";

        RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(RESEND_API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + resendApiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Failed to send email: " + Objects.requireNonNull(response.body()).string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
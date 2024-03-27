package ru.crpt.ismp.crptapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;

public class TerminalCall {
    public static void main(String[] args) throws JsonProcessingException {
        CrptApi crptApi = new CrptApi(new CrptApi.RequestLimiter(Duration.ofSeconds(10), 2));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = "{" +
                "\"description\": { \"participantInn\": \"string\" }, " +
                "\"doc_id\": \"string\", " +
                "\"doc_status\": \"string\"," +
                "\"doc_type\": \"LP_INTRODUCE_GOODS\", " +
                "\"import_request\": true," +
                "\"owner_inn\": \"string\", " +
                "\"participant_inn\": \"string\", " +
                "\"producer_inn\": \"string\", " +
                "\"production_date\": \"2020-01-23\", " +
                "\"production_type\": \"string\"," +
                "\"products\": [ { \"certificate_document\": \"string\"," +
                "\"certificate_document_date\": \"2020-01-23\"," +
                "\"certificate_document_number\": \"string\", " +
                "\"owner_inn\": \"string\"," +
                "\"producer_inn\": \"string\", " +
                "\"production_date\": \"2020-01-23\"," +
                "\"tnved_code\": \"string\", " +
                "\"uit_code\": \"string\", " +
                "\"uitu_code\": \"string\" }," +
                "{ \"certificate_document\": \"string\"," +
                "\"certificate_document_date\": \"2020-01-23\"," +
                "\"certificate_document_number\": \"string\", " +
                "\"owner_inn\": \"string\"," +
                "\"producer_inn\": \"string\", " +
                "\"production_date\": \"2020-01-23\"," +
                "\"tnved_code\": \"string\", " +
                "\"uit_code\": \"string\", " +
                "\"uitu_code\": \"string\" } ]," +
                "\"reg_date\": \"2020-01-23\", " +
                "\"reg_number\": \"string\"" +
                "}";
        CrptApi.Document document = mapper.readValue(json, CrptApi.Document.class);
        for (int i = 0; i < 10; i++) {
            int threadNum = i;
            new Runnable() {
                @Override
                public void run() {
                    System.out.println(crptApi.createDocument(document));
                    System.out.println("thread " + threadNum + " has finished");
                }
            }.run();
        }
    }
}

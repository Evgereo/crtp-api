package ru.crpt.ismp.crptapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CrptApi {
    RequestLimiter requestLimiter;

    public String createDocument(Document document) {
        requestLimiter.limit();
        StringBuilder builder = new StringBuilder(1000);
        builder.append("doc_id,doc_status,doc_type,import_request,owner_inn,participant_inn,producer_inn,production_date,production_type,certificate_document,certificate_document_date,certificate_document_number,owner_inn,producer_inn,production_date,tnved_code,uit_code,uitu_code,reg_date,reg_number");
        String join = String.join(",", document.getDescription().keySet());
        if (!join.isEmpty()) {
            builder.insert(0, ",")
                    .insert(0, join);
        }
        for (Product product: document.getProducts()) {
            builder.append("\n");
            if (!join.isEmpty()) {
                builder.append(String.join(",", document.getDescription().values())).append(",");
            }
            builder
                .append(document.getDocId()).append(",")
                .append(document.getDocStatus()).append(",")
                .append(document.getDocType()).append(",")
                .append(document.isImportRequest()).append(",")
                .append(document.getOwnerInn()).append(",")
                .append(document.getParticipantInn()).append(",")
                .append(document.getProducerInn()).append(",")
                .append(document.getProductionDate()).append(",")
                .append(document.getProductionType()).append(",")
                .append(product.getCertificateDocument()).append(",")
                .append(product.getCertificateDocumentDate()).append(",")
                .append(product.getCertificateDocumentNumber()).append(",")
                .append(product.getOwnerInn()).append(",")
                .append(product.getProducerInn()).append(",")
                .append(product.getProductionDate()).append(",")
                .append(product.getTnvedCode()).append(",")
                .append(product.getUitCode()).append(",")
                .append(product.getUituCode()).append(",")
                .append(document.getRegDate()).append(",")
                .append(document.getRegNumber());
        }
        return builder.toString();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Document {
        private Map<String, String> description;
        @JsonProperty("doc_id")
        private String docId;
        @JsonProperty("doc_status")
        private String docStatus;
        @JsonProperty("doc_type")
        private String docType;
        @JsonProperty("import_request")
        private boolean importRequest;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("participant_inn")
        private String participantInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        private LocalDate productionDate;
        @JsonProperty("production_type")
        private String productionType;
        private List<Product> products;
        @JsonProperty("reg_date")
        private LocalDate regDate;
        @JsonProperty("reg_number")
        private String regNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Product {
        @JsonProperty("certificate_document")
        private String certificateDocument;
        @JsonProperty("certificate_document_date")
        private LocalDate certificateDocumentDate;
        @JsonProperty("certificate_document_number")
        private String certificateDocumentNumber;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        private String productionDate;
        @JsonProperty("tnved_code")
        private String tnvedCode;
        @JsonProperty("uit_code")
        private String uitCode;
        @JsonProperty("uitu_code")
        private String uituCode;
    }

    public static class RequestLimiter {
        private static volatile int currentRequestCount = 0;
        private static volatile long time;
        private final Duration duration;
        private final int requestLimit;

        public RequestLimiter(Duration duration, int requestLimit) {
            this.duration = duration;
            this.requestLimit = requestLimit;
        }

        public synchronized void limit() {
            if (currentRequestCount < requestLimit) {
                if (currentRequestCount == 0)
                    time = System.currentTimeMillis();
                currentRequestCount++;
                return;

            } else if ((time + duration.toMillis()) > System.currentTimeMillis()){
                try {
                    Thread.sleep(duration.toMillis() - (System.currentTimeMillis() - time));
                } catch (InterruptedException ignored) {}
            }

            currentRequestCount = 0;
            limit();
        }
    }
}

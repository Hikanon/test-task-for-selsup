package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CrptApi {

    private static final Logger logger = Logger.getLogger(CrptApi.class.getName());

    private static final String URL = "https://www.example.ru/api/v1";

    private final int requestLimit;
    private final TimeUnit timeUnit;
    private final Semaphore semaphore;


    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        if (requestLimit <= 0) {
            throw new IllegalArgumentException("requestLimit must be positive");
        }
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        this.semaphore = new Semaphore(requestLimit, true);


    }

    public static void main(String[] args) {
    }


    public HttpResponse<String> sendDocument(DocumentDto document, String signature) {
        try {
            semaphore.acquireUninterruptibly();
            long startTime = System.currentTimeMillis();
            logger.info("Start send document with id:" + document.getDoc_id());
            HttpResponse<String> response = send(DocumentMarshaller.marshall(document), signature, URL);
            logger.info("End send document with id:" + document.getDoc_id() + " . Time " + (System.currentTimeMillis() - startTime));
            return response;
        } catch (Exception e) {
            logger.severe("Something wrong with send document with id " + document.getDoc_id());
            return null;
        } finally {
            semaphore.release();
        }
    }


    private HttpResponse<String> send(String message, String signature, String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public int getRequestLimit() {
        return requestLimit;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    private static class DocumentMarshaller {

        public static String marshall(DocumentDto dto) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                return ow.writeValueAsString(dto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Something wrong with dto", e);
            }
        }
    }

    private static class BaseDto {
    }

    private static class DocumentDto extends BaseDto {

        private Description description;
        private String doc_id;
        private String doc_status;
        private DocType doc_type;
        private Boolean importRequest;
        private String owner_inn;
        private String participant_inn;

        private String producer_inn;
        private String production_date;
        private String production_type;
        private List<Product> products;
        private String reg_date;
        private String reg_number;

        public DocumentDto() {
        }

        public DocumentDto(Description description,
                           String doc_id,
                           String doc_status,
                           DocType doc_type,
                           Boolean importRequest,
                           String owner_inn,
                           String participant_inn,
                           String producer_inn,
                           String production_date,
                           String production_type,
                           List<Product> products,
                           String reg_date,
                           String reg_number) {
            this.description = description;
            this.doc_id = doc_id;
            this.doc_status = doc_status;
            this.doc_type = doc_type;
            this.importRequest = importRequest;
            this.owner_inn = owner_inn;
            this.participant_inn = participant_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.production_type = production_type;
            this.products = products;
            this.reg_date = reg_date;
            this.reg_number = reg_number;
        }

        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public String getDoc_id() {
            return doc_id;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public String getDoc_status() {
            return doc_status;
        }

        public void setDoc_status(String doc_status) {
            this.doc_status = doc_status;
        }

        public DocType getDoc_type() {
            return doc_type;
        }

        public void setDoc_type(DocType doc_type) {
            this.doc_type = doc_type;
        }

        public Boolean getImportRequest() {
            return importRequest;
        }

        public void setImportRequest(Boolean importRequest) {
            this.importRequest = importRequest;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public String getParticipant_inn() {
            return participant_inn;
        }

        public void setParticipant_inn(String participant_inn) {
            this.participant_inn = participant_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }

        public String getProduction_date() {
            return production_date;
        }

        public void setProduction_date(String production_date) {
            this.production_date = production_date;
        }

        public String getProduction_type() {
            return production_type;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public String getReg_date() {
            return reg_date;
        }

        public void setReg_date(String reg_date) {
            this.reg_date = reg_date;
        }

        public String getReg_number() {
            return reg_number;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }

        private enum DocType {

            LP_INTRODUCE_GOODS;
        }

        private static class Description {

            private String participantInn;

            public Description() {
            }

            public Description(String participantInn) {
                this.participantInn = participantInn;
            }

            public String getParticipantInn() {
                return participantInn;
            }

            public void setParticipantInn(String participantInn) {
                this.participantInn = participantInn;
            }
        }

        private static class Product {

            private String certificate_document;
            private String certificate_document_date;
            private String certificate_document_number;
            private String owner_inn;
            private String producer_inn;
            private String production_date;
            private String tnved_code;
            private String uit_code;
            private String uitu_code;

            public Product() {
            }

            public Product(String certificate_document,
                           String certificate_document_date,
                           String certificate_document_number,
                           String owner_inn,
                           String producer_inn,
                           String production_date,
                           String tnved_code,
                           String uit_code,
                           String uitu_code) {
                this.certificate_document = certificate_document;
                this.certificate_document_date = certificate_document_date;
                this.certificate_document_number = certificate_document_number;
                this.owner_inn = owner_inn;
                this.producer_inn = producer_inn;
                this.production_date = production_date;
                this.tnved_code = tnved_code;
                this.uit_code = uit_code;
                this.uitu_code = uitu_code;
            }

            public String getCertificate_document() {
                return certificate_document;
            }

            public void setCertificate_document(String certificate_document) {
                this.certificate_document = certificate_document;
            }

            public String getCertificate_document_date() {
                return certificate_document_date;
            }

            public void setCertificate_document_date(String certificate_document_date) {
                this.certificate_document_date = certificate_document_date;
            }

            public String getCertificate_document_number() {
                return certificate_document_number;
            }

            public void setCertificate_document_number(String certificate_document_number) {
                this.certificate_document_number = certificate_document_number;
            }

            public String getOwner_inn() {
                return owner_inn;
            }

            public void setOwner_inn(String owner_inn) {
                this.owner_inn = owner_inn;
            }

            public String getProducer_inn() {
                return producer_inn;
            }

            public void setProducer_inn(String producer_inn) {
                this.producer_inn = producer_inn;
            }

            public String getProduction_date() {
                return production_date;
            }

            public void setProduction_date(String production_date) {
                this.production_date = production_date;
            }

            public String getTnved_code() {
                return tnved_code;
            }

            public void setTnved_code(String tnved_code) {
                this.tnved_code = tnved_code;
            }

            public String getUit_code() {
                return uit_code;
            }

            public void setUit_code(String uit_code) {
                this.uit_code = uit_code;
            }

            public String getUitu_code() {
                return uitu_code;
            }

            public void setUitu_code(String uitu_code) {
                this.uitu_code = uitu_code;
            }
        }
    }
}
package patient_management_system.dto;

import lombok.Data;

@Data
public class WebHookPayload {
    private String event;
    private Data data;

    @lombok.Data
    public static class Data{
        private Long id;
        private String reference;
        private String status;
        private String paid_at;
        private String currency;
        private String channel;
    }
}

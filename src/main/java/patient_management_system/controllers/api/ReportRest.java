package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.serviceImpl.ReportServiceImpl;
import patient_management_system.util.AppConstants;

import java.util.Objects;

@Tag(name = "Report Management")
@RequestMapping("/api/reports")
@RestController
public class ReportRest {

    private final ReportServiceImpl reportService;

    @Autowired
    public ReportRest(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "This endpoint is used to generate medical lab report for a given patient")
    @GetMapping("/generate-lab-report")
    public ResponseEntity<?> generateLabReport(@RequestParam(name = "patientId") String patientId,
                                               @RequestParam(name = "labId") String labId,
                                               @RequestParam(name = "viewType", defaultValue = "view") String viewType) {
        ResponseEntity<ResponseDTO> response = reportService.generateLabReport(patientId, labId);
        if (!response.getStatusCode().is2xxSuccessful()){
            return response;
        }
        /**
         * for generating
         */
        byte[] report = (byte[]) Objects.requireNonNull(response.getBody()).getData();
        if (viewType.equalsIgnoreCase(AppConstants.GENERATE)){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patients.pdf")
                    .body(report);
            /**
             * for viewing
             */
        }else {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=patients.pdf")
                    .body(report);
        }
    }

    @Operation(summary = "THis endpoint is used to generate prescription report")
    @GetMapping("/generate-prescription-report/{prescriptionId}")
    public ResponseEntity<?> generatePrescriptionReport(@PathVariable(name = "prescriptionId") String prescriptionId,
                                                        @RequestParam(name = "viewType", defaultValue = "view") String viewType){
        ResponseEntity<ResponseDTO> response = reportService.generatePrescriptionReport(prescriptionId);
        if (!response.getStatusCode().is2xxSuccessful()){
            return response;
        }
        /**
         * for generating
         */
        byte[] report = (byte[]) Objects.requireNonNull(response.getBody()).getData();
        if (viewType.equalsIgnoreCase(AppConstants.GENERATE)){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prescription.pdf")
                    .body(report);
            /**
             * for viewing
             */
        }else {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=prescription.pdf")
                    .body(report);
        }
    }

    @Operation(summary = "THis endpoint is used to generate prescription report")
    @GetMapping("/generate-invoice-report/{paymentId}")
    public ResponseEntity<?> generateInvoiceReport(@PathVariable(name = "paymentId") String paymentId,
                                                        @RequestParam(name = "viewType",defaultValue = "view") String viewType){
        ResponseEntity<ResponseDTO> response = reportService.generateInvoiceReport(paymentId);
        if (!response.getStatusCode().is2xxSuccessful()){
            return response;
        }
        /**
         * for generating
         */
        byte[] report = (byte[]) Objects.requireNonNull(response.getBody()).getData();
        if (viewType.equalsIgnoreCase(AppConstants.GENERATE)){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                    .body(report);
            /**
             * for viewing
             */
        }else {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice.pdf")
                    .body(report);
        }
    }


    @Operation(summary = "This endpoint is used to upload a report")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> uploadReport(@RequestParam(name = "file") MultipartFile file) {
        return reportService.uploadReport(file);
    }
}

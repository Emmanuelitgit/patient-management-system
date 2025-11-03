package patient_management_system.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import patient_management_system.dao.*;
import patient_management_system.dto.InvoiceJasperDTO;
import patient_management_system.dto.LabJasperDTO;
import patient_management_system.dto.PrescriptionJasperDTO;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.*;
import patient_management_system.service.ReportService;
import patient_management_system.util.AppUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final PatientMapper patientMapper;
    private final LabMapper labMapper;
    private final ResourceLoader resourceLoader;
    private final UserMapper userMapper;
    private final PaymentMapper paymentMapper;
    private final PrescriptionMapper prescriptionMapper;

    @Autowired
    public ReportServiceImpl(PatientMapper patientMapper, LabMapper labMapper, ResourceLoader resourceLoader, UserMapper userMapper, PaymentMapper paymentMapper, PrescriptionMapper prescriptionMapper, PrescriptionMapper prescriptionMapper1) {
        this.patientMapper = patientMapper;
        this.labMapper = labMapper;
        this.resourceLoader = resourceLoader;
        this.userMapper = userMapper;
        this.paymentMapper = paymentMapper;
        this.prescriptionMapper = prescriptionMapper1;
    }


    /**
     * @description This method is used to generate a lab report for a patient
     * @param patientId The id of the patient to generate the report for
     * @param labId The id of the specific lab record to generate the report
     * @return ResponseEntity containing the generated report(byte data) and status info
     * @auther Emmanuel Yidana
     * @createdAt 28th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> generateLabReport(String patientId, String labId) {
        try {
            ResponseDTO responseDTO;
            log.info("In generate lab report method");
            /**
             * load patient details
             */
            log.info("About to load patient details:->>{}", patientId);
            Optional<Patient> patientOptional = patientMapper.findById(patientId);
            if (patientOptional.isEmpty()){
                log.error("Patient record cannot be found:->>{}",patientId);
                responseDTO = AppUtils.getResponseDto("Patient record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * load lab details
             */
            log.info("About to load lab details");
            Optional<Lab> labOptional = labMapper.findById(labId);
            if (labOptional.isEmpty()){
                log.error("Lab record cannot be found:->>{}", labId);
                responseDTO = AppUtils.getResponseDto("Lab record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * load doctor details
             */
            log.info("About to load doctor details...");
            Optional<User> doctorOptional = userMapper.findById(labOptional.get().getDoctorId());
            if(doctorOptional.isEmpty()){
                log.error("Doctor record cannot be found:->>{}", labOptional.get().getDoctorId());
                responseDTO = AppUtils.getResponseDto("Doctor record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * fill DTO details
             */
            LabJasperDTO jasperDTO = LabJasperDTO
                    .builder()
                    .patientName(patientOptional.get().getName())
                    .doctorName(doctorOptional.get().getName())
                    .date(labOptional.get().getCreatedAt().toString())
                    .result(labOptional.get().getResult())
                    .testName(labOptional.get().getTestName())
                    .build();
            /**
             * load clinig logo
             */
            log.info("About to load clinic logo...");
            Resource logoResource = resourceLoader.getResource("classpath:images/Screenshot (116).png");
            if (!logoResource.exists()){
                log.error("Logo file cannot found");
                responseDTO = AppUtils.getResponseDto("Logo file cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load jasper file
             */
            log.info("About to load jasper file...");
            Resource resource = resourceLoader.getResource("classpath:reports/labReport.jrxml");
            if (!resource.exists()){
                log.error("Report file cannot be found");
                responseDTO = AppUtils.getResponseDto("Report file cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * compile and export report
             */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("clinicName", "PMS");
            parameters.put("clinicLogo", logoResource.getInputStream());

            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(jasperDTO));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
            byte[] report = JasperExportManager.exportReportToPdf(jasperPrint);
            /**
             * return response on success
             */
            log.info("Lab report was generated successfully");
            responseDTO = AppUtils.getResponseDto("Lab report generated successfully", HttpStatus.OK, report);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to generate an invoice for a given item
     * @param paymentId The id of the specific payment record the invoice is generating on
     * @return ResponseEntity containing the generated report(byte data) and status info
     * @auther Emmanuel Yidana
     * @createdAt 29th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> generateInvoiceReport(String paymentId) {
        try {
            ResponseDTO responseDTO;
            log.info("In generate invoice report method:->>{}", paymentId);
            /**
             * load payment record
             */
            log.info("About to load payment record...");
            Optional<Payment> paymentOptional = paymentMapper.findById(paymentId);
            if (paymentOptional.isEmpty()){
                log.error("Payment record cannot be found:->>{}", paymentId);
                responseDTO = AppUtils.getResponseDto("Payment record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.OK);
            }
            /**
             * load patient record
             */
            log.info("About to load patient details...");
            Optional<Patient> patientOptional = patientMapper.findById(paymentOptional.get().getPatientId());
            if (patientOptional.isEmpty()){
                log.error("Patient record cannot be found:->>{}", paymentOptional.get().getPatientId());
                responseDTO = AppUtils.getResponseDto("Patient record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load entity record
             */
            log.info("About to load entity...");
            Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(paymentOptional.get().getEntityId());
            Optional<Lab> labOptional = labMapper.findById(paymentOptional.get().getEntityId());
            if (prescriptionOptional.isEmpty() && labOptional.isEmpty()){
                log.error("Entity record cannot be found:->>{}", paymentOptional.get().getEntityId());
                responseDTO = AppUtils.getResponseDto("Entity record cannot be found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * build the invoice dto
             */
            InvoiceJasperDTO jasperDTO = InvoiceJasperDTO
                    .builder()
                    .patientName(patientOptional.get().getName())
                    .invoiceDate(paymentOptional.get().getCreatedAt().toString())
                    .invoiceId(paymentOptional.get().getId())
                    .amount(paymentOptional.get().getAmount())
                    .transactionId(paymentOptional.get().getTransactionId())
                    .itemName(
                            prescriptionOptional.isEmpty()?labOptional.get().getTestName()
                                    :prescriptionOptional.get().getMedication()
                    )
                    .description("Description")
                    .build();
            /**
             * load clinic logo
             */
            log.info("About to load clinic logo");
            Resource clinicLogo = resourceLoader.getResource("classpath:images/Screenshot (116).png");
            if (!clinicLogo.exists()){
                log.error("Clinic logo does not exist");
                responseDTO = AppUtils.getResponseDto("Clinic logo does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load jasper file
             */
            log.info("About to load jasper file");
            Resource jasperFile = resourceLoader.getResource("classpath:reports/invoice.jrxml");
            if (!jasperFile.exists()){
                log.error("Jasper file does not exist");
                responseDTO = AppUtils.getResponseDto("Jasper file does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * compile and export report
             */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("clinicName", "PMS");
            parameters.put("clinicLogo", clinicLogo.getInputStream());
            parameters.put("clinicAddress", "Accra, East-Legon");
            parameters.put("clinicContact", "+233597893082");
            parameters.put("title", "PMS Invoice Template");
            parameters.put("amount", paymentOptional.get().getAmount());
            parameters.put("itemName",  prescriptionOptional.isEmpty()?labOptional.get().getTestName()
                    :prescriptionOptional.get().getMedication());
            parameters.put("description", "Just testing");
            parameters.put("invoiceId", jasperDTO.getInvoiceId());
            parameters.put("invoiceDate", jasperDTO.getInvoiceDate());
            parameters.put("patientName", patientOptional.get().getName());
            parameters.put("doctorName", "Emmanuel Yidana");

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperFile.getInputStream());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Arrays.asList(jasperDTO));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
            byte[] report = JasperExportManager.exportReportToPdf(jasperPrint);

            /**
             * return response on success
             */
            log.info("Invoice report was generated successfully");
            responseDTO = AppUtils.getResponseDto("Invoice report generated successfully", HttpStatus.OK, report);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to generate a prescription report
     * @param prescriptionId The id of the specific prescription record the report is generating on
     * @return ResponseEntity containing the generated report(byte data) and status info
     * @auther Emmanuel Yidana
     * @createdAt 31st October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> generatePrescriptionReport(String prescriptionId) {
        try {
            ResponseDTO responseDTO;
            log.info("In generate prescription report method:->>{}", prescriptionId);
            /**
             * load prescription record
             */
            log.info("About to load prescription record");
            Optional<Prescription> prescriptionOptional = prescriptionMapper.findById(prescriptionId);
            if (prescriptionOptional.isEmpty()){
                log.error("Prescription record does not exist:->>{}", prescriptionId);
                responseDTO = AppUtils.getResponseDto("Prescription record does not exist",HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load patient record
             */
            log.info("About to load patient record");
            Optional<Patient> patientOptional = patientMapper.findById(prescriptionOptional.get().getPatientId());
            if (patientOptional.isEmpty()){
                log.error("Patient record does not exist:->>{}", prescriptionOptional.get().getPatientId());
                responseDTO = AppUtils.getResponseDto("Patient record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * load doctor record
             */
            log.info("About to load doctor record...");
            Optional<User> userOptional = userMapper.findById(prescriptionOptional.get().getDoctorId());
            if (userOptional.isEmpty()){
                log.error("Doctor record cannot be found:->>{}", prescriptionOptional.get().getDoctorId());
                responseDTO = AppUtils.getResponseDto("", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * build report payload
             */
            log.info("About to build report payload...");
            PrescriptionJasperDTO jasperDTO = PrescriptionJasperDTO
                    .builder()
                    .instructions(prescriptionOptional.get().getInstructions())
                    .medication(prescriptionOptional.get().getMedication())
                    .date(prescriptionOptional.get().getCreatedAt().toString())
                    .doctorName(userOptional.get().getName())
                    .patientName(patientOptional.get().getName())
                    .dosage(prescriptionOptional.get().getDosage())
                    .build();
            /**
             * load clinic logo
             */
            log.info("About to load clinic logo...");
            Resource clinicLogo = resourceLoader.getResource("classpath:images/Screenshot (116).png");
            if (!clinicLogo.exists()){
                log.error("Clinic logo does not exist");
                responseDTO = AppUtils.getResponseDto("Clinic logo does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * load jasper file
             */
            log.info("About to load jasper file...");
            Resource jasperFile = resourceLoader.getResource("classpath:reports/prescription.jrxml");
            if (!jasperFile.exists()){
                log.error("Jasper file does not exist");
                responseDTO = AppUtils.getResponseDto("Jasper file does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * compile and export
             */
            log.info("About to compile and export report");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("clinicName","PMS");
            parameters.put("clinicLogo",clinicLogo.getInputStream());

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperFile.getInputStream());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(jasperDTO));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
            byte[] report = JasperExportManager.exportReportToPdf(jasperPrint);

            /**
             * return response on success
             */
            log.info("Prescription report was generated successfully");
            responseDTO = AppUtils.getResponseDto("Prescription report was generated successfully", HttpStatus.OK, report);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @description This method is used to upload a report(word,pdf,image)
     * @param multipartFile Represents the file to be uploaded
     * @return ResponseEntity containing a message and status info
     * @auther Emmanuel Yidana
     * @createdAt 3rd November 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> uploadReport(MultipartFile multipartFile) {
        try {
            ResponseDTO responseDTO;
            log.info("In upload report method");
            /**
             *
             */
            Path directory = Paths.get("reports");
            Path fileToSavePath = Paths.get(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            if (!Files.exists(directory)){
                log.info("About to create new directory...");
                Files.createDirectories(directory);
            }
            log.info("About to upload file....");
            Path resolvedPath = directory.resolve(fileToSavePath);
            multipartFile.transferTo(resolvedPath);
            /**
             * return response on success
             */
            log.info("Report was uploaded successfully...");
            responseDTO = AppUtils.getResponseDto("Report uploaded successfully", HttpStatus.OK);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

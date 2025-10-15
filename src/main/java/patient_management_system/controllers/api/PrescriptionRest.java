package patient_management_system.controllers.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patient_management_system.serviceImpl.PrescriptionServiceImpl;

@Tag(name = "Prescription Management")
@RestController
@RequestMapping("/prescriptions")
public class PrescriptionRest {
    private final PrescriptionServiceImpl prescriptionService;

    @Autowired
    public PrescriptionRest(PrescriptionServiceImpl prescriptionService) {
        this.prescriptionService = prescriptionService;
    }
}

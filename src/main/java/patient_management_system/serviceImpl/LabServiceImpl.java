package patient_management_system.serviceImpl;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patient_management_system.dao.LabMapper;
import patient_management_system.dto.ResponseDTO;
import patient_management_system.models.Lab;
import patient_management_system.service.LabService;
import patient_management_system.util.AppUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LabServiceImpl implements LabService {
    private final LabMapper labMapper;

    @Autowired
    public LabServiceImpl(LabMapper labMapper) {
        this.labMapper = labMapper;
    }

    /**
     * @description This method is used to fetch all labs from the db
     * @return ResponseEntity containing the retrieved labs and status info
     * @auther Emmanuel Yidana
     * @createdAt 17th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(String search, Integer size, Integer page) {
        try{
            ResponseDTO responseDTO;
            log.info("In get all lab records method");
            /**
             * load records from db
             */
            if (page<=0){
                page=1;
            }
            Integer offset = (page-1)*size;
            log.info("About to load lab records from db...");
            List<Lab> labs = labMapper.findAll(search,size,offset);
            if (labs.isEmpty()){
                log.error("No lab record found");
                responseDTO = AppUtils.getResponseDto("No lab record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Labs was loaded successfully");
            responseDTO = AppUtils.getResponseDto("Labs fetched successfully", HttpStatus.OK,labs);
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to retrieve lab record by id
     * @param id The id of the lab record to be retrieved
     * @return ResponseEntity containing the retrieved lab and status info
     * @auther Emmanuel Yidana
     * @createdAt 17th October 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findById(String id) {
        try {
            log.info("In get lab record by id method:->>{}", id);
            ResponseDTO responseDTO;
            /**
             * load lab record from db
             */
            log.info("About to load lab record from db...");
            Optional<Lab> labOptional = labMapper.findById(id);
            if (labOptional.isEmpty()){
                log.error("Lab record does not exist:->>{}",id);
                responseDTO = AppUtils.getResponseDto("Lab record does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
            }
            /**
             * return response on success
             */
            log.info("Lab record was retrieved successfully:->>{}", labOptional.get());
            responseDTO = AppUtils.getResponseDto("Lab record was retrieved successfully",HttpStatus.OK,labOptional.get());
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);

        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> addLab(Lab lab) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updateById(Lab lab) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> deleteById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> fetchLabsForDoctor(String search, Integer size, Integer page, String doctorId) {
        return null;
    }
}

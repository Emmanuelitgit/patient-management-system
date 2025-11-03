package patient_management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import patient_management_system.models.Payment;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PaymentMapper {
    List<Payment> findAll(String search, Integer limit, Integer offset);
    Optional<Payment> findById(String id);
    Integer addPayment(Payment payment);
    Integer updateById(Payment payment);
    Integer deleteById(String id);
    Integer countAllLabRecords();
    Optional<Payment> findByReference(String referenceNumber);
    Optional<Payment> findByEntityId(String entityId);
    Optional<Payment> getInvoiceDetails(String paymentId);
}

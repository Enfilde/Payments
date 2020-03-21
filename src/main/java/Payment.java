import Exceptions.*;

import java.time.*;

public class Payment {

   private Organization senderOrganization,recieverOrganization;
   private String uniqueCode;
   private String paymentPurpose;
   private LocalDateTime paymentDateTime;
   private double paymentSum;

   public Payment(Organization senderOrganization, Organization recieverOrganization, String uniqueCode, String paymentPurpose,LocalDateTime paymentDateTime, double paymentSum) throws OrganizationAndPaymentsException {

      if(senderOrganization.getOrganizationName().equals(recieverOrganization.getOrganizationName())) throw new PaymentForItselfException();
      if(uniqueCode.isEmpty()) throw new EmptyPaymentIdException();
      if(paymentPurpose.isEmpty()) throw new EmptyPaymentPurposeException();
      if(paymentDateTime.isAfter(LocalDateTime.now())) throw new InvalidPaymentTimeException();
      if (paymentSum <= 0) throw new InvalidPaymentSumException();

      this.uniqueCode = uniqueCode;
      this.paymentPurpose = paymentPurpose;
      this.paymentDateTime = paymentDateTime;
      this.paymentSum = paymentSum;
      this.senderOrganization = senderOrganization;
      this.recieverOrganization = recieverOrganization;

   }

   public Organization getSenderOrganization() {
      return senderOrganization;
   }

   public Organization getRecieverOrganization() {
      return recieverOrganization;
   }

   public String getUniqueCode() {
      return uniqueCode;
   }

   public String getPaymentPurpose() {
      return paymentPurpose;
   }

   public LocalDateTime getPaymentDateTime() {
      return paymentDateTime;
   }

   public double getPaymentSum() {
      return paymentSum;
   }

}

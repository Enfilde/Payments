import Exceptions.*;

import java.time.*;

public class Payment {

   private Organization senderName,recieverName;
   private String uniqueCode;
   private String paymentPurpose;
   private LocalDateTime paymentDateTime;
   private double paymentSum;

   public Payment(String senderName, String recieverName, String uniqueCode, String paymentPurpose,LocalDateTime paymentDateTime, double paymentSum) throws OrganizationAndPaymentsException {

      if(senderName.equals(recieverName)) throw new PaymentForItselfException();
      if(uniqueCode.isEmpty()) throw new EmptyPaymentIdException();
      if(paymentPurpose.isEmpty()) throw new EmptyPaymentPurposeException();
      if(paymentDateTime.isAfter(LocalDateTime.now())) throw new InvalidPaymentTimeException();
      if (paymentSum <= 0) throw new InvalidPaymentSumException();

      this.uniqueCode = uniqueCode;
      this.paymentPurpose = paymentPurpose;
      this.paymentDateTime = paymentDateTime;
      this.paymentSum = paymentSum;

   }


   public Organization getSenderName() {
      return senderName;
   }

   public Organization getRecieverName() {
      return recieverName;
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

import Exceptions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {


    public enum Found {
        FALSE,
        TRUE,
        HALFTRUE,
    }


    private ArrayList<Payment> payments = new ArrayList<>();
    private ArrayList<Organization> organizations = new ArrayList<>();


    public void addPayment(String senderName, String recieverName, String uniqueCode, String paymentPurpose, LocalDateTime paymentDateTime, double paymentSum) throws OrganizationAndPaymentsException {
        Found organizationFound = Found.FALSE;
        Organization senderOrganization = null;
        Organization recieverOrganization = null;
        for (Payment x : payments) {
            if (x.getUniqueCode().equals(uniqueCode)) throw new DuplicatePaymentException();
        }
        for (Organization x : organizations) {
            if (x.getOrganizationName().equals(recieverName)) {
                organizationFound = Found.HALFTRUE;
                recieverOrganization = x;
                x.setBalance(x.getBalance()+paymentSum);
                break;
            }
        }
        for (Organization x : organizations) {
            if (x.getOrganizationName().equals(senderName) && organizationFound == Found.HALFTRUE) {
                senderOrganization = x;
                organizationFound = Found.TRUE;
                x.setBalance(x.getBalance()-paymentSum);
                break;
            }
        }
        if (organizationFound != Found.TRUE) throw new OrganizationNotFoundException();
        payments.add(new Payment(senderOrganization, recieverOrganization, uniqueCode, paymentPurpose, paymentDateTime, paymentSum));
    }


    public String getPaymentPurpose(String paymentId) throws OrganizationAndPaymentsException {
        String paymentPurpose = null;
        boolean paymentFound = false;
        for (Payment x : payments) {
            if (x.getUniqueCode().equals(paymentId)) {
                paymentPurpose = x.getPaymentPurpose();
                paymentFound = true;
            }
        }
        if (!paymentFound) throw new NotFoundPaymentException();
        return paymentPurpose;
    }


    public LocalDateTime getPaymentDateTime(String paymentId) throws OrganizationAndPaymentsException {
        LocalDateTime date = null;
        boolean checker = false;
        for (Payment x : payments) {
            if (x.getUniqueCode().equals(paymentId)) {
                date = x.getPaymentDateTime();
                checker = true;
            }
        }
        if (!checker) throw new NotFoundPaymentException();
        return date;
    }


    public double getPaymentSum(String paymentId) throws OrganizationAndPaymentsException {
        double sum = 0;
        boolean paymentFound = false;
        for (Payment x : payments) {
            if (x.getUniqueCode().equals(paymentId)) {
                sum = x.getPaymentSum();
                paymentFound = true;
            }
        }
        if (!paymentFound) throw new NotFoundPaymentException();
        return sum;
    }


    public String getSenderName(String paymentId) throws OrganizationAndPaymentsException {
        String senderName = null;
        boolean paymentFound = false;
        for (Payment x : payments) {
            if (x.getUniqueCode().equals(paymentId)) {
                senderName = x.getSenderOrganization().getOrganizationName();
                paymentFound = true;
            }
        }
        if (!paymentFound) throw new NotFoundPaymentException();
        return senderName;
    }


    public String getRecieverName(String paymentId) throws OrganizationAndPaymentsException {
        String recieverName = null;
        boolean paymentFound = false;
        for (Payment x : payments) {
            if (x.getUniqueCode().equals(paymentId)) {
                recieverName = x.getRecieverOrganiaton().getOrganizationName();
                paymentFound = true;
            }
        }
        if (!paymentFound) throw new NotFoundPaymentException();
        return recieverName;
    }


    public void addOrganization(String name, String registrationNumber, double initialBalance) throws OrganizationAndPaymentsException {
        for (Organization x : organizations) {
            if (x.getOrganizationName().equals(name)) throw new DuplicateOrganizationException();
            else if (x.getRegistrationNumber().equals(registrationNumber)) throw new DuplicateNumberException();
        }
        organizations.add(new Organization(name, registrationNumber, initialBalance));

    }


    public String getRegistrationNumber(String organizationName) throws OrganizationAndPaymentsException {
        for (Organization x : organizations) {
            if (x.getOrganizationName().equals(organizationName)) return x.getRegistrationNumber();
        }
        throw new OrganizationNotFoundException();

    }


    public double getBalance(String organizationName) throws OrganizationAndPaymentsException {
        for (Organization x : organizations) {
            if (x.getOrganizationName().equals(organizationName)) return x.getBalance();
        }
        throw new OrganizationNotFoundException();
    }


    public String getIdOfBiggestPayment() {
        double highestPayment = 0;
        String paymentId = null;
        for (Payment x : payments) {
            if (x.getPaymentSum() > highestPayment) {
                highestPayment = x.getPaymentSum();
                paymentId = x.getUniqueCode();
            }
        }
        return paymentId;
    }


    public LocalDate getDateWithBiggestTotalPayments() {
        double totalPayments = 0;
        double biggestTotalPayment = 0;
        LocalDate biggestTotalPaymentDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Payment x : payments) {
            for(Payment y: payments){
                if(formatter.format(x.getPaymentDateTime()).equals(formatter.format(y.getPaymentDateTime()))){
                    totalPayments += x.getPaymentSum();
                }
            }
            if(totalPayments > biggestTotalPayment) {
                biggestTotalPayment = totalPayments;
                biggestTotalPaymentDate = x.getPaymentDateTime().toLocalDate();
            }
            totalPayments = 0;
        }
        return biggestTotalPaymentDate;
    }


    public HashMap<String, Double> getFinalBalances() {
        HashMap<String, Double> currentBalanceMap = new HashMap<>();
        for (Organization x : organizations) {
            currentBalanceMap.put(x.getOrganizationName(), x.getBalance());
        }
        return currentBalanceMap;
    }


    public ArrayList<String> getOrganizationsWithNegativeSaldo() {
        ArrayList<String> negativeBalanceList = new ArrayList<>();
        for (Organization x : organizations) {
            if (x.getBalance() < 0) negativeBalanceList.add(x.getOrganizationName());
        }
        return negativeBalanceList;
    }
}



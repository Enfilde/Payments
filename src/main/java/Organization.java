import Exceptions.EmptyNameException;
import Exceptions.InvalidNumberException;
import Exceptions.OrganizationAndPaymentsException;

import java.util.ArrayList;

public class Organization {

    private String organizationName;
    private String registrationNumber;
    private double balance;
    private ArrayList<Payment> incomingPayments;
    private ArrayList<Payment> outgoingPayments;

    public Organization(String organizationName,String registrationNumber,double balance) throws OrganizationAndPaymentsException {

        if(organizationName.isEmpty()) throw new EmptyNameException();
        if(registrationNumber.length() > 8) throw new InvalidNumberException();
        this.organizationName = organizationName;
        this.registrationNumber = registrationNumber;
        this.balance = balance;

    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public double getBalance() {
        return balance;
    }
}
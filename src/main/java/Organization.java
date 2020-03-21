import Exceptions.EmptyOrganizationNameException;
import Exceptions.InvalidRegistrationNumberFormatException;
import Exceptions.OrganizationAndPaymentsException;

import java.util.ArrayList;

public class Organization {

    private String  organizationName;
    private String registrationNumber;
    private double balance;


    public Organization(String organizationName,String registrationNumber,double balance) throws OrganizationAndPaymentsException {

        if(organizationName.isEmpty()) throw new EmptyOrganizationNameException();
        if(registrationNumber.length() > 8) throw new InvalidRegistrationNumberFormatException();
        this.organizationName = organizationName;
        this.registrationNumber = registrationNumber;
        this.balance = balance;

    }
    //val Number


    public String getOrganizationName() {
        return organizationName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }


    public double getBalance() {
        return balance;
    }


    public void setBalance(double v) {
    }
}
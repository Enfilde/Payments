import Exceptions.EmptyOrganizationNameException;
import Exceptions.InvalidRegistrationNumberFormatException;
import Exceptions.OrganizationAndPaymentsException;

import java.util.ArrayList;
import java.util.regex.*;

public class Organization {

    private String  organizationName;
    private String registrationNumber;
    private double balance;


    public Organization(String organizationName,String registrationNumber,double balance) throws OrganizationAndPaymentsException {

        if(organizationName.isEmpty()) throw new EmptyOrganizationNameException();
        if(!valNumber(registrationNumber)) throw new InvalidRegistrationNumberFormatException();
        this.organizationName = organizationName;
        this.registrationNumber = registrationNumber;
        this.balance = balance;

    }
    public static boolean valNumber(String input) {
        String phoneRegex = "[0-9]{8}";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Matcher matcher = phonePattern.matcher(input);
        return matcher.find();
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

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
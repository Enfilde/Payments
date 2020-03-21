import Exceptions.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PaymentTest {

    @Test
    public void paymentsOrganizationOne() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("exception", "00000001", 5569);

        Assert.assertEquals("00000001", c.getRegistrationNumber("exception"));
        Assert.assertEquals(5569.0, c.getBalance("exception"),1.0);

    }


    @Test
    public void paymentsOrganizationSeveral() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("whiskas", "00000003", 18);
        c.addOrganization("hello-kitty", "00000055", 79);

        Assert.assertEquals("00000003", c.getRegistrationNumber("whiskas"));
        Assert.assertEquals("00000055", c.getRegistrationNumber("hello-kitty"));
        Assert.assertEquals(79, c.getBalance("hello-kitty"),1.0);

    }


    @Test(expected = EmptyOrganizationNameException.class)
    public void organizationWithEmptyName() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("", "00000055", 79);

    }


    @Test(expected = InvalidRegistrationNumberFormatException.class)
    public void organizationWithEmptyRegistrationNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("whiskas", "", 79);

    }


    @Test(expected = DuplicateOrganizationException.class)
    public void organizationWithDuplicateName() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("test", "00000000", 18);
        c.addOrganization("test", "11111111", 43);

    }


    @Test(expected = DuplicateRegistrationNumberException.class)
    public void organizationWithDuplicateRegistrationNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);
        c.addOrganization("test #2", "00000000", 43);

    }


    @Test
    public void organizationWithBadRegistrationNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        try {
            c.addOrganization("test", "0000000", 18);
            Assert.fail("InvalidRegistrationNumberFormat");
        } catch (InvalidRegistrationNumberFormatException e) {
            Assert.assertNotEquals("", e.getMessage());
        }

        try {
            c.addOrganization("test", "000000000", 18);
            Assert.fail("InvalidRegistrationNumberFormat");
        } catch (InvalidRegistrationNumberFormatException e) {
            Assert.assertNotEquals("", e.getMessage());
        }

        try {
            c.addOrganization("test", "0000000A", 18);
            Assert.fail("InvalidRegistrationNumberFormat");
        } catch (InvalidRegistrationNumberFormatException e) {
            Assert.assertNotEquals("InvalidRegistrationNumberFormat", e.getMessage());
        }

        try {
            c.addOrganization("test", "elephant", 18);
            Assert.fail("InvalidRegistrationNumberFormat");
        } catch (InvalidRegistrationNumberFormatException e) {
            Assert.assertNotEquals("", e.getMessage());
        }

        try {
            c.addOrganization("test", "    ", 18);
            Assert.fail("InvalidRegistrationNumberFormat");
        } catch (InvalidRegistrationNumberFormatException e) {
            Assert.assertNotEquals("", e.getMessage());
        }

        try {
            c.addOrganization("test", "OOOOOOOO", 18);
        } catch (InvalidRegistrationNumberFormatException e) {
            Assert.assertEquals("InvalidRegistrationNumberFormat", e.getMessage());
        }
    }


    @Test
    public void organizationWithNegativeBalance() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("own", "00000001", -100);

        Assert.assertEquals(-100, c.getBalance("own"),1.0);

    }


    @Test
    public void organizationWithZeroBalance() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("own", "00000001", 0);

        Assert.assertEquals(0, c.getBalance("own"),1.0);

    }


    @Test(expected = OrganizationCannotBeFoundException.class)
    public void organizationObtainRegistrationNumberByEmptyName() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);
        c.getRegistrationNumber("");

    }


    @Test(expected = OrganizationCannotBeFoundException.class)
    public void organizationObtainRegistrationNumberForMissingOrganization() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);
        c.getRegistrationNumber("test #2");

    }


    @Test(expected = OrganizationCannotBeFoundException.class)
    public void organizationObtainBalanceByEmptyName() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);

        c.getBalance("");

    }


    @Test(expected = OrganizationCannotBeFoundException.class)
    public void organizationObtainBalanceForMissingOrganization() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);

        c.getBalance("test #2");

    }


    @Test
    public void paymentOne() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime localDateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", localDateTime, 69);

        Assert.assertEquals("mystic", c.getSenderName("1"));
        Assert.assertEquals("moon", c.getRecieverName("1"));
        Assert.assertEquals("Just for fun!", c.getPaymentPurpose("1"));
        Assert.assertEquals(localDateTime, c.getPaymentDateTime("1"));
        Assert.assertEquals(69, c.getPaymentSum("1"),1.0);

    }


    @Test
    public void paymentSeveralForDifferentOrganizations() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);
        c.addOrganization("sausages", "22334455", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime1 = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "556", "Rrrrr!", dateTime1, 12);
        LocalDateTime dateTime2 = LocalDateTime.of(2013, 2, 18, 16, 44, 44);
        c.addPayment("sausages", "kitty", "37", "Mew", dateTime2, 13);

        Assert.assertEquals("mystic", c.getSenderName("556"));
        Assert.assertEquals("moon", c.getRecieverName("556"));
        Assert.assertEquals("Rrrrr!", c.getPaymentPurpose("556"));
        Assert.assertEquals(dateTime1, c.getPaymentDateTime("556"));
        Assert.assertEquals(12, c.getPaymentSum("556"),1.0);

        Assert.assertEquals("sausages", c.getSenderName("37"));
        Assert.assertEquals("kitty", c.getRecieverName("37"));
        Assert.assertEquals("Mew", c.getPaymentPurpose("37"));
        Assert.assertEquals(dateTime2, c.getPaymentDateTime("37"));
        Assert.assertEquals(13, c.getPaymentSum("37"),1.0);

    }


    @Test
    public void paymentSeveralForOneSender() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("moon", "12345678", 999);
        c.addOrganization("sausages", "22334455", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime1 = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("sausages", "moon", "556", "Rrrrr!", dateTime1, 12);
        LocalDateTime dateTime2 = LocalDateTime.of(2013, 2, 18, 16, 44, 44);
        c.addPayment("sausages", "kitty", "37", "Mew", dateTime2, 13);

        Assert.assertEquals("sausages", c.getSenderName("556"));
        Assert.assertEquals("moon", c.getRecieverName("556"));
        Assert.assertEquals("Rrrrr!", c.getPaymentPurpose("556"));
        Assert.assertEquals(dateTime1, c.getPaymentDateTime("556"));
        Assert.assertEquals(12, c.getPaymentSum("556"),1.0);

        Assert.assertEquals("sausages", c.getSenderName("37"));
        Assert.assertEquals("kitty", c.getRecieverName("37"));
        Assert.assertEquals("Mew", c.getPaymentPurpose("37"));
        Assert.assertEquals(dateTime2, c.getPaymentDateTime("37"));
        Assert.assertEquals(13, c.getPaymentSum("37"),1.0);

    }


    @Test
    public void paymentSeveralForOneReceiver() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime1 = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2013, 2, 18, 16, 44, 44);

        c.addPayment("mystic", "kitty", "556", "Rrrrr!", dateTime1, 12);
        c.addPayment("sausages", "kitty", "37", "Mew", dateTime2, 13);

        Assert.assertEquals("mystic", c.getSenderName("556"));
        Assert.assertEquals("kitty", c.getRecieverName("556"));
        Assert.assertEquals("Rrrrr!", c.getPaymentPurpose("556"));
        Assert.assertEquals(dateTime1, c.getPaymentDateTime("556"));
        Assert.assertEquals(12, c.getPaymentSum("556"),1.0);

        Assert.assertEquals("sausages", c.getSenderName("37"));
        Assert.assertEquals("kitty", c.getRecieverName("37"));
        Assert.assertEquals("Mew", c.getPaymentPurpose("37"));
        Assert.assertEquals(dateTime2, c.getPaymentDateTime("37"));
        Assert.assertEquals(13, c.getPaymentSum("37"),1.0);

    }

    @Test(expected = OrganizationCannotBeFoundException.class)
    public void paymentWithEmptySenderName() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.addPayment("", "kitty", "1", "Just for fun!", dateTime, 69);

    }


    @Test(expected = OrganizationAndPaymentsException.class)
    public void paymentWithEmptyRecieverName() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("sausages", "", "1", "Just for fun!", dateTime, 69);

    }


    @Test(expected = EmptyPaymentIdException.class)
    public void paymentWithEmptyUniqueNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.addPayment("sausages", "kitty", "", "Just for fun!", dateTime, 69);

    }


    @Test(expected = EmptyPaymentPurposeException.class)
    public void paymentWithEmptyPurpose() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.addPayment("sausages", "kitty", "1", "", dateTime, 69);

    }


    @Test(expected = DuplicatePaymentException.class)
    public void paymentWithDuplicateUniqueNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000100", 122);
        c.addOrganization("moon", "12345578", 133);
        c.addOrganization("sausages", "22334456", 1);
        c.addOrganization("kitty", "00991234", 1);

        LocalDateTime dateTime1 = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2015, 1, 1, 12, 0, 1);

        c.addPayment("mystic", "kitty", "1", "Purpose #1", dateTime1, 67);
        c.addPayment("sausages", "moon", "1", "Purpose #2", dateTime2, 50);

    }


    @Test
    public void paymentWithDuplicatePurpose() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("mystic", "00000100", 122);
        c.addOrganization("moon", "12345578", 133);
        c.addOrganization("sausages", "22334456", 1);
        c.addOrganization("kitty", "00991234", 1);

        LocalDateTime dateTime1 = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2015, 1, 1, 12, 0, 1);
        c.addPayment("mystic", "kitty", "1", "Purpose #1", dateTime1, 67);
        c.addPayment("sausages", "moon", "2", "Purpose #1", dateTime2, 50);

    }


    @Test(expected = InvalidPaymentTimeException.class)
    public void paymentWithTimeInTheFuture() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.of(2093, 7, 19, 11, 0, 0);
        c.addPayment("testFrom", "testTo", "88", "purpose", dateTime, 99);

    }


    @Test
    public void paymentWithCurrentTime() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime localDateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "88", "purpose", localDateTime, 99);

    }


    @Test
    public void paymentTwoPaymentsWithTheSameTime() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime localDateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "88", "purpose", localDateTime, 99);
        c.addPayment("testFrom", "testTo", "89", "purpose", localDateTime, 99);

    }


    @Test
    public void paymentWithCustomCharsInUniqueNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "AAA", "purpose", dateTime, 34);
        c.addPayment("testFrom", "testTo", "%$#@", "purpose", dateTime, 34);
        c.addPayment("testFrom", "testTo", "00DD88", "purpose", dateTime, 34);
        c.addPayment("testFrom", "testTo", "-", "purpose", dateTime, 34);
        c.addPayment("testFrom", "testTo", "bn", "purpose", dateTime, 34);
        c.addPayment("testFrom", "testTo", "[]", "purpose", dateTime, 34);

    }


    @Test(expected = InvalidPaymentSumException.class)
    public void paymentWithNegativeSum() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "88", "purpose", dateTime, -2);

    }


    @Test(expected = InvalidPaymentSumException.class)
    public void paymentWithZeroSum() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "88", "purpose", dateTime, 0);

    }


    @Test(expected = PaymentForItselfException.class)
    public void paymentForItself() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testFrom", "88", "purpose", dateTime, 2);

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainSenderNameByEmptyNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getSenderName("");

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainSenderNameForMissingNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getSenderName("2");

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainReceiverNameByEmptyNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);
        c.getRecieverName("");

    }

    @Test(expected = PaymentCannotBeFoundException.class)
    public void PaymentObtainPurposeByEmptyNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getPaymentPurpose("");

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainReceiverNameForMissingNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getRecieverName("2");

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainPurposeByEmptyNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);
        c.getPaymentPurpose("");

    }

    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainPurposeForMissingNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);
        c.getPaymentPurpose("2");

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainDatetimeByEmptyNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getPaymentDateTime("");

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainDateTimeForMissingNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.getPaymentDateTime("2");

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainSumByEmptyNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getPaymentSum("");

    }


    @Test(expected = PaymentCannotBeFoundException.class)
    public void paymentObtainSumForMissingNumber() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getPaymentSum("2");

    }

    public LocalDateTime generateModel(int year,int month,int day) {

        int hours = (int) (Math.random()*24);
        int minutes = (int) (Math.random()*60);
        int seconds = (int) (Math.random()*60);

        return LocalDateTime.of(year,month,day,hours,minutes,seconds);

    }

    @Test
    public void queriesGetFinalBalances() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization( "Yoha", "00110030", 0 );
        c.addOrganization( "Toto", "00110047", 100 );
        c.addOrganization( "Rute", "00110134", -25 );
        c.addOrganization( "Gofi", "00120267", 45 );
        c.addOrganization( "Qwer", "01003401", 20 );

        c.addPayment( "Yoha", "Toto", "01", "payment #01", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Rute", "02", "payment #02", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Gofi", "03", "payment #03", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Qwer", "04", "payment #04", generateModel( 2010, 1, 1 ), 21 );

        c.addPayment( "Toto", "Yoha", "05", "payment #05", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Rute", "06", "payment #06", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Gofi", "07", "payment #07", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Qwer", "08", "payment #08", generateModel( 2010, 2, 1 ), 21 );

        c.addPayment( "Rute", "Yoha", "09", "payment #09", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Toto", "10", "payment #10", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Gofi", "11", "payment #11", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Qwer", "12", "payment #12", generateModel( 2010, 1, 2 ), 21 );

        c.addPayment( "Gofi", "Yoha", "13", "payment #13", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Toto", "14", "payment #14", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Rute", "15", "payment #15", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Qwer", "16", "payment #16", generateModel( 2010, 3, 1 ), 21 );

        c.addPayment( "Qwer", "Yoha", "17", "payment #17", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Toto", "18", "payment #18", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Rute", "19", "payment #19", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Gofi", "20", "payment #20", generateModel( 2010, 1, 3 ), 21 );

        c.addPayment( "Qwer", "Rute", "21", "payment #21", generateModel( 2010, 4, 1 ), 50 );
        c.addPayment( "Gofi", "Yoha", "22", "payment #22", generateModel( 2010, 4, 1 ), 25 );
        c.addPayment( "Gofi", "Toto", "23", "payment #23", generateModel( 2010, 4, 1 ), 20 );

        HashMap<String,Double> expectation = new HashMap<>();
        expectation.put("Gofi", 0.0);
        expectation.put("Qwer", -30.0);
        expectation.put("Rute", 25.0);
        expectation.put("Toto", 120.0);
        expectation.put( "Yoha", 25.0);
        Assert.assertEquals(expectation,c.getFinalBalances());
    }


    @Test
    public void queriesGetOrganizationsWithNegativeSaldo() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization( "Yoha", "00110030", 0 );
        c.addOrganization( "Toto", "00110047", 100 );
        c.addOrganization( "Rute", "00110134", -25 );
        c.addOrganization( "Gofi", "00120267", 45 );
        c.addOrganization( "Qwer", "01003401", 20 );

        c.addPayment( "Yoha", "Toto", "01", "payment #01", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Rute", "02", "payment #02", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Gofi", "03", "payment #03", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Qwer", "04", "payment #04", generateModel( 2010, 1, 1 ), 21 );

        c.addPayment( "Toto", "Yoha", "05", "payment #05", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Rute", "06", "payment #06", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Gofi", "07", "payment #07", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Qwer", "08", "payment #08", generateModel( 2010, 2, 1 ), 21 );

        c.addPayment( "Rute", "Yoha", "09", "payment #09", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Toto", "10", "payment #10", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Gofi", "11", "payment #11", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Qwer", "12", "payment #12", generateModel( 2010, 1, 2 ), 21 );

        c.addPayment( "Gofi", "Yoha", "13", "payment #13", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Toto", "14", "payment #14", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Rute", "15", "payment #15", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Qwer", "16", "payment #16", generateModel( 2010, 3, 1 ), 21 );

        c.addPayment( "Qwer", "Yoha", "17", "payment #17", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Toto", "18", "payment #18", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Rute", "19", "payment #19", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Gofi", "20", "payment #20", generateModel( 2010, 1, 3 ), 21 );

        c.addPayment( "Qwer", "Rute", "21", "payment #21", generateModel( 2010, 4, 1 ), 50 );
        c.addPayment( "Gofi", "Yoha", "22", "payment #22", generateModel( 2010, 4, 1 ), 25 );
        c.addPayment( "Gofi", "Toto", "23", "payment #23", generateModel( 2010, 4, 1 ), 20 );

        List<String> exceptions = Arrays.asList("Qwer");
        Assert.assertEquals(exceptions,c.getOrganizationsWithNegativeSaldo());

    }


    @Test
    public void queriesGetBiggestPaymentData() throws OrganizationAndPaymentsException {

        Controller c = new Controller();
        c.addOrganization( "Yoha", "00110030", 0 );
        c.addOrganization( "Toto", "00110047", 100 );
        c.addOrganization( "Rute", "00110134", -25 );
        c.addOrganization( "Gofi", "00120267", 45 );
        c.addOrganization( "Qwer", "01003401", 20 );

        c.addPayment( "Yoha", "Toto", "01", "payment #01", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Rute", "02", "payment #02", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Gofi", "03", "payment #03", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Qwer", "04", "payment #04", generateModel( 2010, 1, 1 ), 21 );

        c.addPayment( "Toto", "Yoha", "05", "payment #05", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Rute", "06", "payment #06", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Gofi", "07", "payment #07", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Qwer", "08", "payment #08", generateModel( 2010, 2, 1 ), 21 );

        c.addPayment( "Rute", "Yoha", "09", "payment #09", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Toto", "10", "payment #10", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Gofi", "11", "payment #11", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Qwer", "12", "payment #12", generateModel( 2010, 1, 2 ), 21 );

        c.addPayment( "Gofi", "Yoha", "13", "payment #13", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Toto", "14", "payment #14", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Rute", "15", "payment #15", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Qwer", "16", "payment #16", generateModel( 2010, 3, 1 ), 21 );

        c.addPayment( "Qwer", "Yoha", "17", "payment #17", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Toto", "18", "payment #18", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Rute", "19", "payment #19", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Gofi", "20", "payment #20", generateModel( 2010, 1, 3 ), 21 );

        c.addPayment( "Qwer", "Rute", "21", "payment #21", generateModel( 2010, 4, 1 ), 50 );
        c.addPayment( "Gofi", "Yoha", "22", "payment #22", generateModel( 2010, 4, 1 ), 25 );
        c.addPayment( "Gofi", "Toto", "23", "payment #23", generateModel( 2010, 4, 1 ), 20 );


        Assert.assertEquals("21",c.getIdOfBiggestPayment());

    }

    @Test
    public void queriesGetDateWithBiggestTotalPaymentsAmount() throws OrganizationAndPaymentsException {

        Controller c = new Controller();

        c.addOrganization( "Yoha", "00110030", 0 );
        c.addOrganization( "Toto", "00110047", 100 );
        c.addOrganization( "Rute", "00110134", -25 );
        c.addOrganization( "Gofi", "00120267", 45 );
        c.addOrganization( "Qwer", "01003401", 20 );

        c.addPayment( "Yoha", "Toto", "01", "payment #01", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Rute", "02", "payment #02", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Gofi", "03", "payment #03", generateModel( 2010, 1, 1 ), 21 );
        c.addPayment( "Yoha", "Qwer", "04", "payment #04", generateModel( 2010, 1, 1 ), 21 );

        c.addPayment( "Toto", "Yoha", "05", "payment #05", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Rute", "06", "payment #06", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Gofi", "07", "payment #07", generateModel( 2010, 2, 1 ), 21 );
        c.addPayment( "Toto", "Qwer", "08", "payment #08", generateModel( 2010, 2, 1 ), 21 );

        c.addPayment( "Rute", "Yoha", "09", "payment #09", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Toto", "10", "payment #10", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Gofi", "11", "payment #11", generateModel( 2010, 1, 2 ), 21 );
        c.addPayment( "Rute", "Qwer", "12", "payment #12", generateModel( 2010, 1, 2 ), 21 );

        c.addPayment( "Gofi", "Yoha", "13", "payment #13", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Toto", "14", "payment #14", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Rute", "15", "payment #15", generateModel( 2010, 3, 1 ), 21 );
        c.addPayment( "Gofi", "Qwer", "16", "payment #16", generateModel( 2010, 3, 1 ), 21 );

        c.addPayment( "Qwer", "Yoha", "17", "payment #17", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Toto", "18", "payment #18", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Rute", "19", "payment #19", generateModel( 2010, 1, 3 ), 21 );
        c.addPayment( "Qwer", "Gofi", "20", "payment #20", generateModel( 2010, 1, 3 ), 21 );

        c.addPayment( "Qwer", "Rute", "21", "payment #21", generateModel( 2010, 4, 1 ), 50 );
        c.addPayment( "Gofi", "Yoha", "22", "payment #22", generateModel( 2010, 4, 1 ), 25 );
        c.addPayment( "Gofi", "Toto", "23", "payment #23", generateModel( 2010, 4, 1 ), 20 );


        LocalDate exceptions = LocalDate.of(2010, 4, 1);
        Assert.assertEquals(exceptions,c.getDateWithBiggestTotalPayments());

    }
    
}

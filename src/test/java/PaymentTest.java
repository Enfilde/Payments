import Exceptions.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;

public class PaymentTest {

    @Test
    public void paymentsOrganizationOne() {

        Controller c = new Controller();
        c.addOrganization("exception", "00000001", 5569);

        Assert.assertEquals("00000001", c.getRecipientName("exception"));
        Assert.assertEquals(5569.0, c.getBalance("exception"));

    }


    @Test
    public void paymentsOrganizationSeveral() {

        Controller c = new Controller();
        c.addOrganization("whiskas", "00000003", 18);
        c.addOrganization("hello-kitty", "00000055", 79);

        Assert.assertEquals("00000003", c.getRegistrationNumber("whiskas"));
        Assert.assertEquals("00000055", c.getRegistrationNumber("hello-kitty"));
        Assert.assertEquals(79, c.getBalance("hello-kitty"));

    }


    @Test(expected = EmptyNameException.class)
    public void organizationWithEmptyName() {

        Controller c = new Controller();
        c.addOrganization("", "00000055", 79);

    }


    @Test(expected = InvalidNumberException.class)
    public void organizationWithEmptyRegistrationNumber() {

        Controller c = new Controller();
        c.addOrganization("whiskas", "", 79);

    }


    @Test(expected = DuplicateOrganizationException.class)
    public void organizationWithDuplicateName() {

        Controller c = new Controller();
        c.addOrganization("test", "00000000", 18);
        c.addOrganization("test", "11111111", 43);

    }


    @Test(expected = DuplicateNumberException.class)
    public void organizationWithDuplicateRegistrationNumber() {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);
        c.addOrganization("test #2", "00000000", 43);

    }


    @Test
    public void organizationWithBadRegistrationNumber() {

        Controller c = new Controller();

        try {
            c.addOrganization("test", "0000000", 18);
        } catch (InvalidNumberException e) {
            Assert.assertEquals("InvalidRegistrationNumberFormat", e.getMessage());
        }

        try {
            c.addOrganization("test", "000000000", 18);
        } catch (InvalidNumberException e) {
            Assert.assertEquals("InvalidRegistrationNumberFormat", e.getMessage());
        }

        try {
            c.addOrganization("test", "0000000A", 18);
        } catch (InvalidNumberException e) {
            Assert.assertEquals("InvalidRegistrationNumberFormat", e.getMessage());
        }

        try {
            c.addOrganization("test", "elephant", 18);
        } catch (InvalidNumberException e) {
            Assert.assertEquals("InvalidRegistrationNumberFormat", e.getMessage());
        }

        try {
            c.addOrganization("test", "    ", 18);
        } catch (InvalidNumberException e) {
            Assert.assertEquals("InvalidRegistrationNumberFormat", e.getMessage());
        }

        try {
            c.addOrganization("test", "OOOOOOOO", 18);
        } catch (InvalidNumberException e) {
            Assert.assertEquals("InvalidRegistrationNumberFormat", e.getMessage());
        }
    }


    @Test
    public void organizationWithNegativeBalance() {

        Controller c = new Controller();
        c.addOrganization("own", "00000001", -100);

        Assert.assertEquals(-100, c.getBalance("own"));

    }


    @Test
    public void organizationWithZeroBalance() {

        Controller c = new Controller();
        c.addOrganization("own", "00000001", 0);

        Assert.assertEquals(0, c.getBalance("own"));

    }


    @Test(expected = OrganizationNotFoundException.class)
    public void organizationObtainRegistrationNumberByEmptyName() {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);
        c.getRegistrationNumber("");

    }


    @Test(expected = OrganizationNotFoundException.class)
    public void organizationObtainRegistrationNumberForMissingOrganization() {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);
        c.getRegistrationNumber("test #2");

    }


    @Test(expected = OrganizationNotFoundException.class)
    public void organizationObtainBalanceByEmptyName() {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);

        c.getBalance("");

    }


    @Test(expected = OrganizationNotFoundException.class)
    public void organizationObtainBalanceForMissingOrganization() {

        Controller c = new Controller();
        c.addOrganization("test #1", "00000000", 18);

        c.getBalance("test #2");

    }


    @Test
    public void paymentOne() {

        Controller c = new Controller();
        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime localDateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", localDateTime, 69);

        Assert.assertEquals("mystic", c.getSenderName("1"));
        Assert.assertEquals("moon", c.getRecipientName("1"));
        Assert.assertEquals("Just for fun!", c.getPaymentPurpose("1"));
        Assert.assertEquals(localDateTime, c.getPaymentDateTime("1"));
        Assert.assertEquals(69, c.getPaymentSum("1"));

    }


    @Test
    public void paymentSeveralForDifferentOrganizations() {

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
        Assert.assertEquals("moon", c.getRecipientName("556"));
        Assert.assertEquals("Rrrrr!", c.getPaymentPurpose("556"));
        Assert.assertEquals(dateTime1, c.getPaymentDateTime("556"));
        Assert.assertEquals(12, c.getPaymentSum("556"));

        Assert.assertEquals("sausages", c.getSenderName("37"));
        Assert.assertEquals("kitty", c.getRecipientName("37"));
        Assert.assertEquals("Mew", c.getPaymentPurpose("37"));
        Assert.assertEquals(dateTime2, c.getPaymentDateTime("37"));
        Assert.assertEquals(13, c.getPaymentSum("37"));

    }


    @Test
    public void paymentSeveralForOneSender() {

        Controller c = new Controller();

        c.addOrganization("moon", "12345678", 999);
        c.addOrganization("sausages", "22334455", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime1 = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("sausages", "moon", "556", "Rrrrr!", dateTime1, 12);
        LocalDateTime dateTime2 = LocalDateTime.of(2013, 2, 18, 16, 44, 44);
        c.addPayment("sausages", "kitty", "37", "Mew", dateTime2, 13);

        Assert.assertEquals("sausages", c.getSenderName("556"));
        Assert.assertEquals("moon", c.getRecipientName("556"));
        Assert.assertEquals("Rrrrr!", c.getPaymentPurpose("556"));
        Assert.assertEquals(dateTime1, c.getPaymentDateTime("556"));
        Assert.assertEquals(12, c.getPaymentSum("556"));

        Assert.assertEquals("sausages", c.getSenderName("37"));
        Assert.assertEquals("kitty", c.getRecipientName("37"));
        Assert.assertEquals("Mew", c.getPaymentPurpose("37"));
        Assert.assertEquals(dateTime2, c.getPaymentDateTime("37"));
        Assert.assertEquals(13, c.getPaymentSum("37"));

    }


    @Test
    public void paymentSeveralForOneReceiver() {

        Controller c = new Controller();
        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime1 = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2013, 2, 18, 16, 44, 44);

        c.addPayment("mystic", "kitty", "556", "Rrrrr!", dateTime1, 12);
        c.addPayment("sausages", "kitty", "37", "Mew", dateTime2, 13);

        Assert.assertEquals("mystic", c.getSenderName("556"));
        Assert.assertEquals("kitty", c.getRecipientName("556"));
        Assert.assertEquals("Rrrrr!", c.getPaymentPurpose("556"));
        Assert.assertEquals(dateTime1, c.getPaymentDateTime("556"));
        Assert.assertEquals(12, c.getPaymentSum("556"));

        Assert.assertEquals("sausages", c.getSenderName("37"));
        Assert.assertEquals("kitty", c.getRecipientName("37"));
        Assert.assertEquals("Mew", c.getPaymentPurpose("37"));
        Assert.assertEquals(dateTime2, c.getPaymentDateTime("37"));
        Assert.assertEquals(13, c.getPaymentSum("37"));

    }

    @Test
    public void paymentWithEmptySenderName() {

        Controller c = new Controller();
        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.addPayment("", "kitty", "1", "Just for fun!", dateTime, 69);

    }


    @Test(expected = OrganizationAndPaymentsException.class)
    public void paymentWithEmptyRecieverName() {

        Controller c = new Controller();
        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("sausages", "", "1", "Just for fun!", dateTime, 69);

    }


    @Test(expected = EmptyIdException.class)
    public void paymentWithEmptyUniqueNumber() {

        Controller c = new Controller();
        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.addPayment("sausages", "kitty", "", "Just for fun!", dateTime, 69);

    }


    @Test(expected = EmptyPaymentException.class)
    public void paymentWithEmptyPurpose() {

        Controller c = new Controller();

        c.addOrganization("sausages", "22334456", 0);
        c.addOrganization("kitty", "00991234", -3);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.addPayment("sausages", "kitty", "1", "", dateTime, 69);

    }


    @Test
    public void paymentWithDuplicateUniqueNumber() {

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
    public void paymentWithDuplicatePurpose() {

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
    public void paymentWithTimeInTheFuture() {

        Controller c = new Controller();
        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.of(2093, 7, 19, 11, 0, 0);
        c.addPayment("testFrom", "testTo", "88", "purpose", dateTime, 99);

    }


    @Test
    public void paymentWithCurrentTime() {

        Controller c = new Controller();
        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime localDateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "88", "purpose", localDateTime, 99);

    }


    @Test
    public void paymentTwoPaymentsWithTheSameTime() {

        Controller c = new Controller();

        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime localDateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "88", "purpose", localDateTime, 99);
        c.addPayment("testFrom", "testTo", "89", "purpose", localDateTime, 99);

    }


    @Test
    public void paymentWithCustomCharsInUniqueNumber() {

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
    public void paymentWithNegativeSum() {

        Controller c = new Controller();

        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "88", "purpose", dateTime, -2);

    }


    @Test(expected = InvalidPaymentSumException.class)
    public void paymentWithZeroSum() {

        Controller c = new Controller();

        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testTo", "88", "purpose", dateTime, 0);

    }


    @Test(expected = PaymentForItselfException.class)
    public void paymentForItself() {

        Controller c = new Controller();

        c.addOrganization("testFrom", "00000100", 100);
        c.addOrganization("testTo", "10000000", 200);

        LocalDateTime dateTime = LocalDateTime.now();
        c.addPayment("testFrom", "testFrom", "88", "purpose", dateTime, 2);

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainSenderNameByEmptyNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getSenderName("");

    }


    @Test
    public void paymentObtainSenderNameForMissingNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getSenderName("2");

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainReceiverNameByEmptyNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);
        c.getRecipientName("");

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainSenderNameForMissingNumber() {

        Controller c = new Controller();
        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getSenderName("2");
    }


    @Test(expected = NotFoundPaymentException.class)
    public void PaymentObtainPurposeByEmptyNumber() {

        Controller c = new Controller();
        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getPaymentPurpose("");

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainReceiverNameForMissingNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getRecipientName("2");

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainPurposeByEmptyNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);
        c.getPaymentPurpose("");

    }

    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainPurposeForMissingNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);
        c.getPaymentPurpose("2");

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainDatetimeByEmptyNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getPaymentDateTime("");

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainDateTimeForMissingNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);

        c.getPaymentDateTime("2");

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainSumByEmptyNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getPaymentSum("");

    }


    @Test(expected = NotFoundPaymentException.class)
    public void paymentObtainSumForMissingNumber() {

        Controller c = new Controller();

        c.addOrganization("mystic", "00000000", 666);
        c.addOrganization("moon", "12345678", 999);

        LocalDateTime dateTime = LocalDateTime.of(2015, 1, 1, 12, 0, 0);
        c.addPayment("mystic", "moon", "1", "Just for fun!", dateTime, 69);

        c.getPaymentSum("2");

    }


    @Test
    public void queriesGetOrganizationsWithNegativeSaldo() {

        Controller c = new Controller();
//        generateModel(c);

        List<String> exceptions = Arrays.asList("Gofi","Qwer");
        Assert.assertEquals(exceptions,c.getOrganizationsWithNegativeSaldo());

    }


    @Test
    public void queriesGetBiggestPaymentData() {

        Controller c = new Controller();
//        generateModel(c);

        Assert.assertEquals("21",c.getIdOfBiggestPayment());

    }

    @Test
    public void queriesGetDateWithBiggestTotalPaymentsAmount() {

        Controller c = new Controller();
//        generateModel(c);

        LocalDateTime exceptions = LocalDateTime.of(2010, 4, 1,0,0);
        Assert.assertEquals(exceptions,c.getDateWithBiggestTotalPayments());

    }
    
}

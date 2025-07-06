package fa.training.utils;

import java.util.regex.Pattern;

public class Validator {
    
    /**
     * Validates phone number format
     * @param phoneNumber the phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(Constants.PHONE_REGEX, phoneNumber.trim());
    }
    
    /**
     * Validates order number format (must be exactly 10 characters)
     * @param orderNumber the order number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            return false;
        }
        return orderNumber.trim().length() == Constants.ORDER_NUMBER_LENGTH;
    }
    
    /**
     * Validates if a string is not null or empty
     * @param value the string to validate
     * @return true if not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
} 
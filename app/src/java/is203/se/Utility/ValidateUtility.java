package is203.se.Utility;

import java.util.ArrayList;

public final class ValidateUtility {

    // returns null if no blank field
    // else, returns array telling us which field is blank
    public static String[] checkForBlankFields(String[] header, String[] row) {

        ArrayList<String> blankFields = new ArrayList<>();
        boolean hasBlankSpace = false;
        for (int i = 0; i < row.length; i++) {
            String field = row[i];
            if (field.trim().equals("")) {
                blankFields.add(header[i] + " field is empty");
                hasBlankSpace = true;
            }
        }
        if (hasBlankSpace) {
            return blankFields.toArray(new String[0]);
        }
        return null;
    }

    public static boolean validateLocationId(String unvalidatedLocationID) {
        return unvalidatedLocationID.matches("^[0-9]*$");
    }

    public static boolean validateSemanticPlace(String unvalidatedSemanticPlace) {
        return unvalidatedSemanticPlace.matches("SMUSIS(L[1-5]|B1)[\\p{Graph}]*");
    }

//    public static boolean validateLocationId(String unvalidatedLocationID) {
//        return unvalidatedLocationID.matches("^[0-9]*$");
//    }
    public static boolean validateTimestamp(String unvalidatedTimestamp) {
        return unvalidatedTimestamp.matches("^\\d{4}-([0][1-9]|[1][0-2])-([0][1-9]|[1-2][0-9]|[3][0-1]) ([0-1][0-9]|[2][0-3]):([0-5][0-9]):([0-5][0-9])$");
    }

    public static boolean validateMacAddress(String unvalidatedMacAddress) {
        return unvalidatedMacAddress.matches("^[0-9A-Fa-f]{40}$");
    }

    public static boolean validatePassword(String unvalidatedPassword) {
        return unvalidatedPassword.matches("^([a-zA-Z0-9]{8,}|(?!\\s))$");
    }

    public static boolean validateEmail(String lowercaseUnvalidatedEmail) {
        return lowercaseUnvalidatedEmail.matches("[0-9A-Za-z]*[.][0-9A-Za-z]*[.][2][0][1][3-7][@](economics|sis|socsc|law|accountancy|business).smu.edu.sg");
    }

    public static boolean validateGender(String unvalidatedGender) {
        return (unvalidatedGender.equals("M") || unvalidatedGender.equals("F"));
    }
    
}

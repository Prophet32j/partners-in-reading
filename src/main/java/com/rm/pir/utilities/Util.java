package com.rm.pir.utilities;

public class Util {
    
    public static String sanitize(String str) {
        if (str.contains("'")) {
            return str.replace("'", "''");
        }
        return str;
    }
    
    public static String convertPhone(String phone) {
        phone = phone.replaceAll("\\D", "");
        String zip = phone.substring(0, 3);
        String prefix = phone.substring(3, 6);
        String line = phone.substring(6);
        phone = zip + "-" + prefix + "-" + line;
        return phone;
    }
}

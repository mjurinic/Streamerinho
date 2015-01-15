package com.mjurinic.streamsource;

import java.util.HashMap;

public class Base64 {

    private static String workingAddress = "";

    private static HashMap<String, String> table64 = new HashMap<String, String>();

    public static String getBase64(String IPAddress) {
        workingAddress = "";

        generateTable();
        generateBinary(IPAddress);

        return encode();
    }

    public static String decodeBase64(String code) {
        return decode(code);
    }

    private static void generateBinary(String inputAddress) {
        String tmpAddress = "";

        for (int i = 0; i < inputAddress.length(); ++i) {
            if (inputAddress.charAt(i) == '.' || inputAddress.charAt(i) == ':') {
                workingAddress += checkMultiple(tmpAddress, 8, 1);
                tmpAddress = "";
            } else {
                tmpAddress += inputAddress.charAt(i);
            }
        }

        workingAddress += checkMultiple(tmpAddress, 16, 1); //adding port number
    }

    private static void generateTable() {
        for (Integer i = 0; i < 26; ++i) {
            Integer num = i;
            table64.put(checkMultiple(num.toString(num, 2), 6, 0), "" + ((char)('A' + i)));
            table64.put("" + ((char)('A' + i)), checkMultiple(num.toString(num, 2), 6, 0));
        }

        for (Integer i = 26, j = 0; i < 52; ++i, ++j) {
            table64.put(checkMultiple(i.toString(i, 2), 6, 0), "" + ((char)('a' + j)));
            table64.put("" + ((char)('a' + j)), checkMultiple(i.toString(i, 2), 6, 0));
        }

        for (Integer i = 52, j = 0; i < 62; ++i, ++j) {
            table64.put(checkMultiple(i.toString(i, 2), 6, 0), "" + ((char)('0' + j)));
            table64.put("" + ((char)('0' + j)), checkMultiple(i.toString(i, 2), 6, 0));
        }

        for (Integer i = 62, j = 0; i < 64; ++i, ++j) {
            if (i == 62) {
                table64.put(checkMultiple(i.toString(i, 2), 6, 0), "+");
                table64.put("+", checkMultiple(i.toString(i, 2), 6, 0));
            }
            else {
                table64.put(checkMultiple(i.toString(i, 2), 6, 0), "/");
                table64.put("/", checkMultiple(i.toString(i, 2), 6, 0));
            }
        }
    }

    private static String encode() {
        String ret = "";
        String set = "";

        for (int i = workingAddress.length() - 1, j = 0; i >= 0; --i, ++j) {
            if (j % 6 == 0 && j != 0) {
                String _set = new StringBuffer(set).reverse().toString();

                ret = table64.get(_set) + ret;
                set = "";
            }

            set += workingAddress.charAt(i);
        }

        if (set != "") {
            String _set = new StringBuffer(set).reverse().toString();
            ret = table64.get(checkMultiple(_set, 6, 0)) + ret;
        }

        return ret;
    }

    private static String decode(String encoded) {
        String ret = "";
        String set = "";
        String bin = "";

        for (int i = 0; i < encoded.length(); ++i) {
            String tmpChar = "" + encoded.charAt(i);
            String tmpNum = table64.get(tmpChar);

            bin += tmpNum;
        }

        for (int i = 0; i <= 32; ++i) {
            if (set.length() == 8) {
                ret += Integer.parseInt(set, 2);

                if (i + 1 != 33) ret += ".";
                else ret += ":";

                set = "";
            }

            set += bin.charAt(i);
        }

        for (int i = 33; i < bin.length(); ++i) {
            set += bin.charAt(i);
        }

        ret += Integer.parseInt(set, 2);

        return ret;
    }

    private static String checkMultiple(String number, Integer factor, Integer mode) {
        if (mode == 1) {
            Integer tmpNumber = Integer.parseInt(number);
            String Conversion = tmpNumber.toString(tmpNumber, 2);
            String dummyBits = "";

            Integer lenConversion = Conversion.length();

            if (lenConversion % factor != 0) {
                Integer closes_multiple = lenConversion + factor - (lenConversion % factor);

                for (int j = 0; j < closes_multiple - lenConversion; ++j) {
                    dummyBits += "0";
                }
            }

            return (dummyBits + Conversion);
        }

        if (mode == 0) {
            String Conversion = number;
            String dummyBits = "";

            Integer lenConversion = Conversion.length();

            if (lenConversion % factor != 0) {
                Integer closes_multiple = lenConversion + factor - (lenConversion % factor);

                for (int j = 0; j < closes_multiple - lenConversion; ++j) {
                    dummyBits += "0";
                }
            }

            return (dummyBits + Conversion);
        }

        return null;
    }
}
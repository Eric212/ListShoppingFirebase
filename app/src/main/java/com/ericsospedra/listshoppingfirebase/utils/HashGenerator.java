package com.ericsospedra.listshoppingfirebase.utils;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * HashGenerator
 * License: 🅮 Public Domain
 * <p>PruebaSQLite</p>
 *
 * @author Germán Gascón
 * @version 0.3, 2023-01-11
 * @since 0.1, 2019-01-28
 **/
public class HashGenerator {
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String getSHAString(String input) throws NoSuchAlgorithmException {
        return toHexString(getSHA(input));
    }

    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
        // Pad with leading zeros
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}

package com.tp1.passwordcracker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilitaire centralisant le calcul de hash MD5, utilisé par les
 * différentes stratégies afin d'éviter toute duplication de code.
 */
public final class MD5Util {

    private MD5Util() {
        // classe utilitaire non instanciable
    }

    /**
     * Calcule le hash MD5 d'une chaîne de caractères et le retourne
     * sous forme hexadécimale (minuscules).
     *
     * @param input la chaîne à hacher
     * @return le hash MD5 en hexadécimal
     */
    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // MD5 est toujours disponible dans le JDK standard
            throw new RuntimeException("Algorithme MD5 non disponible", e);
        }
    }
}

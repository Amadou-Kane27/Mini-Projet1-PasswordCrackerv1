package com.tp1.passwordcracker;

/**
 * Stratégie de cassage par force brute : génère toutes les combinaisons
 * possibles de lettres minuscules (a-z) jusqu'à une longueur maximale,
 * et compare leur hash MD5 au hash recherché.
 */
public class BruteForceHashCracker implements HashCracker {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int MAX_LENGTH = 4;

    private int attempts = 0;

    @Override
    public String crack(String hash) {
        attempts = 0;
        for (int length = 1; length <= MAX_LENGTH; length++) {
            String result = tryLength(hash, new char[length], 0);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Génère récursivement toutes les combinaisons de la longueur donnée
     * et teste chacune d'elles.
     */
    private String tryLength(String hash, char[] current, int position) {
        if (position == current.length) {
            String candidate = new String(current);
            attempts++;
            if (MD5Util.hash(candidate).equals(hash)) {
                return candidate;
            }
            return null;
        }
        for (char c : ALPHABET.toCharArray()) {
            current[position] = c;
            String result = tryLength(hash, current, position + 1);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public int getAttempts() {
        return attempts;
    }
}

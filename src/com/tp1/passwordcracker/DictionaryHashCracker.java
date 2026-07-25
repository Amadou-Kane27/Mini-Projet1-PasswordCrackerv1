package com.tp1.passwordcracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Stratégie de cassage par dictionnaire : charge une liste de mots
 * depuis un fichier, calcule le hash MD5 de chacun et le compare au
 * hash recherché.
 */
public class DictionaryHashCracker implements HashCracker {

    private final String dictionaryPath;
    private int attempts = 0;

    public DictionaryHashCracker(String dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
    }

    @Override
    public String crack(String hash) {
        attempts = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryPath))) {
            String word;
            while ((word = reader.readLine()) != null) {
                word = word.trim();
                if (word.isEmpty()) {
                    continue;
                }
                attempts++;
                if (MD5Util.hash(word).equals(hash)) {
                    return word;
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du dictionnaire : " + e.getMessage());
        }
        return null;
    }

    public int getAttempts() {
        return attempts;
    }
}

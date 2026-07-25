package com.tp1.passwordcracker;

/**
 * Fabrique simple (Simple Factory) responsable de la création
 * centralisée des instances de HashCracker. C'est le seul endroit
 * du programme où les classes concrètes sont instanciées.
 */
public class HashCrackerFactory {

    private static final String DEFAULT_DICTIONARY_PATH = "resources/dictionary.txt";

    public static HashCracker create(String method) {
        if (method == null) {
            throw new IllegalArgumentException("La méthode ne peut pas être nulle");
        }

        switch (method.toUpperCase()) {
            case "DICO":
                return new DictionaryHashCracker(DEFAULT_DICTIONARY_PATH);
            case "BRUTE":
                return new BruteForceHashCracker();
            default:
                throw new IllegalArgumentException("Méthode inconnue : " + method);
        }
    }
}

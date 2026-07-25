package com.tp1.passwordcracker;

/**
 * Interface commune à toutes les stratégies de cassage de mot de passe.
 */
public interface HashCracker {

    /**
     * Tente de retrouver le mot de passe correspondant au hash donné.
     *
     * @param hash le hash MD5 à casser
     * @return le mot de passe trouvé, ou null si aucun résultat
     */
    String crack(String hash);
}

package com.tp1.passwordcracker;

/**
 * Point d'entrée de l'application en ligne de commande.
 *
 * Usage :
 *   passwordCracker -m BRUTE -h e7247759c1633c0f9f1485f3690294a9
 *   passwordCracker -m DICO -h e7247759c1633c0f9f1485f3690294a9
 */
public class PasswordCracker {

    public static void main(String[] args) {
        String method = null;
        String hash = null;

        for (int i = 0; i < args.length - 1; i++) {
            if ("-m".equals(args[i])) {
                method = args[i + 1];
            } else if ("-h".equals(args[i])) {
                hash = args[i + 1];
            }
        }

        if (method == null || hash == null) {
            System.out.println("Usage: passwordCracker -m <BRUTE|DICO> -h <hash_md5>");
            return;
        }

        HashCracker cracker;
        try {
            cracker = HashCrackerFactory.create(method);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            return;
        }

        long start = System.currentTimeMillis();
        String password = cracker.crack(hash);
        long elapsed = System.currentTimeMillis() - start;

        if (password != null) {
            System.out.println("Password found: " + password);
        } else {
            System.out.println("Password not found");
        }
        System.out.println("Execution time: " + elapsed + " ms");
    }
}

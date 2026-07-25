# PasswordCracker v1

Mini-Projet 1 — L3 GLSI — Mise en œuvre du patron **Simple Factory**

## 1. Introduction

Ce projet consiste à développer une première version d'un outil en ligne de commande,
`PasswordCracker`, capable de retrouver un mot de passe à partir de son empreinte
MD5. Il s'agit d'un exercice de conception orientée objet visant à mettre en
pratique le patron de création **Simple Factory**, le polymorphisme, et une
architecture modulaire.

## 2. Présentation du problème

Dans le domaine de la cybersécurité, les mots de passe ne sont jamais stockés en
clair : ils sont transformés à l'aide de fonctions de hachage cryptographiques
comme MD5. Lors d'un audit de sécurité, il est souvent nécessaire de vérifier la
robustesse des mots de passe utilisés en tentant de retrouver le mot de passe
d'origine à partir de son hash.

L'outil doit proposer deux méthodes de cassage :
- **DICO** : recherche par dictionnaire (liste de mots courants) ;
- **BRUTE** : recherche par force brute (génération exhaustive de toutes les
  combinaisons de lettres minuscules, jusqu'à 4 caractères).

## 3. Architecture

L'architecture repose sur une interface commune implémentée par deux stratégies
concrètes, et une fabrique centralisant leur création :

| Classe | Rôle |
|---|---|
| `HashCracker` | Interface commune définissant le contrat `crack(hash): String` |
| `DictionaryHashCracker` | Stratégie de cassage par dictionnaire |
| `BruteForceHashCracker` | Stratégie de cassage par force brute |
| `HashCrackerFactory` | Fabrique simple, seul point de création des stratégies |
| `MD5Util` | Utilitaire centralisant le calcul du hash MD5 (évite la duplication de code) |
| `PasswordCracker` | Point d'entrée de l'application en ligne de commande |

Les classes concrètes ne sont jamais instanciées directement dans `PasswordCracker` :
la création passe systématiquement par `HashCrackerFactory.create(method)`.

## 4. Diagramme UML

```
              <<interface>>
                HashCracker
           +crack(hash: String): String
                ▲           ▲
                |           |
  DictionaryHashCracker   BruteForceHashCracker
  +crack(hash): String    +crack(hash): String
  -dictionaryPath: String -ALPHABET: String
  -attempts: int          -MAX_LENGTH: int
                          -attempts: int

              HashCrackerFactory
         +create(method: String): HashCracker
```

## 5. Usage du patron Simple Factory

Le patron **Simple Factory** est appliqué dans la classe `HashCrackerFactory` :

```java
HashCracker cracker = HashCrackerFactory.create("DICO");
```

La fabrique reçoit une chaîne de caractères (`"DICO"` ou `"BRUTE"`) et retourne
l'instance de `HashCracker` correspondante. Le programme principal ne manipule
que l'interface `HashCracker` (polymorphisme) et ignore totalement les classes
concrètes, ce qui centralise la logique de création et respecte la contrainte
imposée : *« Les classes concrètes ne doivent pas être instanciées directement
dans le programme principal. »*

## 6. Résultats obtenus

Exemples d'exécution (hash MD5 du mot `test` : `098f6bcd4621d373cade4e832627b4f6`) :

```
$ passwordCracker -m DICO -h 098f6bcd4621d373cade4e832627b4f6
Password found: test
Execution time: 52 ms

$ passwordCracker -m BRUTE -h 098f6bcd4621d373cade4e832627b4f6
Password found: test
Execution time: 3463 ms

$ passwordCracker -m DICO -h ffffffffffffffffffffffffffffffff
Password not found
Execution time: 56 ms
```

On observe que la recherche par dictionnaire est nettement plus rapide que la
force brute, celle-ci devant explorer un espace de recherche bien plus large.

*Lien vers la vidéo de démonstration : à ajouter ici (max. 10 minutes).*

## 7. Difficultés rencontrées

- Éviter la duplication de code entre les deux stratégies pour le calcul du hash
  MD5, résolu en centralisant ce calcul dans une classe utilitaire `MD5Util`.
- Gérer proprement le cas où la méthode fournie en argument est invalide ou
  absente, sans faire planter le programme.
- Générer efficacement toutes les combinaisons de la force brute par longueur
  croissante (1 à 4 caractères) à l'aide d'une fonction récursive.

## 8. Conclusion

Ce mini-projet a permis de mettre en pratique le patron de création **Simple
Factory** dans un contexte concret de cybersécurité. L'architecture proposée
sépare clairement les responsabilités (stratégies de cassage, création des
objets, point d'entrée) et respecte le principe de centralisation imposé par
l'énoncé. La limite identifiée est que l'ajout d'une nouvelle stratégie impose
de modifier `HashCrackerFactory`, ce qui sera corrigé dans le mini-projet
suivant.

---

## Questions de réflexion

**1. Quels avantages apporte la fabrique simple ?**
Elle centralise la logique de création des objets, masque les classes concrètes
au code appelant, et simplifie l'ajout de nouvelles instanciations à un seul
endroit du code.

**2. Quels sont ses inconvénients ?**
Elle viole le principe Open/Closed : toute nouvelle stratégie oblige à modifier
le corps de la fabrique (ajout d'un `case`). Elle n'est pas polymorphe au niveau
de la création elle-même, contrairement à un Factory Method ou une Abstract
Factory.

**3. Que faut-il modifier lorsqu'une nouvelle stratégie est ajoutée ?**
Il faut créer la nouvelle classe implémentant `HashCracker`, puis modifier le
`switch` de `HashCrackerFactory.create()` pour y ajouter le nouveau cas.

**4. La fabrique respecte-t-elle le principe Open/Closed ?**
Non. Le principe Open/Closed exige d'être ouvert à l'extension mais fermé à la
modification ; or ici, chaque nouvelle stratégie nécessite de modifier le code
existant de la fabrique plutôt que de simplement l'étendre.

## Compilation et exécution

```bash
javac -d out src/com/tp1/passwordcracker/*.java

# Remplacez le hash ci-dessous par celui que vous voulez casser
java -cp out com.tp1.passwordcracker.PasswordCracker -m DICO -h 098f6bcd4621d373cade4e832627b4f6
java -cp out com.tp1.passwordcracker.PasswordCracker -m BRUTE -h 098f6bcd4621d373cade4e832627b4f6
```

# MineboxTheoryCraft

Un simple bot Discord qui utilise *Javacord*. Le but de ce bot est de faire de calculer la rentabilité des objets sur un jeu.  


## Commandes

#### Ajouter un item

Cette commande ajoute les items d'un jeu dans un fichier qui sauvegarde tous les items.

```
  /add-item
```

| Paramètre | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `Texte` | **Requis**. Nom de l'item |
| `price` | `Texte` | **Requis**. Prix de l'item |
| `image` | `Image` | **Facultatif**. Image de l'item |

#### Ajouter les crafts d'un item

Cette commande ajoute les crafts d'un item. Il met à jour dans le fichier de sauvegarde.

```
  /craft-item
```

| Paramètre | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `Texte` | **Requis**. Nom de l'item  |

#### Modifier les information d'un item

Cette commande modifie les informations d'un item. Il renvoie vers un modal discord avec plusieurs sections où on peut changer ces informations.

```
  /modify
```

| Paramètre | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `Texte` | **Requis**. Nom de l'item |

#### Rechercher un item

Cette commande recherche un item dans le fichier de sauvegarde. Il renvoie une fiche de présentation de l'item recherché.

```
  /search
```

| Paramètre | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `Texte` | **Requis**. Nom de l'item |

#### Afficher ou modifier un message commerce

Cette commande renvoie deux boutons où l'utilisateur peut choisir entre afficher et modifier un message commerce (Le message commerce est spécifique au serveur Minecraft `Minebox`).

```
  /message-commerce
```



## Exécuter en local

Cloner le projet 

```bash
  git clone https://github.com/SaitamTheBest/MineboxTheorycraft.git
```

En utilisant un IDE, ajouter en paramètre de l'exécution du programme le `token` de votre bot :

![screeshot_configuration](https://github.com/SaitamTheBest/MineboxTheorycraft/blob/main/images/screeshot_configuration.png)



## Auteurs

- [@SaitamTheBest](https://github.com/SaitamTheBest) - DEVEZE Matias


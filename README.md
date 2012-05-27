# homework

## Dépendances

- bootstrap 2.0.3
- jquery 1.7.2
- highcharts 2.2.3
- press module 1.0.25


### Bootstrap
- Modification du css pour le bug d'affichage des span dans un layout fluid

## Conception

On a trois fonctions distinctes :
- Un champ de recherche pour chercher le repository sur github
- La liste les repository correspondant à la recherche
- Le détail du repository et qui peut être bookmarkée

### Page d'accueil
- Gérer sur cette page les deux fonctions recherche et liste.
- Lorsque l'on valide la recherche on affiche la liste dessous en ajax.
- Proposer également en v2 de l'autocomplétion au niveau de la recherche pour aller directement sur le détail

### Accès github

- http://develop.github.com/p/general.html
- Définir une api pour que les comportements communs soient mis en communs
- Utiliser le monitoring pour voir les temps d'accès
- Prévoir de faire les accès en asynchrone
- On utilise l'api v2 de github qui semble avoir toutes les fonctionnalités requises. Je ne trouve pas la fonction de recherche en api v3 (http://stackoverflow.com/questions/9310657/searching-for-a-repo-in-github-apiv3)
    - recherche d'un repository suivant des critères : http://github.com/api/v2/json/repos/search/
    - utiliser &start_page=x pour aller à une page donnée
    - a priori pas de moyen de définir la taille de la page ni de connaître le nombre total d'éléments
    - on affiche le bouton next si le nombre d'éléments de la page est égal au nombre d'éléments max : bug si jamais nbElems % pageSize = 0

## Points qui pourraient être améliorés

- Revoir le tag de pagination et la classe Page pour qu'ils soient plus génériques

## TODO
- voir pourquoi on n'a pas tous les commiters dans l'appel au meta : est il paginé ?
- si oui, créer peut-être la liste des utilisateurs plutôt via le second appel
# Extensions de la version 2 :
  1-  Introduction d’une nouvelle force : le joueur est maintenant soumis à une force de frottement qui va influencer sur son mode de déplacement, ainsi la surface de certains blocs dans son monde auront un frottement negatif ou positif sur la vitesse de deplacement du joueur. Par exemple lorsque le joueur se trouve à proximité d'un bloc de type boue et que le joueur s'y frotte alors il s’embourbe ; il avance alors d’une case de l’environnement en faisant deux déplacement ralentissant ainsi le joueur.
A l’inverse, si le joueur rencontre un bloc de type glace et qu’il s’y frotte alors il glisse et avance de deux cases en faisant uniquement un déplacement.

Cette extension peut se faire en ajoutant :
* Deux classes qui hériteront de la classe Bloc, une classe BlocGlass et une classe BlocBoue.
* Un trait qui sera appliquer à la classe Jeu et qui consiste à modifier la méthode deplacer_joueur() en lui ajoutant une condition pour connaitre le type du bloc sur lequel le joueur se trouve et définissant ainsi le nombre de deplacements et de cases que ce joueur devra faire.

  2-  Extention par pleuging : 
  
  3-  monde créatif :
  4-  Mise en place des portails spatiaux :
  8-  Mise en place des portails temporels :

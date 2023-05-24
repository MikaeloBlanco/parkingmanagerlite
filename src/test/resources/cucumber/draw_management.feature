# language: es
Característica: Gestion de sorteos
  
  Escenario: Navegación a la lista de sorteos
    Dado un usuario esta en la pagina inicial
    Cuando el usuario hace click sobre el botón de Sorteos
    Entonces esta en la pagina de lista de sorteos
  
  Escenario: Navegación a la creación de sorteos 
    Dado un usuario esta en la pagina inicial
    Cuando el usuario hace click sobre el botón de Sorteos
    Y el usuario hace click sobre el botón de creación de Sorteos
    Entonces esta en la pagina de creación de Sorteos

  Escenario: Comprobar que el formulario de creacion de sorteos tiene todos los elementos
    Dado un usuario está en la pagina de creación de Sorteos
    Entonces se muestra un campo de descripción
  
  Escenario: Creación de sorteos
    Dado un usuario esta en la pagina de creación de Sorteos
    Cuando el usuario introduce una descripción
    Y hace click en el botón de crear Sorteos
    Entonces esta en la pagina de lista de sorteos
  
  Escenario: Creación de sorteos sin descripción
    Dado un usuario esta en la pagina de creación de Sorteos
    Y hace click en el boton de crear Sorteos
    Entonces esta en la pagina de creación de Sorteos 
    Y salta un error de descripción no introducida
    
# language: es
Característica: Gestion de usuarios
  Escenario: Crear un usuario correctamente
    Dado un administrador esta en el formulario de creación
    Y el correo no esta asignado a otro usuario
    Cuando relleno el correo
    Y relleno el nombre
    Y relleno el primer apellido
    Y pulso el botón de Crear
    Entonces se navega a la lista
    Y existe el usuario
# TodoBa

## Descripción
Se busca crear una aplicación que se ajuste a las necesidades de un estudiante, ayudándolo en el día a día a mantenerse organizado, planificando todas sus tareas en un solo espacio. Nuestra aplicación les dará las herramientas necesarias para llevar el control de su vida estudiantil, ofreciéndoles herramientas para organizar su día a día. La aplicación tendrá características tales como: crear nuevos eventos, crear y dividir eventos por categorías, clasificaciones identificadas por colores y la posibilidad de registro de usuarios. Será una app desarrollada en el lenguaje de programación Kotlin, utilizando el programa de Android Studio como base en el desarrollo. 

## Servicios
1. Firebase
   - Se usará firebase como metodo de autenticación de usuarios, para que puedan acceder a su cuenta y guardar sus datos.
2. FireStore
   - Se usará firestore para guardar los datos de los usuarios, como sus eventos, categorías, etc.


## Librerías

1. Jetpack Navigation
    - Se usará para crear una navegación fluida para el usuario, donde se buscará la simplicidad de uso. 
2. Retrofit 2
    - Se usará para interactuar con la API que se usará para este proyecto. Se pedirá información dependiendo de las necesidades del usuario, de manera dinámica. 
3. Datastore
    - Se utilizará para guardar las configuraciones de la aplicación. Por ejemplo se guardará si el usuario inició sesión, y los eventos que él cree.
4. Coroutines
    - Esta librería se usará como complemento para la librería de DataStore, ya que hay funciones que necesitan ser ejecutadas en corutinas para el correcto funcionamiento.
5. Room
   - Se usará para guardar los eventos que el usuario cree, y así poder mostrarlos en la pantalla principal de la aplicación cuando no se tenga acceso a internet.


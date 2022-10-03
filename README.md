# NOMBRE

## Descripción
Se busca crear una aplicación que se ajuste a las necesidades de un estudiante, ayudándolo en el día a día a mantenerse organizado, planificando todas sus tareas en un solo espacio. Nuestra aplicación les dará las herramientas necesarias para llevar el control de su vida estudiantil, ofreciéndoles herramientas para organizar su día a día. La aplicación tendrá características tales como: crear nuevos eventos, crear y dividir eventos por categorías, clasificaciones identificadas por colores y la posibilidad de registro de usuarios. Será una app desarrollada en el lenguaje de programación Kotlin, utilizando el programa de Android Studio como base en el desarrollo. 

## Servicios
1. Open Weather API 
    - https://openweathermap.org/current 
    - Esta API se usará para determinar el clima durante el día de cada evento que el usuario cree. La API necesita la latitud y longitud de la ciudad, así como una “key” gratuita para utilizar la API.
    - Ejemplo de respuesta de la API:

```
{
  "coord": {
    "lon": 10.99,
    "lat": 44.34
  },
  "weather": [
    {
      "id": 501,
      "main": "Rain",
      "description": "moderate rain",
      "icon": "10d"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 298.48,
    "feels_like": 298.74,
    "temp_min": 297.56,
    "temp_max": 300.05,
    "pressure": 1015,
    "humidity": 64,
    "sea_level": 1015,
    "grnd_level": 933
  },
  "visibility": 10000,
  "wind": {
    "speed": 0.62,
    "deg": 349,
    "gust": 1.18
  },
  "rain": {
    "1h": 3.16
  },
  "clouds": {
    "all": 100
  },
  "dt": 1661870592,
  "sys": {
    "type": 2,
    "id": 2075663,
    "country": "IT",
    "sunrise": 1661834187,
    "sunset": 1661882248
  },
  "timezone": 7200,
  "id": 3163858,
  "name": "Zocca",
  "cod": 200
}  
```

2. Geocoding API
    - https://openweathermap.org/api/geocoding-api 
    - Es una API desarrollada para facilitar la búsqueda de localizaciones de las ciudades. Permite conocer con facilidad las coordenadas de una ciudad específica, utilizando el nombre de la ciudad y la llave, la cual es gratuita.
    - Ejemplo de respuesta de la API:

```
[
  {
    "name": "London",
    "local_names": {
      "af": "Londen",
      "ar": "لندن",
      "ascii": "London",
      "az": "London",
      "bg": "Лондон",
      "ca": "Londres",
      "da": "London",
      "de": "London",
      "el": "Λονδίνο",
      "en": "London",
      "eu": "Londres",
      "fa": "لندن",
      "feature_name": "London",
      "fi": "Lontoo",
      "fr": "Londres",
      "gl": "Londres",
      "he": "לונדון",
      "hi": "लंदन",
      "hr": "London",
      "hu": "London",
      "id": "London",
      "it": "Londra",
      "ja": "ロンドン",
      "la": "Londinium",
      "lt": "Londonas",
      "mk": "Лондон",
      "nl": "Londen",
      "no": "London",
      "pl": "Londyn",
      "pt": "Londres",
      "ro": "Londra",
      "ru": "Лондон",
      "sk": "Londýn",
      "sl": "London",
      "sr": "Лондон",
      "th": "ลอนดอน",
      "tr": "Londra",
      "vi": "Luân Đôn",
      "zu": "ILondon"
    },
    "lat": 51.5085,
    "lon": -0.1257,
    "country": "GB"
  },
  {
    "name": "London",
    "local_names": {
      "ar": "لندن",
      "ascii": "London",
      "bg": "Лондон",
      "de": "London",
      "en": "London",
      "fa": "لندن، انتاریو",
      "feature_name": "London",
      "fi": "London",
      "fr": "London",
      "he": "לונדון",
      "ja": "ロンドン",
      "lt": "Londonas",
      "nl": "London",
      "pl": "London",
      "pt": "London",
      "ru": "Лондон",
      "sr": "Лондон"
    },
    "lat": 42.9834,
    "lon": -81.233,
    "country": "CA"
  },
  {
    "name": "London",
    "local_names": {
      "ar": "لندن",
      "ascii": "London",
      "en": "London",
      "fa": "لندن، اوهایو",
      "feature_name": "London",
      "sr": "Ландон"
    },
    "lat": 39.8865,
    "lon": -83.4483,
    "country": "US",
    "state": "OH"
  },
  {
    "name": "London",
    "local_names": {
      "ar": "لندن",
      "ascii": "London",
      "en": "London",
      "fa": "لندن، کنتاکی",
      "feature_name": "London",
      "sr": "Ландон"
    },
    "lat": 37.129,
    "lon": -84.0833,
    "country": "US",
    "state": "KY"
  },
  {
    "name": "London",
    "local_names": {
      "ascii": "London",
      "ca": "Londres",
      "en": "London",
      "feature_name": "London"
    },
    "lat": 36.4761,
    "lon": -119.4432,
    "country": "US",
    "state": "CA"
  }
]
```


## Librerías

1. Jetpack Navigation
    - Se usará para crear una navegación fluida para el usuario, donde se buscará la simplicidad de uso. 
2. Retrofit 2
    - Se usará para interactuar con la API que se usará para este proyecto. Se pedirá información dependiendo de las necesidades del usuario, de manera dinámica. 
3. Datastore
    - Se utilizará para guardar las configuraciones de la aplicación. Por ejemplo se guardará si el usuario inició sesión, y los eventos que él cree.
4. Coroutines
    - Esta librería se usará como complemento para la librería de DataStore, ya que hay funciones que necesitan ser ejecutadas en corutinas para el correcto funcionamiento. 


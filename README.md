# Car Manager


##   Nombre de los layouts
```
NombreEspecífico_layout.xml

- Main: main_layout.xml
- Estadísticas: statistical_layout.xml
- Detalles coche: car_details_editor_layout.xml
```
        
**TODO: ir moviendo cada string que tengamos a pelo al archivo de strings.xml**
            
##    Changelog
Alex
```
Creado:
        + Las clases de actividad necesarias (CarDetailsActivity, StatisticsActivity)
        + car_details_editor_layout.xml
        + tipos_combustible.xml (bajo la carpeta values/)
        
El car_details_editor_layout.xml todavía no es definitivo, se ve un poco feucho pero ya se irá mejorando 🤢
        
He conectado los ítems del menú de opciones con cada actividad. Para ir a estadísticas: pulsar sobre el menú y elegir estadistícas. Para ir a detalles del coche: pulsar sobre el menú y elegir detalles del coche. Para volver al main se puede usar la tecla de atrás pero estaría bien buscar si se puede poner una barra superior con un botón con una flecha que te llevara de vuelta al main.
```

Antonio
```
Creado: 
        + Menu folder
        + options_menu.xml
        + statistical_layout.xml
        + Métodos para añadir el menú de opciones en el main_activity
        
He añadido un date picker en mi layout, no sé como interactuará con vuestros layouts pero no debería haber problema.

He seguido todos los pasos de aquí: https://github.com/jhonnyx2012/HorizontalPicker y he modificado los colores.
```
           
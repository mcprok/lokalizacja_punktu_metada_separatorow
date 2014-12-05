#Projekt z geometrii obliczeniowej.

#### Lokalizacja punktu w przestrzeni z podziałem poligonalnym za pomocą metody separatorów.


Link do prezentacji : https://prezi.com/bleykzg9h4ft

##Użytkowanie i instalacja 

Do użycia tego kodu będziesz potrzebować : 
https://github.com/gmiejski/goGui_java -> należy zainstalować w lokalnym repozytorium goGui za pomocą '''mvn clean install'''
https://bitbucket.org/mizmuda/gogui -> stąd przyda się część do wizualizacji :) 

(PS. Dzięki chłopaki ;) )

Następnie w katalogu projektu wykonujemy :
``` 
mvn clean package
java -jar /target/projekt.jar nazwaPlikuZDanymi
```
(plik z danymi powinien znajdować się w ```src/main/resources/``` 


###PS. Uwaga !
W kodzie jest mnóstwo rzeczy, które są użyte tylko i wyłącznie do lepszego zwizualizowania przebiegu algorytmu. Bez nich kod stałby się bardziej czytelny, ale za to nie moglibyśmy się nacieszyć widokiem naszego grafu w goGui :) 


<img src="https://raw.githubusercontent.com/mcprok/lokalizacja_punktu_metada_separatorow/master/result.gif">

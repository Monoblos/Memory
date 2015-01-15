Memory
======
### 1. Zweck des Projektes
* Erzeugen eines Memory Spiels für das Fach Software Engineering.

### 2. Der Anwender, der Kunde und andere Verantwortliche.
* Frei benutzbar für jeden.
* Prof. Dr. Marco Boger

### 3. Benutzer des Produkts
* Frei benutzbar für jeden, Partyspiel.

### 4.Obligatorische Beschränkungen
* Java wird vorrausgesetzt.
* Am besten getestet auf Windows, sollte auf allen anderen Systemen laufen.

### 5. Namenskonventionen und Definitionen
* Listener enthalten den Zusatz "Listener".
* Graphische und Textuelle Oberflächen haben den Namenzusatz "UI".
*  Adapter haben den Zusatz "Adapter".

### 6. Relevante Daten und Annahmen
*  - 

### 7. Arbeitsumfang
* Produktdokumentation.
* Produktpräsentation.


### 8. Produktumfang
* Eigene Karten verwenden.
* Multiplayer.
* Skalierbare Oberfläche.
* Beliebige Feldgröße.
* Frei wählbare Größe der Kartensets.
* Neustartbar.

### 9. Funktionale und Datenanforderungen
*  Savefile.

### 10. Designaspekte
* Skalierbares Spielbrett.
* Skalierbare Karten.

### 11. Nutzbarkeit
* Graphische Oberfläche zum Bedienen des Memory Spiels.
* Textuelle Oberfläche, falls keine graphischen Oberflächen unterstützt wird.

### 12. Laufzeitanforderungen
* Reaktionszeiten von < 1 sec.

### 13.Operationale Anforderungen
* 

### Aufbau des Spiels
* main 
  * Main
    * Enthält main Methode zur Ausführung der UI


* ui
  * VirtualConsole
    * Dient zur Darstellung der TUI.
  * TUI
    * Öffnet und Verwaltet die textbasierte Benutzeroberfläche. 
  * MemCardButton
    * Zur GUI zugehörige Klasse zur Darstellung der Memory Karte.
  * GUI
    * Öffnet und Verwaltet die graphische Benutzeroberfläche.  


* entities
  * MemoryCard
    * Beinhaltet alle Attribute einer Memory Karte.  
    * Enthält Methoden zum Abfragen und Ändern dieser Attribute sowie Methoden zum Vergleich von Memory Karten.
  * Board
    * Die Klasse Board setzt ein Spielbrett mit spezifischer Höhe und Breite und mit Memory Karten auf. 
    * Sie enthält alle Methoden, die zur Verwaltung der Karten des Spielbrettes, gebraucht werden.
  * MemoryCardEventListener
    * Interface dient zur Weiterleitung von Informationen der Klasse MemoryCard an die Klasse Board.
  * MemoryCardInterface
    * Interface beinhaltet Methode zum setzen der Sichtbarkeit einer Karte.
  
  
* logic
  * BoardEventListener
    * Interface enthält Methoden zur Weitergabe von Informationen der Klasse Board. 
  * BoardEventAdapter
    * Implementiert die Funktionen von BoardEventListener als leere Funktionen.
    * Erleichtert Anwendung des BoardEventListeners.
  * SettingUtil
    * Bündelt alle Einstellungen in einer Klasse.
  * Util 
    * Enthält allgemeine, keiner spezifischen Klasse zuordenbare, Methoden.

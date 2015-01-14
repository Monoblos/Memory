Memory
======
* Entities
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


    

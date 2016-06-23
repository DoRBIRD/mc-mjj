# Mobile Computing - LibGDX Game - Icy Road

## Dokumentation zum Projekt Icy Road
Der Sourcecode kann auf GitHub ([https://github.com/mellmers/mc-mjj](https://github.com/mellmers/mc-mjj)) runtergeladen werden. Bis auf normale Android Dateien (AndroidLauncher und Manifest) sind alle Klassen unter core>java>de.mc.game. Und alle Assets liegen unter android>assets>(subfolder). Ziel Android Platform ist 23 und minimale liegt bei 8.

Außerdem kann das fertige Spiel als Android application package (apk) unter folgendem Link oder QRCode heruntergeladen werden: [http://ow.ly/42la301iQvd](http://ow.ly/42la301iQvd). <img src="https://s3.eu-central-1.amazonaws.com/mellmers/app-download.png" alt="QR-Code" title="QR-Code to download apk" width="150" height="150" />

Unsere Grafiken wurden größtenteils selbst erstellt. Allerdings wurden einige GUI Elemente mit Hilfe des “Game GUI TwentyFive” ([http://graphicriver.net/item/game-gui-pack-twentyfive/15763210](http://graphicriver.net/item/game-gui-pack-twentyfive/15763210)) erstellt.

## Funktionen der Klassen
**McGame**.java - Erste Klasse, die aufgerufen wird. Startet das Laden der Assets und auch den Spielernamen. Verwaltet und zeigt die Screens und zeigt zum Anfang den MenuScreen.

**MapBlock**.java - Objekt zur Darstellung von Puzzleteil für die Prozedural-Map-Generation. Beinhaltet jeweilige TileMap und welche Verbindungen der Baustein hat.

**MapManager**.java - Verwaltet die Map: Übernimmt das Zusammenbauen der Bausteine, die Kollision mit dem Spielern und das Speichern der MapBausteine.

**Player**.java - Beinhaltet das Spielerbild und dessen verschiedene Varianten (z.B. nach links gedreht oder mit Ring). Übernimmt die PowerUp Timer.

**RectTile**.java - Objekt zum Speichern einer bestimmten Zelle der TileMap. Speichert dessen Tile und die Raster Koordinaten der Zelle.

**DatabaseConnection**.java - Übernimmt die Verbindung unserer Datenbank.

**HighscoreDAO**.java - Führt die Prepared Statements auf der DatabaseConnection aus. Speichert oder holt so die Highscoredaten.

**AndroidOnlyInterface**.java - Interface das genutzt wird um Android Toasts anzuzeigen.

**Assets**.java - Lädt alle Assets zum Start der App und macht sie für die anderen Klassen verfügbar.

**Constants**.java - Speicherung von Konstanten wie z.B. der Bildschirmauflösung, Map Größe uvm.

**CustomProgressBar**.java - Progressbar, um anzuzeigen wie lange die Powerups noch halten.

**CustomTextButton**.java - Unsere abgeänderten Buttons (Text bewegt sich beim Klicken ein wenig nach unten)

**SaveUserNameListener**.java - Speichert nach Eingabe den Namen in den Android Preferences

**TextureMapObjectRenderer**.java - Renderer für TiledMapObjekte.

**CustomScreenAdapter**.java - Screen Super Class von der die anderen Screen Klassen erben. Beinhaltet render loop und draw Funktion.

**MainMenuScreen**.java - Menubildschirm. zeigt Play-, Tutorial-, Optionsbutton.

**GameScreen**.java - Hauptspielbildschirm. Beinhaltet viel Spielogik in der Render Methode vom libgdx Screen.

**TutorialScreen**.java - Wie GameScreen jedoch mit Tutorial map und Tipps.

**GameOverOverlay**.java - zeigt am Ende des Spiels die Punkte der Runde an und bietet Buttons fürs Neustarten und zum Zurückkehren zum Startbildschirm.

**HighscoreOverlay**.java - Overlay, das Highscores aus der Datenbank abfragt und diese dann anzeigt

**OptionsOverlay**.java - Overlay für Änderung des Spielernamens (Später sollte dort auch die Audiolautstärke angepasst werden können).

**PauseOverlay**.java - Overlay, das bei pausiertem Spiel angezeigt wird. Zeigt Buttons zum Öffnen von OptionsOverlay und HighscoreOverlay.

**TutorialOverlay**.java - Overlay zur Anzeige von Tipps

**State**.java - Enum zur Darstellung des Spielstatus


## Programmablauf
Beim ersten Start der App wird der Benutzer gebeten seinen Namen einzugeben. Der Name ist nicht zwingend einzugeben und man kann diesen später unter “Optionen” ändern. Der Name wird benutzt, um den Punktestand am Ende eines Spiels in die Highscoreliste einzutragen. Der Highscore wird nach jedem Spielende automatisch an die Datenbank übermittelt, wenn eine Internetverbindung besteht (momentan kann es sein, dass die Anwendung lange Zeit lädt, wenn die Internetverbindung langsam ist).

Im Menü kann man zwischen dem normalen Spiel oder einem Tutorial wählen. Zu Beginn sollte das Tutorial einmal durchgespielt werden, da dort alle Funktionen des Spiels erklärt werden. Während man spielt kann man das Spiel über den Pause-Button, welcher sich in der oberen, rechten Ecke befindet, pausieren. Wenn man weiterspielen möchte, muss man bloß das Overlay mit einem Klick auf das X schließen.

Unter Optionen kann der Spielername geändert werden und zukünftig sollte man dort auch die Audiolautstärke anpassen können, jedoch gibt es bisher noch keine Audios, welche abgespielt werden.

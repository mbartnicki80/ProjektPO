package agh.ics.oop.model;

import agh.ics.oop.model.annotations.Observer;

@Observer
public class FileMapDisplay implements MapChangeListener {

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        /* TODO
            Zapisywanie logów do pliku
            Dodaj kolejnego obserwatora mapy - tym razem niech to będzie klasa FileMapDisplay.
            W reakcji na modyfikację mapy, obserwator powinien otwierać plik o nazwie map_id.log (gdzie id to identyfikator mapy) i dopisywać do niego na koniec informacje o ruchu i aktualnym wyglądzie mapy.
            Pamiętaj by poprawnie obsługiwać zamykanie pliku i ewentualne błędy. Użyj w tym celu mechanizmu try-with-resources.
        */
    }
}

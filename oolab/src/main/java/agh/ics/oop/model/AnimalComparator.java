package agh.ics.oop.model;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    
    @Override
    public int compare(Animal a, Animal b) {
        // Porównywanie energii
        int energyComparison = Integer.compare(a.getEnergy(), b.getEnergy());
        if (energyComparison != 0) {
            return energyComparison;
        }

        // Jeżeli energia jest taka sama, porównujemy wiek
        int birthComparison = Integer.compare(a.getDayOfBirth(), b.getDayOfBirth());
        if (birthComparison != 0) {
            return -birthComparison;
        }

        // Jeżeli wiek też jest taki sam, porównujemy liczbę dzieci
        int childrenComparison = Integer.compare(a.getChildrenCount(), b.getChildrenCount());
        if (childrenComparison != 0) {
            return childrenComparison;
        }

        // Jeżeli liczba dzieci też jest taka sama, zwracamy losowy wynik
        return Math.random() < 0.5 ? -1 : 1;
    }
}

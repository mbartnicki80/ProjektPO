package agh.ics.oop.model;

import agh.ics.oop.model.annotations.Observer;

@Observer
public interface DeadAnimalsObserver {
    void update(int currentDay);
}

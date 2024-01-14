package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import java.util.List;
import java.util.stream.Stream;

public class OptionsParser {

    public static List<MoveDirection> convertStringToMoveDirection(String[] args) throws IllegalArgumentException {

        return Stream.of(args)
                .map(arg -> switch (arg) {
                    case "f" -> MoveDirection.FORWARD;
                    case "b" -> MoveDirection.BACKWARD;
                    case "r" -> MoveDirection.RIGHT;
                    case "l" -> MoveDirection.LEFT;
                    default -> throw new IllegalArgumentException(arg + " is not a legal move specification");
                })
                .toList();
    }
}
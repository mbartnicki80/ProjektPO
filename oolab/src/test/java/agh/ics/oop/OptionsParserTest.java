package agh.ics.oop;
import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OptionsParserTest {
    @Test
    public void optionsParserTest() {
        List<MoveDirection> directions1 = new ArrayList<>(Arrays.asList(MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.RIGHT, MoveDirection.LEFT));
        String[] input1 = {"f", "b", "r", "l"};
        String[] input2 = {"a", "c", "d", "e"};
        List<MoveDirection> directions3 = new ArrayList<>(List.of());
        String[] input3 = {};
        String[] input4 = {"f", "b", "forward", "r"};

        assertIterableEquals(directions1, OptionsParser.convertStringToMoveDirection(input1));

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class, () -> OptionsParser.convertStringToMoveDirection(input2));
        assertEquals("a is not legal move specification", exception2.getMessage());

        assertIterableEquals(directions3, OptionsParser.convertStringToMoveDirection(input3));
        IllegalArgumentException exception4 = assertThrows(IllegalArgumentException.class, () -> OptionsParser.convertStringToMoveDirection(input4));
        assertEquals("forward is not legal move specification", exception4.getMessage());

    }
}
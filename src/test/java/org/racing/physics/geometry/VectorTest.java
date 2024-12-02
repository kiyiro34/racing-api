package org.racing.physics.geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorTest {

    @Test
    public void test_heading(){
        Vector v = new Vector(-1,0);
        assertEquals(Math.toDegrees(v.heading()),-90.0);
    }
}

package geometry;

import org.junit.jupiter.api.Test;
import org.racing.geometry.Vector;

public class VectorTest {

    @Test
    public void test_heading(){
        Vector v = new Vector(-1,0);
        System.out.println(Math.toDegrees(v.heading()));
    }
}

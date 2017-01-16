package com.itservz.bookex.android;

import com.google.android.gms.nearby.messages.Distance;
import com.itservz.bookex.android.util.DistanceCalculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void canVefiyDistance(){
        double d = DistanceCalculator.distance(28.599891,77.080505,28,78);
        System.out.println(d);
    }
}
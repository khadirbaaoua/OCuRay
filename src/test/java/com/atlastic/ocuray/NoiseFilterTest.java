package com.atlastic.ocuray;

import static org.junit.Assert.assertEquals;

import com.atlastic.ocuray.image.filter.NoiseFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/**
 * Created by khadirbaaoua on 06/03/2016.
 */
public class NoiseFilterTest {
    short[][] pixels;
    @Before
    public void setUp() {
        pixels = new short[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                pixels[i][j] = 255;
            }
        }
    }

    @After
    public void tearDown() {
        pixels = null;
    }

    @Test
    public void doFilterCaseNoNeighbours() {
        // set one pixel with no neighbour
        pixels[2][2] = 0;
        assertEquals(false, NoiseFilter.hasAnyNeighbour(2, 2, pixels));
    }

    @Test
    public void doFilterCaseNeighbours() {
        // set one pixel with no neighbour
        pixels[2][2] = 0;
        pixels[2][3] = 0;
        assertEquals(true, NoiseFilter.hasAnyNeighbour(2, 2, pixels));
    }
}

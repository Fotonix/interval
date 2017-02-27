package com.vimond.interval.test.junit;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.vimond.interval.Interval;
import com.vimond.interval.IntervalProcessor;

/**
 * Tests <code>IntervalProcessor</code>.
 *
 * @author Aleksei Zabezhinsky
 */
public class IntervalProcessorTest {

    /**
     * Tests one inclusion interval and one exclusion interval.<br>
     * Includes: 10-100<br>
     * Excludes: 20-30<br>
     * Result: 10-19, 31-100
     *
     */
    @Test
    public void testSingleInclusionSingleExclusion() {
        List<Interval> includeInterval = Arrays.asList(new Interval(10, 100));
        List<Interval> excludeInterval = Arrays.asList(new Interval(20, 30));
        IntervalProcessor processor = new IntervalProcessor(includeInterval, excludeInterval);

        assertArrayEquals(new Interval[] {new Interval(10, 19), new Interval(31, 100)},
                processor.process().toArray());
    }

    /**
     * Tests multiple inclusion intervals and empty exclusion interval.<br>
     * Includes: 50-5000, 10-100<br>
     * Excludes: (note)<br>
     * Result: 10-5000
     *
     */
    @Test
    public void testMultipleInclusionsNoExclusion() {
        List<Interval> includeInterval = Arrays.asList(new Interval(50, 5000), new Interval(10, 100));
        List<Interval> excludeInterval = Collections.emptyList();
        IntervalProcessor processor = new IntervalProcessor(includeInterval, excludeInterval);

        assertArrayEquals(new Interval[] {new Interval(10, 5000)}, processor.process().toArray());
    }

    /**
     * Tests multiple inclusion intervals and one exclusion interval.<br>
     * Includes: 10-100, 200-300<br>
     * Excludes: 95-205<br>
     * Result: 10-94, 206-300
     *
     */
    @Test
    public void testMultipleInclusionsSingleExclusion() {
        List<Interval> includeInterval = Arrays.asList(new Interval(10, 100), new Interval(200, 300));
        List<Interval> excludeInterval = Arrays.asList(new Interval(95, 205));
        IntervalProcessor processor = new IntervalProcessor(includeInterval, excludeInterval);

        assertArrayEquals(new Interval[] {new Interval(10, 94), new Interval(206, 300)},
                processor.process().toArray());
    }

    /**
     * Tests multiple inclusion intervals and multiple exclusion intervals.<br>
     * Includes: 10-100, 200-300, 400-500<br>
     * Excludes: 95-205, 410-420<br>
     * Result: 10-94, 206-300, 400-409, 421-500
     *
     */
    @Test
    public void testMultipleInclusionsMultipleExclusions() {
        List<Interval> includeInterval = Arrays.asList(new Interval(10, 100), new Interval(200, 300),
                new Interval(400, 500));
        List<Interval> excludeInterval = Arrays.asList(new Interval(95, 205), new Interval(410, 420));
        IntervalProcessor processor = new IntervalProcessor(includeInterval, excludeInterval);

        assertArrayEquals(new Interval[] {new Interval(10, 94), new Interval(206, 300),
                new Interval(400, 409), new Interval(421, 500)},
                processor.process().toArray());
    }

    /**
     * Tests empty inclusion interval and multiple exclusion intervals.<br>
     * Includes: (none)<br>
     * Excludes: 20-30, 40-31<br>
     * Result: (none)
     *
     */
    @Test
    public void testNoInclusionMultipleExclusions() {
        List<Interval> includeInterval = Collections.emptyList();
        List<Interval> excludeInterval = Arrays.asList(new Interval(20, 30), new Interval(40, 31));
        IntervalProcessor processor = new IntervalProcessor(includeInterval, excludeInterval);

        assertArrayEquals(new Interval[] {}, processor.process().toArray());
    }

}

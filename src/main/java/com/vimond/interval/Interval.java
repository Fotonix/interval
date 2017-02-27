package com.vimond.interval;

/**
 * Represents an interval.
 *
 * @author Aleksei Zabezhinsky
 */
public class Interval {

    private final int start;

    private final int end;

    /**
     * Constructor.
     *
     * @param start the start value of the interval
     * @param end the end value of the interval
     */
    public Interval(int start, int end) {
        super();
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the start value of the interval
     *
     * @return the start value of the interval
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the end value of the interval
     *
     * @return the end value of the interval
     */

    public int getEnd() {
        return end;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + end;
        result = prime * result + start;
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Interval other = (Interval) obj;
        if (end != other.end) {
            return false;
        }
        if (start != other.start) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Interval [start=" + start + ", end=" + end + "]";
    }

}

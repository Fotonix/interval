package com.vimond.interval;

/**
 * Endpoint of an interval.
 *
 * @author Aleksei Zabezhinsky
 */
public class Endpoint implements Comparable<Endpoint> {

    private final int point;

    private final EndpointType type;

    /**
     * Gets the point.
     *
     * @return the point
     */
    public int getPoint() {
        return point;
    }

    /**
     * Gets the type of the endpoint
     *
     * @return the type of the endpoint
     */
    public EndpointType getType() {
        return type;
    }

    /**
     * Constructor.
     *
     * @param point the point
     * @param type the type of the endpoint
     */
    public Endpoint(int point, EndpointType type) {
        super();
        this.point = point;
        this.type = type;
    }

    /**
     * The type of the endpoint
     *
     */
    public enum EndpointType {
        INCLUDE_BEGIN(4),
        INCLUDE_END(1),
        EXCLUDE_BEGIN(3),
        EXCLUDE_END(2);

        private int priority;

        /**
         * Constructor.
         *
         * @param priority the priority of an endpoint if the value of the point is the same as comparing point
         */
        EndpointType(int priority) {
            this.priority = priority;
        }

    }

    @Override
    public int compareTo(Endpoint newEndpoint) {
        // If points are the same then use priority else just compare point values
        if (this.point == newEndpoint.point) {
            return this.type.priority - newEndpoint.type.priority;
        } else {
            return this.point - newEndpoint.point;
        }
    }

    @Override
    public String toString() {
        return "Endpoint [point=" + point + ", type=" + type + "]";
    }

}

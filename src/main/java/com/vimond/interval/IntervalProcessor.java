package com.vimond.interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.vimond.interval.Endpoint.EndpointType;

/**
 * Represents the main algorithm.
 * Resolves overlapping. "Removes" all the excludes from the include intervals.<br>
 *
 * Final complexity of the main algorithm is O(n*log(n)).
 * The steps of big O calculation can be found in javadoc of "process" method.
 *
 * @author Aleksei Zabezhinsky
 */
public class IntervalProcessor {

    private final List<Interval> include;

    private final List<Interval> exclude;

    /**
     * Constructor.
     *
     * @param include the list of include intervals
     * @param exclude the list of exclude intervals
     */
    public IntervalProcessor(List<Interval> include, List<Interval> exclude) {
        super();
        Objects.requireNonNull(include, "\"include\" parameter cannot be null");
        Objects.requireNonNull(exclude, "\"exclude\" parameter cannot be null");
        this.include = include;
        this.exclude = exclude;
    }

    /**
     * Processes intervals.
     * Returns all the includes and "remove" all the excludes.<br>
     *
     * Complexity of this part of the algorithm is<br>
     * 1. We invoke "calculateEndpoints" method twice. So the complexity is O(2n*log(n)) = O(n*log(n))
     * 2. Complexity of the sort of the endpoints is O(n*log(n))
     * 2. Complexity of the loop thru all endpoints is O(n)
     * Final complexity is O(n*log(n)) + O(n*log(n)) + O(n) = O(n*log(n))
     *
     * @return the processed list of intervals
     */
    public List<Interval> process() {
        if (include.isEmpty()) {
            return Collections.emptyList();
        }

        // Creates and joins endpoints for include and exclude intervals
        List<Endpoint> endpoints = calculateEndpoints(include, IntervalType.INCLUDE);
        if (exclude.size() > 0) {
            endpoints.addAll(calculateEndpoints(exclude, IntervalType.EXCLUDE));
        }
        // Sorts endpoints using natural order
        Collections.sort(endpoints);

        // Loops throw all endpoints and creates final intervals
        List<Interval> intervals = new ArrayList<>();
        boolean exclusion = false;
        boolean inclusion = true;
        int startPoint = 0; // Value of this variable does not matter. We redefine it before first using
        for (Endpoint endpoint : endpoints) {
            switch (endpoint.getType()) {
                case INCLUDE_BEGIN :
                    if (!exclusion) {
                        startPoint = endpoint.getPoint();
                    }
                    inclusion = true;
                    break;
                case INCLUDE_END :
                    if (!exclusion) {
                        intervals.add(new Interval(startPoint, endpoint.getPoint()));
                    }
                    inclusion = false;
                    break;
                case EXCLUDE_BEGIN :
                    if (inclusion && startPoint < endpoint.getPoint()) {
                        intervals.add(new Interval(startPoint, endpoint.getPoint() - 1));
                    }
                    exclusion = true;
                    break;
                case EXCLUDE_END :
                    if (inclusion) {
                        startPoint = endpoint.getPoint() + 1;
                    }
                    exclusion = false;
                    break;
                default :
                    throw new IllegalArgumentException("Unable to find appropriate case for type: "
                            + endpoint.getType());
            } // switch
        } // for
        return intervals;
    }

    /**
     * Sorts the intervals and creates a list endpoints for given intervals.<br>
     *
     * Complexity of this part of the algorithm is<br>
     * 1. Complexity of the sort of the intervals is O(n*log(n))
     * 2. Complexity of the loop thru all intervals is O(n)
     * Final complexity is O(n*log(n)) + O(n) = O(n*log(n))
     *
     * @param intervals the intervals
     * @param type the type of the intervals
     * @return the list of the endpoints
     */
    private List<Endpoint> calculateEndpoints(List<Interval> intervals, IntervalType type) {

        List<Endpoint> endpoints = new ArrayList<>(2 * intervals.size());

        // Sorts by beginnings of intervals
        Collections.sort(intervals, (i1, i2) -> i1.getStart() - i2.getStart());

        Iterator<Interval> iterator = intervals.iterator();
        Interval init = iterator.next();
        int begin = init.getStart();
        int end = init.getEnd();

        while (iterator.hasNext()) {
            Interval next = iterator.next();
            // If we have overlapped interval then extend the end
            if (next.getStart() <= end) {
                end = Math.max(end, next.getEnd());
            } else { // Else creates two new endpoints
                endpoints.addAll(createEndpoints(begin, end, type));
                begin = next.getStart();
                end = next.getEnd();
            }
        }
        endpoints.addAll(createEndpoints(begin, end, type));
        return endpoints;
    }

    /**
     * Creates start and end endpoints of the given type.
     *
     * @param begin the start endpoint
     * @param end the end endpoint
     * @param type the type of the interval
     * @return the list of start and end endpoints
     */
    private List<Endpoint> createEndpoints(int begin, int end, IntervalType type) {
        List<Endpoint> result = new ArrayList<>(2);
        if (type == IntervalType.INCLUDE) {
            result.add(new Endpoint(begin, EndpointType.INCLUDE_BEGIN));
            result.add(new Endpoint(end, EndpointType.INCLUDE_END));
        } else { // EXCLUDE
            result.add(new Endpoint(begin, EndpointType.EXCLUDE_BEGIN));
            result.add(new Endpoint(end, EndpointType.EXCLUDE_END));
        }
        return result;
    }

}

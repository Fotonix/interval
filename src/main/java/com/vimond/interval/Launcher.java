package com.vimond.interval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Application entry point.<br>
 * Reads the data from the console, parses inputs and lunches the main algorithm.
 *
 * @author Aleksei Zabezhinsky
 */
public final class Launcher {

    /**
     * Method main. Lunches the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<Interval> includeIntervals;
        List<Interval> excludeIntervals;
        try (Scanner console = new Scanner(System.in)) {
            includeIntervals = readIntervals("Please enter include intervals (example 1-3, 4-5): ", console);
            excludeIntervals = readIntervals("Please enter exclude intervals (example 1-3, 4-5): ", console);
        }

        IntervalProcessor processor = new IntervalProcessor(includeIntervals, excludeIntervals);
        List<Interval> mergedIntervals = processor.process();

        System.out.println("Output:");
        if (mergedIntervals.isEmpty()) {
            System.out.println("(none)");
        } else {
            Iterator<Interval> iterator = mergedIntervals.iterator();
            StringBuilder output = new StringBuilder();
            while (iterator.hasNext()) {
                Interval interval = iterator.next();
                output.append(interval.getStart()).append('-').append(interval.getEnd());
                if (iterator.hasNext()) {
                    output.append(", ");
                }
            }
            System.out.println(output.toString());
        }
    }

    /**
     * Reads the string line of intervals.
     * Asks for re-entering the interval if the previous enter was incorrect.
     *
     * @param message the welcome message
     * @param input the <code>Scanner</code> for reading input data
     * @return the list of intervals
     */
    private static List<Interval> readIntervals(String message, Scanner input) {
        System.out.println(message);
        do {
            try {
                return intervalParser(input.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Please re-enter the intervals: ");
            }
        } while (true);
    }

    /**
     * Parses string of intervals.
     *
     * @param value the string of intervals
     * @return the list of parsed intervals
     * @throws IllegalArgumentException occurs if unable to parse an interval
     */
    private static List<Interval> intervalParser(String value) throws IllegalArgumentException {
        if (value.isEmpty()) {
            return Collections.emptyList();
        }

        String[] intervalArr = value.split(",");
        List<Interval> result = new ArrayList<>(intervalArr.length);
        // Goes thru array of strings representing intervals
        for (String intervalStr: intervalArr) {
            String str = intervalStr.trim();
            if (!str.isEmpty()) {
                String[] endpoints = str.split("-");
                // If after splitting by "-" we don't have 2 elements that means something wrong
                if (endpoints.length != 2) {
                    throw new IllegalArgumentException("Unable to parse interval: " + str);
                }
                // Creates an Interval, throws an error if unable to parse integer
                try {
                    int start = Integer.parseInt(endpoints[0]);
                    int end = Integer.parseInt(endpoints[1]);
                    result.add(new Interval(start, end));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Unable to parse interval: " + str, e);
                }
            }
        }
        return result;
    }

    /** Private default constructor. */
    private Launcher() {
        super();
    }

}

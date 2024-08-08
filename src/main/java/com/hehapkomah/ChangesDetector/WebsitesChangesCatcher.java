package com.hehapkomah.ChangesDetector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Detecting changes on websites and generating reports class
 */
public class WebsitesChangesCatcher {
    private static final Logger logger = LoggerFactory.getLogger(WebsitesChangesCatcher.class);

    public static void main(String[] args) {
        logger.info("Starting the WebsiteChangesDetector application.");

        ConfigLoader configLoader = new ConfigLoader("config.properties");
        List<String> predefinedPages = Arrays.asList(configLoader.getProperty("pages").split(","));

        if (predefinedPages.isEmpty()) {
            logger.warn("No pages were found in the configuration file");
            return;
        }

        Set<String> allPages = new HashSet<>(predefinedPages);
        Map<String, String> yesterdayPages = RandomPagesGenerator.generateRandomPages(allPages);
        Map<String, String> todayPages = RandomPagesGenerator.generateRandomPages(allPages);

        logger.debug("Yesterday's pages: {}", yesterdayPages);
        logger.debug("Today's pages: {}", todayPages);

        String letter = generateChangeReport(yesterdayPages, todayPages, 0, 3);
        System.out.println(letter);

        logger.info("Report generated.");
    }

    /**
     * Generating a report of changes between two sets of pages
     *
     * @param yesterday Map of URLs and HTML content from yesterday
     * @param today Map of URLs and HTML content for today
     * @param minPages Minimum number of pages for random selection
     * @param maxPages Maximum number of pages for random selection
     * @return Change report
     */
    public static String generateChangeReport(Map<String, String> yesterday, Map<String, String> today, int minPages, int maxPages) {
        Set<String> disappeared = getRandomSubset(yesterday.keySet().stream()
                .filter(url -> !today.containsKey(url))
                .collect(Collectors.toSet()), minPages, maxPages);
        Set<String> appeared = getRandomSubset(today.keySet().stream()
                .filter(url -> !yesterday.containsKey(url))
                .collect(Collectors.toSet()), minPages, maxPages);
        Set<String> changed = getRandomSubset(yesterday.keySet().stream()
                .filter(url -> today.containsKey(url) && !yesterday.get(url).equals(today.get(url)))
                .collect(Collectors.toSet()), minPages, maxPages);

        return buildReport(disappeared, appeared, changed);
    }

    /**
     * Retrieving a random subset of pages
     *
     * @param pages Set of pages
     * @param min Minimum number of pages
     * @param max Maximum number of pages
     * @return Random subset of pages
     */
    private static Set<String> getRandomSubset(Set<String> pages, int min, int max) {
        List<String> pageList = new ArrayList<>(pages);
        Collections.shuffle(pageList);
        int numberOfPages = Math.max(min, Math.min(max, pageList.size()));
        return pageList.stream().limit(numberOfPages).collect(Collectors.toSet());
    }

    /**
     * Generating a change report
     *
     * @param disappeared Disappeared pages
     * @param appeared Appeared pages
     * @param changed Changed pages
     * @return Report line
     */
    private static String buildReport(Set<String> disappeared, Set<String> appeared, Set<String> changed) {
        StringBuilder report = new StringBuilder();
        report.append("Hello dear Isaac Clarke!\n\n")
                .append("Over the past 24 hours, ");

        if (disappeared.isEmpty() && appeared.isEmpty() && changed.isEmpty()) {
            report.append("there have been no changes in the sites entrusted to you.");
        } else {
            report.append("the following changes have occurred in the sites entrusted to you:\n\n");
            appendSection(report, "The following pages have disappeared:", disappeared);
            appendSection(report, "The following new pages have appeared:", appeared);
            appendSection(report, "The following pages have changed:", changed);
        }

        return report.toString();
    }

    /**
     * Adding a section to the report
     *
     * @param report Report builder
     * @param title Section title
     * @param urls Page URLs
     */
    private static void appendSection(StringBuilder report, String title, Set<String> urls) {
        report.append(title).append("\n");
        if (urls.isEmpty()) {
            report.append("no pages\n");
        } else {
            urls.forEach(url -> report.append(url).append("\n"));
        }
        report.append("\n");
    }
}

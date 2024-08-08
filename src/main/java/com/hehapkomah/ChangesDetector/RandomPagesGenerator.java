package com.hehapkomah.ChangesDetector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generating random pages class
 */
public class RandomPagesGenerator {

    /**
     * Generating random pages from a given set
     *
     * @param pages Set of pages
     * @return Map of URL and HTML content
     */
    public static Map<String, String> generateRandomPages(Set<String> pages) {
        Random random = new Random();
        List<String> pageList = new ArrayList<>(pages);
        Collections.shuffle(pageList);
        int numPages = random.nextInt(pageList.size() + 1);
        return pageList.stream().limit(numPages)
                .collect(Collectors.toMap(
                        url -> url,
                        url -> "<html>Random content for " + url + " " + random.nextInt(100) + "...</html>",
                        (existing, replacement) -> existing));
    }
}

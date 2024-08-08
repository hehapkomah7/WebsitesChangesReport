package com.hehapkomah.ChangesDetector;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing functionality of WebsitesChangesCatcher class
 */
public class CatcherTest {

    /**
     * Checking the correct generation of a non-empty set of pages using the generateRandomPages method when passing a non-empty set of URLs
     */
    @Test
    public void testGenerateRandomPages() {
        Set<String> pages = Set.of(
                "https://h3hota.com/",
                "https://www.moddb.com/games/dawn-of-war/"
        );
        Map<String, String> result = RandomPagesGenerator.generateRandomPages(pages);
        assertNotNull(result, "The generated pages map should not be null.");
        assertTrue(pages.containsAll(result.keySet()),
                "All generated pages should be part of the input pages.");
    }

    /**
     * Checking the correct generation of a report on added and deleted pages using the generateChangeReport method
     */
    @Test
    public void testAddedAndRemovedPages() {
        Map<String, String> yesterday = Map.of(
                "https://h3hota.com/", "<html>Content</html>",
                "https://www.moddb.com/games/dawn-of-war/", "<html>Content</html>"
        );
        Map<String, String> today = Map.of(
                "https://www.moddb.com/games/dawn-of-war/", "<html>Content</html>",
                "https://stalkerportaal.ru/", "<html>Content</html>"
        );
        String report = WebsitesChangesCatcher.generateChangeReport(yesterday, today, 0, 3);
        assertTrue(report.contains("The following pages have disappeared:"),
                "The report should indicate that pages have disappeared.");
        assertTrue(report.contains("https://h3hota.com/"),
                "The report should list the disappeared page.");
        assertTrue(report.contains("The following new pages have appeared:"),
                "The report should indicate that pages have appeared.");
        assertTrue(report.contains("https://stalkerportaal.ru/"),
                "The report should list the new page.");
    }

    /**
     * Checking the correct generation of a report on changed pages or missing pages using the generateChangeReport method
     */
    @Test
    public void testGenerateChangeReport() {
        Map<String, String> yesterday = Map.of(
                "https://h3hota.com/", "<html>Old content</html>"
        );
        Map<String, String> today = Map.of(
                "https://h3hota.com/", "<html>New content</html>"
        );
        String report = WebsitesChangesCatcher.generateChangeReport(yesterday, today, 0, 3);
        assertTrue(report.contains("The following pages have changed:"),
                "The report should indicate that pages have changed.");
        assertTrue(report.contains("https://h3hota.com/"),
                "The report should list the changed page.");
    }

    /**
     * Checking the correct generation of a no change report using empty maps in the generateChangeReport method
     */
    @Test
    public void testEmptyReports() {
        Map<String, String> emptyMap = Map.of();
        String report = WebsitesChangesCatcher.generateChangeReport(emptyMap, emptyMap, 0, 3);
        assertTrue(report.contains("there have been no changes in the sites entrusted to you."),
                "The report should indicate no changes.");
    }

    /**
     * Checking the correct generation of a report using the generateChangeReport method if there are no changes on the pages
     */
    @Test
    public void testNoChanges() {
        Map<String, String> yesterday = Map.of(
                "https://h3hota.com/", "<html>Content</html>"
        );
        Map<String, String> today = Map.of(
                "https://h3hota.com/", "<html>Content</html>"
        );
        String report = WebsitesChangesCatcher.generateChangeReport(yesterday, today, 0, 3);
        assertTrue(report.contains("there have been no changes in the sites entrusted to you."),
                "The report should indicate no changes.");
    }

    /**
     * Checking the correct processing of cases with empty map for the one of the days using the generateChangeReport method
     */
    @Test
    public void testEdgeCases() {
        Map<String, String> yesterday = Map.of(
                "https://h3hota.com/", "<html>Old content</html>"
        );
        Map<String, String> today = Map.of();
        String report = WebsitesChangesCatcher.generateChangeReport(yesterday, today, 0, 3);
        assertTrue(report.contains("The following pages have disappeared:"),
                "The report should indicate that pages have disappeared.");
        assertTrue(report.contains("https://h3hota.com/"),
                "The report should list the disappeared page.");
    }
}

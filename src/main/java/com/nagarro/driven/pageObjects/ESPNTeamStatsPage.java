package com.nagarro.driven.pageObjects;

import com.nagarro.driven.base.BasePage;
import com.nagarro.driven.utils.TestReportLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class ESPNTeamStatsPage extends BasePage {

    private final By teamsMenu = By.xpath("//a[@title='Cricket Teams']");
    private final By statsTab = By.xpath("//a[contains(.,'Stats')]");
    private final By viewFullListButtons = By.xpath("//a[contains(.,'View full list')]");

    public ESPNTeamStatsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the ESPN Cricinfo homepage.
     */
    public void openHomePage(String url) {
        TestReportLogger.info("Navigating to ESPN Cricinfo homepage: " + url);
        driver.get(url);
    }

    /**
     * Hover over "Teams" and click on the given team name (e.g., India).
     */
    public void navigateToTeam(String teamName) throws InterruptedException {
        Actions actions = new Actions(driver);
        TestReportLogger.info("Hovering over Teams menu...");
        WebElement teamsElement = driver.findElement(teamsMenu);
        actions.moveToElement(teamsElement).perform();
        Thread.sleep(1000);

        TestReportLogger.info("Clicking on team: " + teamName);
        WebElement teamElement = driver.findElement(By.xpath("//div[@class='ds-px-2 ds-py-2']//span[contains(.,'" + teamName + "')]"));
        teamElement.click();
        Thread.sleep(3000);
    }

    /**
     * Clicks on the "Stats" tab in the team's header.
     */
    public void openTeamStatsTab() throws InterruptedException {
        TestReportLogger.info("Opening the Stats tab...");
        WebElement statsElement = driver.findElement(statsTab);
        statsElement.click();
        Thread.sleep(3000);
    }

    /**
     * Clicks on "View full list" for the specific match format (ODI, Test, T20I)
     * under the section for either "Most Runs" or "Most Wickets".
     */
    public void openFullStats(String matchFormat, String statsBy) throws InterruptedException {
        TestReportLogger.info("Opening full stats for: " + matchFormat + " - " + statsBy);

        // Locate the section based on "Most Runs" or "Most Wickets"
        String sectionXPath = String.format("//h2[contains(.,'%s')]/ancestor::div[contains(@class,'ds-px-4')]", statsBy);
        WebElement sectionElement = driver.findElement(By.xpath(sectionXPath));

        // Inside that section, find the correct format (ODI/Test/T20I)
        WebElement formatElement = sectionElement.findElement(By.xpath(".//span[contains(.,'" + matchFormat + "')]/ancestor::div[contains(@class,'ds-flex')]//a[contains(.,'View full list')]"));
        formatElement.click();

        TestReportLogger.pass("Navigated to full list for " + statsBy + " in " + matchFormat);
        Thread.sleep(4000);
    }

    /**
     * Extracts and returns the top player's name from the full list.
     */
    public String getTopPlayerName() {
        try {
            WebElement topPlayer = driver.findElement(By.xpath("(//table//tr[2]//a)[1]"));
            String playerName = topPlayer.getText().trim();
            TestReportLogger.pass("Top player extracted: " + playerName);
            return playerName;
        } catch (Exception e) {
            TestReportLogger.fail("Failed to extract top player name: " + e.getMessage());
            return "N/A";
        }
    }
}

package com.nagarro.driven.pageObjects;

import com.nagarro.driven.base.BasePage;
import com.nagarro.driven.utils.TestReportLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class loginPage extends BasePage {

    private final By usernameField = By.id("field-userName");
    private final By passwordField = By.id("field-password");
    private final By loginButton   = By.id("btn-login");
    public final By liveScore   = By.xpath("//a[@title='Live Cricket Score']");
    public final By seasonView   = By.xpath("//a[contains(.,'Season view')]");
    public final By schedule = By.xpath("//a[contains(.,'Schedule')]");
    public final By series = By.xpath("//a[@title='Series']");
    public final By ranjiTrophy = By.xpath("//a[contains(.,'Ranji Trophy')]");
    public final By features = By.xpath("//a[contains(.,'Features')]");
    public final By writer = By.xpath("//a[contains(.,'Writer')]");
    public final By videos = By.xpath("//a[contains(.,'Videos')]");
    public final By beyondBoundaries = By.xpath("//a[contains(.,'Beyond Boundaries')]");
    public final By teams = By.xpath("//a[contains(.,'Teams')]");
    public final By indiaTeam = By.xpath("//a[contains(.,'India')]");
    public final By desktopScorecard = By.xpath("//a[contains(.,'Desktop Scorecard')]");
    public final By seriesMenu = By.xpath("//a[@title='Cricket Fixtures']");
    public final By featuresMenu = By.xpath("//a[@title='Cricket Features']");
    public final By videosMenu = By.xpath("//a[@title='Cricket Videos']");
    public final By teamsMenu = By.xpath("//a[@title='Cricket Teams']");
    public final By ranjiTYrophySubMenu = By.xpath("//div[@class='ds-px-2 ds-py-2']//a[contains(.,'Ranji Trophy')]");
    public final By writersSubMenu = By.xpath("//span[contains(.,'Writers')]");
    public final By australiaVsIndiaSubMenu = By.xpath("//span[contains(.,'Australia vs India')]");
    public final By beyondBoundariesSubMenu = By.xpath("//div[@class='ds-px-2 ds-py-2']//span[contains(.,'Beyond Boundaries')]");
    public final By indiaSubMenu = By.xpath("//div[@class='ds-px-2 ds-py-2']//span[contains(.,'India')]");
    public final By scheduleSubMenu = By.xpath("//div[@class='ds-px-2 ds-py-2']//span[contains(.,'Schedule')]");

    public loginPage(WebDriver driver) {
        super(driver);
        this.driver=driver;
    }

    public void navigateToLoginPage(String url) {
        TestReportLogger.info("Navigating to Login Page: " + url);
        this.driver.get(url);
    }

    public void enterUsername(String username) {
        TestReportLogger.info("Entering username: " + username);
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        TestReportLogger.info("Entering password.");
        type(passwordField, password);
    }

    public void clickLogin() {
        TestReportLogger.info("Clicking on Login button.");
        click(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public void hoverOverLiveScore() {
        TestReportLogger.info("Hovering over Live Score element.");
        hoverOverElement(liveScore);
    }
    public String getSeasonViewText() {
        TestReportLogger.info("Fetching text from 'Season view' element.");
        String text = getText(seasonView);
        TestReportLogger.pass("Fetched text: " + text);
        return text;
    }

    public String getElementHref(String elementName) {
        By locator = null;
        switch (elementName) {
            case "liveScore":
                locator = liveScore;
                break;
            case "seasonView":
                locator = seasonView;
                break;
            default:
                throw new IllegalArgumentException("Invalid element name: " + elementName);
        }

        TestReportLogger.info("Fetching href for element: " + elementName);
        String href = driver.findElement(locator).getAttribute("href");
        TestReportLogger.pass("Fetched href: " + href);
        return href;
    }

}
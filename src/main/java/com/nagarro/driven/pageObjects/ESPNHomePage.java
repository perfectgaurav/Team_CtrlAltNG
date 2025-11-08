package com.nagarro.driven.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ESPNHomePage {

    WebDriver driver;

    public ESPNHomePage(WebDriver driver) {
        this.driver = driver;
    }

    private By teamsMenu = By.xpath("//a[@title='Cricket Teams']");

    public void navigateToTeam(String teamName) {
        Actions actions = new Actions(driver);
        WebElement teamsElement = driver.findElement(teamsMenu);
        actions.moveToElement(teamsElement).perform();

        WebElement teamOption = driver.findElement(By.xpath("//div[@class='tippy-content']//a[contains(@href,'" + teamName.toLowerCase() + "')]"));
        teamOption.click();
        System.out.println("✅ Navigated to team: " + teamName);
    }
}

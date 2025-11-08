package com.nagarro.driven.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ESPNTeamPage {

    WebDriver driver;

    public ESPNTeamPage(WebDriver driver) {
        this.driver = driver;
    }

    // Navigate to Teams → [Team Name]
    public void openTeam(String teamName) {
        driver.findElement( By.xpath("//a[@title='Cricket Teams']")).click();
        driver.findElement(By.xpath("//a[text()='" + teamName + "']")).click();
    }

    // Click Stats tab
    public void clickStatsTab() {
        driver.findElement(By.xpath("//a[@href='/team/india-6/stats']")).click();
    }


    public void clickFullList(String matchFormat, String statsBy) {
        String yes="wzrk-confirm";

//        if(driver.findElement(By.id(yes)).isDisplayed()){
//            driver.findElement(By.id(yes)).click();
//        }
        String xpath = "//h3[contains(.,'ODI')]//a[@title='View full list']";
        driver.findElement(By.xpath(xpath)).click();
    }


    public List<List<String>> captureTable(String statsBy) {
        List<List<String>> tableData = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement table;

            if (statsBy == null || statsBy.isEmpty()) {
                // Get first visible table on page
                table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//table[1]")
                ));
            } else {
                // Flexible XPath: finds h2/h3/h4 containing the text, then the first following table
                String xpath = "//*[self::h2 or self::h3 or self::h4][contains(.,'" + statsBy + "')]"
                        + "/following::table[1]";

                table = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(xpath)
                ));
            }

            // Capture headers
            List<WebElement> headers = table.findElements(By.xpath(".//thead/tr/th"));
            List<String> headerRow = new ArrayList<>();
            for (WebElement header : headers) {
                headerRow.add(header.getText().trim());
            }
            tableData.add(headerRow);

            // Capture all rows
            List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                List<String> rowData = new ArrayList<>();
                for (WebElement cell : cells) {
                    rowData.add(cell.getText().trim());
                }
                tableData.add(rowData);
            }

            System.out.println("✅ Captured table: " + statsBy + " | Rows: " + (tableData.size() - 1));

        } catch (TimeoutException te) {
            System.out.println("⚠️ Table not found or not visible for: " + statsBy);
        } catch (Exception e) {
            System.out.println("⚠️ Could not capture table for: " + statsBy);
            e.printStackTrace();
        }

        return tableData;
    }

}

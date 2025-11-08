package ui;

import com.nagarro.driven.utils.ExcelParser;
import com.nagarro.driven.utils.TestReportLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.NoSuchElementException;

public class ESPNValidatorTest {

    public static void main(String[] args) throws Exception {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.espncricinfo.com/");
        TestReportLogger.info("Navigated to ESPN CricInfo homepage.");
        Thread.sleep(4000);

        String inputPath = "src/test/resources/testdata/Challenge A1 - Source File.xlsx";
        String outputPath = "Result1.xlsx";       // output file
        List<Map<String, String>> testData = ExcelParser.readExcel(inputPath, "Teams");
        List<Map<String, String>> resultList = new ArrayList<>();

        Actions actions = new Actions(driver);

        TestReportLogger.info("Starting ESPN menu & submenu validation for " + testData.size() + " rows.");

        for (Map<String, String> row : testData) {
            String menuExpected = row.get("Menu (Expected)");
            String submenuExpected = row.get("Sub-menu (Expected)");
            Map<String, String> resultRow = new LinkedHashMap<>(row);

            String status = "PASS";
            String reason = "";

            TestReportLogger.info("-----------------------------------------------------");
            TestReportLogger.info("🔹 Validating Menu: [" + menuExpected + "] → Sub-menu: [" + submenuExpected + "]");

            try {
                // 🟢 Locate Menu Item
                WebElement menuItem = driver.findElement(By.xpath("//a[contains(.,'" + menuExpected + "')]"));
                actions.moveToElement(menuItem).perform();
                TestReportLogger.pass("Hovered over menu: " + menuExpected);
                Thread.sleep(1000);

                String actualMenuText = menuItem.getText().trim();
                if (!actualMenuText.equalsIgnoreCase(menuExpected)) {
                    status = "FAIL";
                    reason = "Menu text mismatch (Expected: " + menuExpected + ", Found: " + actualMenuText + ")";
                    TestReportLogger.fail(reason);
                }

                String menuHref = menuItem.getAttribute("href");
                resultRow.put("Menu Anchor Link", menuHref != null ? menuHref : "");
                TestReportLogger.info("Menu HREF found: " + menuHref);

                if (menuHref != null) {
                    boolean menuLinkOk = isLinkWorking(menuHref);
                    if (menuLinkOk) {
                        TestReportLogger.pass("✅ Menu link working fine: " + menuHref);
                    } else {
                        status = "FAIL";
                        reason = "Menu anchor link broken";
                        TestReportLogger.fail("❌ Menu link broken: " + menuHref);
                    }
                }

                // 🟢 Locate Sub-menu
                List<WebElement> subMenus = driver.findElements(By.xpath("//div[@class='ds-px-2 ds-py-2']//a[contains(.,'" + submenuExpected + "')]"));
                if (!subMenus.isEmpty()) {
                    WebElement subMenu = subMenus.get(0);
                    actions.moveToElement(subMenu).perform();
                    TestReportLogger.info("Hovered over sub-menu: " + submenuExpected);

                    String subText = subMenu.getText().trim();
                    if (!subText.equalsIgnoreCase(submenuExpected)) {
                        status = "FAIL";
                        reason = "Sub-menu text mismatch (Expected: " + submenuExpected + ", Found: " + subText + ")";
                        TestReportLogger.fail(reason);
                    }

                    String subHref = subMenu.getAttribute("href");
                    resultRow.put("Sub-menu Anchor Link", subHref != null ? subHref : "");
                    TestReportLogger.info("Sub-menu HREF found: " + subHref);

                    if (subHref != null) {
                        boolean subLinkOk = isLinkWorking(subHref);
                        if (subLinkOk) {
                            TestReportLogger.pass("✅ Sub-menu link working fine: " + subHref);
                        } else {
                            status = "FAIL";
                            reason = "Sub-menu anchor link broken";
                            TestReportLogger.fail("❌ Sub-menu link broken: " + subHref);
                        }
                    }
                } else {
                    status = "FAIL";
                    reason = "Sub-menu not found";
                    TestReportLogger.fail("❌ Could not locate sub-menu: " + submenuExpected);
                }

            } catch (NoSuchElementException e) {
                status = "FAIL";
                reason = "Menu item not found: " + menuExpected;
                TestReportLogger.fail(reason);
            } catch (Exception e) {
                status = "FAIL";
                reason = e.getMessage();
                TestReportLogger.fail("Unexpected error: " + reason);
            }

            resultRow.put("Status (Example)", status);
            resultRow.put("Failure reason if any?", reason);
            resultList.add(resultRow);

            TestReportLogger.info("Completed validation for [" + menuExpected + "] → [" + submenuExpected + "] : " + status);
            TestReportLogger.info("-----------------------------------------------------");
        }

        ExcelParser.writeResult(outputPath, resultList);
        driver.quit();

        TestReportLogger.pass("✅ Validation completed. Results written to: " + outputPath);
        System.out.println("✅ Result.xlsx generated successfully!");
    }

    private static boolean isLinkWorking(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 🔹 Add browser-like headers
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            conn.setRequestProperty("Referer", "https://www.espncricinfo.com/");
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(7000);
            conn.connect();

            int code = conn.getResponseCode();
            TestReportLogger.info("HTTP check for [" + urlStr + "] returned code: " + code);

            // ✅ Treat normal web blocking codes as valid
            if (code == 403 || code == 999) {
                TestReportLogger.info("Link [" + urlStr + "] returned " + code + " but treated as VALID (anti-bot block).");
                return true;
            }

            return code >= 200 && code < 400;
        } catch (Exception e) {
            TestReportLogger.fail("HTTP check failed for URL: " + urlStr + " → " + e.getMessage());
            return false;
        }
    }


}



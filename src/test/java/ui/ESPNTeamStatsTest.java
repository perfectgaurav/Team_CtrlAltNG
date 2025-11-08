package ui;

import com.nagarro.driven.pageObjects.ESPNHomePage;
import com.nagarro.driven.pageObjects.ESPNTeamPage;
import com.nagarro.driven.utils.ExcelParser2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.io.FileOutputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ESPNTeamStatsTest {

    public static void main(String[] args) {

        // Load Excel
        List<Map<String, String>> testData = ExcelParser2.readExcel("src/test/resources/testdata/Challenge A2 - Source File.xlsx");

        // Setup ChromeDriver
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.espncricinfo.com/");

        ESPNTeamPage teamPage = new ESPNTeamPage(driver);
        ESPNHomePage homepage=new ESPNHomePage(driver);
        for (Map<String, String> row : testData) {
            String team = row.get("Team");
            String format = row.get("Match Format");
            String statsBy = row.get("Stats By");

            System.out.println("➡️ Running test: " + team + " | " + format + " | " + statsBy);

            try {
                homepage.navigateToTeam(team);
                teamPage.clickStatsTab();
                teamPage.clickFullList(format, statsBy);
                List<WebElement> headers = driver.findElements(By.xpath(".//thead/tr/td"));

                for (WebElement header : headers) {
                    System.out.print(header.getText() + " | ");
                }
                System.out.println();

                List<WebElement> rows = driver.findElements(By.xpath(".//tbody/tr"));
                for (WebElement i : rows) {
                    List<WebElement> cells = i.findElements(By.tagName("td"));
                    for (WebElement cell : cells) {
                        System.out.print(cell.getText() + " | ");
                    }
                    System.out.println();
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet(team + "_" + format + "_" + statsBy);
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.size(); i++) {
                    headerRow.createCell(i).setCellValue(headers.get(i).getText());
                }

                for (int i = 0; i < rows.size(); i++) {
                    Row excelRow = sheet.createRow(i + 1);
                    List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
                    for (int j = 0; j < cells.size(); j++) {
                        excelRow.createCell(j).setCellValue(cells.get(j).getText());
                    }
                }

                for (int i = 0; i < headers.size(); i++) {
                    sheet.autoSizeColumn(i);
                }

                FileOutputStream fos = new FileOutputStream("India_ODI_MostRuns.xlsx");
                workbook.write(fos);
                fos.close();
                workbook.close();

                System.out.println("✅ Table data written successfully to India_ODI_MostRuns.xlsx");


                System.out.println("✅ Stats page processed successfully for: " + team);

            } catch (Exception e) {
                System.out.println("❌ Failed for: " + row);
                e.printStackTrace();
            }
        }

        driver.quit();
        System.out.println("🎯 All test executions completed.");
    }
}

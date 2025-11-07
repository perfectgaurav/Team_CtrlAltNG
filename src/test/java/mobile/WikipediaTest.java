package mobile;

import com.nagarro.driven.base.BaseMobileTest;
import com.nagarro.driven.pageObjects.WikipediaPage;
import org.testng.annotations.Test;

public class WikipediaTest extends BaseMobileTest {

    @Test
    public void searchInWikipedia() throws InterruptedException {
        WikipediaPage wikiPage = new WikipediaPage(driver);
        wikiPage.skipOnboarding();
        wikiPage.openOptions();
        wikiPage.goToLogin();
        wikiPage.switchToCreateAccountLogin();
        wikiPage.enterUsername();
        wikiPage.enterPassword();
        wikiPage.clickLogin();
        wikiPage.declineSavePassword();
    }
}
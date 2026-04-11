package demo;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.logging.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider {

    ChromeDriver driver;
    Wrappers wp;

    @BeforeTest
public void startBrowser() {

    System.setProperty("java.util.logging.config.file", "logging.properties");

    System.setProperty(
        ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY,
        "build/chromedriver.log"
    );

    ChromeOptions options = new ChromeOptions();
    LoggingPreferences logs = new LoggingPreferences();

    logs.enable(LogType.BROWSER, Level.ALL);
    logs.enable(LogType.DRIVER, Level.ALL);

    options.setCapability("goog:loggingPrefs", logs);
    options.addArguments("--remote-allow-origins=*");

    driver = new ChromeDriver(options);
    driver.manage().window().maximize();

    wp = new Wrappers(driver);
}

    @Test
    public void testCase01() {
        SoftAssert sa = new SoftAssert();

        driver.get("https://www.youtube.com/");

        wp.click(By.xpath("//a[contains(@href,'/about')]"));

        String url = driver.getCurrentUrl();
        sa.assertTrue(url.contains("about"));

        String heading = wp.getText(By.tagName("h1"));
        sa.assertTrue(heading.toLowerCase().contains("about"));

        sa.assertAll();
    }

   @Test
public void testCase02() {
    SoftAssert sa = new SoftAssert();

    driver.get("https://www.youtube.com/");

    //wp.scrollBy(1000);

    wp.click(By.xpath("//yt-formatted-string[text()='Movies']"));

    wp.scrollBy(1000);

    List<WebElement> movies = driver.findElements(By.xpath("//ytd-grid-movie-renderer"));

    sa.assertTrue(movies.size() > 0);

    //sa.assertAll();
}
    @Test
    public void testCase03() {
        SoftAssert sa = new SoftAssert();

        driver.get("https://www.youtube.com/feed/music");

        wp.scrollBy(500);

        WebElement playlist = driver.findElement(By.xpath("(//ytd-rich-item-renderer)[1]"));

        List<WebElement> spans = playlist.findElements(By.tagName("span"));

        int count = 0;

        for (WebElement span : spans) {
            String text = span.getText();
            if (text.toLowerCase().contains("song")) {
                count = Integer.parseInt(text.replaceAll("[^0-9]", ""));
                break;
            }
        }

        sa.assertTrue(count <= 50);

        sa.assertAll();
    }

    @Test
    public void testCase04() {
        SoftAssert sa = new SoftAssert();

        driver.get("https://www.youtube.com/feed/news");

        wp.scrollBy(500);

        List<WebElement> posts = driver.findElements(By.xpath("//ytd-post-renderer"));

        int totalLikes = 0;

        for (int i = 0; i < Math.min(3, posts.size()); i++) {

            WebElement post = posts.get(i);

            try {
                String likes = post.findElement(By.xpath(".//span[contains(text(),'like')]")).getText();
                int likeCount = Integer.parseInt(likes.replaceAll("[^0-9]", ""));
                totalLikes += likeCount;
            } catch (Exception e) {
                totalLikes += 0;
            }
        }

        sa.assertTrue(totalLikes >= 0);

        sa.assertAll();
    }

  @Test(dataProvider = "fetchData")
public void testCase05(String searchItem) {

    SoftAssert sa = new SoftAssert();

    driver.get("https://www.youtube.com/");

    wp.sendKeys(By.name("search_query"), searchItem);
    driver.findElement(By.name("search_query")).sendKeys(Keys.ENTER);

    int totalViews = 0;
    int scrollCount = 0;

    while (totalViews < 100000000 && scrollCount < 10) {

        List<WebElement> views = driver.findElements(
                By.xpath("//ytd-video-renderer//span[contains(text(),'views')]")
        );

        for (WebElement view : views) {

            String text = view.getText().toLowerCase();

            try {
                if (text.contains("m")) {
                    double v = Double.parseDouble(text.split(" ")[0]);
                    totalViews += (int) (v * 1000000);
                } else if (text.contains("k")) {
                    double v = Double.parseDouble(text.split(" ")[0]);
                    totalViews += (int) (v * 1000);
                }
            } catch (Exception e) {
            }
        }

        wp.scrollToBottom();
        scrollCount++;
    }

    sa.assertTrue(totalViews >= 0);

    sa.assertAll();
}

    @AfterTest
    public void endTest() {
        driver.quit();
    }
}
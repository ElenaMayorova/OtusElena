import config.ProjectServerConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class lesson2 {
    public static WebDriver chromeDriver;
    private static Logger logger = LogManager.getLogger(lesson2.class);
    private static ProjectServerConfig projectServerConfig = ConfigFactory.create(ProjectServerConfig.class);

    @BeforeClass
    public static void webDriverOpen() {
        WebDriverManager.chromedriver().setup();
        chromeDriver = new ChromeDriver();
        logger.info("Запуск ChromeDriver перед тестом");
    }

    @Test
    public void openOtus() {
        chromeDriver.get(projectServerConfig.url());
        logger.info("Открываем сайт otus.ru");
        Assert.assertEquals("Онлайн‑курсы для профессионалов, дистанционное обучение современным профессиям", chromeDriver.getTitle());
        logger.info("Проверяем, что сайт открыт");
    }

    @AfterClass
    public static void webDriverClose() {
        logger.info("Закрываем ChromeDriver после теста");
        if (chromeDriver != null)
            chromeDriver.quit();
    }
}

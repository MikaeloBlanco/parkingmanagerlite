package com.hormigo.david.parkingmanager.draw;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.hormigo.david.parkingmanager.draw.domain.Draw;
import com.hormigo.david.parkingmanager.user.service.UserService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DrawIntegrationTest {

        @MockBean
        private UserService userService;
        @Value("${local.server.port}")
        private  int port;
        private static ChromeDriver chromeDriver;
        @BeforeAll
        public static void prepareWebDriver() {

            System.setProperty("webdriver.chrome.driver","D:\\ChromeDriver\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            chromeDriver = new ChromeDriver(options);
        
        }
    
    @Test
    public  void checkDrawListIsDisplayed(){
        String url = "http://localhost:" + port + "/draws";
        chromeDriver.get(url);
        String actualTitle = chromeDriver.getTitle();
        WebElement createButton = chromeDriver.findElement(By.id("draws-button-create"));
        WebElement actualHeading = chromeDriver.findElement(By.id("draws-title"));
        WebElement table = chromeDriver.findElement(By.className("table"));
        String actualHeadingText = actualHeading.getText();

        assertAll("Comprobar que se muestra la lista de sorteos", 
        () -> {assertNotNull(createButton);},
        () -> {assertEquals("Sorteos", actualTitle);},
        () -> {assertEquals("Sorteos",actualHeadingText);},
        () -> {assertNotNull(table);}
        );
        chromeDriver.quit();
    }
    @Test
    public void checkDrawIsCreated(){
        String url = "http://localhost:" + port + "/newDraws";
        chromeDriver.get(url);
        String actualTitle = chromeDriver.getTitle();
        WebElement createButton = chromeDriver.findElement(By.id("newDraws-button-create"));
        WebElement actualHeading = chromeDriver.findElement(By.id("newDraws-title"));
        String actualHeadingText = actualHeading.getText();
        Draw sorteo = new Draw();
        long id = sorteo.getId(); 

        assertAll("Comprobar si un Sorteo ha sido creado",
        () -> {assertNotNull(createButton);},
        () -> {assertEquals("Nuevo Sorteo", actualTitle);},
        () -> {assertEquals("Nuevo Sorteo", actualHeadingText);},
        () -> {assertNotNull(sorteo.getId());},
        () -> {assertNotEquals(sorteo.getId(), id);}
        );

        chromeDriver.quit();
    }
}

package com.hormigo.david.parkingmanager.user;

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

import com.hormigo.david.parkingmanager.user.domain.Role;
import com.hormigo.david.parkingmanager.user.domain.User;
import com.hormigo.david.parkingmanager.user.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

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
    public  void checkUserListIsDisplayed(){
        String url = "http://localhost:" + port + "/users";
        chromeDriver.get(url);
        String actualTitle = chromeDriver.getTitle();
        WebElement createButton = chromeDriver.findElement(By.id("users-button-create"));
        WebElement actualHeading = chromeDriver.findElement(By.id("users-title"));
        WebElement table = chromeDriver.findElement(By.className("table"));
        String actualHeadingText = actualHeading.getText();

        assertAll("Comprobar que se muestra la lista de usuarios", 
        () -> {assertNotNull(createButton);},
        () -> {assertEquals("Usuarios", actualTitle);},
        () -> {assertEquals("Usuarios",actualHeadingText);},
        () -> {assertNotNull(table);}
        );
        chromeDriver.quit();
    }
    @Test
    public void checkUserIsCreated(){
        String url = "http://localhost:" + port + "/newUsers";
        chromeDriver.get(url);
        String actualTitle = chromeDriver.getTitle();
        WebElement createButton = chromeDriver.findElement(By.id("newUsers-button-create"));
        WebElement actualHeading = chromeDriver.findElement(By.id("newUsers-title"));
        String actualHeadingText = actualHeading.getText();
        User usuario = new User("David@correo", "David", "Hormigo", "Ramirez", Role.PROFESSOR);
        String email = "david@email";

        assertAll("Comprobar si un usuario ha sido creado",
        () -> {assertNotNull(createButton);},
        () -> {assertEquals("Nuevo Usuario", actualTitle);},
        () -> {assertEquals("Nuevo Usuario", actualHeadingText);},
        () -> {assertNotNull(usuario.getEmail());},
        () -> {assertNotEquals(usuario.getEmail(), email);}
        );

        chromeDriver.quit();
    }
}

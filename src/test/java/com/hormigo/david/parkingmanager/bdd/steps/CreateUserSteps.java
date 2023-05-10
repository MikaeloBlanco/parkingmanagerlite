package com.hormigo.david.parkingmanager.bdd.steps;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.hormigo.david.parkingmanager.user.service.UserService;

import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class CreateUserSteps {
    @MockBean
    private UserService mockedUserService;
    @Value("${local.server.port}")
    private  int port;
    private static ChromeDriver driver;
    @BeforeAll
    public static void prepareWebDriver() {

        System.setProperty("webdriver.chrome.driver","C:\\ChromeDriver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        
    }  

    @Given("un administrador esta en el formulario de creación")
    public void openCreateUserForm() {
        driver.get("http://localhost:" + port + "/newUser");
    }
    //@Given("el correo no esta asignado a otro usuario")
    //public void emailDoesNotExists(){
    //    when(mockedUserService.userExists(anyString())).thenReturn(false);
    //}

    @When("relleno el correo")
    public void typeEmail(){
        driver.findElement(By.id("user")).sendKeys("cualquier@correo.es");
    }
}

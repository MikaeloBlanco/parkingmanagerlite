package com.hormigo.david.parkingmanager.bdd.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.Name;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.hormigo.david.parkingmanager.bdd.CucumberConfiguration;
import com.hormigo.david.parkingmanager.core.exceptions.BadRegisterException;
import com.hormigo.david.parkingmanager.user.UserAlreadyExistsException;
import com.hormigo.david.parkingmanager.user.domain.Role;
import com.hormigo.david.parkingmanager.user.domain.User;
import com.hormigo.david.parkingmanager.user.domain.UserDao;
import com.hormigo.david.parkingmanager.user.domain.UserRepository;
import com.hormigo.david.parkingmanager.user.service.UserService;
import com.hormigo.david.parkingmanager.user.service.UserServiceImpl;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Dado.Dados;
import io.cucumber.spring.CucumberContextConfiguration;
import net.bytebuddy.asm.Advice.FieldValue;

@CucumberContextConfiguration
public class CucumberSteps extends CucumberConfiguration {

    private static ChromeDriver driver;
    @BeforeAll
    public static void prepareWebDriver() {
        System.setProperty("webdriver.chrome.driver", "D:\\ChromeDriver\\chromedriver.exe");

    }
    @MockBean
    private UserRepository mockedRepository;
    @InjectMocks
    private UserServiceImpl mockedUserService;

    @Value("${local.server.port}")
    private int port;

    @Before
    public void createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void quitDriver() {
        driver.quit();
    
    }

    @Dado("un usuario esta en la pagina {}")
    public void openPage(String pageName) {
        driver.get(getUrlFromPageName(pageName));

    }

    @Dado("el correo {} no esta asignado a otro usuario")
    public void mockUserNotExists(String email){
        when(mockedUserService.userExists(email)).thenReturn(false);
        when(mockedRepository.findByEmail(email)).thenReturn(null);
        
    }

    @Cuando("relleno el campo {} con {}")
    public void populateField(String fieldName,String fieldValue){
        WebElement inputField = driver.findElement(By.id(getFieldIdFromName(fieldName)));
        inputField.sendKeys(fieldValue);
    }

    @Cuando("un usuario rellena el campo {} con {} menos nombre")
    public void populateFieldMinusName(String fieldName, String fieldValue){
        WebElement inputField = driver.findElement(By.id(getFieldIdFromName(fieldName)));
        if(inputField.equals(fieldName.equals("*{name}"))){
            inputField.sendKeys(null);
        }
        inputField.sendKeys(fieldValue);
    }

    @Cuando("un usuario rellena el campo {} con {} menos email")
    public void populateFieldMinusEmail(String fieldName, String fieldValue){
        WebElement inputField = driver.findElement(By.id(getFieldIdFromName(fieldName)));
        if(inputField.equals(fieldName.equals("*{email}"))){
            inputField.sendKeys(null);
        }
        inputField.sendKeys(fieldValue);
    }

    @Cuando("un usuario rellena el campo {} con {} menos apellido1")
    public void populateFieldMinusLastName(String fieldName, String fieldValue){
        WebElement inputField = driver.findElement(By.id(getFieldIdFromName(fieldName)));
        if(inputField.equals(fieldName.equals("*{lastName1}"))){
            inputField.sendKeys(null);
        }
        inputField.sendKeys(fieldValue);
    }

    @Cuando("un usuario rellena el campo {} con {} y email ya existe")
    public void populateFieldEmailTaken(String fieldName, String fieldValue){
        UserRepository mockedRepository = mock(UserRepository.class);
        UserDao userDao = new UserDao(fieldValue,fieldValue,fieldValue,fieldValue,Role.PROFESSOR);
        when(mockedRepository.findByEmail(fieldValue)).thenReturn(notNull());
    }
    

    @Cuando("el usuario hace click sobre el botón de {}")
    public void clickButton(String buttonName) {
        String buttonId = "";
        switch (buttonName) {
            case "Usuarios":
                buttonId = "to-users-link";
                break;
            case "Sorteos":
                buttonId = "to-draws-link";
                break;
            case "crear usuario":
                buttonId = "user-create-button-submit";
                break;
            default:
                break;
        }
        driver.findElement(By.id(buttonId)).click();
    }

    @Entonces("esta en la pagina de {}")
    public void isInPage(String pageName) {

        assertTrue(driver.getCurrentUrl().equals(getUrlFromPageName(pageName)));
    }

    @Entonces("se ha persistido el usuario en la base de datos")
    public void checkUserWasSaved(){
        verify(mockedRepository,times(1)).save(any(User.class));
    }

    @Entonces("se muestra un campo de {}")
    public void fieldIsDisplayed(String fieldName){
        String fieldId = getFieldIdFromName(fieldName);
        WebElement field = driver.findElement(By.id(fieldId));
        
        assertTrue(field.isDisplayed());
    }

    @Entonces("Salta error de que falta {} y se mantiene el usuario en la pagina de creación de usuarios")
        public void errorIsCatched(String fieldName, String fieldValue) throws BadRegisterException{
            switch(fieldName){
                case "*{email}":
                    populateFieldMinusEmail(fieldName, fieldValue);
                    break;
                case "*{name}":
                    populateFieldMinusName(fieldName, fieldValue);
                    break;
                case "*{lastName1}":
                    populateFieldMinusLastName(fieldName, fieldValue);
                    break;
            }
        }

    @Entonces("Salta un error de que el usuario ya existe")
        public void userIsCreated(){
            verify(mockedRepository,times(1)).findByEmail(anyString());
        }


    private String getUrlFromPageName(String pageName) {
        String endPoint = "";
        switch (pageName) {
            case "inicial":
                endPoint = "/";
                break;
            case "lista de usuarios":
                endPoint = "/users";
                break;
            case "lista de sorteos":
                endPoint = "/draws";
                break;
            case "creación de usuarios":
                endPoint = "/newUser";
            break;
            default:
                break;
        }
        return getUrlFromEndPoint(endPoint);
    }

    private String getFieldIdFromName(String fieldName) {
        String fieldId ="";
        switch (fieldName) {
            case "correo electrónico":
                fieldId = "user-create-field-email";
                break;
            case "nombre":
            fieldId = "user-create-field-name";
            break;
            case "primer apellido":
            fieldId = "user-create-field-lastname1";
            break;
            case "segundo apellido":
            fieldId = "user-create-field-lastname2";
            break;
            default:
                break;
        }
        return fieldId;
    }
    private String getUrlFromEndPoint(String endpoint) {
        return "http://localhost:" + port + endpoint;
    }

}

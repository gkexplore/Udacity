package edu.udacity.java.nano;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatApplicationTest {

    private static final String APP_URL = "http://localhost:8080/";

    private WebDriver webDriver;

    @Before
    public void beforeTest(){
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        webDriver = new ChromeDriver();
        webDriver.get(APP_URL);
    }

    @Test
    public void testApplication() throws Exception {

        login("Karthik");

        Thread.sleep(2000);

        //verify OnOpen works fine
        Assert.assertTrue("Verify the user count is 1", getChatCount() == 1 );


        //switch to new tab and login with another user
        ((JavascriptExecutor)webDriver).executeScript("window.open()");
        Thread.sleep(2000);
        ArrayList<String> tabs = new ArrayList<String> (webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1)); //switches to new tab
        webDriver.get(APP_URL);
        login("John");

        //wait
        Thread.sleep(2000);

        //verify the chat count increase after new user logged in
        Assert.assertTrue("Verify the user count is 2", getChatCount() == 2 );

        //send text from the newly logged in user
        sendText("Hi");

        //Verify On Message works fine
        //verify the first logged in user receives the message
        Thread.sleep(2000);
        webDriver.switchTo().window(tabs.get(0));
        WebElement johnText = webDriver.findElement(By.xpath("//div[contains(.,'John：Hi')]"));
        Assert.assertTrue("Verify if John's Text Exist", johnText.isDisplayed());

        //send text from the first logged in user
        sendText("Hi");

        //switch to john
        Thread.sleep(2000);
        webDriver.switchTo().window(tabs.get(1)); //switches to new tab
        WebElement karthikText = webDriver.findElement(By.xpath("//div[contains(.,'Karthik：Hi')]"));
        Assert.assertTrue("Verify if Karthik's Text Exist", karthikText.isDisplayed());

        //John leaves from chat
        leave();

        //wait
        Thread.sleep(2000);

        //Verify Leave works fine
        webDriver.switchTo().window(tabs.get(0));
        Assert.assertTrue("Verify the user count is greater than zero", getChatCount() == 1 );

        //Karthik leaves from chat
        leave();

    }

    @After
    public void afterTest(){
        webDriver.quit();
    }

    public void login(String name){
        WebElement userName = webDriver.findElement(By.id("username"));
        Assert.assertTrue(userName.isDisplayed());
        userName.sendKeys(name);
        WebElement loginButton = webDriver.findElement(By.xpath("//a[contains(.,'Login')]"));
        loginButton.click();
    }

    public void sendText(String text){
        WebElement sendTextInputField = webDriver.findElement(By.id("msg"));
        sendTextInputField.sendKeys(text);
        WebElement sendButton = webDriver.findElement(By.xpath("//button[contains(.,'SEND (ENTER)')]"));
        sendButton.click();
    }

    public int getChatCount(){
        WebElement countElement =  webDriver.findElement(By.xpath("//span[contains(@class,'chat-num')]"));
        return Integer.parseInt(countElement.getText());
    }

    public void leave(){
        WebElement leaveButton = webDriver.findElement(By.xpath(("//a[contains(@class, 'mdui-btn mdui-btn-icon')]")));
        leaveButton.click();
    }
}
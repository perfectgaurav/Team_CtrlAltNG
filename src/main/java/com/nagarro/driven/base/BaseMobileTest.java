package com.nagarro.driven.base;

import com.nagarro.driven.drivers.MobileDriverFactory;
import com.nagarro.driven.drivers.MobileService;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseMobileTest {

    protected AppiumDriver driver;

    @BeforeClass
    public void setup() throws Exception {
        MobileService.startService("127.0.0.1", 4723);
        MobileDriverFactory.setDriver("Android");
        driver = MobileDriverFactory.getDriver();
    }

    @AfterClass
    public void teardown() {
        MobileDriverFactory.quitDriver();
    }
}

package com.nagarro.driven.drivers;

import com.nagarro.driven.config.ConfigManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;

public class MobileDriverFactory {

    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(String platformName) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", platformName);

        if (platformName.equalsIgnoreCase("Android")) {
            caps.setCapability("platformName", ConfigManager.get("platformName"));
            caps.setCapability("deviceName", ConfigManager.get("deviceName"));
            caps.setCapability("automationName", ConfigManager.get("automationName"));
            caps.setCapability("app", new File(ConfigManager.get("app")).getAbsolutePath());
            caps.setCapability("appPackage", ConfigManager.get("appPackage"));
            caps.setCapability("appActivity", ConfigManager.get("appActivity"));
            caps.setCapability("autoGrantPermissions", true);
            caps.setCapability("newCommandTimeout", 300);
            driver.set(new AndroidDriver(MobileService.getAppiumService(), caps));

        }
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}

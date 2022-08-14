package com.mallex2000;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

//https://www.browserstack.com/docs/automate/selenium/getting-started/java#introduction
//https://www.browserstack.com/guide/selenium-webdriver-tutorial

//https://woowee.de/surfbar/
public class Main {

	public static void main(String[] args) throws InterruptedException {
		System.out.println(args.length + " main start parameter");
		System.out.println("1. driver dir (\"C:\\\\Users\\\\malle\\\\github\\\\driver");
		System.out.println("2. surfbar (mallex2000)");
		System.out.println("3. browser (firefox, edge, chrome)");
		System.out.println("4. sleepTime (20)");

		if (args.length != 4) {
			throw new RuntimeException(args.length + " main parameter sind falsch.");
		}
		String driverDir = args[0];
		String surfbar = args[1];
		String browser = args[2];
		int sleepTime = Integer.valueOf(args[3]);
		System.out.println("Parameter");
		System.out.println("1. driver dir " + driverDir);
		System.out.println("2. surfbar " + surfbar);
		System.out.println("3. browser " + browser);
		System.out.println("4. sleepTime " + sleepTime);
		System.setProperty("webdriver.chrome.driver", driverDir + "\\chromedriver.exe");
		System.setProperty("webdriver.edge.driver", driverDir + "\\msedgedriver.exe");
		System.setProperty("webdriver.gecko.driver", driverDir + "\\geckodriver.exe");
		killAllChromeProcesses(browser);
		startEbesucher(surfbar, browser, sleepTime);
		System.out.println("done");
	}

	private static void killAllChromeProcesses(String browser) {
//		startCommand("tasklist.exe");
		startCommand("taskkill /F /IM " + browser + ".exe");
	}

	private static void startEbesucher(String surfbar, String browser, int sleepTime) throws InterruptedException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("profile.default_content_setting_values.notifications", 2);
		System.out.println("open " + browser);
		WebDriver driver = null;
		if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", map);
			driver = new ChromeDriver(options);
		}
		if (browser.equalsIgnoreCase("edge")) {
			// https://docs.microsoft.com/en-us/microsoft-edge/webdriver-chromium/
			EdgeOptions options = new EdgeOptions();
			options.setExperimentalOption("prefs", map);
			driver = new EdgeDriver(options);
		}
		if (browser.equalsIgnoreCase("firefox")) {
			// https://www.guru99.com/gecko-marionette-driver-selenium.html
			FirefoxOptions options = new FirefoxOptions();
			options.setCapability("marionette", true);
			driver = new FirefoxDriver(options);
		}
//		driver.manage().window().setSize(new Dimension(1024,768));
		System.out.println("maximize " + browser);
		driver.manage().window().maximize();
		System.out.println("implicitlyWait");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(sleepTime * 2));
		System.out.println("sleep " + (sleepTime));
		Thread.sleep(sleepTime * 1000);
		String url = "https://www.ebesucher.de/surfbar/" + surfbar;
		System.out.println("call " + url);
		driver.get(url);
		System.out.println("sleep " + (sleepTime));
		Thread.sleep(sleepTime * 1000);
		System.out.println("find link surf_now_button");
		WebElement element = driver.findElement(By.id("surf_now_button"));
		System.out.println("click surf_now_button");
		element.click();
		System.out.println("end");
	}

//	public void login() {
//		System.setProperty("webdriver.chrome.driver", "path of driver");
//		WebDriver driver = new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.get("https://www.browserstack.com/users/sign_in");
//		WebElement username = driver.findElement(By.id("user_email_Login"));
//		WebElement password = driver.findElement(By.id("user_password"));
//		WebElement login = driver.findElement(By.name("commit"));
//		username.sendKeys("abc@gmail.com");
//		password.sendKeys("your_password");
//		login.click();
//		String expectedUrl = driver.getCurrentUrl();
//	}

	private static void startCommand(String cmd) {
		Process pr;
		System.out.println("call: " + cmd);
		try {
			pr = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			pr.waitFor();
			System.out.println("ok!");
			in.close();
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
			System.exit(2);
		}

//		Runtime rt = Runtime.getRuntime();
//		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1)
//			try {
//				rt.exec("tasklist.exe");
////				rt.exec("taskkill /F /IM chrome.exe");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		else {
//			try {
//				rt.exec("kill -9 xxx");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

	}
}
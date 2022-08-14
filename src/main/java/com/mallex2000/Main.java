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

//https://www.browserstack.com/docs/automate/selenium/getting-started/java#introduction
//https://www.browserstack.com/guide/selenium-webdriver-tutorial

//https://woowee.de/surfbar/
public class Main {

	public static void main(String[] args) {
		System.out.print(args.length + " main start parameter");
		System.out.print(
				"1. driver dir (\"C:\\\\Users\\\\malle\\\\github\\\\driver\\\\chromedriver.exe\"");
		System.out.print("2. surfbar (mallex2000)");
		if (args.length != 2) {
			throw new RuntimeException(args.length + " main parameter sind falsch.");
		}
		String driver = args[0];
		String surfbar = args[1];
		System.out.print("Parameter");
		System.out.print("1. driver " + driver);
		System.out.print("2. surfbar " + surfbar);
		System.setProperty("webdriver.chrome.driver", driver);
		System.setProperty("webdriver.edge.driver", "C:\\Users\\malle\\github\\chromiumdriver\\driver\\msedgedriver.exe");
		
		killAllChromeProcesses();
		startEbesucher(surfbar, "edge");
		System.out.print("done");
	}

	private static void killAllChromeProcesses() {
		startCommand("tasklist.exe");
		startCommand("taskkill /F /IM chrome.exe");
	}

	private static void startEbesucher(String surfbar, String browser) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("profile.default_content_setting_values.notifications", 2);
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
		
//		firefox: 		https://www.guru99.com/gecko-marionette-driver-selenium.html
		
//		driver.manage().window().setSize(new Dimension(1024,768));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.get("https://www.ebesucher.de/surfbar/" + surfbar);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		WebElement element = driver.findElement(By.id("surf_now_button"));
		element.click();
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
# Test Examples for Common Languages
Every subdirectory provides a smoke test of the selenium grid and its supported drivers for a language such as Java or JS.  

Each has its own instructions and contains a **configuration.json** within the source directory. Here's an example:
```
{
    "hubUrl": "http://localhost:4444/wd/hub",
    "homePage": "http://www.google.com",
    "expectedTitle": "Google",
    "labelChrome": "chrome",
    "labelFirefox": "firefox",
    "screenshotDestination": "screenshots"
}
```
This will be used by default. You can override any of the configuration values by providing your own **configuration.json** in the the location of the according README.md. This will be merged with the default configuration when running the tests and is ignored by **.gitignore**.

# Links
https://seleniumhq.github.io/docs/  

# Test examples for several languages
Every provided example contains a smoke test of the selenium grid and its supported drivers within its own subdirectory.  

Each directory has its own instructions and contains the default **configuration.json**. Here's an example:
```
{
    "hubUrl": "http://localhost:4444/wd/hub",
    "homePage": "http://www.google.com",
    "expectedTitle": "Google",
    "chrome": "chrome",
    "firefox": "firefox",
    "screenshotDestination": "screenshots"
}
```
You can override any of the configuration values by providing your own **configuration.json** in the root of each directory. This will be merged with the default configuration when running the tests and is ignored by **.gitignore**.

# Links
https://seleniumhq.github.io/docs/  

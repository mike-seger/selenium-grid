const fs = require('fs');
const { Builder } = require('selenium-webdriver');
const { expect } = require('chai');

describe('RemoteWebDriverTest', function() {
  var chrome;
  var firefox;
  var configuration;
  var screenshotDir;

  before(async function () {
    configuration = loadConfiguration();
    screenshotDir = configuration.screenshotDestination.replace('/', '')+'/';
    try { fs.mkdirSync(screenshotDir); } catch(err) {}
    chrome = await new Builder().forBrowser('chrome').usingServer(configuration.hubUrl).build();
    firefox = await new Builder().forBrowser('firefox').usingServer(configuration.hubUrl).build();
  });

  it('Test Chrome', async function () {
    await chrome.get(configuration.homePage);
    expect(await chrome.getTitle()).to.equal(configuration.expectedTitle);
    await takeScreenShot(chrome, configuration.labelChrome);
  });

  it('Test Firefox', async function () {
    await firefox.get(configuration.homePage);
    expect(await firefox.getTitle()).to.equal(configuration.expectedTitle);
    await takeScreenShot(firefox, configuration.labelFirefox);
  });

  after(async () => { 
    await chrome.quit();
    await firefox.quit(); 
  });

  function getDateString() {
    return new Date().toISOString().replace(/[Z:-]/g, "")
      .replace(/[.][0-9]*$/g, "").replace("T", "-");
  }

  function takeScreenShot(driver, namePrefix) {
    driver.takeScreenshot().then(
      function(image, err) {
        var fileName=screenshotDir+namePrefix+'-'+getDateString()+'.png'
        fs.writeFile(fileName, image, 'base64', function(err) {
            if(err) console.log(err);
        });
      }
    );
  }

  function loadConfiguration() {
    const defaultConfiguration = require('./configuration.json');
    var userConfiguration = {};
    if (fs.existsSync('./configuration.json')) 
      userConfiguration = require('../configuration.json');
    var configuration = Object.assign({}, defaultConfiguration, userConfiguration);
    console.log(configuration);
    return configuration;
  }
});

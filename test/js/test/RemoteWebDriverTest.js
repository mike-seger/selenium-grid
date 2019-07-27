const fs = require('fs');
const {Builder, By, until} = require('selenium-webdriver');
const { expect } = require('chai');

describe('RemoteWebDriverTest', function() {
  var firefox;
  var configuration;
  var screenshotDir;
  
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

  before(async function () {
    configuration = loadConfiguration();
    screenshotDir = configuration.screenshotDestination.replace('/', '')+'/';
    try { fs.mkdirSync(screenshotDir); } catch(err) {}
    firefox = await new Builder().forBrowser('firefox').usingServer(configuration.hubUrl).build();
    chrome = await new Builder().forBrowser('chrome').usingServer(configuration.hubUrl).build();
  });

  it('Test Chrome', async function () {
    await chrome.get(configuration.homePage);
    const title = await chrome.getTitle();
    expect(title).to.equal(configuration.expectedTitle);
    await takeScreenShot(chrome, 'chrome');
  });

  it('Test Firefox', async function () {
    await firefox.get(configuration.homePage);
    const title = await firefox.getTitle();
    expect(title).to.equal(configuration.expectedTitle);
    await takeScreenShot(firefox, 'firefox');
  });

  after(async () => { 
    await chrome.quit();
    await firefox.quit(); 
  });
});

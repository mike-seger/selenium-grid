const fs = require('fs');
const log4js = require('log4js');
const { Builder } = require('selenium-webdriver');
const { expect } = require('chai');

describe('RemoteWebDriverTest', function() {
  var chrome;
  var firefox;
  var configuration;
  var screenshotDir;
  var logger;

  before(async function () {
    configuration = loadConfiguration();
    log4js.configure(configuration.log4jsConfiguration);
    delete configuration.log4jsConfiguration;
    logger=log4js.getLogger("RemoteWebDriverTest");
    logger.info("\n"+colorJson(configuration));
    screenshotDir = configuration.screenshotDestination.replace('/', '')+'/';
    try { fs.mkdirSync(screenshotDir); } catch(err) {}
    logger.info("Setting up drivers");
    chrome = await new Builder().forBrowser('chrome').usingServer(configuration.hubUrl).build();
    firefox = await new Builder().forBrowser('firefox').usingServer(configuration.hubUrl).build();
    logger.info("Done setting up drivers");
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
    logger.info("Quitting drivers");
    await chrome.quit();
    await firefox.quit();
    logger.info("Done quitting drivers");
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
    return configuration;
  }

  function colorJson(json, prefix, indent) {
    var result;
    result=JSON.stringify(configuration, null, 2);
    result=result.replace(/^/gm, "  ");
    result=result.replace(/([{}])/gm, "\u001b[36m$1\u001b[0m");
    result=result.replace(/\":([^,]+)(,|$)/gm, "\":\u001b[32m$1\u001b[0m$2");
    result=result.replace(/("[a-zA-Z0-9]+"):/gm, "\u001b[34m$1\u001b[0m:");
    return result;
  }
});

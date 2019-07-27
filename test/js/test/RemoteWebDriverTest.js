const {Builder, By, until} = require('selenium-webdriver');
let fs = require('fs');
const { expect } = require('chai');

describe('RemoteWebDriverTest', function() {
  var firefox;
  var configuration;
  
  before(function () {
    const defaultConfiguration = require('./configuration.json');
    var userConfiguration = {};
    if (fs.existsSync('./configuration.json')) userConfiguration = require('../configuration.json');
    configuration = Object.assign({}, defaultConfiguration, userConfiguration);
    console.log(configuration);

    firefox = new Builder().forBrowser('firefox')
      .usingServer(configuration.hubUrl)
      .build();
  });

  it('Test Firefox', async function () {
    await firefox.get(configuration.homePage);
    const title = await firefox.getTitle();
    expect(title).to.equal(configuration.expectedTitle);
  });

  after(() => firefox.quit());
});

# selenium-grid-howto

Running a Selenium grid with Firefox and Chrome browsers and drivers in 5
minutes.  

 
# Using

```
# Start grid with 1 node each
$ docker-compose up -d

# Start grid with 15 nodes each
$ docker-compose scale chrome=15 firefox=15

# Stop grid
$ docker-compose down
```

# Links
- https://github.com/SeleniumHQ/docker-selenium  
- https://jitpack.io/p/SeleniumHQ/docker-selenium  
- https://hub.docker.com/u/selenium  
- https://robotninja.com/blog/introduction-using-selenium-docker-containers-end-end-testing/
- https://stackoverflow.com/questions/24319662/from-inside-of-a-docker-container-how-do-i-connect-to-the-localhost-of-the-mach

package com.net128.test.selenium;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.skjolber.jackson.jsh.AnsiSyntaxHighlight;
import com.github.skjolber.jackson.jsh.DefaultSyntaxHighlighter;
import com.github.skjolber.jackson.jsh.SyntaxHighlighter;
import com.github.skjolber.jackson.jsh.SyntaxHighlightingJsonGenerator;
import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

class Configuration {
    public URL hubUrl;
    public String screenshotDestination;
    public List<Page> pages;
    public Duration maxPageLoadDuration;
    public Browsers browsers;
    private final static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new JavaTimeModule());

    static class Browsers {
        public Browser chrome;
        public Browser firefox;
        static class Browser {
            public Size dimension;
            @SuppressWarnings("unused")
            static class Size extends Dimension {
                public Size() {
                    super(0, 0);
                }
                public Size(int width, int height) {
                    super(width, height);
                }
            }
        }
    }

    static class Page {
        public String url;
        public String title;
    }

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class.getSimpleName());

    private static String colorizedJson(Object o) {
        try {
            JsonFactory jsonFactory = new JsonFactory();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            JsonGenerator delegate = jsonFactory.createGenerator(os, JsonEncoding.UTF8);
            SyntaxHighlighter highlighter = DefaultSyntaxHighlighter
                    .newBuilder()
                    .withField(AnsiSyntaxHighlight.BLUE)
                    .withString(AnsiSyntaxHighlight.GREEN)
                    .withNumber(AnsiSyntaxHighlight.CYAN)
                    .withCurlyBrackets(AnsiSyntaxHighlight.CYAN)
                    .withComma(AnsiSyntaxHighlight.WHITE)
                    .withColon(AnsiSyntaxHighlight.WHITE)
                    .build();
            try (JsonGenerator jsonGenerator = new SyntaxHighlightingJsonGenerator(delegate, highlighter, true)) {
                jsonGenerator.setCodec(objectMapper);
                jsonGenerator.writeObject(o);
                os.write(AnsiSyntaxHighlight.RESET.getBytes());
                return os.toString();
            } catch(Exception e) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

    public static Configuration load() throws IOException {
        String configName = "configuration.json";
        Configuration configuration = objectMapper.readValue(PageTest.class
                .getResource("/"+configName), Configuration.class);
        if(new File(configName).exists())
            try (FileInputStream fis=new FileInputStream(configName))
            { objectMapper.readerForUpdating(configuration).readValue(fis); }
        logger.info("Active Configuration:\n{}", colorizedJson(configuration));
        return configuration;
    }

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}

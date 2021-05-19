package com.net128.test.selenium;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.util.List;

class Configuration {
    public URL hubUrl;
    public String screenshotDestination;
    public List<Page> pages;
    public Browsers browsers;

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
        ObjectMapper om=new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            JsonFactory jsonFactory = new JsonFactory();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JsonGenerator delegate = jsonFactory.createGenerator(baos, JsonEncoding.UTF8);
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
                jsonGenerator.setCodec(om);
                jsonGenerator.writeObject(o);
                baos.write(AnsiSyntaxHighlight.RESET.getBytes());
                return baos.toString();
            } catch(Exception e) {
                return om.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

    public static Configuration load() throws IOException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String configName = "configuration.json";
        Configuration configuration = mapper.readValue(PageTest.class
                .getResource("/"+configName), Configuration.class);
        if(new File(configName).exists())
            try (FileInputStream fis=new FileInputStream(configName))
            { mapper.readerForUpdating(configuration).readValue(fis); }
        logger.info("Active Configuration:\n{}", colorizedJson(configuration));
        return configuration;
    }

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}

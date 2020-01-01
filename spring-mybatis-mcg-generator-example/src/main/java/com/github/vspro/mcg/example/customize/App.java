package com.github.vspro.mcg.example.customize;

import com.github.vspro.cg.api.McgGenerator;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.config.parser.ConfigurationParser;
import com.github.vspro.cg.exception.XMLParseException;

import java.io.File;
import java.io.IOException;

public class App {


    public static void main(String[] args) throws IOException, XMLParseException {
        ConfigurationParser parser = new ConfigurationParser();
        File file = new File(App.class.getClassLoader().getResource("mcg-config.xml").getPath());
//        File file = new File(App.class.getClassLoader().getResource("customize-mcg-config.xml").getPath());
        Configuration configuration = parser.parseConfiguration(file);
        McgGenerator generator = new CustomizeMcgGenerator(configuration);
        generator.generate();
    }
}

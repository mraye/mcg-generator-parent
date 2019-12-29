package com.github.vspro.cg.config.parser.node.impl;

import com.github.vspro.cg.config.constant.CfgNodeConstants;
import com.github.vspro.cg.config.parser.node.BaseNodeParser;
import com.github.vspro.cg.config.profile.MapperGeneratorConfiguration;
import com.github.vspro.cg.config.Configuration;
import com.github.vspro.cg.exception.InvalidException;

import java.util.Properties;

import static com.github.vspro.cg.util.StringUtil.stringHasValue;

public class MapperNodeParser extends BaseNodeParser {


    public MapperNodeParser() {
        super(CfgNodeConstants.NODE_MAPPER);
    }


    @Override
    protected void doInjectProperties(Configuration configuration, Properties properties) throws InvalidException {
        MapperGeneratorConfiguration mapper = new MapperGeneratorConfiguration();
        configuration.setMapperGeneratorConfiguration(mapper);

        String targetPackage = properties.getProperty("targetPackage");
        String targetProject = properties.getProperty("targetProject");
        String useClassPath = properties.getProperty("useClassPath");
        String rootInterface = properties.getProperty("rootInterface");

        mapper.setTargetPackage(targetPackage);
        mapper.setTargetProject(targetProject);
        mapper.setRootInterface(rootInterface);

        if (stringHasValue(useClassPath)) {
            mapper.setUserClassPath(Boolean.parseBoolean(useClassPath));
        } else {
            mapper.setUserClassPath(true);
        }

        mapper.validate();

    }
}

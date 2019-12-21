package com.github.vspro.cg.config.xml;

import com.github.vspro.cg.config.constant.XmlConstants;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class XmlParserEntityResolver implements EntityResolver {


    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

        if (XmlConstants.MCG_PUBLIC_ID.equalsIgnoreCase(publicId)) {
            InputStream inputStream = getClass().getResourceAsStream(XmlConstants.MCG_DTD_URL);
            return new InputSource(inputStream);
        }

        return null;
    }


}

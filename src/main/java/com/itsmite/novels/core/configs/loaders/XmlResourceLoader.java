package com.itsmite.novels.core.configs.loaders;

import com.itsmite.novels.core.errors.exceptions.RuntimeCoreException;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.function.Function;

import static java.lang.String.format;

@Slf4j
public class XmlResourceLoader<R> {

    private final Function<Document, R> processor;

    public XmlResourceLoader(Function<Document, R> processor) {
        this.processor = processor;
    }

    public R process(String resource) {
        InputStream resourceInputStream = getClass().getResourceAsStream(resource);
        if (resourceInputStream == null) {
            throw new RuntimeCoreException(format("Could not resolve resource '%s'", resource));
        }

        try {
            Document document = readXmlFile(resourceInputStream);
            return processor.apply(document);
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
            throw new UncheckedIOException(ioException);
        } catch (SAXException | ParserConfigurationException e) {
            log.error(e.getMessage());
            throw new RuntimeCoreException(e.getMessage());
        }
    }

    private Document readXmlFile(InputStream resourceInputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(resourceInputStream);
    }
}

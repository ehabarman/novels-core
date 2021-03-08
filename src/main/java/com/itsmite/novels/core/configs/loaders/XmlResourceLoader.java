package com.itsmite.novels.core.configs.loaders;

import com.itsmite.novels.core.errors.exceptions.RuntimeCoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import static java.lang.String.format;
import static java.nio.file.FileSystems.newFileSystem;
import static java.util.Collections.emptyMap;

@Slf4j
public class XmlResourceLoader<R> {

    private final Function<Document, R> processor;

    public XmlResourceLoader(Function<Document, R> processor) {
        this.processor = processor;
    }

    public R process(String resource) {
        URL url = resolveResource(resource);
        URI location;
        try {
            location = url.toURI();
        } catch (URISyntaxException uriException) {
            throw new RuntimeCoreException(format("Could not resolve resource '%s'", resource));
        }

        // creating a filesystem to make sure traversal works in a jar
        try (FileSystem ignored = (location.getScheme().equals("jar") ? newFileSystem(location, emptyMap()) : null)) {
            Path path = Paths.get(location);
            Document document = readXmlFile(path);
            return processor.apply(document);
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
            throw new UncheckedIOException(ioException);
        } catch (SAXException | ParserConfigurationException e) {
            log.error(e.getMessage());
            throw new RuntimeCoreException(e.getMessage());
        }
    }

    /**
     * @param resource resource of interest
     * @return URL of the resource full path
     */
    public URL resolveResource(String resource) {
        URL url = ResourceLoader.class.getResource(resource);
        if (url == null) {
            throw new RuntimeCoreException(format("Could not find resource '%s'", resource));
        }
        return url;
    }

    private Document readXmlFile(Path path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(path.toFile());
    }
}

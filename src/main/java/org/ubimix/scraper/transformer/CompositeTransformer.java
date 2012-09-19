package org.ubimix.scraper.transformer;

import java.io.IOException;

import org.ubimix.commons.uri.Path;
import org.ubimix.commons.uri.Uri;
import org.ubimix.commons.uri.UriToPath;
import org.ubimix.commons.uri.path.PathManager;
import org.ubimix.commons.xml.XmlException;
import org.ubimix.commons.xml.XmlWrapper;
import org.ubimix.commons.xml.atom.AtomFeed;

/**
 * Dispatcher to external transformers. The transformations are based on a map
 * of URLs and other transformers. When a given URL is passed, the closest
 * prefix is used (the longest common prefix) for identifying the transformer to
 * be used.
 * 
 * @author kotelnikov
 */
public class CompositeTransformer implements IDocumentTransformer {

    private IDocumentTransformer fDefaultTransformer;

    private PathManager<IDocumentTransformer> fUrlMapping = new PathManager<IDocumentTransformer>();

    public CompositeTransformer() {
        this(null);
    }

    public CompositeTransformer(IDocumentTransformer defaultTransformer) {
        setDefaultTransformer(defaultTransformer);
    }

    public void addTransformer(Uri baseUrl, IDocumentTransformer normalizer) {
        Path path = UriToPath.getPath(baseUrl);
        String str = path.toString();
        fUrlMapping.add(str, normalizer);
    }

    public IDocumentTransformer getDefaultTransformer() {
        return fDefaultTransformer;
    }

    public void removeNormalizer(Uri baseUrl) {
        Path path = UriToPath.getPath(baseUrl);
        String str = path.toString();
        fUrlMapping.remove(str);
    }

    public void setDefaultTransformer(IDocumentTransformer defaultTransformer) {
        fDefaultTransformer = defaultTransformer;
    }

    public AtomFeed transformDocument(Uri url, XmlWrapper doc)
        throws XmlException,
        IOException {
        Uri.Builder builder = url.getBuilder();
        builder.getPathBuilder().setFileName(null);
        builder.setQuery((String) null);
        Path path = UriToPath.getPath(builder.build());
        String str = path.toString();
        AtomFeed result = null;
        IDocumentTransformer normalizer = fUrlMapping.getNearestValue(str);
        if (normalizer == null) {
            normalizer = fDefaultTransformer;
        }
        if (normalizer != null) {
            result = normalizer.transformDocument(url, doc);
        }
        return result;
    }

}
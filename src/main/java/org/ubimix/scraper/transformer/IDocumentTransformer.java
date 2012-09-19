package org.ubimix.scraper.transformer;

import java.io.IOException;

import org.ubimix.commons.uri.Uri;
import org.ubimix.commons.xml.XmlException;
import org.ubimix.commons.xml.XmlWrapper;
import org.ubimix.commons.xml.atom.AtomFeed;

/**
 * @author arkub
 */
public interface IDocumentTransformer {
    AtomFeed transformDocument(Uri url, XmlWrapper doc)
        throws XmlException,
        IOException;
}
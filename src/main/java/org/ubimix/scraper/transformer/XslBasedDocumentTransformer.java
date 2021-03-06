package org.ubimix.scraper.transformer;

import java.io.IOException;

import org.ubimix.commons.uri.Uri;
import org.ubimix.commons.xml.XmlException;
import org.ubimix.commons.xml.XmlWrapper;
import org.ubimix.commons.xml.atom.AtomFeed;

/**
 * Transformer used for applying a specific XSL transformation. See also
 * transformers applying automated transformations.
 * 
 * @author kotelnikov
 */
public class XslBasedDocumentTransformer implements IDocumentTransformer {

    private XmlWrapper fXsl;

    public XslBasedDocumentTransformer(XmlWrapper xsl) {
        fXsl = xsl;
    }

    public AtomFeed transformDocument(Uri url, XmlWrapper doc)
        throws XmlException,
        IOException {
        AtomFeed result = doc.applyXSL(fXsl, AtomFeed.class);
        String prefix = TransformerUtils.getNamespacePrefix(result
            .getXmlContext()
            .getNamespaceContext(), "http://www.w3.org/1999/xhtml", "xhtml");
        if (prefix != null && !"".equals(prefix)) {
            prefix += ":";
        }
        TransformerUtils.resolveLinks(result, url, prefix + "a", "href");
        TransformerUtils.resolveLinks(result, url, prefix + "img", "src");
        return result;
    }

}
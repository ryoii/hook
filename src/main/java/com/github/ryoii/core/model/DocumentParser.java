package com.github.ryoii.core.model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

abstract class DocumentParser {


    abstract Document getDocument();

    /*
     *  Document Parse
     * */
    public Links links(String selector) {
        return links(selector, false);
    }

    public Links links(String selector, boolean containSrc) {
        Elements select = getDocument().select(selector);
        List<String> links = select.eachAttr("abs:href");
        if (containSrc) {
            links.addAll(select.eachAttr("abs:src"));
        }
        return new Links(links);
    }

    public Links linksMatch(String regex, boolean containSrc) {
        Links links = new Links();
        List<String> urls = getDocument().select("a[href]").eachAttr("abs:href");
        for (String url : urls) {
            if (url.matches(regex)) {
                links.add(url);
            }
        }

        if (containSrc) {
            List<String> sources = getDocument().select("*[src]").eachAttr("abs:src");
            for (String src : sources) {
                if (src.matches(regex)) {
                    links.add(src);
                }
            }
        }
        return links;
    }

    public List<String> textList(String selector) {
        return getDocument().select(selector).eachText();
    }

    public String text(String selector, int index) {
        return getDocument().select(selector).eq(index).text();
    }

    public String text(String selector) {
        return getDocument().select(selector).first().text();
    }

    public List<String> attrs(String selector, String attrName) {
        return getDocument().select(selector).eachAttr(attrName);
    }

    public String attr(String selector, int index, String attrName) {
        return getDocument().select(selector).eq(index).attr(attrName);
    }

    public String attr(String selector, String attrName) {
        return getDocument().select(selector).first().attr(attrName);
    }

    public Elements select(String selector) {
        return getDocument().select(selector);
    }

    public Element selectFirst(String selector) {
        return getDocument().selectFirst(selector);
    }
}

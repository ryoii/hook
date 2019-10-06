package core.model;

import core.util.ContentTypeCharsetDetector;
import core.util.RegexCharsetDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.charset.Charset;
import java.util.Optional;

public class Page extends DocumentParser implements MetaGetter{

    private Task task;
    private byte[] content;
    private String contentType;
    private Charset charset = null;
    private String html = null;
    private Document document = null;

    public Page (Task task, byte[] content, String contentType) {
        this.task = task;
        this.content = content;
        this.contentType = contentType;
    }

    public Task task() {
        return task;
    }

    public byte[] content() {
        return content;
    }

    public String contentType() {
        return contentType;
    }

    public String html() {
        if (html == null) {
            html = new String(content, Optional.of(charset()).orElse(Charset.defaultCharset()));
        }
        return html;
    }

    public Document getDocument() {
        if (this.document == null) {
            document = Jsoup.parse(html(), task.getUrl());
        }
        return document;
    }

    public Charset charset() {
        if (charset == null) {
            charset = ContentTypeCharsetDetector.detect(contentType);
            if (charset == null) {
                charset = RegexCharsetDetector.detect(content);
            }
        }
        return charset;
    }

    public void charset(Charset charset) {
        this.charset = charset;
    }

    public String url() {
        return task.getUrl();
    }

    public boolean typeMatch(String type) {
        return type.equals(type());
    }

    public boolean isHtml() {
        return contentType.contains("text/html");
    }

    public boolean isPic() {
        return contentType.contains("image/");
    }

    public boolean isJson() {
        return contentType.contains("application/json");
    }

    @Override
    public String meta(String key) {
        return task.meta(key);
    }
}

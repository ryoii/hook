package core.model;

import core.util.RegexCharsetDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.charset.Charset;
import java.util.Optional;

public class Page extends DocumentParser implements MetaGetter{

    private Task task;
    private byte[] content;
    private Charset charset = null;
    private String html = null;
    private Document document = null;

    public Page (Task task, byte[] content) {
        this.task = task;
        this.content = content;
    }

    public Task task() {
        return task;
    }

    public byte[] content() {
        return content;
    }

    public void content(byte[] content) {
        this.content = content;
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
            charset = RegexCharsetDetector.detect(content);
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

    @Override
    public String meta(String key) {
        return task.meta(key);
    }
}

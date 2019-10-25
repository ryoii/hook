package com.github.ryoii.core.util;

import com.github.ryoii.core.model.Page;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharsetDetector {

    public static Charset detect(Page page) {
        Charset charset;
        charset = contentTypeDetect(page.contentType());
        if (charset != null) {
            return charset;
        }

        charset = headerDetect(page.content());
        if (charset != null) {
            return charset;
        }

        // default charset UTF-8
        return StandardCharsets.UTF_8;
    }

    private static Pattern CONTENT_TYPE_PATTERN =
            Pattern.compile("charset=\"?((?:[0-9A-Za-z]|-)*)\"?");

    private static Charset contentTypeDetect(String contentType) {
        Matcher matcher = CONTENT_TYPE_PATTERN.matcher(contentType);
        Charset charset;
        if (matcher.find()) {
            charset = Charset.forName(matcher.group(1));
        } else {
            charset = null;
        }
        return charset;
    }

    private static Pattern HEADER_PATTERN =
            Pattern.compile("<meta .*charset=\"?((?:[0-9A-Za-z]|-)*)\"?.*>");

    private static Charset headerDetect(byte[] html) {
        String htmlStr = new String(html, StandardCharsets.US_ASCII);
        Matcher matcher = HEADER_PATTERN.matcher(htmlStr);
        Charset charset;
        if (matcher.find()) {
            charset = Charset.forName(matcher.group(1));
        } else {
            charset = null;
        }
        return charset;
    }
}

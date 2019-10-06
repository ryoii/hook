package core.util;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentTypeCharsetDetector {
    private static Pattern pattern =
            Pattern.compile("charset=\"?((?:[0-9A-Za-z]|-)*)\"?");

    public static Charset detect(String contentType) {
        Matcher matcher = pattern.matcher(contentType);
        Charset charset;
        if (matcher.find()) {
            charset = Charset.forName(matcher.group(1));
        } else {
            charset = null;
        }
        return charset;
    }
}

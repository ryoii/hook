package core.util;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCharsetDetector {

    private static Pattern pattern =
            Pattern.compile("<meta .*charset=\"?((?:[0-9A-Za-z]|-)*)\"?.*>");

    public static Charset detect(byte[] html) {
        String htmlStr = new String(html, Charset.forName("ascii"));
        Matcher matcher = pattern.matcher(htmlStr);
        Charset charset;
        if (matcher.find()) {
            charset = Charset.forName(matcher.group(1));
    } else {
        charset = null;
    }
        return charset;
    }
}

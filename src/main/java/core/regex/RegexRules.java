package core.regex;

import java.util.ArrayList;
import java.util.List;

public class RegexRules {

    private List<RuleEntry> whiteName = new ArrayList<>();
    private List<RuleEntry> blackName = new ArrayList<>();

    public RegexRules() {
    }

    public void addRule(String rule) {
        addRule(rule, null);
    }

    public void addRule(String rule, String type) {
        if (rule.startsWith("-")) {
            blackName.add(new RuleEntry(rule, null));
        } else {
            whiteName.add(new RuleEntry(rule, type));
        }
    }

    public String checkAndGetType(String url) {
        if (whiteName.size() > 0) {
            return isWhite(url);
        } else if (blackName.size() > 0) {
            return isBlack(url);
        } else {
            return null;
        }
    }

    private String isWhite(String url) {
        for (RuleEntry entry : whiteName) {
            if (url.matches(entry.rule)) return entry.type;
        }
        return null;
    }

    private String isBlack(String url) {
        for (RuleEntry entry : blackName) {
            if (url.matches(entry.rule)) return null;
        }
        return "HOOK_DEFAULT_TYPE";
    }


    private class RuleEntry {
        RuleEntry(String rule, String type) {
            this.rule = rule;
            this.type = type;
        }

        String rule;
        String type;
    }
}

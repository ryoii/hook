package com.github.ryoii.core.crawler;

import com.github.ryoii.core.config.Configuration;
import com.github.ryoii.core.model.AddOnlyTaskList;
import com.github.ryoii.core.model.Page;
import com.github.ryoii.core.model.Task;
import com.github.ryoii.core.regex.RegexRules;
import org.jsoup.nodes.Document;

import java.util.List;

public abstract class AutoDetectCrawler extends BaseCrawler {

    public AutoDetectCrawler() {
        super();
    }

    public AutoDetectCrawler(Configuration configuration) {
        super(configuration);
    }

    private RegexRules regexRules = new RegexRules();

    @Override
    protected final void afterVisit(Page page, AddOnlyTaskList taskList) {
        if (!conf().isAutoDetect()) return;

        if (page.isHtml()) {
            Document document = page.getDocument();
            List<String> href = document.select("a[href]").eachAttr("abs:href");
            for (String url : href) {
                String type = regexRules.checkAndGetType(url);
                if (type != null) {
                    taskList.add(new Task(url).type(type));
                }
            }

            if (conf().isAutoDetectImg()) {
                List<String> src = document.select("*[src]").eachAttr("abs:src");
                for (String url : src) {
                    String type = regexRules.checkAndGetType(url);
                    if (type != null) {
                        taskList.add(new Task(url).type(type));
                    }
                }
            }
        }
    }

    public AutoDetectCrawler addRegex(String regex) {
        regexRules.addRule(regex);
        return this;
    }

    public AutoDetectCrawler addRegex(String regex, String type) {
        regexRules.addRule(regex, type);
        return this;
    }

    public AutoDetectCrawler setAutoDetectImg(boolean autoDetectImg) {
        conf().setAutoDetectImg(autoDetectImg);
        return this;
    }

    public AutoDetectCrawler setRegexRules(RegexRules regexRules) {
        this.regexRules = regexRules;
        return this;
    }
}

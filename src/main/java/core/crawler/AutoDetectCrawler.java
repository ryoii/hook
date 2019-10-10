package core.crawler;

import core.model.AddOnlyTaskList;
import core.model.Page;
import core.model.Task;
import core.regex.RegexRules;
import org.jsoup.nodes.Document;

import java.util.List;

public abstract class AutoDetectCrawler extends BaseCrawler{

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

    public void addRegex(String regex) {
        regexRules.addRule(regex);
    }

    public void addRegex(String regex, String type) {
        regexRules.addRule(regex, type);
    }

    public void setAutoDetectImg(boolean autoDetectImg) {
        conf().setAutoDetectImg(autoDetectImg);
    }
}

package Example;

import core.crawler.AutoDetectCrawler;
import core.model.AddOnlyTaskList;
import core.model.Links;
import core.model.Page;

public class GithubTrending extends AutoDetectCrawler {

    private static String baseUrl = "https://github.com/trending";

    public static void main(String[] args) {
        GithubTrending crawler = new GithubTrending();
        crawler.conf().setThreadNum(10)
                .setConnectTimeout(1000)
                .setTimeout(1000);
        crawler.addSeed(baseUrl).type("trending").setLife(5);
        crawler.start();
    }

    @Override
    public void visit(Page page, AddOnlyTaskList tasks) {
        if (page.typeMatch("trending")) {
            Links links = page.links("article.Box-row>h1>a");
            tasks.add(links.toTaskList("repo"));

        } else if (page.typeMatch("repo")) {
            String name = page.text("strong[itemprop=name] > a");
            String stars = page.text("a.social-count");

            System.out.println(Thread.currentThread().getName());
            System.out.println("repository: " + name);
            System.out.println("url: " + page.url());
            System.out.println("stars: " + stars);
            System.out.println("-----------------------------------");
        }
    }
}

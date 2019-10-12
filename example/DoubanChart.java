import com.github.ryoii.core.crawler.BaseCrawler;
import com.github.ryoii.core.model.AddOnlyTaskList;
import com.github.ryoii.core.model.Links;
import com.github.ryoii.core.model.Page;

public class DoubanChart extends BaseCrawler {

    private static String baseUrl = "https://movie.douban.com/chart";

    public static void main(String[] args) {
        DoubanChart crawler = new DoubanChart();
        crawler.addSeed(baseUrl, "chart").setLife(5);
        crawler.conf().setThreadNum(5);
        crawler.start();
    }

    @Override
    public void visit(Page page, AddOnlyTaskList tasks) {
        if (page.typeMatch("chart")) {
            Links links = page.links("tr.item > td > a.nbg");
            tasks.add(links.toTaskList("movie"));

        } else if (page.typeMatch("movie")) {
            String name = page.text("div[id=content]>h1>span[property=v:itemreviewed]");
            String rate = page.text("strong.rating_num");
            System.out.println(name + ": " + rate);
        }
    }
}

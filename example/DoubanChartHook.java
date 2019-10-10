import core.crawler.Hook;
import core.model.Links;

public class DoubanChartHook {

    public static void main(String[] args) {
        Hook.newHook().restTime(5).retryTime(5)
                .addSeed("https://movie.douban.com/chart", "chart")
                .select("chart", "tr.item > td > a.nbg").type("movie")
                .visitHandler(((page, tasks) -> {
                    if (page.typeMatch("movie")) {
                        String name = page.text("div[id=content]>h1>span[property=v:itemreviewed]");
                        String rate = page.text("strong.rating_num");
                        System.out.println(name + ": " + rate);
                    }
                })).start(5);
    }
}

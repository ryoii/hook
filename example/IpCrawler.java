import com.github.ryoii.core.crawler.Hook;

public class IpCrawler {

    public static void main(String[] args) {
        Hook.newHook().retryTime(5).restTime(1000)
                .addSeed("https://www.ipip.net/ip.html", true)
                .addSeed("https://www.ipip.net/ip.html", true)
                .addSeed("https://www.ipip.net/ip.html", true)
                .addSeed("https://www.ipip.net/ip.html", true)
                .addSeed("https://www.ipip.net/ip.html", true)
                .visitHandler(((page, taskList) ->
                        System.out.println(page.text("div.tableNormal table a"))
                )).start(5);
    }
}

import core.crawler.BaseCrawler;
import core.model.AddOnlyTaskList;
import core.model.Page;
import core.proxy.Proxies;

public class ProxyTest extends BaseCrawler {

    private static String baseUrl = "https://www.ipip.net/ip.html";

    public static void main(String[] args) {
        ProxyTest crawler = new ProxyTest();
        Proxies proxies = crawler.conf().getProxies();
        proxies.add("140.143.48.49", "1080");
        proxies.add("125.123.125.15", "9000");
        crawler.conf().setTimeout(2000).setConnectTimeout(2000);
        crawler.conf().setThreadNum(5).setRetryTime(10);
        for (int i = 0; i < 10; i++) {
            crawler.addSeed(baseUrl, true);
        }
        crawler.start();
    }

    @Override
    public void visit(Page page, AddOnlyTaskList tasks) {
        String text = page.text("div.tableNormal table a");
        System.out.println(text);
    }
}

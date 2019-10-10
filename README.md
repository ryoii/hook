# hook
A **declarative** Java crawler framework that can perform data crawling with only a few lines of code. At the same time, it has high scalability and can be customized to fully customized crawler programs.

**English | [中文文档](https://github.com/darkcloth/hook/blob/master/README_zh.md)**

## Advantages

1. Declarative programming, smooth development experience, less code
2. Support regular expression matching, high applicability
3. Support meta data annotation to simplify crawler logic
4. Modular configuration to customize fully customized modules
5. Automatically detect hyperlinks and image links to reduce unnecessary code
6. Based on Java 11, promoting the new version, everyone is responsible

## Download

### With Maven

> Haven't uploaded to Maven yet

### Without Maven

> Download the jar package and have your fun based on the examples

## Quick start

### Experience declarative crawlers

```java
class Example {
    public static void main(String[] args) {
        Hook.newHook().retryTime(5).restTime(1000)
                .addSeed("https://www.ipip.net/ip.html")
                .visitHandler(((page, taskList) -> 
                    System.out.println(page.text("div.tableNormal table a"))
                )).start(5);
    }
}

// Duplicate URLs will be filtered
class Example {
    public static void main(String[] args) {
        Hook.newHook().retryTime(5).restTime(1000)
                .addSeed("https://www.ipip.net/ip.html")
                .addSeed("https://www.ipip.net/ip.html")
                .addSeed("https://www.ipip.net/ip.html")
                .addSeed("https://www.ipip.net/ip.html")
                .addSeed("https://www.ipip.net/ip.html")
                .visitHandler(((page, taskList) -> 
                    System.out.println(page.text("div.tableNormal table a"))
                )).start(5);
    }
}

// You can force URLs to be unfiltered, which is useful for crawling APIs.
class Example {
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
```

### Experience automatic detection of URLs

Automatically detect all the repositories under the github account by URL regular expression matching

```java
class Example {
    public static void main(String[] args) {
        Hook.newHook().retryTime(5).restTime(1000)
                .addSeed("https://github.com/ryoii?tab=repositories", true)
                .autoDetect(true)
                .regex("https?://github.com/ryoii/[^/?#]+$", "repo")
                .visitHandler(((page, taskList) -> {
                    if (page.typeMatch("repo")) {
                        System.out.println(page.url());
                    }
                })).start(5);
    }
}
```

### Advanced customized crawler

Implement the crawler by extend `BaseCrawler` or `AutoDetectCrawler` and overriding the `visit()` method.

Here's how to implement a crawler that fetching the github trend list.

```java
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

```

## More examples

[more examples](https://github.com/ryoii/hook/tree/master/examples)

## Todo list

- [x] auto detect crawler
- [x] proxy pool
- [ ] plugins
- [ ] better regex express
- [ ] parse and run java script
- [ ] distributed crawler
- [ ] more filter
- [ ] convenient persistent tools

## Thanks

[jsoup](https://github.com/jhy/jsoup): Java HTML Parser, with best of DOM, CSS, and jquery [website](https://jsoup.org/)


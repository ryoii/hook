# hook
一个**声明式**的Java爬虫框架，可以仅用寥寥数行代码完成数据爬取功能。同时具有很高的扩展性，可以定制完全客制化的爬虫程序。

**[English](https://github.com/darkcloth/hook/blob/master/README.md) | 中文文档**

## 优点

1. 声明式编程，流畅的开发体验，代码量少
2. 支持正则匹配，适用性高
3. 支持元数据标注，简化爬虫逻辑
4. 组件化配置，可定制完全客制化的模块
5. 对超链接、图片链接自动解析，减少不必要的代码
6. 基于Java 11编写，推动新版本，人人有责

## 开始

### 通过Maven

> 还没有上传Maven

### 不通过Maven

> 下载jar包并根据例程开始你的表演

## 快速开始

### 体验声明式爬虫

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

// 重复的URL会被过滤
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

// 可以强制要求URL不被过滤，这对爬取重复变化的API时十分有用
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

### 体验自动检测URL

通过URL正则匹配自动检测获取github账户下的所有仓库

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

### 高级定制爬虫

通过继承`BaseCrawler`类或者`AutoDetectCrawler`类并重写`visit()`方法来实现爬虫功能

下面展示了如何实现爬取github趋势列表的爬虫程序

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

## 更多例程

[更多例程](https://github.com/ryoii/hook/tree/master/examples)

## 展望

+ [x] 自动解析爬虫
+ [x] 代理池
+ [ ] 完全实现插件化
+ [ ] 更好的正则表达式
+ [ ] 运行JS解析动态页面
+ [ ] 分布式爬虫
+ [ ] 更多的过滤器
+ [ ] 开箱即用的持久化工具

## 感谢项目

[jsoup](https://github.com/jhy/jsoup): Java HTML解析器 [官网](https://jsoup.org/)

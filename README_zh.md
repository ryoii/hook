# hook
一个**声明式**的Java爬虫框架，可以仅用寥寥数行代码完成数据爬取功能。同时具有很高的扩展性，可以定制完全客制化的爬虫程序。

<p align="center">
<a href="#"><img alt="JDK" src="https://img.shields.io/badge/JDK-11+-yellow.svg"/></a>
<a href="https://github.com/ryoii/hook/blob/master/LICENSE"><img alt="JDK" src="https://img.shields.io/badge/LICENSE-MIT+-lightgrey.svg"/></a>
<p>

**[English](https://github.com/ryoii/hook/blob/master/README.md) | 中文文档**

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

## 更多例程

[更多例程](https://github.com/ryoii/hook/tree/master/example)

|声明式爬虫|
|:---|
|[体验1，爬取豆瓣热门](https://github.com/ryoii/hook/blob/master/example/DoubanChartHook.java)|
|[体验2，爬取当前IP](https://github.com/ryoii/hook/blob/master/example/IpCrawler.java)|

|自动爬虫|
|:---|
|[体验正则探测](https://github.com/ryoii/hook/blob/master/example/RegexExample.java)|
|[体验Page探测](https://github.com/ryoii/hook/blob/master/example/PageDetect.java)|

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

[jsoup](https://github.com/jhy/jsoup): Java HTML Parser, with best of DOM, CSS, and jquery [https://jsoup.org/](https://jsoup.org/)

[log4j2](https://github.com/apache/logging-log4j2): Mirror of Apache Logging Log4J2

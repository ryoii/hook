import core.crawler.Hook;

public class RegexExample {

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

package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloWorldController {

    @GetMapping("/")
    public String helloWorld(@RequestParam(value = "message", required = false) String message, Model model) {
        String url = "http://oblsud.vld.sudrf.ru/";

        try {
            // Загрузка страницы
            Document doc = Jsoup.connect(url).get();

            // Извлечение заголовка страницы
            String pageTitle = doc.title();
            model.addAttribute("pageTitle", pageTitle);

            // Извлечение текста ключевых слов
            Element keywordsElement = doc.selectFirst("meta[name=Keywords]");
            String keywords = keywordsElement != null ? keywordsElement.attr("content") : "";
            model.addAttribute("keywords", keywords);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "index";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<String> search(@RequestParam("date") String date) {
        String searchUrl = "https://oblsud--vld.sudrf.ru/modules.php?name=sud_delo";
        List<String> caseUrls = new ArrayList<>();

        try {
            // Выполнение POST-запроса с указанной датой
            System.out.println("Sending POST request with date: " + date);
            Connection.Response response = Jsoup.connect(searchUrl)
                    .data("H_date", date)
                    .method(Connection.Method.POST)
                    .execute();

            // Извлечение всех ссылок на дела
            Document searchResultPage = response.parse();
            Elements caseLinks = searchResultPage.select("a[href^=/modules.php?name=sud_delo_view]");

            // Добавление ссылок в список
            for (Element link : caseLinks) {
                String caseUrl = link.absUrl("href");
                caseUrls.add(caseUrl);
            }

            System.out.println("Number of case URLs found: " + caseUrls.size());
            System.out.println("Case URLs: " + caseUrls);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return caseUrls;
    }
}

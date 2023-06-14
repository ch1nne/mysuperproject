package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

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

}

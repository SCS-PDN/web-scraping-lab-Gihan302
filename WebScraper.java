import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {

    public static void main(String[] args) {
        String url = "https://www.bbc.com";

        try {
            Document doc = Jsoup.connect(url).get();
            
            System.out.println("Title: " + doc.title());

            System.out.println("\nHeadings:");
            Elements headings = doc.select("h1");
            for (Element heading : headings) {
                System.out.println(heading.tagName() + ": " + heading.text());
            }

            System.out.println("\nLinks:");
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                System.out.println(link.attr("abs:href"));
            }

            List<NewsArticle> newsArticles = new ArrayList<>();
            Elements newsElements = doc.select("div.gs-c-promo");

            for (Element element : newsElements) {
                String headline = element.select("h3.gs-c-promo-heading__title").text();
                String date = element.select("time").attr("datetime");
                String author = element.select("p.gs-c-promo-contributor").text();

                newsArticles.add(new NewsArticle(headline, date, author));
            }

            System.out.println("\nNews Articles:");
            for (NewsArticle article : newsArticles) {
                System.out.println(article);
            }

        } catch (IOException e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    static class NewsArticle {
        private String header;
        private String date;
        private String author;

        public NewsArticle(String header, String date, String author) {
            this.header = header;
            this.date = date;
            this.author = author;
        }

        @Override
        public String toString() {
            return "Headline: " + header +
                    (date.isEmpty() ? "" : "\nPublication Date: " + date) +
                    (author.isEmpty() ? "" : "\nAuthor: " + author) + "\n";
        }
    }
}

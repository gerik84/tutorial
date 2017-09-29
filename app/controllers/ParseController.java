package controllers;

import akka.actor.ActorSystem;
import com.google.common.io.Files;
import models.Goods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.Logger;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by pavel on 29.09.17.
 */
public class ParseController extends BaseController {

    @Inject
    public ParseController(ActorSystem actorSystem, ExecutionContextExecutor exec) {
        super(actorSystem, exec);
    }

    public Result parseReStore() {
        try {
            new ParseReStoreService().parse();
        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError();
        }
        return ok();
    }


    class ParseReStoreService {

        private HashMap<String, Goods> goods = new HashMap<>();
        private HashSet<String> hrefs = new HashSet<>();
        private HashSet<String> hrefsGoods = new HashSet<>();

        String baseUrl = "https://www.re-store.ru/";

        public void parse() throws IOException {

            parsePagination(baseUrl + "apple-mac");
            hrefs.forEach(page -> {
                try {
                    parseCatalog(baseUrl + page);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            hrefsGoods.forEach(item -> {
                try {
                    parseDetails(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Logger.error("asd");
        }

        private void parsePagination(String url) throws IOException {
            Document document = Jsoup.connect(url).get();
            Elements pagination = document.select(".pagination");
            pagination.get(0).children().forEach(page -> {
                Elements element = page.children();
                if (element.size() > 0) {
                    String href = element.get(0).attr("href");
                    hrefs.add(href);
                    Logger.debug("Add href => " + href);
                }
            });
            Logger.debug("asd");
        }

        private void parseCatalog(String url) throws IOException {
            Document document = Jsoup.connect(url).get();
            Elements pagination = document.select(".product-item__title");
            pagination.forEach(item -> {
                item.children().forEach(page -> {
                    Element a = page.select("a").get(0);
                    String href = a.attr("href");
                    hrefsGoods.add(href);
                    Logger.debug("Add href goods details => " + href);
                });
            });
        }

        private void parseDetails(String url) throws IOException {
            Document document = Jsoup.connect(url).get();
            Elements dom = document.select(".img-wrapper");
            dom.select("img").forEach(img -> {
                String image_path = baseUrl + img.attr("src");
                Logger.debug(image_path);

                try {
                    File f = new File(baseUrl + img.attr("src"));
                    models.File file = new models.File();
                    file.setType("image/jpeg");
                    byte[] data = Files.toByteArray(f);
                    if (data == null) {
                        Logger.error("DBStorageSystem: can't save file ");
                        return;
                    }
                    file.setData(data);
                    file.setLength(data.length);
                    file.save();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
    }
}

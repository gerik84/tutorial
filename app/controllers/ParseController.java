package controllers;

import akka.actor.ActorSystem;
import io.ebean.Ebean;
import models.File;
import models.Goods;
import models.GoodsTree;
import models.Media;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.Logger;
import play.mvc.Result;
import scala.concurrent.ExecutionContextExecutor;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

            //TODO: -------- CAP -------//
            Goods parent = new Goods();
            parent.setName("MacBook");
            parent.save();
            //-------------------------//

            try {
                Ebean.beginTransaction();
                Goods goods = new Goods();
                List<Media> images = new ArrayList<>();
                dom.select("img").forEach(img -> {
                    try {
                        String image_path = baseUrl + img.attr("src");
                        images.add(download(image_path));
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                });
                Element h1 = document.select("h1").get(0);
                String text = h1.text();
                goods.setName(text);
                String text1 = document.select(".product__descr").html();
                goods.setDescription(text1);
                if (images.size() > 0) {
                    goods.setCover(images.get(0));
                }
                if (images.size() > 1) {
                    goods.setImages(images.subList(1, images.size() - 1));
                }
                goods.save();

                GoodsTree tree = new GoodsTree();
                tree.setParent(null);
                tree.setChildren(goods);
                tree.save();

                Ebean.commitTransaction();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                Ebean.endTransaction();
            }
        }
    }

    private Media download(String url) throws IOException {
        URL url1 = new URL(url);
        BufferedImage originalImage =
                ImageIO.read(url1);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", baos);
        baos.flush();
        byte[] bytes = baos.toByteArray();
        baos.close();
        Logger.debug("Image URL: " + url);

        File file = new models.File();
        file.setType("image/jpeg");
        if (bytes == null) {
            Logger.error("DBStorageSystem: can't save file ");
            throw new IOException("Image save error");
        }
        file.setData(bytes);
        file.setLength(bytes.length);
        file.save();

        Media media = new Media();
        media.setUrl("/file/" + file.getId());
        media.setType(Media.MediaType.Image);
        media.save();
        return media;
    }
}

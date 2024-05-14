package com.micropos.products.repository;

import com.micropos.products.model.Category;
import com.micropos.products.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

//@Repository
public class JD implements ProductDB {
    private List<Product> products = null;
    private final Map<String, Category> categories = new HashMap<>();

    private static final String cookiePath = "src/main/resources/jd-cookies";

    @Override
    @Cacheable(cacheNames = "products")
    public List<Product> getProducts() {
        try {
            if (products == null)
                products = parseJD("Java");
        } catch (Exception e) {
            products = new ArrayList<>();
        }
        return products;
    }

    @Override
    @Cacheable(cacheNames = "product", key = "#productId")
    public Product getProduct(String productId) {
        for (Product p : getProducts()) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = "categories")
    public List<Category> getCategories() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Category getCategory(String categoryName) {
        return categories.get(categoryName);
    }

    private static Map<String, String> getCookies() throws IOException {
        var cookie = Files.readString(new File(cookiePath).toPath());
        var items = cookie.trim().split(";");
        var cookiesMap = new HashMap<String, String>();
        for (var item : items) {
            var kv = item.split("=");
            cookiesMap.put(kv[0], kv[1]);
        }
        return cookiesMap;
    }

    public List<Product> parseJD(String keyword) throws IOException {
        //获取请求https://search.jd.com/Search?keyword=java
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        //解析网页
        var conn = Jsoup.connect(url).cookies(getCookies()).timeout(10000);
        var document = conn.get();
        System.out.println("get url: " + url);
//        Document document = Jsoup.parse(URI.create(url).toURL(), 10000);
        //所有js的方法都能用
        Element element = document.getElementById("J_goodsList");
        if (element == null) {
            return List.of();
        }

        //获取所有li标签
        Elements elements = element.getElementsByTag("li");
        List<Product> list = new ArrayList<>();

        //获取元素的内容
        for (Element el : elements) {
            //关于图片特别多的网站，所有图片都是延迟加载的
            String id = el.attr("data-spu");
            String img = "https:".concat(el.getElementsByTag("img").eq(0).attr("data-lazy-img"));
            String price = el.getElementsByAttribute("data-price").text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            if (title.contains("，")) {
                title = title.substring(0, title.indexOf("，"));
            }

            Product product = new Product(id, title, Double.parseDouble(price), categories.get("JD"));
            list.add(product);
        }
        return list;
    }


    JD() {
        this.categories.put("drink", new Category("1711853606", "drink"));
    }
}

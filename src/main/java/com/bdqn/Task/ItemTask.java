package com.bdqn.Task;

import com.bdqn.ClientUtil.HttpClient;
import com.bdqn.ItemService.ServiceImpl;
import com.bdqn.pojo.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ItemTask {

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ServiceImpl service;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    //设置定时任务执行完成后，再间隔100秒执行一次
    @Scheduled(fixedDelay = 1000 * 100)
    public void process() throws Exception {
        System.setProperty("webdriver.chrome.driver","E:\\Driver\\chromedriver.exe");
        //初始化浏览器名为driver
        WebDriver driver = new ChromeDriver();

        //窗口最大化
        driver.manage().window().maximize();

        //使用get()方法，打开京东网址
        driver.get("https://www.jd.com/");

        
        //分析页面发现访问的地址,页码page从1开始，下一页oage加2
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&cid2=653&cid3=655&s=5760&click=0&page=";

        //遍历执行，获取所有的数据,抓取数据
        for (int i = 1; i < 10; i = i + 2) {
            //发起请求进行访问，获取页面数据,先访问第一页
            String html = this.httpClient.getHtml(url + i);
            System.out.println(html);
            //解析页面数据，保存数据到数据库中
            this.parseHtml(html);
            System.out.println(html);

        }
        System.out.println("执行完成");

    }
    //解析页面，并把数据保存到数据库中
    private void parseHtml(String html) throws Exception {
        //使用jsoup解析页面
        Document document = Jsoup.parse(html);

        //获取商品数据
        Elements spus = document.select("div#J_goodsList > ul > li");

        //遍历商品spu数据
        for (Element spuEle : spus) {
            //获取商品spu
            Long spuId = Long.parseLong(spuEle.attr("data-spu"));

            //获取商品sku数据
            Elements skus = spuEle.select("li.gl-item img");
            for (Element skuEle : skus) {
                //获取商品sku
                Long skuId = Long.parseLong(skuEle.attr("data-sku"));

                //判断商品是否被抓取过，可以根据sku判断
                Item param = new Item();
                param.setSku(skuId);
                List<Item> list = this.service.findAll(param);
                //判断是否查询到结果
                if (list.size() > 0) {
                    //如果有结果，表示商品已下载，进行下一次遍历
                    continue;
                }

                //保存商品数据，声明商品对象
                Item item = new Item();

                //商品spu
                item.setSpu(spuId);
                //商品sku
                item.setSku(skuId);
                //商品url地址
                item.setUrl("https://item.jd.com/" + skuId + ".html");
                //创建时间
                item.setCreated(new Date());
                //修改时间
                item.setUpdated(item.getCreated());


                //获取商品标题
                String itemHtml = this.httpClient.getHtml(item.getUrl());
                String title = Jsoup.parse(itemHtml).select("div.sku-name").text();
                item.setTitle(title);

                //获取商品价格
                String priceUrl = "https://p.3.cn/prices/mgets?skuIds=J_" + skuId;
                String priceJson = this.httpClient.getHtml(priceUrl);
                //解析json数据获取商品价格
                double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                item.setPrice(price);

                //获取图片地址
                String pic = "https:" + skuEle.attr("data-lazy-img").replace("/n9/", "/n1/");
                System.out.println(pic);
                //下载图片
                String picName = this.httpClient.getImage(pic);
                item.setPic(picName);

                //保存商品数据
                this.service.save(item);
            }
        }

    }
    }

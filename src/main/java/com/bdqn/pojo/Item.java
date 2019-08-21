package com.bdqn.pojo;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Item {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;  //主键id
   private long spu; //商品集合id
   private long sku; //商品最小品类单元id
   private String title;   //商品标题
   private double price;  //商品价格
   private String pic;  //商品图片
   private String url;  //商品详情地址
   private Date created;   //创建时间
   private Date updated;   //更新时间

   @Override
   public String toString() {
      return "Item{" +
              "id=" + id +
              ", spu=" + spu +
              ", sku=" + sku +
              ", title='" + title + '\'' +
              ", price=" + price +
              ", pic='" + pic + '\'' +
              ", url='" + url + '\'' +
              ", created=" + created +
              ", updated=" + updated +
              '}';
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getSpu() {
      return spu;
   }

   public void setSpu(long spu) {
      this.spu = spu;
   }

   public long getSku() {
      return sku;
   }

   public void setSku(long sku) {
      this.sku = sku;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   public String getPic() {
      return pic;
   }

   public void setPic(String pic) {
      this.pic = pic;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public Date getCreated() {
      return created;
   }

   public void setCreated(Date created) {
      this.created = created;
   }

   public Date getUpdated() {
      return updated;
   }

   public void setUpdated(Date updated) {
      this.updated = updated;
   }
}

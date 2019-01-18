package com.wolf.cache.model;

/**
 * 商品信息
 * @author Administrator
 *
 */
public class ProductInfo {

	private String id;
	private String name;
	private Double price;
	private String pictureList;
	private String specification;
	private String service;
	private String color;
	private String size;
	private Long shopId;
	
	public ProductInfo() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPictureList() {
		return pictureList;
	}

	public void setPictureList(String pictureList) {
		this.pictureList = pictureList;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Override
	public String toString() {
		return "ProductInfo [id=" + id + ", name=" + name + ", price=" + price
				+ ", pictureList=" + pictureList + ", specification="
				+ specification + ", service=" + service + ", color=" + color
				+ ", size=" + size + ", shopId=" + shopId + "]";
	}
	
}

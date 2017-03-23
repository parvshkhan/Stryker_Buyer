package com.app.mycartpackage;

import java.util.List;

public class ProductCategory {

    String CategoryID, CategoryName;
    List<Product> products;

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int GetProductyQantity(Product newProduct) {
        int quantity = 0;

        for (Product prod : products) {
            if (prod.getProdId().toString().equals(newProduct.getProdId().toString())) {
                quantity = prod.getProQuantity();
                continue;
            }
        }
        return quantity;
    }

    public void AddProduct(Product newProduct) {
        Boolean bFound = false;
        for (Product prod : products) {
            if (prod.getProdId().toString().equals(newProduct.getProdId().toString())) {
                prod.proQuantity++;
                bFound = true;
                continue;
            }
        }
        if (!bFound) {
            newProduct.proQuantity = 1;
            products.add(newProduct);
        }
    }

    public void RemoveProduct(Product oldProduct) {
        for (Product prod : products) {
            if (prod.getProdId().toString().equals(oldProduct.getProdId().toString())) {
                prod.proQuantity--;
                if (prod.proQuantity == 0)
                    products.remove(prod);
                continue;
            }
        }
    }
}

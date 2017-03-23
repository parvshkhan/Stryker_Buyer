package com.app.mycartpackage;

import java.util.List;

public class StoreItem {
    String StoreName;
    int TotalProducts;
    int TotalPrice;
    List<ProductCategory> categories;

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public int getTotalProducts() {
        return TotalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        TotalProducts = totalProducts;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }

    public List<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductCategory> categories) {
        this.categories = categories;
    }

    public int GetProductQuantity(Product prod) {
        int quantity = 0;

        ProductCategory cat = new ProductCategory();
        cat.CategoryName = prod.prodCategory;
        cat.CategoryID = prod.prodcategoryID;
        cat = this.GetProductCategory(cat);
        if (cat.getProducts().size() > 0) {
            quantity = cat.GetProductyQantity(prod);
        }
        return quantity;
    }

    public ProductCategory GetProductCategory(ProductCategory category) {
        ProductCategory cat = new ProductCategory();
        for (ProductCategory categ : categories) {
            if (categ.getCategoryID().toString().equals(category.getCategoryID().toString())) {
                cat = categ;
                continue;
            }
        }
        return cat;
    }

    public void AddProductToStore(Product prod) {
        ProductCategory cat = new ProductCategory();
        cat.CategoryName = prod.prodCategory;
        cat.CategoryID = prod.prodcategoryID;
        cat = this.AddCategory(cat);
        cat.AddProduct(prod);
    }

    public void RemoveProductFromStore(Product prod) {
        ProductCategory cat = new ProductCategory();
        cat.CategoryName = prod.prodCategory;
        cat.CategoryID = prod.prodcategoryID;
        cat.RemoveProduct(prod);
        this.RemoveCategory(cat);
    }

    public ProductCategory AddCategory(ProductCategory newCategory) {
        ProductCategory category = new ProductCategory();
        Boolean bFound = false;
        for (ProductCategory cat : categories) {
            if (cat.getCategoryID().toString().equals(newCategory.getCategoryID().toString())) {
                category = cat;
                bFound = true;
                continue;
            }
        }
        if (!bFound) {
            categories.add(newCategory);
            category = newCategory;
        }
        return category;
    }

    public void RemoveCategory(ProductCategory oldCategory) {
        for (ProductCategory cat : categories) {
            if (cat.getCategoryID().toString().equals(oldCategory.getCategoryID().toString())) {
                if (cat.products.size() == 0) {
                    categories.remove(oldCategory);
                }
            }
        }
    }

}

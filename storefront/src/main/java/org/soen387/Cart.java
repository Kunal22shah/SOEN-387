package org.soen387;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cart {

    private List<Product> products;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product p) {
        if (p != null) {

            for (Product product : products) {
                if (product.getSku().equals(p.getSku())) {
                    return;
                }
            }
            products.add(p);
        }

    }

    public void removeProductBySku(String sku) {
        products = products.stream()
                .filter(product -> !product.getSku().equals(sku))
                .collect(Collectors.toList());

    }
}

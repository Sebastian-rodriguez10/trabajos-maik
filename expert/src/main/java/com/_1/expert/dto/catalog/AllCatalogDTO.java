package com._1.expert.dto.catalog;

import lombok.Data;

@Data
public class AllCatalogDTO {
    private String nameCatalog;
    private String categoryName;
    private String productName;
    private Float price;
    private Integer productStock;
}

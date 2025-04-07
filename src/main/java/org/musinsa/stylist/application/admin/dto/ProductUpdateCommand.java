package org.musinsa.stylist.application.admin.dto;

public record ProductUpdateCommand(
    Long categoryId,
    int price
) {

}
package org.musinsa.stylist.application.admin.dto;

public record ProductRegistrationCommand(
    Long categoryId,
    int price
) {

}
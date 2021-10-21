package com.example.demo.entity;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 结算价格
 * @author jicai 2021-10-13
 */
@Data
public class TaskSettlementPriceReqDto {

    /**
     * 人数  用于 >=N 时 填充N
     * 必传
     */
    @NotNull
    @Min(1)
    @Max(1000)
    private Integer person;

    /**
     * 结算金额
     * 必传
     */
    @NotNull
    @DecimalMin("1")
    @DecimalMax("9999")
    private BigDecimal money;

}

package com.example.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ChangLF 2022-01-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestCopyOne extends User{


    private String one;
}

package com.hychen11.common.to;

import lombok.Data;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: BrandTo
 * @date ：2025/7/28 23:57
 */
@Data
public class BrandTo {
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名
     */
    private String name;
    /**
     * 品牌logo地址
     */
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    private Integer showStatus;
    /**
     * 检索首字母
     */
    private String firstLetter;
    /**
     * 排序
     */

    private Integer sort;
}

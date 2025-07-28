package com.hychen11.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ：hychen11
 * @Description:
 * @ClassName: SpuItemAttrGroupVo
 * @date ：2025/7/29 00:24
 */
@Data
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<Attr> attrs;
}

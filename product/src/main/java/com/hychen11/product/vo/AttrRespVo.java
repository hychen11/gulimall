package com.hychen11.product.vo;

import lombok.Data;

@Data
public class AttrRespVo extends AttrVo{
    /**
     * 所属于分类名字
     */
    private String catelogName;
    /**
     * 所属属性分组名称
     */
    private String groupName;
    private Long[] catelogPath;
}

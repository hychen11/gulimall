package com.hychen11.search.service;

import com.hychen11.search.vo.SearchResult;
import com.hychen11.search.vo.SearchParam;


/**
 * @author ：hychen11
 * @Description: 
 * @ClassName: MallSearchService
 * @date ：2025/7/29 00:52
 */
public interface MallSearchService {
    SearchResult search(SearchParam param);
}

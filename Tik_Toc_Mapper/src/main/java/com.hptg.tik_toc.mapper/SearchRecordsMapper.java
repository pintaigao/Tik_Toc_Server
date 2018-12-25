package com.hptg.tik_toc.mapper;

import com.hptg.tik_toc.pojo.SearchRecords;
import com.hptg.tik_toc.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    public List<String> getHotwords();
}
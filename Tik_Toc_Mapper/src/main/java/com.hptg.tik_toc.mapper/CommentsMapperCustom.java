package com.hptg.tik_toc.mapper;

import java.util.List;

import com.hptg.tik_toc.pojo.Comments;
import com.hptg.tik_toc.pojo.vo.CommentsVO;
import com.hptg.tik_toc.utils.MyMapper;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}
package com.nowcoder.community.dao.elasticsearch;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Oliver
 * @create 2022-12-31 19:35
 */
@Repository
//继承ElasticsearchRepository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {

}

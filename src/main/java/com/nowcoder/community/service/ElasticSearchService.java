package com.nowcoder.community.service;

import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Oliver
 * @create 2023-01-02 11:12
 */
@Service
public class ElasticSearchService {
    @Autowired
    private DiscussPostRepository discussRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    //将贴子保存至elasticsearch服务器
    public void saveDiscussPost(DiscussPost discussPost){
        discussRepository.save(discussPost);
    }
    //从elasticsearch服务器中删除帖子
    public void deleteDiscussPost(int id){
        discussRepository.deleteById(id);
    }


    /**
     * 从elasticsearch服务器中搜索帖子
     * @param Keyword 关键词
     * @param current 当前页
     * @param limit 每页显示条数
     * @return
     */
    public Page<DiscussPost> searchDiscussPost(String Keyword,int current,int limit){
        Pageable pageable = PageRequest.of(current, limit);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                //多个字段筛选
                .withQuery(QueryBuilders.multiMatchQuery(Keyword,"title","content"))
                //设置排序规则
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                //分页规则
                .withPageable(pageable)
                //设置高亮
                .withHighlightFields(
                        //高亮字段
                        new HighlightBuilder.Field("title")
                                //前标签
                                .preTags("<em>")
                                //后标签
                                .postTags("</em>"),
                        new HighlightBuilder.Field("content")
                                .preTags("<em>")
                                .postTags("</em>")
                ).build();
        /*----------------------------------------------------------------------------------------------*/
        return  elasticsearchTemplate.queryForPage(searchQuery, DiscussPost.class,
                new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        //命中目标数据
                        SearchHits hits = searchResponse.getHits();
                        if(hits.getTotalHits() <= 0){
                            return null;
                        }
                        //处理命中数据
                        List<DiscussPost> list = new ArrayList<>();
                        for (SearchHit hit : hits){
                            //重新封装SearchHit中的数据
                            DiscussPost post = new DiscussPost();

                            String id = hit.getSourceAsMap().get("id").toString();
                            post.setId(Integer.parseInt(id));

                            String userId = hit.getSourceAsMap().get("userId").toString();
                            post.setUserId(Integer.parseInt(userId));

                            String title = hit.getSourceAsMap().get("title").toString();
                            post.setTitle(title);

                            String content = hit.getSourceAsMap().get("content").toString();
                            post.setContent(content);

                            String type = hit.getSourceAsMap().get("type").toString();
                            post.setType(Integer.parseInt(type));

                            String status = hit.getSourceAsMap().get("status").toString();
                            post.setStatus(Integer.parseInt(status));

                            String createTime = hit.getSourceAsMap().get("createTime").toString();
                            post.setCreateTime(new Date(Long.valueOf(createTime)));

                            String commentCount = hit.getSourceAsMap().get("commentCount").toString();
                            post.setCommentCount(Integer.parseInt(commentCount));

                            String score = hit.getSourceAsMap().get("score").toString();
                            post.setScore(Double.parseDouble(score));

                            //处理高亮显示结果
                            HighlightField titleField = hit.getHighlightFields().get("title");
                            if(titleField!=null){
                                post.setTitle(titleField.getFragments()[0].toString());
                            }

                            HighlightField contentField = hit.getHighlightFields().get("content");
                            if(contentField!=null){
                                post.setContent(contentField.getFragments()[0].toString());
                            }

                            list.add(post);
                        }
                        //返回实现类对象
                        return new AggregatedPageImpl(list,pageable, hits.getTotalHits(),
                                searchResponse.getAggregations(),searchResponse.getScrollId(),hits.getMaxScore());
                    }
                });
    }
}

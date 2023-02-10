package com.nowcoder.community;

import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;;import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Oliver
 * @create 2022-12-31 19:29
 */
@SpringBootTest
public class ElasticSearchTests {
    @Autowired
    private  DiscussPostRepository discussPostRepository;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Test
    @DisplayName("插入数据")
    public void testInsert(){
        discussPostRepository.save(discussPostMapper.findDiscussById(231));
    }
    @Test
    @DisplayName("插入多条数据")
    public void testInsertList(){
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(101,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(102,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(103,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(111,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(112,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(131,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(132,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(133,0,100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPost(134,0,100,0));
    }
    @Test
    @DisplayName("修改数据")
    public void testUpdate(){
        DiscussPost post = discussPostMapper.findDiscussById(231);
        post.setContent("我是新人，使劲灌水！");
        discussPostRepository.save(post);
    }

    @Test
    @DisplayName("删除数据")
    public void testDelete(){
        discussPostRepository.deleteById(231); //删除单条数据
//        discussPostRepository.deleteAll(); //删除所有数据
    }
    @Test
    @DisplayName("搜索ElasticsearchRepository")
    public void testSearchByRepository(){
        //请求分页数据规则
        Pageable pageable = PageRequest.of(0, 10);
        //设置搜索规则
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                //多个字段筛选
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬","tile","content"))
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
//        elasticsearchTemplate.queryForPage(searchQuery,class, SearchResultMapper);
//        底层获取到了高亮显示的值但是没有返回

        //展示分页数据
        Page<DiscussPost> page = discussPostRepository.search(searchQuery);
        //总条数
        System.out.println(page.getTotalElements());
        //总页数
        System.out.println(page.getTotalPages());
        //当前页数
        System.out.println(page.getNumber());
        //每页最多显示多少数据
        System.out.println(page.getSize());
        for(DiscussPost post : page){
            System.out.println(post);
        }
    }
    @Test
    @DisplayName("搜索ElasticsearchTemplate")
    public void testSearchByTemplate(){
        Pageable pageable = PageRequest.of(0, 10);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                //多个字段筛选
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬","tile","content"))
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
        Page<DiscussPost> page = elasticsearchTemplate.queryForPage(searchQuery, DiscussPost.class,
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

        /*----------------------------------------------------------------------------------------------*/
        //总条数
        System.out.println(page.getTotalElements());
        //总页数
        System.out.println(page.getTotalPages());
        //当前页数
        System.out.println(page.getNumber());
        //每页最多显示多少数据
        System.out.println(page.getSize());
        for(DiscussPost post : page){
            System.out.println(post);
        }
    }
}


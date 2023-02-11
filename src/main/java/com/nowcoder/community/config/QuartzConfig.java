package com.nowcoder.community.config;

import com.nowcoder.community.quartz.AlphaJob;
import com.nowcoder.community.quartz.PostScoreRefreshJob;
import com.nowcoder.community.quartz.WkDeleteJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @author Oliver
 * @create 2023-02-08 11:58
 */
//配置-->数据库-->调用
@Configuration
public class QuartzConfig {
    //FactoryBean可简化Bean的示例化过程；
    //1.通过FactoryBean封装Bean的实例化过程
    //2.将FactoryBean装配到spring容器中
    //3.将FactoryBean注入给其他的Bean
    //4.该Bean得到的是FactoryBean所管理的对象实例

    //配置JobDetail
   //@Bean
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();//实例化bean
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }
    //配置Trigger(SimpleTriggerFactoryBean,CronTriggerFactoryBean)
    //@Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
    //配置JobDetail
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();//实例化bean
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }
    //配置Trigger(SimpleTriggerFactoryBean,CronTriggerFactoryBean)
    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000*60*5);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
    //配置WkDeleteJob
    @Bean
    public JobDetailFactoryBean wkDeleteJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();//实例化bean
        factoryBean.setJobClass(WkDeleteJob.class);
        factoryBean.setName("WkDeleteJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }
    //配置Trigger(SimpleTriggerFactoryBean,CronTriggerFactoryBean)
    @Bean
    public SimpleTriggerFactoryBean wkDeleteJobTrigger(JobDetail wkDeleteJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(wkDeleteJobDetail);
        factoryBean.setName("wkDeleteJobTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000*60*4);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
}

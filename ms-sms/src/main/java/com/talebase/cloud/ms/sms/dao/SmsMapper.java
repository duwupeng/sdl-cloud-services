package com.talebase.cloud.ms.sms.dao;

import java.util.List;

import com.talebase.cloud.base.ms.sms.TSmsInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SmsMapper {

    @Select("select * from t_sms_send where id = #{smsId} limit 1")
    TSmsInfo getById(Integer smsId);
    
    @Select("select * from t_sms_send where guid = #{smsGuid} limit 1")
    TSmsInfo getByGuid(String smsGuid);
    
    @Select("select * from t_sms_send where send_Id = #{sendId} limit 1")
    TSmsInfo getBySendId(String sendId);
 
 
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_sms_send"
    		+ "(guid, sendto, content, send_time, status, send_count, received_time, channel_name, has_feedback,send_id) "
    		+ "values"
    		+ "(#{guid}, #{sendto}, #{content}, #{sendTime}, #{status}, #{sendCount}, #{receivedTime}, #{channelName}, #{hasFeedback},#{sendId})")
    Integer insert(TSmsInfo smsInfo);

    @Select("select * from t_sms_send ")
    List<TSmsInfo> findAll();

    @Select("select * from t_sms_send where status=0 or status=2")
    List<TSmsInfo> findAllUnsend();
 
    @Delete("delete from t_sms_send where id = #{smsId}")
    Integer delete(Integer smsId);
    
    @Update("update t_sms_send set status=#{status},send_count=#{sendCount},send_time=#{sendTime},send_id=#{sendId},received_time=#{receivedTime} where guid=#{guid}")
    Integer updateStatus(TSmsInfo smsInfo);


}

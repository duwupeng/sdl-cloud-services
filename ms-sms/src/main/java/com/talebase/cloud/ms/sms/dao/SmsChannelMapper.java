package com.talebase.cloud.ms.sms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.talebase.cloud.ms.sms.model.SmsChannel; 

@Mapper
public interface SmsChannelMapper {

	@Select("select * from t_sms_channel c left join t_sms_gateway g on c.id=g.channel_id "
			+ "where server_name=#{name}")
	List<SmsChannel> findByGatewayName(String name);
}

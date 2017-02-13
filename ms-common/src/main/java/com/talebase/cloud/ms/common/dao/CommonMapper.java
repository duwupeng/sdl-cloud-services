package com.talebase.cloud.ms.common.dao;


import com.talebase.cloud.base.ms.admin.dto.DCompany;
import com.talebase.cloud.base.ms.common.domain.TCode;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by rong on 2016/11/27.
 */
@Mapper
public interface CommonMapper {

    @Select("select * from  t_code where type=#{type} and status =1")
    List<TCode> getCodeByType(@Param("type") String type);
}

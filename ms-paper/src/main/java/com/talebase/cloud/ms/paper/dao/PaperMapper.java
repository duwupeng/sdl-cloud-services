package com.talebase.cloud.ms.paper.dao;


import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.paper.domain.TPage;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.dto.DPage;
import com.talebase.cloud.base.ms.paper.dto.DPaper;
import com.talebase.cloud.base.ms.paper.dto.DPaperQuery;
import com.talebase.cloud.common.protocal.PageRequest;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-08.
 */

@Mapper
public interface PaperMapper {

    @InsertProvider(type = PaperSqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(TPaper tPaper);

    @SelectProvider(type = PaperSqlProvider.class, method = "queryList")
    List<TPaper> queryList(@Param("companyId") Integer companyId,@Param("creator") String creator, @Param("dPaperQuery") DPaperQuery dPaperQuery, @Param("pageReq") PageRequest pageReq);

    @SelectProvider(type = PaperSqlProvider.class, method = "queryCount")
    Integer queryCount(@Param("companyId") Integer companyId,@Param("creator") String creator, @Param("dPaperQuery") DPaperQuery dPaperQuery);

    @SelectProvider(type = PaperSqlProvider.class, method = "queryListByIds")
    List<TPaper> queryListByIds(@Param("companyId") Integer companyId,@Param("creator") String creator,@Param("ids") List<Integer> ids, @Param("pageReq") PageRequest pageReq);

    @SelectProvider(type = PaperSqlProvider.class, method = "queryCountByIds")
    Integer queryCountByIds(@Param("companyId") Integer companyId,@Param("creator") String creator, @Param("ids") List<Integer> ids);

    @Select("select * from t_paper where id = #{id}")
    TPaper queryById(Integer id);

    @Select("select * from t_paper where version = (select max(version)version from t_paper where unicode = #{unicode})  and unicode = #{unicode}")
    TPaper queryLatestByUnicode(@Param("unicode") String unicode);

    @SelectProvider(type = PaperSqlProvider.class, method = "queryPaperByUnicode")
    List<TPaper> queryPaperByUnicodes(@Param("unicodes") List<String> unicodes);

    @Select("select max(version)version from t_paper where unicode = #{unicode}")
    BigDecimal queryByUnicode(@Param("unicode") String unicode);

    @UpdateProvider(type = PaperSqlProvider.class, method = "update")
    Integer update(TPaper tPaper);

    @UpdateProvider(type = PaperSqlProvider.class, method = "updateStatus")
    Integer updateStatus(@Param("id") Integer id,  @Param("status") boolean status);

    @UpdateProvider(type = PaperSqlProvider.class, method = "updateStatusByUnicode")
    Integer updateStatusByUnicode(@Param("unicode") String unicode, @Param("status") boolean status);

    @UpdateProvider(type = PaperSqlProvider.class, method = "updateMode")
    Integer updateMode(@Param("id") int id, @Param("mode") int mode, @Param("status") boolean status);

    @UpdateProvider(type = PaperSqlProvider.class, method = "updateAddTimes")
    Integer updateAddTimes(@Param("ids") List<Integer> ids);

    @UpdateProvider(type = PaperSqlProvider.class, method = "updateMinusTimes")
    Integer updateMinusTimes(@Param("ids") List<Integer> ids);

    @UpdateProvider(type = PaperSqlProvider.class, method = "deleteById")
    Integer deleteById(@Param("id") Integer id);

    @UpdateProvider(type = PaperSqlProvider.class, method = "deleteByUnicode")
    Integer deleteByUnicode(@Param("unicode")String unicode);

    @SelectProvider(type = PaperSqlProvider.class, method = "getByIds")
    List<TPaper> getByIds(@Param("ids") List<Integer> ids);

    @SelectProvider(type = PaperSqlProvider.class, method = "checkName")
    Integer checkName(@Param("paperUnicode") String paperUnicode, @Param("name") String name, @Param("companyId") Integer companyId);

    @Select("select composer from t_paper where id = #{id}")
    String queryPaperComposers(@Param("id") Integer id);
}

package ${mapperInterface.packageName};

import ${baseInfo.basePackage}.qo.${tableClass.shortClassName}QO;
import ${tableClass.fullClassName};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.foresee.icap.framework.api.page.IPage;

/**
* @author ${author!}
* @description 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的数据库操作Mapper
* @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
* @Entity ${tableClass.fullClassName}
*/
public interface ${mapperInterface.fileName} extends BaseMapper<${tableClass.shortClassName}> {
    IPage<${tableClass.shortClassName}> listPage(${tableClass.shortClassName}QO qo);
}




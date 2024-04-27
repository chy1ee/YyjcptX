package ${baseInfo.packageName};


import com.baomidou.mybatisplus.extension.service.IService;

import com.foresee.icap.framework.api.page.IPage;

import ${tableClass.fullClassName};
import ${baseInfo.basePackage}.qo.${tableClass.shortClassName}QO;
<#if baseService??&&baseService!="">
import ${baseService};
    <#list baseService?split(".") as simpleName>
        <#if !simpleName_has_next>
            <#assign serviceSimpleName>${simpleName}</#assign>
        </#if>
    </#list>
</#if>

/**
* @author ${author!}
* @description 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的数据库操作Service
* @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
*/
public interface ${baseInfo.fileName} extends IService<${tableClass.shortClassName}> {
    IPage<${tableClass.shortClassName}> page${tableClass.shortClassName}(${tableClass.shortClassName}QO qo);
    ${tableClass.shortClassName} save${tableClass.shortClassName}(${tableClass.shortClassName} model);
}

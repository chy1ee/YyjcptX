package ${baseInfo.packageName};

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.foresee.icap.framework.api.page.IPage;

import ${tableClass.fullClassName};
import ${serviceInterface.packageName}.${serviceInterface.fileName};
import ${mapperInterface.packageName}.${mapperInterface.fileName};
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
* @description 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的数据库操作Service实现
* @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
*/
@Service
public class ${baseInfo.fileName} extends ServiceImpl<${mapperInterface.fileName}, ${tableClass.shortClassName}>
        implements ${serviceInterface.fileName} {
    @Override
    public IPage<${tableClass.shortClassName}> page${tableClass.shortClassName}(${tableClass.shortClassName}QO qo) {
        return getBaseMapper().listPage(qo);
    }

    @Override
    public ${tableClass.shortClassName} save${tableClass.shortClassName}(${tableClass.shortClassName} model) {
        boolean result = super.saveOrUpdate(model);
        if (result)
           return model;
        else
            throw new RuntimeException("保存失败");
    }
}





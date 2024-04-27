package ${baseInfo.packageName};

import com.foresee.icap.framework.api.page.BasePageQueryParam;
<#list tableClass.importList as fieldType>import ${fieldType};${"\n"}</#list>
import lombok.Data;

/**
* @author ${author}
* @description ${tableClass.shortClassName}的前端传参
* @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
*/
@Data
public class ${queryObject.fileName} extends BasePageQueryParam{
<#list tableClass.parameterFields as field>
    private ${field.shortTypeName} ${field.fieldName};
</#list>
}

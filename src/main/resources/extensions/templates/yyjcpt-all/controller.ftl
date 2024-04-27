package ${baseInfo.packageName};

import ${tableClass.fullClassName};
import ${baseInfo.basePackage}.qo.${tableClass.shortClassName}QO;
import ${baseInfo.basePackage}.service.${tableClass.shortClassName}Service;
import com.foresee.icap.framework.entity.api.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author ${author!}
* @description 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的增删改查
* @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
*/
@RestController
@RequestMapping("/back/${tableClass.shortClassName2}")
@RequiredArgsConstructor
public class ${baseInfo.fileName} {
    private final ${tableClass.shortClassName}Service ${tableClass.shortClassName2}Service;

    /**
    * 列表
    */
    @PostMapping("page")
    public Response<?> list(@RequestBody ${tableClass.shortClassName}QO qo){
        return Response.successData(${tableClass.shortClassName2}Service.page${tableClass.shortClassName}(qo));
    }

    /**
    * 新增 / 修改
    */
    @PostMapping
    public Response<?> save(@RequestBody @Validated ${tableClass.shortClassName} model){
        return Response.successData(
            ${tableClass.shortClassName2}Service.save${tableClass.shortClassName}(model));
    }
}

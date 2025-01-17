package com.baomidou.plugin.idea.mybatisx.generate.template;

import com.baomidou.plugin.idea.mybatisx.generate.dto.FieldInfo;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import com.baomidou.plugin.idea.mybatisx.yyjcpt.constant.ConstantUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 实体类的信息
 */
public class ClassInfo {
    /**
     * 类的全称(包括包名)
     */
    private String fullClassName;
    /**
     * 类的简称
     */
    private String shortClassName;

    public String getTableName() {
        return tableName;
    }

    /**
     * 表名
     */
    private String tableName;
    /**
     * 表的注释
     */
    private String remark;
    /**
     * 主键字段列表
     */
    private List<FieldInfo> pkFields;
    /**
     * 全部字段
     */
    private List<FieldInfo> allFields;
    /**
     * 除了主键的所有字段
     */
    private List<FieldInfo> baseFields;
    /**
     * 所有的blob字段
     */
    private List<FieldInfo> baseBlobFields;
    /**
     * 需要导入的实体类的所有字段类型
     */
    private List<String> importList;
    /**
     * 查询参数字段类型
     */
    private List<FieldInfo> parameterFields;

    private String shortClassName2;

    public static ClassInfo build(IntrospectedTable introspectedTable) {
        ClassInfo classInfo = new ClassInfo();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        classInfo.fullClassName = introspectedTable.getBaseRecordType();
        classInfo.shortClassName = type.getShortName();
        classInfo.tableName = introspectedTable.getFullyQualifiedTable().getIntrospectedTableName();
        classInfo.remark = introspectedTable.getRemarks() == null ? "" : introspectedTable.getRemarks();
        classInfo.shortClassName2 = StringUtils.lowerCaseFirstChar(classInfo.shortClassName);

        classInfo.pkFields = introspectedTable.getPrimaryKeyColumns()
                .stream()
                .map(FieldInfo::build)
                .collect(Collectors.toList());

        classInfo.allFields = Stream.of(introspectedTable.getPrimaryKeyColumns(),
                introspectedTable.getBaseColumns(),
                introspectedTable.getBLOBColumns())
                .flatMap(Collection::stream)
                .map(FieldInfo::build)
                .collect(Collectors.toList());

        classInfo.baseFields = introspectedTable.getBaseColumns().stream()
                .map(FieldInfo::build)
                .collect(Collectors.toList());

        classInfo.baseBlobFields = Stream.of(introspectedTable.getBaseColumns(),
            introspectedTable.getBLOBColumns())
            .flatMap(Collection::stream)
            .map(FieldInfo::build)
            .collect(Collectors.toList());

        // 拿到所有需要import的类型, 不是java.lang包开头的,并且不是数组类型 去重的所有类型
        classInfo.importList = classInfo.allFields.stream()
            .filter(fieldInfo -> !fieldInfo.isColumnIsArray())
            .map(FieldInfo::getFullTypeName)
            .filter(typeName->!typeName.startsWith("java.lang"))
            .distinct()
            .collect(Collectors.toList());

        List<IntrospectedColumn> parameterList = (List<IntrospectedColumn>)introspectedTable.getAttribute(ConstantUtils.YYJCPT_PARAMETER_INFOS);
        classInfo.parameterFields = parameterList == null
            ? new ArrayList<>()
            :  parameterList.stream().map(FieldInfo::build).collect(Collectors.toList());

        return classInfo;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getShortClassName() {
        return shortClassName;
    }

    public List<FieldInfo> getPkFields() {
        return pkFields;
    }

    public List<FieldInfo> getAllFields() {
        return allFields;
    }

    public List<FieldInfo> getBaseFields() {
        return baseFields;
    }

    public List<FieldInfo> getBaseBlobFields() {
        return baseBlobFields;
    }

    public String getRemark() {
        return remark;
    }

    public List<String> getImportList() {
        return importList;
    }

    public List<FieldInfo> getParameterFields() {
        return parameterFields;
    }

    public String getShortClassName2() {
        return shortClassName2;
    }
}

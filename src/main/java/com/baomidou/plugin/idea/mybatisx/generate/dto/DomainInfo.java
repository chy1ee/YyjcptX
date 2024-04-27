package com.baomidou.plugin.idea.mybatisx.generate.dto;

import java.io.Serializable;

public class DomainInfo implements Serializable {
    private String encoding;
    private String basePackage;
    private String relativePackage;
    private String fileName;
    private String tableName;
    private String basePath;
    private String modulePath;
    private String vueSrcPath;

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRelativePackage() {
        return relativePackage;
    }

    public void setRelativePackage(String relativePackage) {
        this.relativePackage = relativePackage;
    }

    public String getVueSrcPath() {
        return vueSrcPath;
    }

    public void setVueSrcPath(String vueSrcPath) {
        this.vueSrcPath = vueSrcPath;
    }

    public DomainInfo copyFromFileName(String extraDomainName) {
        DomainInfo domainInfo = new DomainInfo();
        domainInfo.setModulePath(modulePath);
        domainInfo.setBasePath(basePath);
        domainInfo.setEncoding(encoding);
        domainInfo.setBasePackage(basePackage);
        domainInfo.setFileName(extraDomainName);
        domainInfo.setRelativePackage(relativePackage);
        domainInfo.setVueSrcPath(vueSrcPath);
        domainInfo.setTableName(tableName);
        return domainInfo;
    }
}


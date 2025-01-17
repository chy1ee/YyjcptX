package com.baomidou.plugin.idea.mybatisx.generate.action;

import com.baomidou.plugin.idea.mybatisx.generate.dto.DefaultGenerateConfig;
import com.baomidou.plugin.idea.mybatisx.generate.dto.DomainInfo;
import com.baomidou.plugin.idea.mybatisx.generate.dto.GenerateConfig;
import com.baomidou.plugin.idea.mybatisx.generate.dto.TemplateContext;
import com.baomidou.plugin.idea.mybatisx.generate.dto.TemplateSettingDTO;
import com.baomidou.plugin.idea.mybatisx.generate.setting.TemplatesSettings;
import com.baomidou.plugin.idea.mybatisx.generate.ui.CodeGenerateUI;
import com.baomidou.plugin.idea.mybatisx.generate.ui.TablePreviewUI;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import com.baomidou.plugin.idea.mybatisx.yyjcpt.GenerateDialogCallback;
import com.baomidou.plugin.idea.mybatisx.yyjcpt.ui.ColumnChooseUI;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassGenerateDialogWrapper extends DialogWrapper {

    private static final Logger logger = LoggerFactory.getLogger(ClassGenerateDialogWrapper.class);

    private CodeGenerateUI codeGenerateUI;

    private TablePreviewUI tablePreviewUI = new TablePreviewUI();

    private ColumnChooseUI columnChooseUI = new ColumnChooseUI();

    private JPanel rootPanel = new JPanel();

    private java.util.List<JPanel> containerPanelList;

    private Action previousAction;

    private int page = 0;
    private int lastPage = 2;
    private Project project;
    private List<DbTable> tableElements;

    private GenerateDialogCallback dialogCallback;

    protected ClassGenerateDialogWrapper(@Nullable Project project) {
        super(project);
        this.setTitle("Generate Options");
        setOKButtonText("Next");
        setCancelButtonText("Cancel");

        dialogCallback = new GenerateDialogCallback(getOKAction());
        codeGenerateUI = new CodeGenerateUI(dialogCallback);

        previousAction = new DialogWrapperAction("Previous") {
            @Override
            protected void doAction(ActionEvent e) {
                page = page - 1;
                switchPage(page);
                if (page == 1)
                    setOKButtonText("Next");
                else if (page == 0)
                    previousAction.setEnabled(false);
            }
        };
        // 默认禁用 上一个设置
        previousAction.setEnabled(false);
        // 初始化容器列表
        java.util.List<JPanel> list = new ArrayList<>();
        list.add(tablePreviewUI.getRootPanel());
        list.add(codeGenerateUI.getRootPanel());
        list.add(columnChooseUI.getRootPanel());
        containerPanelList = list;
        // 默认切换到第一页
        switchPage(0);

        super.init();
    }

    @Override
    protected void doOKAction() {
        if (page == lastPage) {
            super.doOKAction();
            return;
        }

        if (page == 0) {
            // 替换第二个panel的占位符
            DomainInfo domainInfo = tablePreviewUI.buildDomainInfo();
            if (StringUtils.isEmpty(domainInfo.getModulePath())) {
                Messages.showMessageDialog("Please select module to generate files", "Generate File", Messages.getWarningIcon());
                return;
            }
            previousAction.setEnabled(true);

            TemplatesSettings templatesSettings = TemplatesSettings.getInstance(project);
            final TemplateContext templateContext = templatesSettings.getTemplateConfigs();
            final Map<String, List<TemplateSettingDTO>> settingMap = templatesSettings.getTemplateSettingMap();
            if (settingMap.isEmpty()) {
                throw new RuntimeException("无法获取模板");
            }

            codeGenerateUI.fillData(project,
                generateConfig,
                domainInfo,
                templateContext.getTemplateName(),
                settingMap);
        }
        else if (page == 1) {
            if (dialogCallback.isLastStep())
                super.doOKAction();
            else {
                columnChooseUI.fillData(tableElements);
                setOKButtonText("Finish");
            }
        }

        page = page + 1;
        switchPage(page);
    }


    private void switchPage(int newPage) {
        rootPanel.removeAll();
        JPanel comp = containerPanelList.get(newPage);
        rootPanel.add(comp);
        rootPanel.repaint();//刷新页面，重绘面板
        rootPanel.validate();//使重绘的面板确认生效
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return rootPanel;
    }

    @Override
    protected Action[] createActions() {
        return new Action[]{previousAction, getOKAction(), getCancelAction()};
    }

    private GenerateConfig generateConfig;

    public void fillData(Project project, List<DbTable> tableElements) {
        this.project = project;
        this.tableElements = tableElements;
        TemplatesSettings templatesSettings = TemplatesSettings.getInstance(project);
        final TemplateContext templateContext = templatesSettings.getTemplateConfigs();
        generateConfig = templateContext.getGenerateConfig();
        if (generateConfig == null) {
            generateConfig = new DefaultGenerateConfig(templateContext);
        }

        final Map<String, List<TemplateSettingDTO>> settingMap = templatesSettings.getTemplateSettingMap();
        if (settingMap.isEmpty()) {
            throw new RuntimeException("无法获取模板");
        }

        tablePreviewUI.fillData(project, tableElements, generateConfig);
    }

    public GenerateConfig determineGenerateConfig() {
        GenerateConfig generateConfig = new GenerateConfig();
        codeGenerateUI.refreshGenerateConfig(generateConfig);
        tablePreviewUI.refreshGenerateConfig(generateConfig);
        columnChooseUI.refreshGenerateConfig(generateConfig);
        return generateConfig;
    }


}

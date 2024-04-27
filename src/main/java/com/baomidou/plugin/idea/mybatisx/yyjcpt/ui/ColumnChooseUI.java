package com.baomidou.plugin.idea.mybatisx.yyjcpt.ui;

import com.baomidou.plugin.idea.mybatisx.generate.dto.GenerateConfig;
import com.baomidou.plugin.idea.mybatisx.generate.dto.TableUIInfo;
import com.baomidou.plugin.idea.mybatisx.generate.plugin.helper.IntellijColumnInfo;
import com.baomidou.plugin.idea.mybatisx.generate.plugin.helper.IntellijTableInfo;
import com.baomidou.plugin.idea.mybatisx.util.DbToolsUtils;
import com.intellij.database.psi.DbTable;
import com.intellij.ui.JBColor;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.TableView;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnChooseUI {
    private final Map<String, List<IntellijColumnInfo>> tableColumnsMap;

    private JPanel rootPanel;
    private JPanel listPanel;
    private JComboBox<DbTable> dbTableList;

    ListTableModel<IntellijColumnInfo> dbTableColumnsModel = new ListTableModel<>(
        new TextTableColumnInfo("name"),
        new TextTableColumnInfo("remarks"),
        new CheckboxTableColumnInfo("selected"),
        new CheckboxTableColumnInfo("required")
    );

    DefaultComboBoxModel<DbTable> dbTableListModel = new DefaultComboBoxModel<>();

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public ColumnChooseUI() {
        tableColumnsMap = new HashMap<>();

        TableView<IntellijColumnInfo> tableView = new TableView<>(dbTableColumnsModel);
        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setFill(GridConstraints.FILL_HORIZONTAL);

        listPanel.add(ToolbarDecorator.createDecorator(tableView)
                .setPreferredSize(new Dimension(860, 330))
                .disableAddAction()
                .disableRemoveAction()
                .disableUpDownActions()
                .createPanel(),
            gridConstraints);

        dbTableList.setModel(dbTableListModel);
        dbTableList.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value == null ? " " : value.getName());
            label.setBackground(JBColor.WHITE);
            label.setForeground(isSelected ? JBColor.BLUE : JBColor.BLACK);
            return label;
        });
        dbTableList.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
                switchTable((DbTable)e.getItem());
        });
    }

    public void fillData(List<DbTable> dbTables) {
        tableColumnsMap.clear();
        dbTableListModel.removeAllElements();

        for (DbTable dbTable : dbTables)
            dbTableListModel.addElement(dbTable);
    }

    private void switchTable(DbTable dbTable) {
        for (int currentRowIndex = dbTableColumnsModel.getRowCount() - 1; currentRowIndex >= 0; currentRowIndex--)
            dbTableColumnsModel.removeRow(currentRowIndex);

        if (dbTable == null)
            return;

        IntellijTableInfo tableInfo = DbToolsUtils.buildIntellijTableInfo(dbTable);
        String tableName = tableInfo.getTableName();
        List<IntellijColumnInfo> tableColumns = tableColumnsMap.get(tableName);
        if (tableColumns == null) {
            tableColumns = new ArrayList<>();
            for (IntellijColumnInfo columnInfo : tableInfo.getColumnInfos()) {
                IntellijColumnInfo info = new IntellijColumnInfo();
                info.setName(columnInfo.getName());
                info.setDataType(columnInfo.getDataType());
                info.setRemarks(columnInfo.getRemarks());
                info.setNullable(true);
                tableColumns.add(info);
                dbTableColumnsModel.addRow(info);
            }
            tableColumnsMap.put(tableName, tableColumns);
        }
        else
            dbTableColumnsModel.addRows(tableColumns);
    }

    public void refreshGenerateConfig(GenerateConfig generateConfig) {
        List<TableUIInfo> tableUIInfos = generateConfig.getTableUIInfoList();
        for (TableUIInfo tableUIInfo : tableUIInfos) {
            List<IntellijColumnInfo> parameterInfos = tableColumnsMap.get(tableUIInfo.getTableName());

            List<IntellijColumnInfo> answer = new ArrayList<>();
            if (parameterInfos != null) {
                for (IntellijColumnInfo parameterInfo : parameterInfos) {
                    if (parameterInfo.isSelected()) {
                        IntellijColumnInfo temp = new IntellijColumnInfo();
                        temp.setName(parameterInfo.getName());
                        temp.setDataType(parameterInfo.getDataType());
                        temp.setRemarks(parameterInfo.getRemarks());
                        temp.setRequired(parameterInfo.isRequired());
                        temp.setNullable(parameterInfo.getNullable());

                        answer.add(temp);
                    }
                }
            }

            tableUIInfo.setParameterInfos(answer);
        }
    }

    private static class TextTableColumnInfo extends ColumnInfo<IntellijColumnInfo, String> {
        public TextTableColumnInfo(String name) {
            super(name);
        }

        @Override
        public @Nullable String valueOf(IntellijColumnInfo queryObjectUIInfo) {
            String value = null;
            if ("name".equals(getName()))
                value = queryObjectUIInfo.getName();
            else if ("remarks".equals(getName()))
                value = queryObjectUIInfo.getRemarks();
            return value;
        }
    }


    private static class CheckboxTableColumnInfo extends ColumnInfo<IntellijColumnInfo, Boolean> {
        public CheckboxTableColumnInfo(String name) {
            super(name);
        }

        @Override
        public boolean isCellEditable(IntellijColumnInfo tableUIInfo) {
            return true;
        }

        @Override
        public int getWidth(JTable table) {
            return 120;
        }

        @Nullable
        @Override
        public TableCellEditor getEditor(IntellijColumnInfo columnInfo) {
            return new CheckBoxCellEditor(columnInfo, getName());
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(IntellijColumnInfo columnInfo) {
            return new CheckboxCellRenderer();
        }

        @Override
        public Boolean valueOf(IntellijColumnInfo item) {
            Boolean answer = null;
            if ("required".equals(getName()))
                answer = item.isRequired();
            else if ("selected".equals(getName()))
                answer = item.isSelected();

            return answer;
        }
    }

    private static class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        protected JCheckBox checkBox;

        public CheckBoxCellEditor(IntellijColumnInfo columnInfo, String name) {
            checkBox = new JCheckBox();
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
            checkBox.addChangeListener(e -> {
                boolean value = checkBox.isSelected();
                if ("required".equals(name)) {
                    columnInfo.setRequired(value);
                    if (value && !columnInfo.isSelected())
                        columnInfo.setSelected(true);
                } else if ("selected".equals(name)) {
                    columnInfo.setSelected(value);
                    if (!value && columnInfo.isRequired())
                        columnInfo.setRequired(false);
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return checkBox.isSelected();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            checkBox.setSelected((Boolean) value);
            checkBox.setBackground(new Color(38, 117, 191));
            return checkBox;
        }
    }

    private static class CheckboxCellRenderer extends JCheckBox implements TableCellRenderer {
        public CheckboxCellRenderer() {
            super();
            this.setOpaque(true);
            this.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Boolean) {
                setSelected((Boolean) value);
                setBackground(isSelected ? new Color(38, 117, 191) : table.getBackground());
            }
            return this;
        }
    }

}

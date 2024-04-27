<template>
  <div class="right-container">
    <el-form ref="searchForm" class="searchBox">
      <el-row><#list tableClass.parameterFields as field>
        <el-col :span="8">
          <el-form-item label="${field.fieldName}：" class="ywlbItem" prop="${field.fieldName}">
            <el-input placeholder="请输入" v-model="searchForm.${field.fieldName}"></el-input>
          </el-form-item>
        </el-col></#list>
        <el-col :span="24" style="text-align: right">
          <el-button class="searchBtn" type="primary" @click="handleSearch">查询</el-button>
        </el-col>
      </el-row>
    </el-form>
    <div class="tableBox">
      <table-comp
        ref="table"
        :url="myTableApiUrl"
        :tableColumn="tableColumn"
        :params="searchParams"
        :isShowToolbar="false"
        @getCheckboxChangeInfo="getSelectedItem">
      </table-comp>
    </div>
  </div>
</template>
<script>
  import tableComp from '@/components/TableComp';
  import commonTools from '@/utils/common-tools';
  import { page${tableClass.shortClassName}ApiUrl } from './api';

  export default {
    components: {
      tableComp
    },
    data() {
      return {
        titleName: '',
        showDialog: false,
        myTableApiUrl: page${tableClass.shortClassName}ApiUrl(),
        searchForm: {<#list tableClass.parameterFields as field>
          ${field.fieldName}: ''<#sep>,</#list>
        },
        searchParams: {<#list tableClass.parameterFields as field>
          ${field.fieldName}: ''<#sep>,</#list>
        },
        tableColumn: [<#list tableClass.allFields as field>
          {
            field: '${field.columnName}',
            title: '${field.columnName}',
            minWidth: 150,
            align: 'left'
          }<#sep>,</#list>
        ],
        selectedRow: []
      }
    },
    methods: {
      handleSearch() {
        this.searchParams = { ...this.searchForm };
      },
      getSelectedItem(data) {
        this.selectedRow = data.selection;
      }
    }
  }
</script>
<style lang="scss" scoped>
  .searchBox {
    margin-top: 12px;

    /deep/.el-form-item{
      &.ywlbItem{
        .el-form-item__label {
          min-width: 120px;
          width: 120px;
        }
        .el-form-item__content {
          width: calc(100% - 120px);
        }
      }
    }

    .searchBtn {
      margin-left: 20px;
    }
  }
  .tableBox {
    margin-top: 8px;
  }
</style>

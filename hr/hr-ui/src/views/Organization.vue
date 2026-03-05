<template>
  <div class="w-full h-full flex gap-6 overflow-hidden">
    <!-- 左侧：部门树 -->
    <el-card class="w-80 h-full flex flex-col !border-slate-200" shadow="never" :body-style="{ padding: '16px', display: 'flex', flexDirection: 'column', flex: 1, overflow: 'hidden' }">
      <template #header>
        <div class="font-bold text-slate-800 text-base border-none">公司层级</div>
      </template>
      <div class="flex-1 overflow-auto mt-2">
        <el-tree
          ref="treeRef"
          :data="deptTree"
          :props="defaultProps"
          :filter-node-method="filterNode"
          default-expand-all
          highlight-current
          node-key="id"
          class="custom-tree block"
          @current-change="handleNodeClick"
        />
      </div>
    </el-card>

    <!-- 右侧：数据与成员 -->
    <el-card class="flex-1 h-full shadow-none !border-slate-200" :body-style="{ padding: '24px', display: 'flex', flexDirection: 'column', height: '100%', boxSizing: 'border-box' }">
      <!-- 右侧头部 -->
      <div class="flex justify-between items-center mb-6">
        <div>
          <h2 class="text-xl font-bold text-slate-800 m-0 leading-tight">研发中心</h2>
          <span class="text-sm text-slate-500 mt-1 inline-block">共 128 名成员</span>
        </div>
        <el-button type="primary" class="!bg-primary !border-none !rounded-lg" size="large">
          <el-icon class="mr-1"><Plus /></el-icon> 添加成员
        </el-button>
      </div>

      <!-- 右侧表格 -->
      <div class="flex-1 overflow-auto">
        <el-table :data="members" border class="w-full" stripe>
          <el-table-column prop="name" label="姓名" align="center" width="120" />
          <el-table-column prop="empId" label="工号" align="center" width="120" />
          <el-table-column prop="position" label="职位" align="center" />
          <el-table-column prop="status" label="状态" align="center" width="100">
            <template #default="scope">
              <span class="px-2 py-1 rounded-full text-xs font-medium" 
                :class="scope.row.status === '在职' ? 'bg-green-100 text-green-700' : 'bg-slate-100 text-slate-600'">
                {{ scope.row.status }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" fixed="right">
            <template #default>
              <el-button link type="primary" size="small" class="!text-primary">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { Plus } from '@element-plus/icons-vue';

// 树形图搜索过滤
const filterNode = (value: string, data: any) => {
  if (!value) return true;
  return data.name.includes(value);
};

// 模拟数据
const defaultProps = {
  children: 'children',
  label: 'name',
};

const deptTree = ref([
  {
    id: 1,
    name: '力度科技 (总公司)',
    children: [
      { id: 2, name: '研发中心' },
      { id: 3, name: '营销与发展部' },
      { id: 4, name: '战略采购部' }
    ]
  }
]);

const currentDept = ref<any>(deptTree.value[0]?.children?.[0]);

const members = ref([
  { id: 1, name: '钱以雷', empId: 'EMP-001', position: '高级前端工程师', status: '在职' },
  { id: 2, name: '李四', empId: 'EMP-002', position: '后端架构师', status: '在职' },
]);

const handleNodeClick = (data: any) => {
  currentDept.value = data;
};
</script>

<style scoped>
:deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid var(--color-slate-100, #f1f5f9);
}
:deep(.custom-tree .el-tree-node__content) {
  height: 40px;
  border-radius: 8px;
  margin-bottom: 4px;
}
:deep(.custom-tree .el-tree-node.is-current > .el-tree-node__content) {
  background-color: var(--color-slate-50);
  color: var(--color-primary);
  font-weight: 500;
}
:deep(.custom-tree .el-tree-node.is-current > .el-tree-node__content .el-tree-node__expand-icon) {
  color: var(--color-primary);
}
</style>

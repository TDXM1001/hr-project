<template>
  <div class="h-full flex flex-col gap-6 animate-fade-in p-6">
    <!-- 顶部统计概览 -->
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-3">
        <h2 class="text-2xl font-bold text-slate-900 tracking-tight">组织架构</h2>
        <el-tag type="info" effect="plain" class="!px-3 !h-7 !rounded-lg !border-slate-200 !text-slate-500 font-medium">
          全员 128 名成员
        </el-tag>
      </div>
      <div class="flex items-center gap-3">
        <el-button icon="Plus" type="primary" class="!rounded-xl !px-6 !h-10 !bg-primary !border-none font-bold shadow-md shadow-primary/20">
          添加成员
        </el-button>
        <el-button icon="Filter" class="!rounded-xl !px-4 !h-10 !border-slate-200 !text-slate-600">
          视图设置
        </el-button>
      </div>
    </div>

    <!-- 主展示区：左侧组织树 + 右侧人员表 -->
    <div class="flex-1 flex gap-6 min-h-0">
      <!-- 部门层级树 (Card) -->
      <div class="w-80 bg-white rounded-2xl shadow-sm border border-slate-100 flex flex-col p-4 overflow-hidden">
        <div class="mb-4 flex items-center justify-between pb-3 border-b border-slate-50">
          <span class="text-sm font-bold text-slate-800">组织目录</span>
          <el-icon class="cursor-pointer text-slate-400 hover:text-primary transition-colors"><Plus /></el-icon>
        </div>
        <el-input 
          v-model="deptFilter" 
          placeholder="搜索部门..." 
          prefix-icon="Search" 
          size="default" 
          class="!mb-4 custom-search !rounded-xl"
        />
        <el-tree 
          :data="deptData" 
          :props="defaultProps" 
          highlight-current 
          default-expand-all 
          class="dept-tree !text-sm flex-1 overflow-y-auto"
        >
          <template #default="{ node, data }">
            <div class="flex items-center gap-2 group w-full pr-4 py-1">
              <el-icon v-if="data.isTop" class="text-primary/70"><OfficeBuilding /></el-icon>
              <el-icon v-else class="text-slate-400"><Folder /></el-icon>
              <span class="flex-1 text-slate-700 font-medium group-hover:text-primary transition-colors">{{ node.label }}</span>
              <span class="text-xs text-slate-400 opacity-0 group-hover:opacity-100 transition-opacity">12</span>
            </div>
          </template>
        </el-tree>
      </div>

      <!-- 人员名单列表 (Card) -->
      <div class="flex-1 bg-white rounded-2xl shadow-sm border border-slate-100 flex flex-col p-4 min-w-0">
        <div class="flex items-center justify-between mb-6">
          <span class="text-base font-bold text-slate-900 border-l-4 border-primary pl-3">研发中心 - 后端开发组</span>
          <div class="flex items-center gap-2">
            <el-select v-model="statusFilter" placeholder="状态" size="small" class="!w-24">
              <el-option label="全部状态" value="" />
              <el-option label="在职" value="1" />
              <el-option label="离职" value="0" />
            </el-select>
          </div>
        </div>
        
        <el-table 
          :data="employeeList" 
          class="custom-table flex-1 !text-sm" 
          header-cell-class-name="!bg-slate-50/50 !text-slate-500 !font-bold !py-4"
          cell-class-name="!py-4"
        >
          <!-- 表格列强制绝对居中对齐 -->
          <el-table-column prop="empName" label="姓名" align="center" width="120">
            <template #default="{ row }">
              <div class="font-bold text-slate-800">{{ row.empName }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="empNo" label="工号" align="center" width="140">
            <template #default="{ row }">
              <span class="font-mono text-slate-400">{{ row.empNo }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="posName" label="职位" align="center" />
          <el-table-column prop="status" label="当前状态" align="center" width="120">
            <template #default="{ row }">
              <el-tag 
                v-if="row.status === '在职'" 
                class="!rounded-full !px-3 font-bold !bg-emerald-50 !text-emerald-600 !border-emerald-100"
              >
                在职
              </el-tag>
              <el-tag 
                v-else 
                class="!rounded-full !px-3 font-bold !bg-slate-50 !text-slate-400 !border-slate-100"
              >
                离职
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="快捷操作" align="center" width="160">
            <template #default>
              <div class="flex items-center justify-center gap-4">
                <el-link type="primary" :underline="false" class="font-bold hover:opacity-80 transition-opacity">编辑</el-link>
                <el-link type="danger" :underline="false" class="font-bold hover:opacity-80 transition-opacity">变动</el-link>
              </div>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 底部翻页 -->
        <div class="mt-6 flex justify-end">
          <el-pagination background layout="prev, pager, next" :total="50" class="custom-pagination" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { Plus, Search, OfficeBuilding, Folder, Filter } from '@element-plus/icons-vue';

// 模拟组织架构树
const deptData = [
  {
    label: '丽艺软件科技有限公司',
    isTop: true,
    children: [
      {
        label: '行政部',
        children: [{ label: '人力资源组' }, { label: '财务组' }]
      },
      {
        label: '研发中心',
        children: [{ label: '前端开发组' }, { label: '后端开发组' }, { label: '移动端组' }]
      },
      {
        label: '业务部',
        children: [{ label: '华东区' }, { label: '华南区' }]
      }
    ]
  }
];

// 模拟人员列表
const employeeList = ref([
  { empName: '张建国', empNo: 'EMP-001', posName: '后端开发工程师', status: '在职' },
  { empName: '李美虹', empNo: 'EMP-002', posName: '总架构师', status: '在职' },
  { empName: '王大锤', empNo: 'EMP-003', posName: '全栈工程师', status: '离职' },
  { empName: '赵小琴', empNo: 'EMP-004', posName: '资深开发专家', status: '在职' },
  { empName: '陈志勇', empNo: 'EMP-005', posName: '实习生', status: '在职' },
]);

const deptFilter = ref('');
const statusFilter = ref('');
const defaultProps = { children: 'children', label: 'label' };
</script>

<style scoped>
/* 深度自定义树形组件 */
:deep(.el-tree-node__content) {
  height: 48px !important;
  border-radius: 12px !important;
  margin-bottom: 4px;
}

:deep(.el-tree-node.is-current > .el-tree-node__content) {
  background-color: #EEF2FF !important; /* 浅紫/靛蓝色背景 */
  color: theme('colors.primary') !important;
}

:deep(.el-tree-node.is-current > .el-tree-node__content span) {
  color: theme('colors.primary') !important;
  font-weight: 700;
}

/* 覆盖表格线条与头部样式 */
.custom-table :deep(.el-table__row) {
  transition: background-color 0.2s;
}

.custom-table :deep(.el-table__row:hover > td) {
  background-color: #f8fafc !important;
}

/* 核心动画 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-out;
}

/* 搜索框圆角覆盖 */
:deep(.custom-search .el-input__wrapper) {
  border-radius: 12px;
  background-color: #f8fafc;
}
</style>

import Layout from '@/pages/layout/layoutIndex'
export default {
  path: '/${tableClass.shortClassName2}',
  component: Layout,
  meta: {
    title: '${tableClass.shortClassName}',
    icon: 'nav-icon-left iconfont fsicon-btn-review',
    roleId: 1
  },
  hidden: false,
  children: [{
    path: 'index',
    name: '${tableClass.shortClassName}',
    component: () => import('@/pages/${tableClass.shortClassName2}/index'),
    meta: {
      title: '${tableClass.shortClassName}',
      icon: '',
      roleId: 1
    }
  }]
}

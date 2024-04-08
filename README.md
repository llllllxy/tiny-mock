# Tiny Mock
# 一个基于SpringBoot开发的轻量级在线数据Mock平台

![SpringBoot](https://img.shields.io/badge/springboot-2.6.13-green.svg?style=flat-square)
<a href="https://github.com/llllllxy/tiny-mock/stargazers"><img src="https://img.shields.io/github/stars/llllllxy/tiny-mock?style=flat-square&logo=GitHub"></a>
<a href="https://github.com/llllllxy/tiny-mock/network/members"><img src="https://img.shields.io/github/forks/llllllxy/tiny-mock?style=flat-square&logo=GitHub"></a>
<a href="https://github.com/llllllxy/tiny-mock/watchers"><img src="https://img.shields.io/github/watchers/llllllxy/tiny-mock?style=flat-square&logo=GitHub"></a>
<a href="https://github.com/llllllxy/tiny-mock/issues"><img src="https://img.shields.io/github/issues/llllllxy/tiny-mock.svg?style=flat-square&logo=GitHub"></a>
<a href="https://github.com/llllllxy/tiny-mock/blob/master/LICENSE"><img src="https://img.shields.io/github/license/llllllxy/tiny-mock.svg?style=flat-square"></a>
<a href='https://gitee.com/leisureLXY/tiny-mock/stargazers'><img src='https://gitee.com/leisureLXY/tiny-mock/badge/star.svg?theme=dark' alt='star'></img></a>
<a href='https://gitee.com/leisureLXY/tiny-mock/members'><img src='https://gitee.com/leisureLXY/tiny-mock/badge/fork.svg?theme=dark' alt='fork'></img></a>

## 功能特色
- 快速生成演示数据，将请求根地址指向到模拟地址即可轻松实现接口模拟
- 支持分项目管理，根据项目给接口分组，不同项目拥有不同的接口根地址
- 支持MockJs模拟，根据MockJs的强大能力进行数据生成，文档可见 [mockjs示例](http://mockjs.com/examples.html)
- 支持自定义Http响应码
- 支持模拟接口返回延时
- 支持项目接口导出和导入，方便数据恢复备份
- 支持仪表盘功能，快速预览项目和接口运行调用情况
- 支持邀请码注册功能，方便团队内成员注册

## 主要技术选型

| 依赖                | 说明           |
|-------------------|--------------   |
| SpringBoot 2.6.13 | 基础框架         |
| Hikari            | 高性能数据库连接池 |
| MyBatis-Plus 3.5.5   | MyBatis的增强ORM |
| Redis             | 业务缓存、会话共享    |
| Hutool             | 通用工具包 |
| Layui             | 前端框架         |
| layuimini             | 前端模板         |


## 运行环境
- Jdk8
- MySQL5.6+
- Redis3.0+

## 运行启动教程
1. 新建`MySQL`数据库并导入`sql`文件夹下的数据库脚本
2. 修改配置文件中`application.yml`中数据库连接信息和Redis连接信息
3. 运行启动类`TinyMockApplication`，即可正常启动项目
4. 管理后台登录地址：`http://localhost:9019`  账户密码 `zhangsan / 123456`

## 平台功能
1、项目管理

2、主机管理

3、任务管理

4、任务执行日志

5、预警邮箱配置


## 功能界面展示

登录
![登录](src/main/resources/static/images/readme/登录.png)

注册
![注册](src/main/resources/static/images/readme/注册.png)

首页
![首页](src/main/resources/static/images/readme/首页.png)

我的项目
![我的项目](src/main/resources/static/images/readme/我的项目.png)

我的项目-新建
![我的项目-新建](src/main/resources/static/images/readme/我的项目-新建.png)

接口管理
![接口管理](src/main/resources/static/images/readme/接口管理.png)

接口管理-新增
![接口管理-新增](src/main/resources/static/images/readme/接口管理-新增.png)

接口管理-版本
![接口管理-版本](src/main/resources/static/images/readme/接口管理-版本.png)

接口管理-访问日志
![接口管理-访问日志](src/main/resources/static/images/readme/接口管理-访问日志.png)

数据统计
![数据统计](src/main/resources/static/images/readme/数据统计.png)

问题与建议
![问题与建议](src/main/resources/static/images/readme/问题与建议.png)

使用文档
![使用文档](src/main/resources/static/images/readme/使用文档.png)



## 其他
Mock.js 文档地址 http://mockjs.com/examples.html

## 更新计划
- 核心mock功能  `已完成`
- 项目管理  `已完成`
- 接口管理  `已完成`
- 接口修改历史  `已完成`
- 邀请码注册 `已完成`
- 首页大屏 `已完成`
- 接口访问日志查看  `已完成`
- Mock支持自定义Http状态码  `已完成`
- 数据统计页面 `待完成`
- 项目成员协作功能 `待完成`
- 导出接口，导入接口（项目级别的，方便数据备份和恢复） `待完成`
- 自定义头像修改 `已完成`
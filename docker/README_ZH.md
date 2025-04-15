# tiny-mock Docker镜像


#### 镜像打包说明
注意：jar包要放在target目录下，target目录和Dockerfile在同一目录下
```shell
docker build --no-cache -t tiny-mock:1.1.2 .
```

#### 镜像导出命令
```shell
docker save -o tiny-mock-1.1.2.tar tiny-mock:1.1.2
```

#### 镜像导入命令
```shell
docker load -i tiny-mock-1.1.2.tar
```

#### 镜像启动命令1(通过挂载application.yml的形式)
```shell
docker run --name tinymock -p 9019:9019 -e JAVA_OPTS="-Xms256m -Xmx512m"  -v /opt/tinymock/application.yml:/config/application.yml  -v /opt/tinymock/logs:/opt/tinymock/logs -d tiny-mock:1.1.2 

```

#### 镜像启动命令2(通过透传环境变量的形式)
```shell
docker run --name tinymock -p 9019:9019 -e JAVA_OPTS="-Xms256m -Xmx512m" -e MYSQL_SERVICE_HOST=172.20.88.226 -e MYSQL_SERVICE_USER=root -e MYSQL_SERVICE_DB_NAME=tiny-mock -e MYSQL_SERVICE_PASSWORD=lion@LL99 -e REDIS_SERVICE_HOST=172.20.81.51 -e REDIS_SERVICE_PASSWORD=lion@LL99 -v /opt/tinymock/logs:/opt/tinymock/logs -d tiny-mock:1.1.2 
```


#### 环境变量属性配置列表
| 属性名称                                    | 描述                 | 选项                                                                                                                                        |
|-----------------------------------------|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| TINYMOCK_STORAGETYPE                    | 系统文件存储方式: 本地/minio | local/minio  默认: **local**                                                                                                                |
| TINYMOCK_UPLOADPATH                     | 存储方式为本地时的存储位置      | 默认: **/home/data/tinymock**                                                                                                               |
| TINYMOCK_TENANTAUTHTIMEOUT              | 会话时间(时间类的单位都是秒)    | 默认: **1800**                                                                                                                              |
| TINYMOCK_MAXIMUMCONCURRENTLOGINS        | 用户最大登录并发数          | 默认: **2**                                                                                                                                 |
| TINYMOCK_PROJECTEXPORTDEK               | 项目导出备份加密密钥（sm4）    | 需为16字节的BASE64编码字符串，默认: **内置**                                                                                                             |
| TINYMOCK_JWTSECRET                      | jwt密钥              | 默认: **内置**                                                                                                                                |
| MYSQL_SERVICE_HOST                      | 数据库 连接地址           |                                                                                                                                           |
| MYSQL_SERVICE_PORT                      | 数据库端口              | 默认: **3306**                                                                                                                              |
| MYSQL_SERVICE_DB_NAME                   | 数据库库名              |                                                                                                                                           |
| MYSQL_SERVICE_USER                      | 数据库用户名             |                                                                                                                                           |
| MYSQL_SERVICE_PASSWORD                  | 数据库用户密码            |                                                                                                                                           |
| MYSQL_SERVICE_DB_PARAM                  | 数据库连接参数            | 默认: **useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull&useSSL=true&allowMultiQueries=true** |
| JAVA_OPTS                               | JVM配置参数            | 默认: **-Xms256m -Xmx512m**                                                                                                                 |
| REDIS_SERVICE_HOST                      | Redis 连接地址         | 默认: **127.0.0.1**                                                                                                                         |
| REDIS_SERVICE_PORT                      | Redis 端口           | 默认: **6379**                                                                                                                              |
| REDIS_SERVICE_PASSWORD                  | Redis 密码           | 默认: **123456**                                                                                                                            |
| REDIS_SERVICE_DATABASE                  | Redis 库            | 默认: **0**                                                                                                                                 |
| TINYMOCK_ENDPOINT                       | minio endpoint     | 默认: **http://127.0.0.1:9000**                                                                                                             |
| TINYMOCK_ACCESSKEY                      | minio accessKey    | 默认: **minioadmin**                                                                                                                        |
| TINYMOCK_SECRETKEY                | minio secretKey    | 默认: **123654**                                                                                                                            |
| TINYMOCK_BUCKET                  | minio 存储桶          | 默认: **tinymock**                                                                                                                          |

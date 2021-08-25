#!/usr/bin/env bash
echo '----delete old jar----'
# 强制覆盖老的 jar 包
cp -f /mydata/jenkins_home/workspace/zk-monitor/target/zk_monitor.jar /mydata/jenkins_home/workspace/zk-monitor-run/zk_monitor.jar
# 定义应用组名
group_name='zk-monitor'
# 定义应用名称
app_name='zk-monitor'
# 定义应用版本
app_version='1.0'
# 定义应用环境
profile_active='prod'
echo '----copy jar----'
docker stop ${app_name}
echo '----stop container----'
docker rm ${app_name}
echo '----rm container----'
docker rmi ${group_name}/${app_name}:${app_version}
echo '----rm image----'
# 打包编译docker镜像
docker build -t ${group_name}/${app_name}:${app_version} .
echo '----build image----'
docker run -p 8081:8081 --name ${app_name} \
--network=mydata_default \
--link mysql:db \
-e 'spring.profiles.active'=${profile_active} \
-e TZ="Asia/Shanghai" \
-v /etc/localtime:/etc/localtime \
-v /mydata/app/${app_name}/logs:/var/logs \
-d ${group_name}/${app_name}:${app_version}
echo '----start container----'
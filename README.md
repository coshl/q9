打包的时候可以使用在 maven 中选择父项目的 package 打包，然后将三个 jar 放到一起，解压合并部署,命令是：
unzip -q summer-dao-exec.jar,unzip -q summer-service-exec.jar,unzip -q summer-web.jar
命令 nohup java org.springframework.boot.loader.JarLauncher >output.txt &
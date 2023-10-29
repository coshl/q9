mvn clean package -Dmaven.test.skip=true

# 将应用停止
echo "Stopping SpringBoot Application"
pid=`netstat -nlp | grep :8088 | awk '{print $7}' | awk -F"/" '{ print $1 }'`
if [ -n "$pid" ]
then
   kill -9 $pid
fi

echo "Stopping SpringBoot Application"
pid=`netstat -nlp | grep :8089 | awk '{print $7}' | awk -F"/" '{ print $1 }'`
if [ -n "$pid" ]
then
   kill -9 $pid
fi

rm -rf /home/dev/*
cp -rf /var/lib/jenkins/workspace/dev/target/summer-final /home/dev/node1
cp -rf /var/lib/jenkins/workspace/dev/target/summer-final /home/dev/node2
mkdir /home/dev/node1/logs
mkdir /home/dev/node2/logs
BUILD_ID=dontKillMe nohup java -Dserver.port=8088 -jar /home/dev/node1/summer-web-1.0-SNAPSHOT.jar > /home/dev/node1/logs/info.log &
BUILD_ID=dontKillMe nohup java -Dserver.port=8089 -jar /home/dev/node2/summer-web-1.0-SNAPSHOT.jar > /home/dev/node2/logs/info.log &



#  product
cd /home/summer/
rm -rf summer-final
tar xfm summer-final.tar
rm -rf node1/*
rm -rf node2/*
cp -rf summer-final/* node1
cp -rf summer-final/* node2
# 将应用停止
echo "Stopping SpringBoot Application"
pid=`netstat -nlp | grep :8080 | awk '{print $7}' | awk -F"/" '{ print $1 }'`
if [ -n "$pid" ]
then
   kill -9 $pid
fi
source /etc/profile

cd /home/summer/node1

BUILD_ID=dontKillMe nohup java -Dserver.port=8080 -jar summer-web-1.0-SNAPSHOT.jar &

echo "Stopping SpringBoot Application"
pid=`netstat -nlp | grep :8081 | awk '{print $7}' | awk -F"/" '{ print $1 }'`
if [ -n "$pid" ]
then
   kill -9 $pid
fi

cd /home/summer/node2

BUILD_ID=dontKillMe nohup java -Dserver.port=8081 -jar summer-web-1.0-SNAPSHOT.jar &



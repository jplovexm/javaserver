java -Xms1024M -Xmx1024M -Xmn300M  -XX:MaxDirectMemorySize=218M -XX:PermSize=64M  -XX:+UseConcMarkSweepGC  -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseFastAccessorMethods -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:./log/gc.log -jar javaserver.jar &
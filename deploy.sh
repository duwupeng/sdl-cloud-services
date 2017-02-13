#!/usr/bin/env bash
LOG_FILE=/data/bz-release-package/deploy.log
start(){
    cd ~/bz-services
    rm -rf /data/bz-release-package/*
    git pull
    mvn clean compile package -Dmaven.test.skip=true

  echo "`date "+%Y-%m-%d %H:%M:%S"` start services">>$LOG_FILE
#  cp /home/ceshi/bz-services/os-notify/target/os-notify-1.0-SNAPSHOT.jar                        /data/bz-release-package/
  cp /home/ceshi/bz-services/api-gateway/target/api-gateway-1.0-SNAPSHOT.jar                    /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-admin/target/ms-admin-1.0-SNAPSHOT.jar                          /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-common/target/ms-common-1.0-SNAPSHOT.jar                        /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-company/target/ms-company-1.0-SNAPSHOT.jar                      /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-consumption/target/ms-consumption-1.0-SNAPSHOT.jar                  /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-email/target/ms-email-1.0-SNAPSHOT.jar                          /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-examer/target/ms-examer-1.0-SNAPSHOT.jar                         /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-exercise/target/ms-exercise-1.0-SNAPSHOT.jar                       /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-notify/target/ms-notify-1.0-SNAPSHOT.jar                        /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-paper/target/ms-paper-1.0-SNAPSHOT.jar                         /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-project/target/ms-project-1.0-SNAPSHOT.jar                      /data/bz-release-package/
  cp /home/ceshi/bz-services/ms-sms/target/ms-sms-1.0-SNAPSHOT.jar                              /data/bz-release-package/
  cp /home/ceshi/bz-services/os-admin/target/os-admin-1.0-SNAPSHOT.jar                          /data/bz-release-package/
  cp /home/ceshi/bz-services/os-consume/target/os-consume-1.0-SNAPSHOT.jar                        /data/bz-release-package/
  cp /home/ceshi/bz-services/os-examer/target/os-examer-1.0-SNAPSHOT.jar                         /data/bz-release-package/
  cp /home/ceshi/bz-services/os-login/target/os-login-1.0-SNAPSHOT.jar                          /data/bz-release-package/
  cp /home/ceshi/bz-services/os-paper/target/os-paper-1.0-SNAPSHOT.jar                          /data/bz-release-package/
  cp /home/ceshi/bz-services/os-project/target/os-project-1.0-SNAPSHOT.jar                      /data/bz-release-package/
  cp /home/ceshi/bz-services/registration-center/target/registration-center-1.0-SNAPSHOT.jar    /data/bz-release-package/

  cd /data/bz-release-package/
  java -jar registration-center-1.0-SNAPSHOT.jar> $LOG_FILE"-registration-center" &
  sleep 10
  java -jar ms-admin-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-admin"  &
  sleep 10
  java -jar ms-common-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-common"  &
  sleep 10
  java -jar ms-company-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-company"  &
  sleep 10
  java -jar ms-consumption-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-consumption"  &
  sleep 10
  java -jar ms-email-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-email"  &
  sleep 10
  java -jar ms-examer-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-examer"  &
  sleep 10
  java -jar ms-exercise-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-exercise"  &
  sleep 10
  java -jar ms-notify-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-notify"  &
  sleep 10
  java -jar ms-paper-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-paper"  &
  sleep 10
  java -jar ms-project-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-project" &
  sleep 10
  java -jar ms-sms-1.0-SNAPSHOT.jar> $LOG_FILE"-ms-sms" &
  sleep 10
  java -jar os-admin-1.0-SNAPSHOT.jar > $LOG_FILE"-os-admin" &
  sleep 10
  java -jar os-consume-1.0-SNAPSHOT.jar > $LOG_FILE"-os-consume" &
  sleep 10
  java -jar os-examer-1.0-SNAPSHOT.jar > $LOG_FILE"-os-examer" &
  sleep 10
  java -jar os-login-1.0-SNAPSHOT.jar > $LOG_FILE"-os-login" &
  sleep 10
  java -jar os-paper-1.0-SNAPSHOT.jar > $LOG_FILE"-os-paper" &
  sleep 10
  java -jar os-project-1.0-SNAPSHOT.jar> $LOG_FILE"-os-project" &
  sleep 10
  java -jar api-gateway-1.0-SNAPSHOT.jar> $LOG_FILE"-api-gateway" &
  sleep 10
}

stop(){
  echo "`date "+%Y-%m-%d %H:%M:%S"`  stop services">>$LOG_FILE
  ps -ef | grep java|grep 1.0-SNAPSHOT.jar | awk '{print $2}'|xargs kill -9
}
standLone(){
   cd ~/bz-services
   git  pull
   echo "deploy standlone $@"
 while [ $# \> 0 ]
      do
          echo "`date "+%Y-%m-%d %H:%M:%S"` deploy standlone $@"
          echo "`date "+%Y-%m-%d %H:%M:%S"` deploy standlone $@">>$LOG_FILE"-$@"
          echo "cd  $@"
           cd $@
           echo "mvn clean compile package -Dmaven.test.skip=true"
           mvn clean compile package -Dmaven.test.skip=true
           echo " cp /home/ceshi/bz-services/$@/target/$@-1.0-SNAPSHOT.jar   /data/bz-release-package/"
           cp /home/ceshi/bz-services/$@/target/$@-1.0-SNAPSHOT.jar   /data/bz-release-package/
            echo " cd /data/bz-release-package/"
           cd /data/bz-release-package/
            echo " java -jar $@-1.0-SNAPSHOT.jar> $LOG_FILE"-$@" &"
           java -jar $@-1.0-SNAPSHOT.jar> $LOG_FILE"-$@" &
       shift
    done
}
case $1 in
  "start"|"stop"|"standLone") eval $@ ;;
   *) echo "invalid command $1" ;;
esac
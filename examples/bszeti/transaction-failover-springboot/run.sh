#!/usr/bin/env sh
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
export "MAVEN_OPTS=-Dorg.slf4j.simpleLogger.log.org.apache.activemq.artemis.jms.example=info"
export "MAVEN_OPTS=$MAVEN_OPTS -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.showThreadName=true"
export "MAVEN_OPTS=$MAVEN_OPTS -Dorg.slf4j.simpleLogger.logFile=out.log"
#export MAVEN_OPTS="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

mvn clean install -Dactivemq.basedir=${ARTEMIS_HOME}

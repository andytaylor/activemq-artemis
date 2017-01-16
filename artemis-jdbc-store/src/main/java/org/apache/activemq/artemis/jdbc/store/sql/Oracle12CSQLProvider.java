/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.artemis.jdbc.store.sql;

public class Oracle12CSQLProvider  extends GenericSQLProvider {

   private final String createFileTableSQL;
   private final String[] createJournalTableSQL;
   private final String insertFileSQL;

   public String getWritetoLargeMessage() {
      return writetoLargeMessage;
   }

   private final String writetoLargeMessage = "SELECT DATA FROM " + tableName + " WHERE ID=? for UPDATE";

   public Oracle12CSQLProvider(String tableName) {
      super(tableName);
      createFileTableSQL = "CREATE TABLE " + tableName +
               "(ID NUMBER(10) GENERATED BY DEFAULT ON NULL AS IDENTITY, FILENAME VARCHAR(255), EXTENSION VARCHAR(10), DATA BLOB, PRIMARY KEY(ID))";
      createJournalTableSQL = new String[]{
            "CREATE TABLE " + tableName + "(id NUMBER(19) GENERATED BY DEFAULT ON NULL AS IDENTITY,recordType NUMBER(5),compactCount NUMBER(5),txId NUMBER(19),userRecordType NUMBER(5),variableSize NUMBER(10),record BLOB,txDataSize NUMBER(10),txData BLOB,txCheckNoRecords NUMBER(10),seq NUMBER(19))",
            "CREATE INDEX " + tableName + "_IDX ON " + tableName + " (id)"
      };
      insertFileSQL = "INSERT INTO " + tableName + " (FILENAME, EXTENSION, DATA) VALUES (?,?,EMPTY_BLOB())";
   }

   @Override
   public int getMaxBlobSize() {
      return Integer.MAX_VALUE;// its actually 4294967296;
   }

   @Override
   public String[] getCreateJournalTableSQL() {
      return createJournalTableSQL;
   }

   @Override
   public String getCreateFileTableSQL() {
      return createFileTableSQL;
   }

   @Override
   public String getInsertFileSQL() {
      return insertFileSQL;
   }

   public static class Factory implements SQLProvider.Factory {

      @Override
      public SQLProvider create(String tableName) {
         return new Oracle12CSQLProvider(tableName);
      }
   }
}

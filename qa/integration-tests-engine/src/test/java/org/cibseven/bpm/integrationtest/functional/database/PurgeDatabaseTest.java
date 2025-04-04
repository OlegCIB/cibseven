/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cibseven.bpm.integrationtest.functional.database;

import org.cibseven.bpm.engine.ProcessEngine;
import org.cibseven.bpm.engine.impl.HistoryLevelSetupCommand;
import org.cibseven.bpm.engine.impl.ManagementServiceImpl;
import org.cibseven.bpm.engine.impl.ProcessEngineImpl;
import org.cibseven.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.cibseven.bpm.engine.impl.db.PersistenceSession;
import org.cibseven.bpm.engine.impl.interceptor.Command;
import org.cibseven.bpm.engine.impl.interceptor.CommandContext;
import org.cibseven.bpm.engine.variable.VariableMap;
import org.cibseven.bpm.engine.variable.Variables;
import org.cibseven.bpm.integrationtest.util.AbstractFoxPlatformIntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Christopher Zell <christopher.zell@camunda.com>
 */
@RunWith(Arquillian.class)
public class PurgeDatabaseTest extends AbstractFoxPlatformIntegrationTest {

  public static final List<String> TABLENAMES_EXCLUDED_FROM_DB_CLEAN_CHECK = Arrays.asList(
    "ACT_GE_PROPERTY",
    "ACT_GE_SCHEMA_LOG"
  );

  @Deployment
  public static WebArchive processArchive() {
    return initWebArchiveDeployment()
      .addAsResource("org/cibseven/bpm/integrationtest/testDeployProcessArchive.bpmn20.xml");
  }

  @Test
  public void testPurgeDatabase() {
    Assert.assertNotNull(processEngine);
    VariableMap variableMap = Variables.putValue("var", "value");
    runtimeService.startProcessInstanceByKey("testDeployProcessArchive", variableMap);
    runtimeService.startProcessInstanceByKey("testDeployProcessArchive", variableMap);

    ManagementServiceImpl managementServiceImpl = (ManagementServiceImpl) managementService;
    managementServiceImpl.purge();

    assertAndEnsureCleanDb(processEngine);
  }

  /**
   * Ensures that the database is clean after the test. This means the test has to remove
   * all resources it entered to the database.
   * If the DB is not clean, it is cleaned by performing a create a drop.
   *
   * @param processEngine the {@link ProcessEngine} to check
   * @param fail if true the method will throw an {@link AssertionError} if the database is not clean
   * @return the database summary if fail is set to false or null if database was clean
   * @throws AssertionError if the database was not clean and fail is set to true
   */
  public static void assertAndEnsureCleanDb(ProcessEngine processEngine) {
    ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration();
    String databaseTablePrefix = processEngineConfiguration.getDatabaseTablePrefix().trim();

    Map<String, Long> tableCounts = processEngine.getManagementService().getTableCount();

    StringBuilder outputMessage = new StringBuilder();
    for (String tableName : tableCounts.keySet()) {
      String tableNameWithoutPrefix = tableName.replace(databaseTablePrefix, "");
      if (!TABLENAMES_EXCLUDED_FROM_DB_CLEAN_CHECK.contains(tableNameWithoutPrefix)) {
        Long count = tableCounts.get(tableName);
        if (count!=0L) {
          outputMessage.append("\t").append(tableName).append(": ").append(count).append(" record(s)\n");
        }
      }
    }

    if (outputMessage.length() > 0) {
      outputMessage.insert(0, "DB NOT CLEAN: \n");
      /** skip drop and recreate if a table prefix is used */
      if (databaseTablePrefix.isEmpty()) {
        processEngineConfiguration
          .getCommandExecutorSchemaOperations()
          .execute(new Command<Object>() {
            public Object execute(CommandContext commandContext) {
              PersistenceSession persistenceSession = commandContext.getSession(PersistenceSession.class);
              persistenceSession.dbSchemaDrop();
              persistenceSession.dbSchemaCreate();
              HistoryLevelSetupCommand.dbCreateHistoryLevel(commandContext);
              return null;
            }
          });
      }
      Assert.fail(outputMessage.toString());
    }
  }

}

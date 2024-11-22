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
package org.cibseven.bpm.qa.upgrade.variable;

import org.cibseven.bpm.engine.ProcessEngine;
import org.cibseven.bpm.engine.RuntimeService;
import org.cibseven.bpm.engine.test.Deployment;
import org.cibseven.bpm.engine.variable.Variables;
import org.cibseven.bpm.qa.upgrade.DescribesScenario;
import org.cibseven.bpm.qa.upgrade.ScenarioSetup;

public class EmptyStringVariableScenario {

  @Deployment
  public static String deploy() {
    return "org/cibseven/bpm/qa/upgrade/variable/oneTaskProcess.bpmn20.xml";
  }

  @DescribesScenario("emptyStringVariableScenario")
  public static ScenarioSetup createUserOperationLogEntries() {
    return new ScenarioSetup() {
      @Override
      public void execute(ProcessEngine engine, String scenarioName) {
        RuntimeService runtimeService = engine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("oneTaskProcess_714", scenarioName,
            Variables.createVariables().putValue("myStringVar", ""));
      }
    };
  }

}

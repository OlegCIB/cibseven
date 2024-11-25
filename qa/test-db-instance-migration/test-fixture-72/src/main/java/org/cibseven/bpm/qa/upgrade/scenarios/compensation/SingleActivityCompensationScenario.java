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
package org.cibseven.bpm.qa.upgrade.scenarios.compensation;

import org.cibseven.bpm.engine.ProcessEngine;
import org.cibseven.bpm.engine.task.Task;
import org.cibseven.bpm.engine.test.Deployment;
import org.cibseven.bpm.qa.upgrade.DescribesScenario;
import org.cibseven.bpm.qa.upgrade.ExtendsScenario;
import org.cibseven.bpm.qa.upgrade.ScenarioSetup;
import org.cibseven.bpm.qa.upgrade.Times;

/**
 * @author Thorben Lindhauer
 *
 */
public class SingleActivityCompensationScenario {

  @Deployment
  public static String deployProcess() {
    return "org/cibseven/bpm/qa/upgrade/compensation/simpleCompensationProcess.bpmn20.xml";
  }

  @DescribesScenario("init")
  @Times(3)
  public static ScenarioSetup instantiate() {
    return new ScenarioSetup() {
      public void execute(ProcessEngine engine, String scenarioName) {
        engine
          .getRuntimeService()
          .startProcessInstanceByKey("SingleActivityCompensationScenario", scenarioName);

        // create the compensation event subscription and wait before throwing compensation
        Task userTask = engine.getTaskService().createTaskQuery()
            .processInstanceBusinessKey(scenarioName).singleResult();
        engine.getTaskService().complete(userTask.getId());
      }
    };
  }

  @DescribesScenario("init.triggerCompensation")
  @ExtendsScenario("init")
  @Times(3)
  public static ScenarioSetup instantiateAndTriggerCompensation() {
    return new ScenarioSetup() {
      public void execute(ProcessEngine engine, String scenarioName) {
        // throw compensation; the compensation handler for userTask should then be active
        Task beforeCompensateTask = engine.getTaskService().createTaskQuery()
            .processInstanceBusinessKey(scenarioName).singleResult();
        engine.getTaskService().complete(beforeCompensateTask.getId());
      }
    };
  }

}

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
package org.cibseven.bpm.engine.test.bpmn.async;

import static org.junit.Assert.assertNotNull;

import org.cibseven.bpm.engine.runtime.Job;
import org.cibseven.bpm.engine.task.Task;
import org.cibseven.bpm.engine.test.Deployment;
import org.cibseven.bpm.engine.test.util.PluggableProcessEngineTest;
import org.junit.Test;

/**
 * @author Thorben Lindhauer
 *
 */
public class AsyncCallActivityTest extends PluggableProcessEngineTest {


  @Deployment(resources = { "org/cibseven/bpm/engine/test/bpmn/async/AsyncCallActivityTest.asyncStartEvent.bpmn20.xml",
  "org/cibseven/bpm/engine/test/bpmn/async/AsyncCallActivityTest.testCallSubProcess.bpmn20.xml" })
  @Test
  public void testCallProcessWithAsyncOnStartEvent() {

    runtimeService.startProcessInstanceByKey("callAsyncSubProcess");

    Job job = managementService.createJobQuery().singleResult();
    assertNotNull(job);

    managementService.executeJob(job.getId());

    Task task = taskService.createTaskQuery().singleResult();
    assertNotNull(task);
    taskService.complete(task.getId());

  }
}

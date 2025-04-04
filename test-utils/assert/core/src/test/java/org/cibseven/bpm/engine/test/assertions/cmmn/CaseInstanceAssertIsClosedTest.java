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
package org.cibseven.bpm.engine.test.assertions.cmmn;

import static org.cibseven.bpm.engine.test.assertions.cmmn.CmmnAwareTests.assertThat;
import static org.cibseven.bpm.engine.test.assertions.cmmn.CmmnAwareTests.caseService;

import org.cibseven.bpm.engine.runtime.CaseInstance;
import org.cibseven.bpm.engine.test.Deployment;
import org.cibseven.bpm.engine.test.ProcessEngineRule;
import org.cibseven.bpm.engine.test.assertions.helpers.Failure;
import org.cibseven.bpm.engine.test.assertions.helpers.ProcessAssertTestCase;
import org.junit.Rule;
import org.junit.Test;

public class CaseInstanceAssertIsClosedTest extends ProcessAssertTestCase {

  public static final String TASK_B = "PI_TaskB";
  public static final String CASE_KEY = "Case_CaseTaskAssertIsTerminatedTest";

  @Rule
  public ProcessEngineRule processEngineRule = new ProcessEngineRule();

  @Test
  @Deployment(resources = { "cmmn/CaseTaskAssertIsTerminatedTest.cmmn" })
  public void testIsClosed_Success() {
    // Given
    final CaseInstance caseInstance = givenCaseIsCreated();
    // When
    caseService().terminateCaseExecution(caseInstance.getId());
    assertThat(caseInstance).isTerminated();
    caseService().closeCaseInstance(caseInstance.getId());
    // Then
    assertThat(caseInstance).isClosed();
  }

  @Test
  @Deployment(resources = { "cmmn/CaseTaskAssertIsTerminatedTest.cmmn" })
  public void testIsClosed_Failure() {
    // Given
    final CaseInstance caseInstance = givenCaseIsCreated();
    // When
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(caseInstance).isClosed();
      }
    });
  }

  private CaseInstance givenCaseIsCreated() {
    return caseService().createCaseInstanceByKey(CASE_KEY);
  }
}
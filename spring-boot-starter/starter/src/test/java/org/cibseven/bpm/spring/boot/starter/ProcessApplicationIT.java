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
package org.cibseven.bpm.spring.boot.starter;

import org.cibseven.bpm.spring.boot.starter.event.PostDeployEvent;
import org.cibseven.bpm.spring.boot.starter.test.pa.TestProcessApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Svetlana Dorokhova.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = { TestProcessApplication.class, ProcessApplicationIT.DummyComponent.class },
  webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProcessApplicationIT {

  @Autowired
  private DummyComponent dummyComponent;

  @Test
  public void testPostDeployEvent() {
    assertThat(dummyComponent.isPostDeployEventOccurred()).isTrue();
  }

  @Component
  public static class DummyComponent {

    private boolean postDeployEventOccurred;

    @EventListener
    public void eventOccurred(PostDeployEvent event) {
      this.postDeployEventOccurred = true;
    }

    public boolean isPostDeployEventOccurred() {
      return postDeployEventOccurred;
    }
  }

}

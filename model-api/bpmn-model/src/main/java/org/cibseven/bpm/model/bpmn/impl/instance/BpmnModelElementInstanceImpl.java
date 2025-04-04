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
package org.cibseven.bpm.model.bpmn.impl.instance;

import org.cibseven.bpm.model.bpmn.BpmnModelException;
import org.cibseven.bpm.model.bpmn.builder.AbstractBaseElementBuilder;
import org.cibseven.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.cibseven.bpm.model.bpmn.instance.SubProcess;
import org.cibseven.bpm.model.xml.impl.instance.ModelElementInstanceImpl;
import org.cibseven.bpm.model.xml.impl.instance.ModelTypeInstanceContext;

/**
 * Shared base class for all BPMN Model Elements. Provides implementation
 * of the {@link BpmnModelElementInstance} interface.
 *
 * @author Daniel Meyer
 */
public abstract class BpmnModelElementInstanceImpl extends ModelElementInstanceImpl implements BpmnModelElementInstance {

  public BpmnModelElementInstanceImpl(ModelTypeInstanceContext instanceContext) {
    super(instanceContext);
  }

  @SuppressWarnings("rawtypes")
  public AbstractBaseElementBuilder builder() {
    throw new BpmnModelException("No builder implemented for " + this);
  }

  public boolean isScope() {
    return this instanceof org.cibseven.bpm.model.bpmn.instance.Process || this instanceof SubProcess;
  }

  public BpmnModelElementInstance getScope() {
    BpmnModelElementInstance parentElement = (BpmnModelElementInstance) getParentElement();
    if (parentElement != null) {
      if (parentElement.isScope()) {
        return parentElement;
      }
      else {
        return parentElement.getScope();
      }
    }
    else {
      return null;
    }
  }
}

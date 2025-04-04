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
package org.cibseven.bpm.model.bpmn.impl.instance.bpmndi;

import org.cibseven.bpm.model.bpmn.impl.instance.di.LabeledEdgeImpl;
import org.cibseven.bpm.model.bpmn.instance.BaseElement;
import org.cibseven.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.cibseven.bpm.model.bpmn.instance.bpmndi.BpmnLabel;
import org.cibseven.bpm.model.bpmn.instance.bpmndi.MessageVisibleKind;
import org.cibseven.bpm.model.bpmn.instance.di.DiagramElement;
import org.cibseven.bpm.model.bpmn.instance.di.LabeledEdge;
import org.cibseven.bpm.model.xml.ModelBuilder;
import org.cibseven.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.cibseven.bpm.model.xml.type.ModelElementTypeBuilder;
import org.cibseven.bpm.model.xml.type.attribute.Attribute;
import org.cibseven.bpm.model.xml.type.child.ChildElement;
import org.cibseven.bpm.model.xml.type.child.SequenceBuilder;
import org.cibseven.bpm.model.xml.type.reference.AttributeReference;

import static org.cibseven.bpm.model.bpmn.impl.BpmnModelConstants.*;
import static org.cibseven.bpm.model.xml.type.ModelElementTypeBuilder.ModelTypeInstanceProvider;

/**
 * The BPMNDI BPMNEdge element
 *
 * @author Sebastian Menski
 */
public class BpmnEdgeImpl extends LabeledEdgeImpl implements BpmnEdge {

  protected static AttributeReference<BaseElement> bpmnElementAttribute;
  protected static AttributeReference<DiagramElement> sourceElementAttribute;
  protected static AttributeReference<DiagramElement> targetElementAttribute;
  protected static Attribute<MessageVisibleKind> messageVisibleKindAttribute;
  protected static ChildElement<BpmnLabel> bpmnLabelChild;

  public static void registerType(ModelBuilder modelBuilder) {
    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(BpmnEdge.class, BPMNDI_ELEMENT_BPMN_EDGE)
      .namespaceUri(BPMNDI_NS)
      .extendsType(LabeledEdge.class)
      .instanceProvider(new ModelTypeInstanceProvider<BpmnEdge>() {
        public BpmnEdge newInstance(ModelTypeInstanceContext instanceContext) {
          return new BpmnEdgeImpl(instanceContext);
        }
      });

    bpmnElementAttribute = typeBuilder.stringAttribute(BPMNDI_ATTRIBUTE_BPMN_ELEMENT)
      .qNameAttributeReference(BaseElement.class)
      .build();

    sourceElementAttribute = typeBuilder.stringAttribute(BPMNDI_ATTRIBUTE_SOURCE_ELEMENT)
      .qNameAttributeReference(DiagramElement.class)
      .build();

    targetElementAttribute = typeBuilder.stringAttribute(BPMNDI_ATTRIBUTE_TARGET_ELEMENT)
      .qNameAttributeReference(DiagramElement.class)
      .build();

    messageVisibleKindAttribute = typeBuilder.enumAttribute(BPMNDI_ATTRIBUTE_MESSAGE_VISIBLE_KIND, MessageVisibleKind.class)
      .build();

    SequenceBuilder sequenceBuilder = typeBuilder.sequence();

    bpmnLabelChild = sequenceBuilder.element(BpmnLabel.class)
      .build();

    typeBuilder.build();
  }

  public BpmnEdgeImpl(ModelTypeInstanceContext instanceContext) {
    super(instanceContext);
  }

  public BaseElement getBpmnElement() {
    return bpmnElementAttribute.getReferenceTargetElement(this);
  }

  public void setBpmnElement(BaseElement bpmnElement) {
    bpmnElementAttribute.setReferenceTargetElement(this, bpmnElement);
  }

  public DiagramElement getSourceElement() {
    return sourceElementAttribute.getReferenceTargetElement(this);
  }

  public void setSourceElement(DiagramElement sourceElement) {
    sourceElementAttribute.setReferenceTargetElement(this, sourceElement);
  }

  public DiagramElement getTargetElement() {
    return targetElementAttribute.getReferenceTargetElement(this);
  }

  public void setTargetElement(DiagramElement targetElement) {
    targetElementAttribute.setReferenceTargetElement(this, targetElement);
  }

  public MessageVisibleKind getMessageVisibleKind() {
    return messageVisibleKindAttribute.getValue(this);
  }

  public void setMessageVisibleKind(MessageVisibleKind messageVisibleKind) {
    messageVisibleKindAttribute.setValue(this, messageVisibleKind);
  }

  public BpmnLabel getBpmnLabel() {
    return bpmnLabelChild.getChild(this);
  }

  public void setBpmnLabel(BpmnLabel bpmnLabel) {
    bpmnLabelChild.setChild(this, bpmnLabel);
  }
}

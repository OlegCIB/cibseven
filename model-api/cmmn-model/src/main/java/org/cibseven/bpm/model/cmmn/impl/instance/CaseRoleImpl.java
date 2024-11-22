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
package org.cibseven.bpm.model.cmmn.impl.instance;

import static org.cibseven.bpm.model.cmmn.impl.CmmnModelConstants.CMMN10_NS;
import static org.cibseven.bpm.model.cmmn.impl.CmmnModelConstants.CMMN_ELEMENT_CASE_ROLES;

import org.cibseven.bpm.model.cmmn.instance.CaseRole;
import org.cibseven.bpm.model.cmmn.instance.Role;
import org.cibseven.bpm.model.xml.ModelBuilder;
import org.cibseven.bpm.model.xml.impl.instance.ModelTypeInstanceContext;
import org.cibseven.bpm.model.xml.type.ModelElementTypeBuilder;

/**
 * @author Roman Smirnov
 *
 */
public class CaseRoleImpl extends RoleImpl implements CaseRole {

  public CaseRoleImpl(ModelTypeInstanceContext instanceContext) {
    super(instanceContext);
  }

  public static void registerType(ModelBuilder modelBuilder) {
    ModelElementTypeBuilder typeBuilder = modelBuilder.defineType(CaseRole.class, CMMN_ELEMENT_CASE_ROLES)
      .namespaceUri(CMMN10_NS)
      .extendsType(Role.class)
      .instanceProvider(new ModelElementTypeBuilder.ModelTypeInstanceProvider<CaseRole>() {
        public CaseRole newInstance(ModelTypeInstanceContext instanceContext) {
          return new CaseRoleImpl(instanceContext);
        }
      });

    typeBuilder.build();
  }

}

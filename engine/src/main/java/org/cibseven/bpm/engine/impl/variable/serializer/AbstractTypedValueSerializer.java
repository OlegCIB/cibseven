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
package org.cibseven.bpm.engine.impl.variable.serializer;

import java.util.HashSet;
import java.util.Set;

import org.cibseven.bpm.engine.variable.type.ValueType;
import org.cibseven.bpm.engine.variable.value.TypedValue;

/**
 *
 * @author Daniel Meyer
 */
public abstract class AbstractTypedValueSerializer<T extends TypedValue> implements TypedValueSerializer<T> {

  public static final Set<String> BINARY_VALUE_TYPES = new HashSet<String>();
  static {
    BINARY_VALUE_TYPES.add(ValueType.BYTES.getName());
    BINARY_VALUE_TYPES.add(ValueType.FILE.getName());
  }

  protected ValueType valueType;

  public AbstractTypedValueSerializer(ValueType type) {
    valueType = type;
  }

  public ValueType getType() {
    return valueType;
  }

  public String getSerializationDataformat() {
    // default implementation returns null
    return null;
  }

  public boolean canHandle(TypedValue value) {
    if(value.getType() != null && !valueType.getClass().isAssignableFrom(value.getType().getClass())) {
      return false;
    }
    else {
      return canWriteValue(value);
    }
  }

  protected abstract boolean canWriteValue(TypedValue value);

  public boolean isMutableValue(T typedValue) {
    // default
    return false;
  }

}

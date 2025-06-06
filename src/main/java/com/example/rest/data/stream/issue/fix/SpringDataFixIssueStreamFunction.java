package com.example.rest.data.stream.issue.fix;

/*
 * Copyright 2023-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @author : anasoid
 * Date :   6/6/25
 */

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mapping.context.PersistentEntities;

@Configuration
@Slf4j
public class SpringDataFixIssueStreamFunction {
  @Autowired private ApplicationContext applicationContext;
  @Autowired private PersistentEntities persistentEntities;

  /**
   * @see
   *     org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration#persistentEntities()
   */
  @EventListener(ApplicationReadyEvent.class)
  public void fixPersistentEntities() {
    log.warn("Fixing PersistentEntities contexts via reflection");
    List<MappingContext<?, ?>> arrayList = new ArrayList<>();
    for (MappingContext<?, ?> context :
        BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, MappingContext.class)
            .values()) {
      arrayList.add(context);
    }
    setContexts(persistentEntities, arrayList);
  }

  private void setContexts(
      PersistentEntities persistentEntities,
      Collection<? extends MappingContext<?, ? extends PersistentProperty<?>>> newContexts) {
    try {
      Field contextsField = PersistentEntities.class.getDeclaredField("contexts");
      contextsField.setAccessible(true);
      contextsField.set(persistentEntities, newContexts);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException("Failed to set contexts field via reflection", e);
    }
  }
}

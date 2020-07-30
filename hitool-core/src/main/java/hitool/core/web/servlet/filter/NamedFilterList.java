/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hitool.core.web.servlet.filter;


import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;

public interface NamedFilterList extends List<Filter> {
	 
  /**
    * Returns the configuration-unique name assigned to this {@code Filter} list.
    */
   String getName();

   /**
    * Returns a new {@code FilterChain} instance that will first execute this list's {@code Filter}s (in list order)
    * and end with the execution of the given {@code filterChain} instance.
    */
   FilterChain proxy(FilterChain filterChain);
   
}

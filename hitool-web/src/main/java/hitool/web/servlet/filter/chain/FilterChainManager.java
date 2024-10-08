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
package hitool.web.servlet.filter.chain;

import java.util.Map;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;

import hitool.web.servlet.filter.NamedFilterList;

/*
 * FilterChain管理器，负责创建和维护filterchian
 */
public interface FilterChainManager {

	/*
	 * 获取所有Filter
	 */
    Map<String, Filter> getFilters();

    /*
     * 根据指定的chainName获取filter列表
     * @param chainName
     * @return
     */
    NamedFilterList getChain(String chainName);

    /*
     * 是否有fiterChain
     * @return
     */
    boolean hasChains();

    /*
     * 获取filterChain名称列表
     * @return
     */
    Set<String> getChainNames();

    /*
     * 生成代理FiterChain,先执行chainName指定的filerChian,最后执行servlet容器的original
     * @param original
     * @param chainName
     * @return
     */
    FilterChain proxy(FilterChain original, String chainName);

   /*
    *  增加filter到filter列表中
    * @param name
    * @param filter
    */
    void addFilter(String name, Filter filter);

    /*
     *  增加filter到filter列表前，调用初始化方法
     * @param name
     * @param filter
     * @param init
     */
    void addFilter(String name, Filter filter, boolean init);

    
    /*
     *  创建FilterChain
     * @param chainName
     * @param chainDefinition
     * ******************************************************************
     */
    void createChain(String chainName, String chainDefinition);

    /*
     *  追加filter到指定的filterChian中
     * @param chainName
     * @param filterName
     */
    void addToChain(String chainName, String filterName);
    
}

/**
 *    Copyright 2006-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.vspro.cg.internal.types;


import com.github.vspro.cg.config.context.ContextHolder;
import com.github.vspro.cg.internal.FullyQualifiedJavaType;
import com.github.vspro.cg.internal.db.table.IntrospectedColumn;


public interface JavaTypeResolver {


	void setContext(ContextHolder context);


	FullyQualifiedJavaType calculateJavaType(
			IntrospectedColumn introspectedColumn);

	String calculateJdbcTypeName(IntrospectedColumn introspectedColumn);

	//变量修饰如Long,Integer,int short
	String calculateJavaPropertyName(IntrospectedColumn introspectedColumn);

}
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
package hitool.web.exception;

@SuppressWarnings("serial")
public class ServletFilterException extends RuntimeException {

	public ServletFilterException() {
		super();
	}

	public ServletFilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServletFilterException(String message) {
		super(message);
	}

	public ServletFilterException(Throwable cause) {
		super(cause);
	}

}


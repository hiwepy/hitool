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
package hitool.core.web.multipart;

import hitool.core.web.multipart.filepart.rename.FileRenamePolicy;

public class MultipartConfig {

	public FileRenamePolicy policy;
	public String dirs;
	public String targetDir;
	/**
	 * 编码格式 ，默认： UTF-8
	 */
	public String encoding = " UTF-8";
	public long maxPostSize;
	public long maxFileSize;
	public int thresholdSize;
	
	public String getDirs() {
		return dirs;
	}

	public FileRenamePolicy getPolicy() {
		return policy;
	}

	public void setPolicy(FileRenamePolicy policy) {
		this.policy = policy;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public long getMaxPostSize() {
		return maxPostSize;
	}

	public void setMaxPostSize(long maxPostSize) {
		this.maxPostSize = maxPostSize;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public int getThresholdSize() {
		return thresholdSize;
	}

	public void setThresholdSize(int thresholdSize) {
		this.thresholdSize = thresholdSize;
	}

	public void setDirs(String dirs) {
		this.dirs = dirs;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}

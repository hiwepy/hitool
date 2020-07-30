/** 
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 * All Rights Reserved. 
 */
package hitool.core.beanutils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public final class ResultSetUtils {

	/**
	 * 从ResultSet中的到查询结果的列
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static String[] getColNames(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		String[] colNames = new String[count];
		for (int i = 1; i <= count; i++) {
			colNames[i - 1] = rsmd.getColumnLabel(i).toLowerCase();
		}
		return colNames;
	}

	
}




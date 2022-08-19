/*
 * FILE:            DepartmentDAO.java
 *
 * SW-COMPONENT:    com.ancit.editors
 *
 * DESCRIPTION:     -
 *
 * COPYRIGHT:       © 2015 - 2022 Robert Bosch GmbH
 *
 * The reproduction, distribution and utilization of this file as
 * well as the communication of its contents to others without express
 * authorization is prohibited. Offenders will be held liable for the
 * payment of damages. All rights reserved in the event of the grant
 * of a patent, utility model or design.
 */
package com.ancit.editors.model;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

	private static final List<Department> list = new ArrayList<>();

	static {
		list.add(new Department(10, "D10", "ACCOUNTING", "NEW YORK"));
		list.add(new Department(20, "D20", "RESEARCH", "DALLAS"));
		list.add(new Department(30, "D30", "SALES", "CHICAGO"));
		list.add(new Department(40, "D40", "OPERATIONS", "BOSTON"));
	}

	public static List<Department> listDepartment() {
		return list;
	}

	public static int getMaxDeptId() {
		int max = 0;
		for (Department dept : list) {
			if (dept.getDeptId() > max) {
				max = dept.getDeptId();
			}
		}
		return max;
	}

	public static Department findDepartment(int deptId) {
		for (Department dept : list) {
			if (dept.getDeptId() == deptId) {
				return dept;
			}
		}
		return null;
	}

	public static Department findDepartment(String deptNo) {
		for (Department dept : list) {
			if (dept.getDeptNo().equals(deptNo)) {
				return dept;
			}
		}
		return null;
	}

	public static void deleteDepartment(int deptId) {
		Department dept = findDepartment(deptId);
		if (dept == null) {
			return;
		}
		list.remove(dept);
	}

	public static Department updateDepartment(int deptId, String deptNo, String deptName, String location)
			throws DataException {
		Department dept = findDepartment(deptId);
		if (dept == null) {
			return null;
		}
		Department dept2 = findDepartment(deptNo);
		if (dept2 != null && dept2.getDeptId().intValue() != dept.getDeptId()) {
			throw new DataException("Unique Constraints error - deptNo: " + deptNo);
		}
		dept.setDeptNo(deptNo);
		dept.setDeptName(deptName);
		dept.setLocation(location);
		return dept;
	}

	public static Department insertDepartment(String deptNo, String deptName, String location) throws DataException {
		Department dept = findDepartment(deptNo);
		if (dept != null) {
			throw new DataException("Unique Constraints error - deptNo: " + deptNo);
		}
		dept = new Department();
		int deptId = getMaxDeptId() + 1;
		dept.setDeptId(deptId);
		dept.setDeptNo(deptNo);
		dept.setDeptName(deptName);
		dept.setLocation(location);
		list.add(dept);
		return dept;
	}
}
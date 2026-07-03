/**
 * WorkflowServiceHttpBindingStub.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.weaver.rlww.workflow.webservices;

public class WorkflowServiceHttpBindingStub extends org.apache.axis.client.Stub implements WorkflowServicePortType {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	static org.apache.axis.description.OperationDesc[] _operations;

	static {
		_operations = new org.apache.axis.description.OperationDesc[50];
		_initOperationDesc1();
		_initOperationDesc2();
		_initOperationDesc3();
		_initOperationDesc4();
		_initOperationDesc5();
	}

	private static void _initOperationDesc1() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getWorkflowRequest");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[0] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getHendledWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[1] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getWorkflowRequest4split");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[2] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("submitWorkflowRequest");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"), WorkflowRequestInfo.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[3] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getMyWorkflowRequestCount4OS");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[4] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCCWorkflowRequestList4OS");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[5] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getLeaveDays");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[6] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getHendledWorkflowRequestList4Ofs");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[7] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCreateWorkflowList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowBaseInfo"));
		oper.setReturnClass(WorkflowBaseInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowBaseInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[8] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getProcessedWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[9] = oper;

	}

	private static void _initOperationDesc2() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("doCreateRequest");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"), WorkflowRequestInfo.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[10] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("doCreateWorkflowRequest");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"), WorkflowRequestInfo.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[11] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getToDoWorkflowRequestCount4OS");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[12] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("doForceOver");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[13] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getProcessedWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[14] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCreateWorkflowRequestInfo");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[15] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCreateWorkflowTypeList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowBaseInfo"));
		oper.setReturnClass(WorkflowBaseInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowBaseInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[16] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getHendledWorkflowRequestCount4Ofs");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[17] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getMyWorkflowRequestList4OS");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[18] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getProcessedWorkflowRequestList4OS");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[19] = oper;

	}

	private static void _initOperationDesc3() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getForwardWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[20] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getToDoWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[21] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("givingOpinions");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[22] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCCWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[23] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getProcessedWorkflowRequestCount4OS");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[24] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getBeRejectWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[25] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("forward2WorkflowRequest");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[26] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getAllWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[27] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getToDoWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[28] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCCWorkflowRequestCount4OS");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[29] = oper;

	}

	private static void _initOperationDesc4() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getHendledWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[30] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getToDoWorkflowRequestList4OS");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[31] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getToBeReadWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[32] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCreateWorkflowCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[33] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("forwardWorkflowRequest");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[34] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getToBeReadWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[35] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getBeRejectWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[36] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCCWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[37] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getAllWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[38] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getDoingWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[39] = oper;

	}

	private static void _initOperationDesc5() {
		org.apache.axis.description.OperationDesc oper;
		org.apache.axis.description.ParameterDesc param;
		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getForwardWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[40] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getMyWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[41] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getMyWorkflowRequestCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[42] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getWorkflowNewFlag");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"));
		oper.setReturnClass(String[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[43] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("writeWorkflowReadFlag");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[44] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getCreateWorkflowTypeCount");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		oper.setReturnClass(int.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[45] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getWorkflowRequestLogs");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestLog"));
		oper.setReturnClass(WorkflowRequestLog[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestLog"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[46] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("deleteRequest");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		oper.setReturnClass(boolean.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[47] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getUserId");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
		param.setNillable(true);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[48] = oper;

		oper = new org.apache.axis.description.OperationDesc();
		oper.setName("getDoingWorkflowRequestList");
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString"), String[].class, false, false);
		param.setItemQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string"));
		param.setNillable(true);
		oper.addParameter(param);
		param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "in5"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo"));
		oper.setReturnClass(WorkflowRequestInfo[].class);
		oper.setReturnQName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "out"));
		param = oper.getReturnParamDesc();
		param.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo"));
		oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		_operations[49] = oper;

	}

	public WorkflowServiceHttpBindingStub() throws org.apache.axis.AxisFault {
		this(null);
	}

	public WorkflowServiceHttpBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public WorkflowServiceHttpBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
		Class cls;
		javax.xml.namespace.QName qName;
		javax.xml.namespace.QName qName2;
		Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
		Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
		Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
		Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
		Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
		Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
		Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
		Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
		Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
		Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowBaseInfo");
		cachedSerQNames.add(qName);
		cls = WorkflowBaseInfo[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowBaseInfo");
		qName2 = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowBaseInfo");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowDetailTableInfo");
		cachedSerQNames.add(qName);
		cls = WorkflowDetailTableInfo[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowDetailTableInfo");
		qName2 = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowDetailTableInfo");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestInfo");
		cachedSerQNames.add(qName);
		cls = WorkflowRequestInfo[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo");
		qName2 = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestLog");
		cachedSerQNames.add(qName);
		cls = WorkflowRequestLog[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestLog");
		qName2 = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestLog");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestTableField");
		cachedSerQNames.add(qName);
		cls = WorkflowRequestTableField[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableField");
		qName2 = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableField");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "ArrayOfWorkflowRequestTableRecord");
		cachedSerQNames.add(qName);
		cls = WorkflowRequestTableRecord[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableRecord");
		qName2 = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableRecord");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowBaseInfo");
		cachedSerQNames.add(qName);
		cls = WorkflowBaseInfo.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowDetailTableInfo");
		cachedSerQNames.add(qName);
		cls = WorkflowDetailTableInfo.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowMainTableInfo");
		cachedSerQNames.add(qName);
		cls = WorkflowMainTableInfo.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestInfo");
		cachedSerQNames.add(qName);
		cls = WorkflowRequestInfo.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestLog");
		cachedSerQNames.add(qName);
		cls = WorkflowRequestLog.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableField");
		cachedSerQNames.add(qName);
		cls = WorkflowRequestTableField.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableRecord");
		cachedSerQNames.add(qName);
		cls = WorkflowRequestTableRecord.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfArrayOfString");
		cachedSerQNames.add(qName);
		cls = String[][].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString");
		qName2 = new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

		qName = new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "ArrayOfString");
		cachedSerQNames.add(qName);
		cls = String[].class;
		cachedSerClasses.add(cls);
		qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
		qName2 = new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "string");
		cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

	}

	protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
		try {
			org.apache.axis.client.Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			java.util.Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setEncodingStyle(null);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						Class cls = (Class) cachedSerClasses.get(i);
						javax.xml.namespace.QName qName =
								(javax.xml.namespace.QName) cachedSerQNames.get(i);
						Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							Class sf = (Class)
									cachedSerFactories.get(i);
							Class df = (Class)
									cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
									cachedSerFactories.get(i);
							org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
									cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (Throwable _t) {
			throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public WorkflowRequestInfo getWorkflowRequest(int in0, int in1, int in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getWorkflowRequest");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getWorkflowRequest"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getHendledWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getHendledWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getHendledWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo getWorkflowRequest4Split(int in0, int in1, int in2, int in3) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getWorkflowRequest4split");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getWorkflowRequest4split"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String submitWorkflowRequest(WorkflowRequestInfo in0, int in1, int in2, String in3, String in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[3]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.submitWorkflowRequest");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "submitWorkflowRequest"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{in0, new Integer(in1), new Integer(in2), in3, in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getMyWorkflowRequestCount4OS(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[4]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getMyWorkflowRequestCount4OS");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getMyWorkflowRequestCount4OS"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, new Boolean(in2)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getCCWorkflowRequestList4OS(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[5]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCCWorkflowRequestList4OS");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCCWorkflowRequestList4OS"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4, new Boolean(in5)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String getLeaveDays(String in0, String in1, String in2, String in3, String in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[6]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getLeaveDays");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getLeaveDays"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{in0, in1, in2, in3, in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getHendledWorkflowRequestList4Ofs(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[7]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getHendledWorkflowRequestList4Ofs");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getHendledWorkflowRequestList4Ofs"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4, new Boolean(in5)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowBaseInfo[] getCreateWorkflowList(int in0, int in1, int in2, int in3, int in4, String[] in5) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[8]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCreateWorkflowList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCreateWorkflowList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), new Integer(in4), in5});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowBaseInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowBaseInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowBaseInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getProcessedWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[9]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getProcessedWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getProcessedWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String doCreateRequest(WorkflowRequestInfo in0, int in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[10]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.doCreateRequest");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "doCreateRequest"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{in0, new Integer(in1)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	@Override
	public String doCreateWorkflowRequest(WorkflowRequestInfo in0, int in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[11]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.doCreateWorkflowRequest");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "doCreateWorkflowRequest"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{in0, new Integer(in1)});
			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	@Override
	public int getToDoWorkflowRequestCount4OS(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[12]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getToDoWorkflowRequestCount4OS");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getToDoWorkflowRequestCount4OS"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, new Boolean(in2)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String doForceOver(int in0, int in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[13]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.doForceOver");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "doForceOver"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getProcessedWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[14]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getProcessedWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getProcessedWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo getCreateWorkflowRequestInfo(int in0, int in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[15]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCreateWorkflowRequestInfo");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCreateWorkflowRequestInfo"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowBaseInfo[] getCreateWorkflowTypeList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[16]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCreateWorkflowTypeList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCreateWorkflowTypeList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowBaseInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowBaseInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowBaseInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getHendledWorkflowRequestCount4Ofs(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[17]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getHendledWorkflowRequestCount4Ofs");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getHendledWorkflowRequestCount4Ofs"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, new Boolean(in2)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getMyWorkflowRequestList4OS(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[18]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getMyWorkflowRequestList4OS");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getMyWorkflowRequestList4OS"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4, new Boolean(in5)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getProcessedWorkflowRequestList4OS(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[19]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getProcessedWorkflowRequestList4OS");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getProcessedWorkflowRequestList4OS"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4, new Boolean(in5)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getForwardWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[20]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getForwardWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getForwardWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getToDoWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[21]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getToDoWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getToDoWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String givingOpinions(int in0, int in1, String in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[22]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.givingOpinions");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "givingOpinions"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), in2});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getCCWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[23]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCCWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCCWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getProcessedWorkflowRequestCount4OS(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[24]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getProcessedWorkflowRequestCount4OS");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getProcessedWorkflowRequestCount4OS"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, new Boolean(in2)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getBeRejectWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[25]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getBeRejectWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getBeRejectWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String forward2WorkflowRequest(int in0, String in1, String in2, int in3, String in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[26]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.forward2WorkflowRequest");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "forward2WorkflowRequest"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, in2, new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getAllWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[27]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getAllWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getAllWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getToDoWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[28]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getToDoWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getToDoWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getCCWorkflowRequestCount4OS(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[29]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCCWorkflowRequestCount4OS");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCCWorkflowRequestCount4OS"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, new Boolean(in2)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getHendledWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[30]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getHendledWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getHendledWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getToDoWorkflowRequestList4OS(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[31]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getToDoWorkflowRequestList4OS");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getToDoWorkflowRequestList4OS"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4, new Boolean(in5)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getToBeReadWorkflowRequestCount(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[32]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getToBeReadWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getToBeReadWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, new Boolean(in2)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getCreateWorkflowCount(int in0, int in1, String[] in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[33]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCreateWorkflowCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCreateWorkflowCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), in2});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String forwardWorkflowRequest(int in0, String in1, String in2, int in3, String in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[34]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.forwardWorkflowRequest");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "forwardWorkflowRequest"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, in2, new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getToBeReadWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[35]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getToBeReadWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getToBeReadWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4, new Boolean(in5)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getBeRejectWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[36]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getBeRejectWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getBeRejectWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getCCWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[37]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCCWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCCWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getAllWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[38]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getAllWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getAllWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getDoingWorkflowRequestCount(int in0, String[] in1, boolean in2) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[39]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getDoingWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getDoingWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1, new Boolean(in2)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getForwardWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[40]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getForwardWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getForwardWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getMyWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[41]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getMyWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getMyWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getMyWorkflowRequestCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[42]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getMyWorkflowRequestCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getMyWorkflowRequestCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String[] getWorkflowNewFlag(String[] in0, String in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[43]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getWorkflowNewFlag");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getWorkflowNewFlag"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{in0, in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String[]) _resp;
				} catch (Exception _exception) {
					return (String[]) org.apache.axis.utils.JavaUtils.convert(_resp, String[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public void writeWorkflowReadFlag(String in0, String in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[44]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.writeWorkflowReadFlag");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "writeWorkflowReadFlag"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{in0, in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			}
			extractAttachments(_call);
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public int getCreateWorkflowTypeCount(int in0, String[] in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[45]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getCreateWorkflowTypeCount");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getCreateWorkflowTypeCount"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Integer) _resp).intValue();
				} catch (Exception _exception) {
					return ((Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestLog[] getWorkflowRequestLogs(String in0, String in1, int in2, int in3, int in4) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[46]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getWorkflowRequestLogs");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getWorkflowRequestLogs"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{in0, in1, new Integer(in2), new Integer(in3), new Integer(in4)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestLog[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestLog[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestLog[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public boolean deleteRequest(int in0, int in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[47]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.deleteRequest");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "deleteRequest"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return ((Boolean) _resp).booleanValue();
				} catch (Exception _exception) {
					return ((Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public String getUserId(String in0, String in1) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[48]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getUserid");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getUserId"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{in0, in1});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) org.apache.axis.utils.JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	public WorkflowRequestInfo[] getDoingWorkflowRequestList(int in0, int in1, int in2, int in3, String[] in4, boolean in5) throws java.rmi.RemoteException {
		if (super.cachedEndpoint == null) {
			throw new org.apache.axis.NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[49]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("urn:WorkflowService.getDoingWorkflowRequestList");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new javax.xml.namespace.QName("webservices.services.weaver.com.cn", "getDoingWorkflowRequestList"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[]{new Integer(in0), new Integer(in1), new Integer(in2), new Integer(in3), in4, new Boolean(in5)});

			if (_resp instanceof java.rmi.RemoteException) {
				throw (java.rmi.RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (WorkflowRequestInfo[]) _resp;
				} catch (Exception _exception) {
					return (WorkflowRequestInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, WorkflowRequestInfo[].class);
				}
			}
		} catch (org.apache.axis.AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

}

/**
 * WorkflowMainTableInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.weaver.rlww.workflow.webservices;

public class WorkflowMainTableInfo  implements java.io.Serializable {
    private WorkflowRequestTableRecord[] requestRecords;

    private String tableDBName;

    public WorkflowMainTableInfo() {
    }

    public WorkflowMainTableInfo(
           WorkflowRequestTableRecord[] requestRecords,
           String tableDBName) {
           this.requestRecords = requestRecords;
           this.tableDBName = tableDBName;
    }


    /**
     * Gets the requestRecords value for this WorkflowMainTableInfo.
     * 
     * @return requestRecords
     */
    public WorkflowRequestTableRecord[] getRequestRecords() {
        return requestRecords;
    }


    /**
     * Sets the requestRecords value for this WorkflowMainTableInfo.
     * 
     * @param requestRecords
     */
    public void setRequestRecords(WorkflowRequestTableRecord[] requestRecords) {
        this.requestRecords = requestRecords;
    }


    /**
     * Gets the tableDBName value for this WorkflowMainTableInfo.
     * 
     * @return tableDBName
     */
    public String getTableDBName() {
        return tableDBName;
    }


    /**
     * Sets the tableDBName value for this WorkflowMainTableInfo.
     * 
     * @param tableDBName
     */
    public void setTableDBName(String tableDBName) {
        this.tableDBName = tableDBName;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof WorkflowMainTableInfo)) return false;
        WorkflowMainTableInfo other = (WorkflowMainTableInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.requestRecords==null && other.getRequestRecords()==null) || 
             (this.requestRecords!=null &&
              java.util.Arrays.equals(this.requestRecords, other.getRequestRecords()))) &&
            ((this.tableDBName==null && other.getTableDBName()==null) || 
             (this.tableDBName!=null &&
              this.tableDBName.equals(other.getTableDBName())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRequestRecords() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRequestRecords());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getRequestRecords(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTableDBName() != null) {
            _hashCode += getTableDBName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WorkflowMainTableInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowMainTableInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestRecords");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "requestRecords"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableRecord"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableRecord"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tableDBName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "tableDBName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

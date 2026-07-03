/**
 * AsyncReceivePackage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.jem.service_client.sj;

public class AsyncReceivePackage  implements java.io.Serializable {
    private AsyncReceivePackageBody body;

    private AsyncReceivePackageHeader header;

    public AsyncReceivePackage() {
    }

    public AsyncReceivePackage(
           AsyncReceivePackageBody body,
           AsyncReceivePackageHeader header) {
           this.body = body;
           this.header = header;
    }


    /**
     * Gets the body value for this AsyncReceivePackage.
     * 
     * @return body
     */
    public AsyncReceivePackageBody getBody() {
        return body;
    }


    /**
     * Sets the body value for this AsyncReceivePackage.
     * 
     * @param body
     */
    public void setBody(AsyncReceivePackageBody body) {
        this.body = body;
    }


    /**
     * Gets the header value for this AsyncReceivePackage.
     * 
     * @return header
     */
    public AsyncReceivePackageHeader getHeader() {
        return header;
    }


    /**
     * Sets the header value for this AsyncReceivePackage.
     * 
     * @param header
     */
    public void setHeader(AsyncReceivePackageHeader header) {
        this.header = header;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof AsyncReceivePackage)) return false;
        AsyncReceivePackage other = (AsyncReceivePackage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.body==null && other.getBody()==null) || 
             (this.body!=null &&
              this.body.equals(other.getBody()))) &&
            ((this.header==null && other.getHeader()==null) || 
             (this.header!=null &&
              this.header.equals(other.getHeader())));
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
        if (getBody() != null) {
            _hashCode += getBody().hashCode();
        }
        if (getHeader() != null) {
            _hashCode += getHeader().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AsyncReceivePackage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "AsyncReceivePackage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("body");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "body"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "AsyncReceivePackageBody"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("header");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "header"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "AsyncReceivePackageHeader"));
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

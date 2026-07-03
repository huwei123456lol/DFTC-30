/**
 * SendPackageBody.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.jem.service_client.sj;

public class SendPackageBody  implements java.io.Serializable {
    private byte[] content;

    private byte[] desKey;

    private byte[] securityMessage;

    public SendPackageBody() {
    }

    public SendPackageBody(
           byte[] content,
           byte[] desKey,
           byte[] securityMessage) {
           this.content = content;
           this.desKey = desKey;
           this.securityMessage = securityMessage;
    }


    /**
     * Gets the content value for this SendPackageBody.
     * 
     * @return content
     */
    public byte[] getContent() {
        return content;
    }


    /**
     * Sets the content value for this SendPackageBody.
     * 
     * @param content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }


    /**
     * Gets the desKey value for this SendPackageBody.
     * 
     * @return desKey
     */
    public byte[] getDesKey() {
        return desKey;
    }


    /**
     * Sets the desKey value for this SendPackageBody.
     * 
     * @param desKey
     */
    public void setDesKey(byte[] desKey) {
        this.desKey = desKey;
    }


    /**
     * Gets the securityMessage value for this SendPackageBody.
     * 
     * @return securityMessage
     */
    public byte[] getSecurityMessage() {
        return securityMessage;
    }


    /**
     * Sets the securityMessage value for this SendPackageBody.
     * 
     * @param securityMessage
     */
    public void setSecurityMessage(byte[] securityMessage) {
        this.securityMessage = securityMessage;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SendPackageBody)) return false;
        SendPackageBody other = (SendPackageBody) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.content==null && other.getContent()==null) || 
             (this.content!=null &&
              java.util.Arrays.equals(this.content, other.getContent()))) &&
            ((this.desKey==null && other.getDesKey()==null) || 
             (this.desKey!=null &&
              java.util.Arrays.equals(this.desKey, other.getDesKey()))) &&
            ((this.securityMessage==null && other.getSecurityMessage()==null) || 
             (this.securityMessage!=null &&
              java.util.Arrays.equals(this.securityMessage, other.getSecurityMessage())));
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
        if (getContent() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContent());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getContent(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDesKey() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDesKey());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getDesKey(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSecurityMessage() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSecurityMessage());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getSecurityMessage(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SendPackageBody.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "SendPackageBody"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "desKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "securityMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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

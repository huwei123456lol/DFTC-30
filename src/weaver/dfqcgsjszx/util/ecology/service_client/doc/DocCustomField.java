/**
 * DocCustomField.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.ecology.service_client.doc;

public class DocCustomField  implements java.io.Serializable {
    private String fielddbtype;

    private String fieldhtmltype;

    private Integer fieldid;

    private String fieldshow;

    private Integer fieldtype;

    private String fieldvalue;

    public DocCustomField() {
    }

    public DocCustomField(
           String fielddbtype,
           String fieldhtmltype,
           Integer fieldid,
           String fieldshow,
           Integer fieldtype,
           String fieldvalue) {
           this.fielddbtype = fielddbtype;
           this.fieldhtmltype = fieldhtmltype;
           this.fieldid = fieldid;
           this.fieldshow = fieldshow;
           this.fieldtype = fieldtype;
           this.fieldvalue = fieldvalue;
    }


    /**
     * Gets the fielddbtype value for this DocCustomField.
     *
     * @return fielddbtype
     */
    public String getFielddbtype() {
        return fielddbtype;
    }


    /**
     * Sets the fielddbtype value for this DocCustomField.
     *
     * @param fielddbtype
     */
    public void setFielddbtype(String fielddbtype) {
        this.fielddbtype = fielddbtype;
    }


    /**
     * Gets the fieldhtmltype value for this DocCustomField.
     *
     * @return fieldhtmltype
     */
    public String getFieldhtmltype() {
        return fieldhtmltype;
    }


    /**
     * Sets the fieldhtmltype value for this DocCustomField.
     *
     * @param fieldhtmltype
     */
    public void setFieldhtmltype(String fieldhtmltype) {
        this.fieldhtmltype = fieldhtmltype;
    }


    /**
     * Gets the fieldid value for this DocCustomField.
     *
     * @return fieldid
     */
    public Integer getFieldid() {
        return fieldid;
    }


    /**
     * Sets the fieldid value for this DocCustomField.
     *
     * @param fieldid
     */
    public void setFieldid(Integer fieldid) {
        this.fieldid = fieldid;
    }


    /**
     * Gets the fieldshow value for this DocCustomField.
     *
     * @return fieldshow
     */
    public String getFieldshow() {
        return fieldshow;
    }


    /**
     * Sets the fieldshow value for this DocCustomField.
     *
     * @param fieldshow
     */
    public void setFieldshow(String fieldshow) {
        this.fieldshow = fieldshow;
    }


    /**
     * Gets the fieldtype value for this DocCustomField.
     *
     * @return fieldtype
     */
    public Integer getFieldtype() {
        return fieldtype;
    }


    /**
     * Sets the fieldtype value for this DocCustomField.
     *
     * @param fieldtype
     */
    public void setFieldtype(Integer fieldtype) {
        this.fieldtype = fieldtype;
    }


    /**
     * Gets the fieldvalue value for this DocCustomField.
     *
     * @return fieldvalue
     */
    public String getFieldvalue() {
        return fieldvalue;
    }


    /**
     * Sets the fieldvalue value for this DocCustomField.
     *
     * @param fieldvalue
     */
    public void setFieldvalue(String fieldvalue) {
        this.fieldvalue = fieldvalue;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof DocCustomField)) return false;
        DocCustomField other = (DocCustomField) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&
            ((this.fielddbtype==null && other.getFielddbtype()==null) ||
             (this.fielddbtype!=null &&
              this.fielddbtype.equals(other.getFielddbtype()))) &&
            ((this.fieldhtmltype==null && other.getFieldhtmltype()==null) ||
             (this.fieldhtmltype!=null &&
              this.fieldhtmltype.equals(other.getFieldhtmltype()))) &&
            ((this.fieldid==null && other.getFieldid()==null) ||
             (this.fieldid!=null &&
              this.fieldid.equals(other.getFieldid()))) &&
            ((this.fieldshow==null && other.getFieldshow()==null) ||
             (this.fieldshow!=null &&
              this.fieldshow.equals(other.getFieldshow()))) &&
            ((this.fieldtype==null && other.getFieldtype()==null) ||
             (this.fieldtype!=null &&
              this.fieldtype.equals(other.getFieldtype()))) &&
            ((this.fieldvalue==null && other.getFieldvalue()==null) ||
             (this.fieldvalue!=null &&
              this.fieldvalue.equals(other.getFieldvalue())));
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
        if (getFielddbtype() != null) {
            _hashCode += getFielddbtype().hashCode();
        }
        if (getFieldhtmltype() != null) {
            _hashCode += getFieldhtmltype().hashCode();
        }
        if (getFieldid() != null) {
            _hashCode += getFieldid().hashCode();
        }
        if (getFieldshow() != null) {
            _hashCode += getFieldshow().hashCode();
        }
        if (getFieldtype() != null) {
            _hashCode += getFieldtype().hashCode();
        }
        if (getFieldvalue() != null) {
            _hashCode += getFieldvalue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocCustomField.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.docs.weaver", "DocCustomField"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fielddbtype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.docs.weaver", "fielddbtype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldhtmltype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.docs.weaver", "fieldhtmltype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.docs.weaver", "fieldid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldshow");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.docs.weaver", "fieldshow"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldtype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.docs.weaver", "fieldtype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldvalue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.docs.weaver", "fieldvalue"));
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

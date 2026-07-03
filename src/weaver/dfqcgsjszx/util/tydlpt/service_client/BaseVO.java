/**
 * BaseVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.tydlpt.service_client;

public abstract class BaseVO  extends BaseSQLData  implements java.io.Serializable {
    private String imageStyle;

    private String rowStyle;

    private boolean selecttag;

    public BaseVO() {
    }

    public BaseVO(
           String imageStyle,
           String rowStyle,
           boolean selecttag) {
        this.imageStyle = imageStyle;
        this.rowStyle = rowStyle;
        this.selecttag = selecttag;
    }


    /**
     * Gets the imageStyle value for this BaseVO.
     * 
     * @return imageStyle
     */
    public String getImageStyle() {
        return imageStyle;
    }


    /**
     * Sets the imageStyle value for this BaseVO.
     * 
     * @param imageStyle
     */
    public void setImageStyle(String imageStyle) {
        this.imageStyle = imageStyle;
    }


    /**
     * Gets the rowStyle value for this BaseVO.
     * 
     * @return rowStyle
     */
    public String getRowStyle() {
        return rowStyle;
    }


    /**
     * Sets the rowStyle value for this BaseVO.
     * 
     * @param rowStyle
     */
    public void setRowStyle(String rowStyle) {
        this.rowStyle = rowStyle;
    }


    /**
     * Gets the selecttag value for this BaseVO.
     * 
     * @return selecttag
     */
    public boolean isSelecttag() {
        return selecttag;
    }


    /**
     * Sets the selecttag value for this BaseVO.
     * 
     * @param selecttag
     */
    public void setSelecttag(boolean selecttag) {
        this.selecttag = selecttag;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof BaseVO)) return false;
        BaseVO other = (BaseVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.imageStyle==null && other.getImageStyle()==null) || 
             (this.imageStyle!=null &&
              this.imageStyle.equals(other.getImageStyle()))) &&
            ((this.rowStyle==null && other.getRowStyle()==null) || 
             (this.rowStyle!=null &&
              this.rowStyle.equals(other.getRowStyle()))) &&
            this.selecttag == other.isSelecttag();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getImageStyle() != null) {
            _hashCode += getImageStyle().hashCode();
        }
        if (getRowStyle() != null) {
            _hashCode += getRowStyle().hashCode();
        }
        _hashCode += (isSelecttag() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BaseVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.ws.model.common.dawnpro.com/", "baseVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageStyle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "imageStyle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rowStyle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rowStyle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selecttag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "selecttag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
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

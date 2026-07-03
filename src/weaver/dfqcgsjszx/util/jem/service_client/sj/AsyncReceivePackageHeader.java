/**
 * AsyncReceivePackageHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.jem.service_client.sj;

public class AsyncReceivePackageHeader  implements java.io.Serializable {
    private String calledID;

    private String compressMode;

    private String contentEncode;

    private String contentFormat;

    private Integer contentLength;

    private String endTime;

    private Integer retCode;

    private String systemCalledId;

    private String systemReceivedId;

    private Integer version;

    public AsyncReceivePackageHeader() {
    }

    public AsyncReceivePackageHeader(
           String calledID,
           String compressMode,
           String contentEncode,
           String contentFormat,
           Integer contentLength,
           String endTime,
           Integer retCode,
           String systemCalledId,
           String systemReceivedId,
           Integer version) {
           this.calledID = calledID;
           this.compressMode = compressMode;
           this.contentEncode = contentEncode;
           this.contentFormat = contentFormat;
           this.contentLength = contentLength;
           this.endTime = endTime;
           this.retCode = retCode;
           this.systemCalledId = systemCalledId;
           this.systemReceivedId = systemReceivedId;
           this.version = version;
    }


    /**
     * Gets the calledID value for this AsyncReceivePackageHeader.
     * 
     * @return calledID
     */
    public String getCalledID() {
        return calledID;
    }


    /**
     * Sets the calledID value for this AsyncReceivePackageHeader.
     * 
     * @param calledID
     */
    public void setCalledID(String calledID) {
        this.calledID = calledID;
    }


    /**
     * Gets the compressMode value for this AsyncReceivePackageHeader.
     * 
     * @return compressMode
     */
    public String getCompressMode() {
        return compressMode;
    }


    /**
     * Sets the compressMode value for this AsyncReceivePackageHeader.
     * 
     * @param compressMode
     */
    public void setCompressMode(String compressMode) {
        this.compressMode = compressMode;
    }


    /**
     * Gets the contentEncode value for this AsyncReceivePackageHeader.
     * 
     * @return contentEncode
     */
    public String getContentEncode() {
        return contentEncode;
    }


    /**
     * Sets the contentEncode value for this AsyncReceivePackageHeader.
     * 
     * @param contentEncode
     */
    public void setContentEncode(String contentEncode) {
        this.contentEncode = contentEncode;
    }


    /**
     * Gets the contentFormat value for this AsyncReceivePackageHeader.
     * 
     * @return contentFormat
     */
    public String getContentFormat() {
        return contentFormat;
    }


    /**
     * Sets the contentFormat value for this AsyncReceivePackageHeader.
     * 
     * @param contentFormat
     */
    public void setContentFormat(String contentFormat) {
        this.contentFormat = contentFormat;
    }


    /**
     * Gets the contentLength value for this AsyncReceivePackageHeader.
     * 
     * @return contentLength
     */
    public Integer getContentLength() {
        return contentLength;
    }


    /**
     * Sets the contentLength value for this AsyncReceivePackageHeader.
     * 
     * @param contentLength
     */
    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }


    /**
     * Gets the endTime value for this AsyncReceivePackageHeader.
     * 
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }


    /**
     * Sets the endTime value for this AsyncReceivePackageHeader.
     * 
     * @param endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    /**
     * Gets the retCode value for this AsyncReceivePackageHeader.
     * 
     * @return retCode
     */
    public Integer getRetCode() {
        return retCode;
    }


    /**
     * Sets the retCode value for this AsyncReceivePackageHeader.
     * 
     * @param retCode
     */
    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }


    /**
     * Gets the systemCalledId value for this AsyncReceivePackageHeader.
     * 
     * @return systemCalledId
     */
    public String getSystemCalledId() {
        return systemCalledId;
    }


    /**
     * Sets the systemCalledId value for this AsyncReceivePackageHeader.
     * 
     * @param systemCalledId
     */
    public void setSystemCalledId(String systemCalledId) {
        this.systemCalledId = systemCalledId;
    }


    /**
     * Gets the systemReceivedId value for this AsyncReceivePackageHeader.
     * 
     * @return systemReceivedId
     */
    public String getSystemReceivedId() {
        return systemReceivedId;
    }


    /**
     * Sets the systemReceivedId value for this AsyncReceivePackageHeader.
     * 
     * @param systemReceivedId
     */
    public void setSystemReceivedId(String systemReceivedId) {
        this.systemReceivedId = systemReceivedId;
    }


    /**
     * Gets the version value for this AsyncReceivePackageHeader.
     * 
     * @return version
     */
    public Integer getVersion() {
        return version;
    }


    /**
     * Sets the version value for this AsyncReceivePackageHeader.
     * 
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof AsyncReceivePackageHeader)) return false;
        AsyncReceivePackageHeader other = (AsyncReceivePackageHeader) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.calledID==null && other.getCalledID()==null) || 
             (this.calledID!=null &&
              this.calledID.equals(other.getCalledID()))) &&
            ((this.compressMode==null && other.getCompressMode()==null) || 
             (this.compressMode!=null &&
              this.compressMode.equals(other.getCompressMode()))) &&
            ((this.contentEncode==null && other.getContentEncode()==null) || 
             (this.contentEncode!=null &&
              this.contentEncode.equals(other.getContentEncode()))) &&
            ((this.contentFormat==null && other.getContentFormat()==null) || 
             (this.contentFormat!=null &&
              this.contentFormat.equals(other.getContentFormat()))) &&
            ((this.contentLength==null && other.getContentLength()==null) || 
             (this.contentLength!=null &&
              this.contentLength.equals(other.getContentLength()))) &&
            ((this.endTime==null && other.getEndTime()==null) || 
             (this.endTime!=null &&
              this.endTime.equals(other.getEndTime()))) &&
            ((this.retCode==null && other.getRetCode()==null) || 
             (this.retCode!=null &&
              this.retCode.equals(other.getRetCode()))) &&
            ((this.systemCalledId==null && other.getSystemCalledId()==null) || 
             (this.systemCalledId!=null &&
              this.systemCalledId.equals(other.getSystemCalledId()))) &&
            ((this.systemReceivedId==null && other.getSystemReceivedId()==null) || 
             (this.systemReceivedId!=null &&
              this.systemReceivedId.equals(other.getSystemReceivedId()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion())));
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
        if (getCalledID() != null) {
            _hashCode += getCalledID().hashCode();
        }
        if (getCompressMode() != null) {
            _hashCode += getCompressMode().hashCode();
        }
        if (getContentEncode() != null) {
            _hashCode += getContentEncode().hashCode();
        }
        if (getContentFormat() != null) {
            _hashCode += getContentFormat().hashCode();
        }
        if (getContentLength() != null) {
            _hashCode += getContentLength().hashCode();
        }
        if (getEndTime() != null) {
            _hashCode += getEndTime().hashCode();
        }
        if (getRetCode() != null) {
            _hashCode += getRetCode().hashCode();
        }
        if (getSystemCalledId() != null) {
            _hashCode += getSystemCalledId().hashCode();
        }
        if (getSystemReceivedId() != null) {
            _hashCode += getSystemReceivedId().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AsyncReceivePackageHeader.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "AsyncReceivePackageHeader"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("calledID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "calledID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("compressMode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "compressMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentEncode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "contentEncode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "contentFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentLength");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "contentLength"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "endTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "retCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("systemCalledId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "systemCalledId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("systemReceivedId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "systemReceivedId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
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

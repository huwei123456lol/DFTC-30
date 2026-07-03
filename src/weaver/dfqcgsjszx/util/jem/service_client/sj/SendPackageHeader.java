/**
 * SendPackageHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package weaver.dfqcgsjszx.util.jem.service_client.sj;

public class SendPackageHeader  implements java.io.Serializable {
    private String callMode;

    private String compressMode;

    private String contentEncode;

    private String contentFormat;

    private Integer contentLength;

    private Integer currentPackageNumber;

    private Integer desKeyLength;

    private String encryptMode;

    private Integer packageCount;

    private Long reserve;

    private Integer resultMode;

    private Integer securityMsgLength;

    private String seurityPolicy;

    private String startTime;

    private String systemCalledId;

    private String systemReceivedId;

    private Integer version;

    public SendPackageHeader() {
    }

    public SendPackageHeader(
           String callMode,
           String compressMode,
           String contentEncode,
           String contentFormat,
           Integer contentLength,
           Integer currentPackageNumber,
           Integer desKeyLength,
           String encryptMode,
           Integer packageCount,
           Long reserve,
           Integer resultMode,
           Integer securityMsgLength,
           String seurityPolicy,
           String startTime,
           String systemCalledId,
           String systemReceivedId,
           Integer version) {
           this.callMode = callMode;
           this.compressMode = compressMode;
           this.contentEncode = contentEncode;
           this.contentFormat = contentFormat;
           this.contentLength = contentLength;
           this.currentPackageNumber = currentPackageNumber;
           this.desKeyLength = desKeyLength;
           this.encryptMode = encryptMode;
           this.packageCount = packageCount;
           this.reserve = reserve;
           this.resultMode = resultMode;
           this.securityMsgLength = securityMsgLength;
           this.seurityPolicy = seurityPolicy;
           this.startTime = startTime;
           this.systemCalledId = systemCalledId;
           this.systemReceivedId = systemReceivedId;
           this.version = version;
    }


    /**
     * Gets the callMode value for this SendPackageHeader.
     * 
     * @return callMode
     */
    public String getCallMode() {
        return callMode;
    }


    /**
     * Sets the callMode value for this SendPackageHeader.
     * 
     * @param callMode
     */
    public void setCallMode(String callMode) {
        this.callMode = callMode;
    }


    /**
     * Gets the compressMode value for this SendPackageHeader.
     * 
     * @return compressMode
     */
    public String getCompressMode() {
        return compressMode;
    }


    /**
     * Sets the compressMode value for this SendPackageHeader.
     * 
     * @param compressMode
     */
    public void setCompressMode(String compressMode) {
        this.compressMode = compressMode;
    }


    /**
     * Gets the contentEncode value for this SendPackageHeader.
     * 
     * @return contentEncode
     */
    public String getContentEncode() {
        return contentEncode;
    }


    /**
     * Sets the contentEncode value for this SendPackageHeader.
     * 
     * @param contentEncode
     */
    public void setContentEncode(String contentEncode) {
        this.contentEncode = contentEncode;
    }


    /**
     * Gets the contentFormat value for this SendPackageHeader.
     * 
     * @return contentFormat
     */
    public String getContentFormat() {
        return contentFormat;
    }


    /**
     * Sets the contentFormat value for this SendPackageHeader.
     * 
     * @param contentFormat
     */
    public void setContentFormat(String contentFormat) {
        this.contentFormat = contentFormat;
    }


    /**
     * Gets the contentLength value for this SendPackageHeader.
     * 
     * @return contentLength
     */
    public Integer getContentLength() {
        return contentLength;
    }


    /**
     * Sets the contentLength value for this SendPackageHeader.
     * 
     * @param contentLength
     */
    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }


    /**
     * Gets the currentPackageNumber value for this SendPackageHeader.
     * 
     * @return currentPackageNumber
     */
    public Integer getCurrentPackageNumber() {
        return currentPackageNumber;
    }


    /**
     * Sets the currentPackageNumber value for this SendPackageHeader.
     * 
     * @param currentPackageNumber
     */
    public void setCurrentPackageNumber(Integer currentPackageNumber) {
        this.currentPackageNumber = currentPackageNumber;
    }


    /**
     * Gets the desKeyLength value for this SendPackageHeader.
     * 
     * @return desKeyLength
     */
    public Integer getDesKeyLength() {
        return desKeyLength;
    }


    /**
     * Sets the desKeyLength value for this SendPackageHeader.
     * 
     * @param desKeyLength
     */
    public void setDesKeyLength(Integer desKeyLength) {
        this.desKeyLength = desKeyLength;
    }


    /**
     * Gets the encryptMode value for this SendPackageHeader.
     * 
     * @return encryptMode
     */
    public String getEncryptMode() {
        return encryptMode;
    }


    /**
     * Sets the encryptMode value for this SendPackageHeader.
     * 
     * @param encryptMode
     */
    public void setEncryptMode(String encryptMode) {
        this.encryptMode = encryptMode;
    }


    /**
     * Gets the packageCount value for this SendPackageHeader.
     * 
     * @return packageCount
     */
    public Integer getPackageCount() {
        return packageCount;
    }


    /**
     * Sets the packageCount value for this SendPackageHeader.
     * 
     * @param packageCount
     */
    public void setPackageCount(Integer packageCount) {
        this.packageCount = packageCount;
    }


    /**
     * Gets the reserve value for this SendPackageHeader.
     * 
     * @return reserve
     */
    public Long getReserve() {
        return reserve;
    }


    /**
     * Sets the reserve value for this SendPackageHeader.
     * 
     * @param reserve
     */
    public void setReserve(Long reserve) {
        this.reserve = reserve;
    }


    /**
     * Gets the resultMode value for this SendPackageHeader.
     * 
     * @return resultMode
     */
    public Integer getResultMode() {
        return resultMode;
    }


    /**
     * Sets the resultMode value for this SendPackageHeader.
     * 
     * @param resultMode
     */
    public void setResultMode(Integer resultMode) {
        this.resultMode = resultMode;
    }


    /**
     * Gets the securityMsgLength value for this SendPackageHeader.
     * 
     * @return securityMsgLength
     */
    public Integer getSecurityMsgLength() {
        return securityMsgLength;
    }


    /**
     * Sets the securityMsgLength value for this SendPackageHeader.
     * 
     * @param securityMsgLength
     */
    public void setSecurityMsgLength(Integer securityMsgLength) {
        this.securityMsgLength = securityMsgLength;
    }


    /**
     * Gets the seurityPolicy value for this SendPackageHeader.
     * 
     * @return seurityPolicy
     */
    public String getSeurityPolicy() {
        return seurityPolicy;
    }


    /**
     * Sets the seurityPolicy value for this SendPackageHeader.
     * 
     * @param seurityPolicy
     */
    public void setSeurityPolicy(String seurityPolicy) {
        this.seurityPolicy = seurityPolicy;
    }


    /**
     * Gets the startTime value for this SendPackageHeader.
     * 
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this SendPackageHeader.
     * 
     * @param startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the systemCalledId value for this SendPackageHeader.
     * 
     * @return systemCalledId
     */
    public String getSystemCalledId() {
        return systemCalledId;
    }


    /**
     * Sets the systemCalledId value for this SendPackageHeader.
     * 
     * @param systemCalledId
     */
    public void setSystemCalledId(String systemCalledId) {
        this.systemCalledId = systemCalledId;
    }


    /**
     * Gets the systemReceivedId value for this SendPackageHeader.
     * 
     * @return systemReceivedId
     */
    public String getSystemReceivedId() {
        return systemReceivedId;
    }


    /**
     * Sets the systemReceivedId value for this SendPackageHeader.
     * 
     * @param systemReceivedId
     */
    public void setSystemReceivedId(String systemReceivedId) {
        this.systemReceivedId = systemReceivedId;
    }


    /**
     * Gets the version value for this SendPackageHeader.
     * 
     * @return version
     */
    public Integer getVersion() {
        return version;
    }


    /**
     * Sets the version value for this SendPackageHeader.
     * 
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SendPackageHeader)) return false;
        SendPackageHeader other = (SendPackageHeader) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.callMode==null && other.getCallMode()==null) || 
             (this.callMode!=null &&
              this.callMode.equals(other.getCallMode()))) &&
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
            ((this.currentPackageNumber==null && other.getCurrentPackageNumber()==null) || 
             (this.currentPackageNumber!=null &&
              this.currentPackageNumber.equals(other.getCurrentPackageNumber()))) &&
            ((this.desKeyLength==null && other.getDesKeyLength()==null) || 
             (this.desKeyLength!=null &&
              this.desKeyLength.equals(other.getDesKeyLength()))) &&
            ((this.encryptMode==null && other.getEncryptMode()==null) || 
             (this.encryptMode!=null &&
              this.encryptMode.equals(other.getEncryptMode()))) &&
            ((this.packageCount==null && other.getPackageCount()==null) || 
             (this.packageCount!=null &&
              this.packageCount.equals(other.getPackageCount()))) &&
            ((this.reserve==null && other.getReserve()==null) || 
             (this.reserve!=null &&
              this.reserve.equals(other.getReserve()))) &&
            ((this.resultMode==null && other.getResultMode()==null) || 
             (this.resultMode!=null &&
              this.resultMode.equals(other.getResultMode()))) &&
            ((this.securityMsgLength==null && other.getSecurityMsgLength()==null) || 
             (this.securityMsgLength!=null &&
              this.securityMsgLength.equals(other.getSecurityMsgLength()))) &&
            ((this.seurityPolicy==null && other.getSeurityPolicy()==null) || 
             (this.seurityPolicy!=null &&
              this.seurityPolicy.equals(other.getSeurityPolicy()))) &&
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
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
        if (getCallMode() != null) {
            _hashCode += getCallMode().hashCode();
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
        if (getCurrentPackageNumber() != null) {
            _hashCode += getCurrentPackageNumber().hashCode();
        }
        if (getDesKeyLength() != null) {
            _hashCode += getDesKeyLength().hashCode();
        }
        if (getEncryptMode() != null) {
            _hashCode += getEncryptMode().hashCode();
        }
        if (getPackageCount() != null) {
            _hashCode += getPackageCount().hashCode();
        }
        if (getReserve() != null) {
            _hashCode += getReserve().hashCode();
        }
        if (getResultMode() != null) {
            _hashCode += getResultMode().hashCode();
        }
        if (getSecurityMsgLength() != null) {
            _hashCode += getSecurityMsgLength().hashCode();
        }
        if (getSeurityPolicy() != null) {
            _hashCode += getSeurityPolicy().hashCode();
        }
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
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
        new org.apache.axis.description.TypeDesc(SendPackageHeader.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "SendPackageHeader"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callMode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "callMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
        elemField.setFieldName("currentPackageNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "currentPackageNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desKeyLength");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "desKeyLength"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptMode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "encryptMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("packageCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "packageCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reserve");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "reserve"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultMode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "resultMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityMsgLength");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "securityMsgLength"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("seurityPolicy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "seurityPolicy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://define.connector.rd.dawnpro.com", "startTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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

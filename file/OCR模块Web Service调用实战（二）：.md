## OCR模块Web Service调用实战（二）：

### 字符串与POJO的映射模型

#### 模型一：

```java
@Data
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CTOSRESULT")
@XmlType(name = "CTOSRESULT", propOrder = {"returnInfo", "dataTable"})
public class RegisterResult {

    @XmlElement(name = "RETURNINFO", required = true)
    private ReturnInfo returnInfo;
    @XmlElement(name = "SM001001", required = true)
    private DataTable dataTable;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "SM001001", propOrder = {"ticketId"})
    public static class DataTable{
        @XmlElement(name = "TICKET_ID", required = true)
        private String ticketId;
    }
}

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RETURNINFO", propOrder = {"errorCode", "errorMsg"})
public class ReturnInfo {
    @XmlElement(name = "ERRORCODE", required = true)
    public String errorCode;
    @XmlElement(name = "ERRORMSG", required = true)
    public String errorMsg;
}
```

```java
public void pojoStrToPojo(String pojoStr){
    RegisterResult registerResult = null;
    try{
        StringReader reader = new StringReader(pojoStr);
        JAXBContext context = JAXBContext.newInstance(RegisterResult.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        registerResult = (RegisterResult) unmarshaller.unmarshal(reader);
    }catch (Exception e){
        e.printStackTrace();
    }
    System.out.println(registerResult);
}
```


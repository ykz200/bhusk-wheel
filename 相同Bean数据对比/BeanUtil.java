import	java.util.HashMap;


import SetFieldAttribute;
import UserBean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * 本工具类目的是针对两个Bean内容做比较
 * 效果是为了日志记录方便
 * 例如日志内容 "参数名:xxx, 变动前:xxx -> 变动后:xxx;......"
 *
 * Created by kzyuan on 2019-09-05 11:28
 */
public class BeanUtil {
    private static UserBean userBeanNew;
    private static UserBean userBeanOld;


    public static void main(String[] args)  {
        // 创建User Bean对象，并设值
        setUserBean();
        //数据对比返回结果集
        Map<String,String> resultMap = dataComparison(userBeanNew,userBeanOld);
    }

    // 创建User Bean对象，并设值
    private static void setUserBean() {
        userBeanOld = new UserBean();
        userBeanOld.setId("1");
        userBeanOld.setName("壳叔");
        userBeanOld.setAge("35");
        userBeanOld.setEmail("keshu@bhusk.com");

        userBeanNew = new UserBean();
        userBeanNew.setId("2");
        userBeanNew.setName("kk 壳");
        userBeanNew.setAge("36");
        userBeanNew.setEmail("kk@bhusk.com");
    }

    // 遍历 Bean
    private static Map<String,DataLogger> traversalBean(Object obj) throws Exception {

        //创建Map容器
        Map<String,DataLogger> objMap = new HashMap<String, DataLogger> ();

        //开始记录容器值
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            DataLogger dataLogger = new DataLogger();
            String mod = Modifier.toString(field.getModifiers());
            System.out.println("mod = " + mod);
            dataLogger.setMod(mod);
            // 跳过静态属性
            if (mod.indexOf("static") != -1) {
                continue;
            }

            // 取得注解的设置的属性值
            SetFieldAttribute setField = field.getAnnotation(SetFieldAttribute.class);
            if (setField != null) {
                String fieldName = setField.fieldName();
                String fieldType = setField.fieldType();
                dataLogger.setFieldName(setField.fieldName());
                dataLogger.setFieldType(setField.fieldType());

                System.out.println("注解的属性 fieldName = " + fieldName);
                System.out.println("注解的属性 fieldType = " + fieldType);

                String getMethod = "get" + field.getName().substring(0, 1).toUpperCase()
                        + field.getName().substring(1);
                System.out.println("User Bean 属性的Get方法名 getMethod = " + getMethod);
                dataLogger.setMethod(getMethod);


                Class[] methodParam = null;
                Object[] params = {};
                Object retVal = obj.getClass().getMethod(getMethod, methodParam).invoke(obj, params);

                if (fieldType.equals("list")) {
//                    List<String> list = (List<String>) retVal;
//                    if(null == list) {continue;}
//                    int i = 1;
//                    for (String favourite : list ) {
//                        System.out.println("User Bean 属性的值 " + field.getName() + " : " + i + " = " + favourite);
//                        i++;
//                    }
                } else {
                    // 取得Bean属性的值
                    System.out.println("User Bean 属性的值 " + field.getName() + " = " + retVal);
//                    objMap.put(field.getName(), retVal);
                    dataLogger.setFieldValue(retVal);
                }
            }
            objMap.put(field.getName(), dataLogger);
        }

        return objMap;
    }

    private static void traversalFunction(Object obj) throws Exception {
        System.out.println("通过Bean的方法遍历");
        //创建Map容器
        Map<String,DataLogger> objMap = new HashMap<String, DataLogger> ();

        // 通过Bean的方法遍历
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            DataLogger dataLogger = new DataLogger();
            // 取得注解的设置的属性值
            SetFieldAttribute setField = method.getAnnotation(SetFieldAttribute.class);
            if (setField != null) {
                String fieldName = setField.fieldName();
                String fieldType = setField.fieldType();
                dataLogger.setFieldName(setField.fieldName());
                dataLogger.setFieldType(setField.fieldType());

                System.out.println("注解的属性 fieldName = " + fieldName);
                System.out.println("注解的属性 fieldType = " + fieldType);
                Class[] methodParam = null;
                Object[] params = {};
                Object retVal = null;
                if (method.getName().substring(0, 3).equals("get")) {
                    String mod = Modifier.toString(method.getModifiers());
                    System.out.println("mod = " + mod);
                    System.out.println("User Bean 属性的Get方法名 getMethod = " + method.getName());

                    dataLogger.setMod(mod);
                    dataLogger.setMethod(method.getName());

                    retVal = obj.getClass().getMethod(method.getName(), methodParam).invoke(obj, params);

                    if (fieldType.equals("list")) {
                        List<String> favouriteList = (List<String>) retVal;
                        List<String> list = (List<String>) retVal;
                        if(null == list) {continue;}
                        int i = 1;
                        for (String favourite : favouriteList) {
                            System.out.println("User Bean 属性的值 " + method.getName() + " : " + i + " = " + favourite);
                            i++;
                        }
                    } else {
                        // 取得Bean属性的值
                        System.out.println("User Bean 属性的值 " + method.getName() + " = " + retVal);
                        dataLogger.setFieldValue(retVal);
                    }
                }
                objMap.put(setField.fieldName(), dataLogger);
            }

        }

    }

    /**
     * 类数据对比方法 会返回一个map 会存储发生变动的日志
     * @param newObj 变动后的bean
     * @param oldObj 变动前的bean
     */
    private static Map<String,String> dataComparison(Object newObj,Object oldObj) {

        //结果容器
        Map<String,String> resultMap = new HashMap<String, String>();

        try {
            /**
             * 整理Bean分析数据
             */
            Map<String,DataLogger> newObjMap = BeanUtil.traversalBean(newObj);
            Map<String,DataLogger> oldObjMap = BeanUtil.traversalBean(oldObj);


            /**
             * 开始分析两组map数据
             */
            for (Map.Entry<String, DataLogger> entry : newObjMap.entrySet()) {

                /**
                 * 判断两组值数据
                 */
                if (entry.getValue().getFieldValue() != oldObjMap.get(entry.getKey()).getFieldValue()) {

                    /**
                     * 记录日志的结果
                     */
//                    resultMap.put(entry.getKey(), " field name: " + entry.getValue().getFieldName() +
//                            " , new field value: " + entry.getValue().getFieldValue() +
//                            " -> old field value: " + oldObjMap.get(entry.getKey()).getFieldValue());
                    resultMap.put(entry.getKey(), " 发生列名: " + entry.getValue().getFieldName() +
                            " , 变动后内容值: " + entry.getValue().getFieldValue() +
                            " -> 变动前内容值: " + oldObjMap.get(entry.getKey()).getFieldValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}


/**
 * 用于存储解析后的bean
 */
class DataLogger {
    private String mod;
    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段内容
     */
    private Object fieldValue;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 属性的Get方法名
     */
    private String method;

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
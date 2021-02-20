package cn.lxk.demotest;

import com.googlecode.aviator.AviatorEvaluator;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Liu Xukai
 * @Description
 * @Date 2021/01/28 14:40
 */
@Component
public class RegularCheckUtils {

    private static final Map<Integer,  Method> METHOD_MAP = new HashMap<>();

    static {
        try {
            Class cls = RegularCheckUtils.class;
            METHOD_MAP.put(1, cls.getDeclaredMethod("isEquals", String.class, String.class));
            METHOD_MAP.put(2, cls.getDeclaredMethod("isNotEquals", String.class, String.class));
            METHOD_MAP.put(3, cls.getDeclaredMethod("isGreaterOrEqual", String.class, String.class));
            METHOD_MAP.put(4, cls.getDeclaredMethod("isLessOrEqual", String.class, String.class));
            METHOD_MAP.put(5, cls.getDeclaredMethod("isGreater", String.class, String.class));
            METHOD_MAP.put(6, cls.getDeclaredMethod("isLess", String.class, String.class));
            METHOD_MAP.put(7, cls.getDeclaredMethod("isContains", String.class, String.class));
            METHOD_MAP.put(8, cls.getDeclaredMethod("isNotContains", String.class, String.class));
            METHOD_MAP.put(9, cls.getDeclaredMethod("isEmpty", String.class, String.class));
            METHOD_MAP.put(10, cls.getDeclaredMethod("isNotEmpty", String.class, String.class));
            METHOD_MAP.put(11, cls.getDeclaredMethod("isDigits", String.class, String.class));
        } catch (NoSuchMethodException e) {
            // TODO
            e.printStackTrace();
        }
    }

    /**
     *  1、==
     */
    private boolean isEquals(String param1, String param2){
        return  strEquals(param1,param2);
    }

    /**
     *  2、!=
     */
    private boolean isNotEquals(String param1, String param2){
        return  !strEquals(param1,param2);
    }

    /**
     *  3、>=
     */
    private boolean isGreaterOrEqual(String param1, String param2){
        if(compareNum(param1, param2) < 0){
            return false;
        }
        return true;
    }

    /**
     *  4、<=
     */
    private boolean isLessOrEqual(String param1, String param2){
        if(compareNum(param1, param2) <= 0){
            return true;
        }
        return false;
    }

    /**
     *  5、>
     */
    private boolean isGreater(String param1, String param2){
        if(compareNum(param1, param2) <= 0){
            return false;
        }
        return true;
    }

    /**
     *  6、<
     */
    private boolean isLess(String param1, String param2){
        if(compareNum(param1, param2) < 0){
            return true;
        }
        return false;
    }

    /**
     *  7、包含
     */
    private boolean isContains(String param1, String param2){
        return isStrContains(param1, param2);
    }

    /**
     *  8、不包含
     */
    private boolean isNotContains(String param1, String param2){
        return !isStrContains(param1, param2);
    }

    /**
     *  9、为空
     */
    private boolean isEmpty(String param1, String param2){
        return StringUtils.isEmpty(param1);
    }

    /**
     *  10、不为空
     */
    private boolean isNotEmpty(String param1, String param2){
        return !(StringUtils.isEmpty(param1));
    }

    /**
     *  11、位数为
     */
    private boolean isDigits(String param1, String param2){
        Map<String, Object> env = new HashMap<>();
        env.put("x", param1);
        env.put("y", param2);
        return Boolean.parseBoolean(AviatorEvaluator.execute("string.length(x) == long(y)" , env).toString());
    }


    private boolean strEquals(String param1,String param2){
        Map<String, Object> env = new HashMap<>();
        env.put("x", param1);
        env.put("y", param2);
        return  Boolean.parseBoolean(AviatorEvaluator.execute("x == y" , env).toString());
    }

    private int compareNum(String param1,String param2){
        Map<String, Object> env = new HashMap<>();
        env.put("x", param1);
        env.put("y", param2);

        if(param1.contains(".") || param2.contains(".")){
            return Integer.parseInt(AviatorEvaluator.execute("cmp(double(x),double(y))", env).toString());
        }
        return Integer.parseInt(AviatorEvaluator.execute("cmp(bigint(x),bigint(y))", env).toString());
    }

    private boolean isStrContains(String param1,String param2){
        Map<String, Object> env = new HashMap<>();
        env.put("x", param1);
        env.put("y", param2);
        return Boolean.parseBoolean(AviatorEvaluator.execute("string.contains(x,y)", env).toString());
    }



    // 按组校验 and 条件
    public boolean regularAndCheck(List<Map<String, String>> paramList, RegularCheckUtils regularCheckUtils)throws Exception{
        for (Map<String, String> map : paramList) {
            String param1 = map.get("param1");
            String param2 = map.get("param2");
            String type = map.get("type");
            if(StringUtils.isEmpty(type)){
                // TODO
                break;
            }
            Method m = METHOD_MAP.get(Integer.parseInt(type));
            if(ObjectUtils.isEmpty(m)){
                // TODO
                return false;
            }
            m.setAccessible(true);
            // 短路
            if(!Boolean.parseBoolean(m.invoke(regularCheckUtils, param1, param2).toString())){
                return false;
            }
        }
        return true;
    }

}

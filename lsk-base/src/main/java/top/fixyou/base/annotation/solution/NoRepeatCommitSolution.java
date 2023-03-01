package top.fixyou.base.annotation.solution;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.fixyou.base.annotation.NoRepeatCommit;
import top.fixyou.base.result.ResponseResult;
import top.fixyou.base.util.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author 26018
 * @description
 * @date 2023/3/1 11:09
 */
@Slf4j
@Aspect
@Component
public class NoRepeatCommitSolution {
    private static final HashMap<String, Long> KEYMAP = new HashMap<>();

    @Pointcut(value = "@annotation(top.fixyou.base.annotation.NoRepeatCommit)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object functionAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String ipAddress = IpUtil.getIpAddr(getRequest());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        NoRepeatCommit noRepeatCommit = signature.getMethod().getAnnotation(NoRepeatCommit.class);
        String originalKey = noRepeatCommit.key();
        // 计算键名
        String key = computeKeyName(originalKey, joinPoint) + "." + ipAddress;
        log.info("key:{}", key);
        long timeout = noRepeatCommit.timeout();
        long currentTimeMillis = System.currentTimeMillis();
        Long lastReqTime = KEYMAP.getOrDefault(key, currentTimeMillis - timeout);
        if (currentTimeMillis - lastReqTime < timeout) {
            long restTime = timeout - (currentTimeMillis - lastReqTime);
            return ResponseResult.fail("请勿重复请求,距离下一次请求时间还有：" + restTime / 1000 + "s");
        } else {
            KEYMAP.put(key, currentTimeMillis);
        }
        return joinPoint.proceed();
    }

    public HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return Objects.requireNonNull(servletRequestAttributes).getRequest();
    }

    private String computeKeyName(String originalKey, ProceedingJoinPoint joinPoint) {
        if (!(originalKey.contains("{") && originalKey.contains("}"))) {
            return originalKey;
        }
        // 解析参数列表
        List<String> parameters = findParameters(originalKey);
        // 解析方法参数名及参数
        Map<String, Object> methodArgsMap = methodArgs(joinPoint);
        return generateKey(originalKey, parameters, methodArgsMap);
    }

    private List<String> findParameters(String string) {
        ArrayList<String> parameterList = new ArrayList<>();
        ArrayDeque<Character> characterStack = new ArrayDeque<>();
        StringBuilder builder = new StringBuilder();
        char[] chars = string.toCharArray();
        for (char aChar : chars) {
            if ('}' == aChar) {
                while (!characterStack.isEmpty() && characterStack.peek() != '{') {
                    Character pop = characterStack.pop();
                    builder.append(pop);
                }
                if (!characterStack.isEmpty() && characterStack.peek() == '{') {
                    characterStack.pop();
                }
                String parameter = builder.reverse().toString();
                if (parameter.length() > 0) {
                    parameterList.add(parameter);
                }
                builder.delete(0, builder.length());
            }
            characterStack.push(aChar);
        }
        return parameterList;
    }

    private Map<String, Object> methodArgs(ProceedingJoinPoint joinPoint) {
        HashMap<String, Object> argsMap = new HashMap<>(4);
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            String key = parameters[i].getName();
            Object value = args[i];
            argsMap.put(key, value);
        }
        return argsMap;
    }

    private String generateKey(String originalKey, List<String> parameters, Map<String, Object> argsMap) {
        if (CollectionUtils.isEmpty(parameters)) {
            return originalKey;
        }
        for (String parameter : parameters) {
            // 针对每一个参数进行取值
            String property = parseProperty(parameter, argsMap);
            originalKey = originalKey.replace(parameter, property);
        }
        originalKey = originalKey.replaceAll("\\{", "");
        originalKey = originalKey.replaceAll("}", "");
        return originalKey;
    }

    private String parseProperty(String parameter, Map<String, Object> map) {
        String[] split = parameter.split("\\.");
        Object object = map.get(split[0]);
        if (split.length == 1) {
            return String.valueOf(object);
        }
        try {
            for (int i = 1, splitLength = split.length; i < splitLength; i++) {
                String property = split[i];
                if (Objects.nonNull(object)) {
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(object));
                    object = jsonObject.get(property);
                }
            }
        } catch (Exception exception) {
            log.error("NoRepeatCommit-参数对象解析异常:{}", exception.getMessage());
        }
        return object instanceof String ? String.valueOf(object) : "";
    }
}

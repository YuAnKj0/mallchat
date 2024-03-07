package com.yuankj.mallchat.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Spliterator;

/**
 * @author Ykj
 * @date 2024-02-21/10:29
 * @apiNote  spring el表达式解析
 */

public class SpElUtils {
	private static final ExpressionParser PARSER=new SpelExpressionParser();
	private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER=new DefaultParameterNameDiscoverer();
	
	
	public static String parseSpEl(Method method, Object[] args, String spEL) {
		String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[]{});//解析参数名
		EvaluationContext context = new StandardEvaluationContext();
		for (int i=0;i<params.length;i++){
			context.setVariable(params[i], args[i]);
		}
		Expression expression = PARSER.parseExpression(spEL);
		return expression.getValue(context, String.class);
		
		
	}
	
	public static String getMethodKey(Method method) {
		return method.getDeclaringClass()+"#"+method.getName();
	}
}

package org.varnalab.organic.api.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Chem {
	public String value() default "[type]";
	public Class<?> type() default Object.class;
	public boolean once() default false;
}

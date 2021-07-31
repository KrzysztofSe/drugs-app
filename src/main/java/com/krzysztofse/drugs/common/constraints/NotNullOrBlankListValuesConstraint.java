package com.krzysztofse.drugs.common.constraints;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import static java.util.Objects.isNull;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullOrBlankListValuesConstraint.NotNullOrBlankListValuesConstraintValidator.class)
@Documented
public @interface NotNullOrBlankListValuesConstraint {

    String message() default "Given list contains invalid values (null or blank)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    final class NotNullOrBlankListValuesConstraintValidator
            implements ConstraintValidator<NotNullOrBlankListValuesConstraint, Collection<String>> {
        @Override
        public boolean isValid(final Collection<String> value, final ConstraintValidatorContext context) {
            return value == null || value.stream().allMatch(v -> !isNull(v) && !v.isBlank());
        }
    }
}

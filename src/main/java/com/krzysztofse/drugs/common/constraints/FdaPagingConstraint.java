package com.krzysztofse.drugs.common.constraints;

import com.krzysztofse.drugs.common.request.Paging;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FdaPagingConstraint.FdaPagingConstraintValidator.class)
@Documented
public @interface FdaPagingConstraint {

    int MAX_HITS = 26000; // https://open.fda.gov/apis/paging

    String message() default "Given paging combination exceeds the maximum of " + MAX_HITS;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    final class FdaPagingConstraintValidator implements ConstraintValidator<FdaPagingConstraint, Paging> {
        @Override
        public boolean isValid(final Paging value, final ConstraintValidatorContext context) {
            return value == null || (value.getSkip() + value.getPageSize()) <= MAX_HITS;
        }
    }
}

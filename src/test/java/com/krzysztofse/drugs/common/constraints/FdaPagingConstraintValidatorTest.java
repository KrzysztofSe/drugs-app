package com.krzysztofse.drugs.common.constraints;

import com.krzysztofse.drugs.common.constraints.FdaPagingConstraint.FdaPagingConstraintValidator;
import com.krzysztofse.drugs.common.request.Paging;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FdaPagingConstraintValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private final FdaPagingConstraintValidator cut = new FdaPagingConstraintValidator();

    @Test
    public void shouldAllowForNullPaging() {
        assertThat(cut.isValid(null, context)).isTrue();
    }

    @Test
    public void shouldFailIfPagingTooFar() {
        final Paging paging = new Paging(260, 100);
        assertThat(cut.isValid(paging, context)).isFalse();
    }

    @Test
    public void shouldPassIfPagingWithinLimits() {
        final Paging paging = new Paging(259, 100);
        assertThat(cut.isValid(paging, context)).isTrue();
    }

}

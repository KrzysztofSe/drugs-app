package com.krzysztofse.drugs.common.constraints;

import com.krzysztofse.drugs.common.constraints.NotNullOrBlankListValuesConstraint.NotNullOrBlankListValuesConstraintValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class NotNullOrBlankListValuesConstraintValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private final NotNullOrBlankListValuesConstraintValidator cut = new NotNullOrBlankListValuesConstraintValidator();

    @Test
    public void shouldAllowForNullList() {
        assertThat(cut.isValid(null, context)).isTrue();
    }

    @Test
    public void shouldAllowForEmptyList() {
        assertThat(cut.isValid(emptyList(), context)).isTrue();
    }

    @Test
    public void shouldPassWhenListContainsNoNullsOrBlanks() {
        assertThat(cut.isValid(List.of("one", "two"), context)).isTrue();
    }

    @Test
    public void shouldFailWhenListContainsNulls() {
        assertThat(cut.isValid(Lists.newArrayList("one", "two", null), context)).isFalse();
    }

    @Test
    public void shouldFailWhenListContainsBlanks() {
        assertThat(cut.isValid(List.of("one", "two", " "), context)).isFalse();
    }
}

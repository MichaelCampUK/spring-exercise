package com.michaelcamp.risknarrative.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class OfficerTest {
    private Officer officer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        officer = null;
    }

    @Test
    public void checkActiveTrueIfEmptyResigned() {
        officer = Officer.builder().build();
        assertThat(officer.checkActive()).isTrue();
    }

    @Test
    public void checkActiveTrueIfNullResigned() {
        officer = Officer.builder().resigned_on(null).build();
        assertThat(officer.checkActive()).isTrue();
    }

    @Test
    public void checkActiveFalseIfResigned() {
        officer = Officer.builder().resigned_on("someDate").build();
        assertThat(officer.checkActive()).isFalse();
    }
}

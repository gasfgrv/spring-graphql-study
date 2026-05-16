package com.gasfgrv.graphql.demo_graphql.infrastructure.graphql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.BooleanValue;
import graphql.language.StringValue;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

@ExtendWith(MockitoExtension.class)
public class LocalDateTimeCoercingTest {

    private LocalDateTimeCoercing coercing;

    @BeforeEach
    void setUp() {
        coercing = new LocalDateTimeCoercing();
    }

    @Test
    void testSerialize() {
        LocalDateTime ldt = LocalDateTime.of(2024, 5, 16, 14, 30);
        GraphQLContext context = GraphQLContext.getDefault();
        
        String serialized = coercing.serialize(ldt, context, Locale.getDefault());
        
        assertThat(serialized).isEqualTo("2024-05-16T14:30:00");
    }

    @Test
    void testSerializeThrowsCoercingSerializeException() {
        long epochMilli = Instant.now().toEpochMilli();
        GraphQLContext context = GraphQLContext.getDefault();

        assertThatThrownBy(() -> coercing.serialize(epochMilli, context, Locale.getDefault()))
                .isInstanceOf(CoercingSerializeException.class)
                .hasMessageContaining("It was not possible to serialize");
    }

    @Test
    void testParseValue() {
        String value = "2024-05-16T14:30:00";
        GraphQLContext context = GraphQLContext.getDefault();
        
        LocalDateTime parsed = coercing.parseValue(value, context, Locale.getDefault());
        
        assertThat(parsed).isEqualTo(LocalDateTime.of(2024, 5, 16, 14, 30));
    }

    @Test
    void testParseValueThrowsCoercingParseValueException() {
        long epochMilli = Instant.now().toEpochMilli();
        GraphQLContext context = GraphQLContext.getDefault();

        assertThatThrownBy(() -> coercing.parseValue(epochMilli, context, Locale.getDefault()))
                .isInstanceOf(CoercingParseValueException.class)
                .hasMessageContaining("Invalid string format for LocalDateTime");
    }

    @Test
    void testParseLieteral() {
        StringValue stringValue = new StringValue("2024-05-16T14:30:00");
        GraphQLContext context = GraphQLContext.getDefault();
        CoercedVariables emptyVariables = CoercedVariables.emptyVariables();
        
        LocalDateTime parsed = coercing.parseLiteral(stringValue, emptyVariables, context, Locale.getDefault());
        
        assertThat(parsed).isEqualTo(LocalDateTime.of(2024, 5, 16, 14, 30));
    }

    @Test
    void testParseLiteralThrowsCoercingParseLiteralException() {
        BooleanValue booleanValue = new BooleanValue(false);
        GraphQLContext context = GraphQLContext.getDefault();
        CoercedVariables emptyVariables = CoercedVariables.emptyVariables();

        assertThatThrownBy(() -> coercing.parseLiteral(booleanValue, emptyVariables, context, Locale.getDefault()))
                .isInstanceOf(CoercingParseLiteralException.class)
                .hasMessageContaining("Expected: String literal");
    }

}

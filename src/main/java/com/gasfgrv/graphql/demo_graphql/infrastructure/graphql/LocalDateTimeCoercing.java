package com.gasfgrv.graphql.demo_graphql.infrastructure.graphql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

public class LocalDateTimeCoercing implements Coercing<LocalDateTime, String> {

    @Override
    public @Nullable String serialize(@NonNull Object dataFetcherResult, @NonNull GraphQLContext graphQLContext,
            @NonNull Locale locale) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDateTime ldt) {
            return ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        throw new CoercingSerializeException("It was not possible to serialize: " + dataFetcherResult);
    }

    @Override
    public @Nullable LocalDateTime parseValue(@NonNull Object input, @NonNull GraphQLContext graphQLContext,
            @NonNull Locale locale) throws CoercingParseValueException {
        if (input instanceof String s) {
            return LocalDateTime.parse(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        throw new CoercingParseValueException("Invalid string format for LocalDateTime");
    }

    @Override
    public @Nullable LocalDateTime parseLiteral(@NonNull Value<?> input, @NonNull CoercedVariables variables,
            @NonNull GraphQLContext graphQLContext, @NonNull Locale locale) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
            return LocalDateTime.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        throw new CoercingParseLiteralException("Expected: String literal");
    }

}

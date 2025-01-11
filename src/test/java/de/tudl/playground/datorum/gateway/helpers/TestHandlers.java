package de.tudl.playground.datorum.gateway.helpers;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.gateway.query.annotation.HandlerPriority;
import de.tudl.playground.datorum.gateway.query.exception.ErrorHandlingQueryException;
import lombok.SneakyThrows;

import java.util.Optional;
import java.util.UUID;

public class TestHandlers
{
    @HandlerPriority(2)
    public static class SampleQueryHandler implements QueryHandler<TestQueries.SampleQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.SampleQuery query) {
            return "Handled by SampleQueryHandler".describeConstable();
        }
    }

    @HandlerPriority(1)
    public static class AnotherQueryHandler implements QueryHandler<TestQueries.SampleQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.SampleQuery query) {
            return "Handled by AnotherQueryHandler".describeConstable();
        }
    }

    public static class ParentQueryHandler implements QueryHandler<TestQueries.ParentQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.ParentQuery query) {
            return "Handled by ParentQueryHandler".describeConstable();
        }
    }

    public static class ChildQueryHandler implements QueryHandler<TestQueries.ChildQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.ChildQuery query)
        {
            return "Handled by ChildQueryHandler".describeConstable();
        }
    }

    public static class GenericQueryHandler implements QueryHandler<Object, String>
    {
        @Override
        public Optional<String> handle(Object query) {
            return "Handled by GenericQueryHandler".describeConstable();
        }

        @Override
        public String toString() {
            return "GenericQueryHandler_" + UUID.randomUUID();
        }
    }

    public static class ThrowingQueryHandler implements QueryHandler<Object, String>
    {

        @SneakyThrows
        @Override
        public Optional<String> handle(Object query) {

            throw new ErrorHandlingQueryException("ThrowingQueryHandler error!");
        }
    }

    public static class InvalidQueryHandler implements QueryHandler<String, Integer> {

        @Override
        public Optional<Integer> handle(String query) {
            // Invalid type: String as a query doesn't match any known query type
            return Optional.of(42);
        }
    }

    public static class ValidQueryHandler implements QueryHandler<TestQueries.SampleQuery, String> {

        @Override
        public Optional<String> handle(TestQueries.SampleQuery query) {
            return Optional.of("Handled by ValidQueryHandler");
        }
    }

    public static class NullQueryHandler implements QueryHandler<TestQueries.GenericQuery, String>
    {
        @Override
        public Optional<String> handle(TestQueries.GenericQuery query) {
            return null;
        }
    }

}

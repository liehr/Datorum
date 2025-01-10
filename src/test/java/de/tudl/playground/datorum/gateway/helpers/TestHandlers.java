package de.tudl.playground.datorum.gateway.helpers;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.gateway.query.annotation.HandlerPriority;

import java.util.Optional;

public class TestHandlers
{
    @HandlerPriority(2)
    public static class SampleQueryHandler implements QueryHandler<TestQueries.SampleQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.SampleQuery query) {
            return "Handled SampleQuery".describeConstable();
        }
    }

    @HandlerPriority(1)
    public static class AnotherQueryHandler implements QueryHandler<TestQueries.SampleQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.SampleQuery query) {
            return "Handled AnotherQuery".describeConstable();
        }
    }

    public static class ParentQueryHandler implements QueryHandler<TestQueries.ParentQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.ParentQuery query) {
            return "Handled ParentQuery".describeConstable();
        }
    }

    public static class ChildQueryHandler implements QueryHandler<TestQueries.ChildQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.ChildQuery query)
        {
            return "Handled ChildQuery".describeConstable();
        }
    }

}

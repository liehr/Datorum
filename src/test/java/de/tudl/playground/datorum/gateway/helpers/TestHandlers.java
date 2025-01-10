package de.tudl.playground.datorum.gateway.helpers;

import de.tudl.playground.datorum.gateway.query.QueryHandler;

import java.util.Optional;

public class TestHandlers
{
    public static class SampleQueryHandler implements QueryHandler<TestQueries.SampleQuery, String> {
        @Override
        public Optional<String> handle(TestQueries.SampleQuery query) {
            return "Handled SampleQuery".describeConstable();
        }
    }

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

}

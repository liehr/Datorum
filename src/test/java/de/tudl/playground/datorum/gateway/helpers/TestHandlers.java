package de.tudl.playground.datorum.gateway.helpers;

import de.tudl.playground.datorum.gateway.query.QueryHandler;

public class TestHandlers
{
    public static class SampleQueryHandler implements QueryHandler<TestQueries.SampleQuery, String> {
        @Override
        public String handle(TestQueries.SampleQuery query) {
            return "Handled SampleQuery";
        }
    }

    public static class ParentQueryHandler implements QueryHandler<TestQueries.ParentQuery, String> {
        @Override
        public String handle(TestQueries.ParentQuery query) {
            return "Handled ParentQuery";
        }
    }

}

package com.slimgears.rxrepo.sql;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface SqlStatement {
    SqlStatement empty = create("");

    String statement();
    Object[] args();

    default SqlStatement mapArgs(Function<Object, Object> argMapper) {
        return args().length > 0
                ? create(statement(), Arrays.stream(args()).map(argMapper).toArray(Object[]::new))
                : this;
    }

    default SqlStatement withArgs(Object... args) {
        return create(statement(), args);
    }

    static SqlStatement of(String... clauses) {
        return create(Arrays
                .stream(clauses).filter(c -> !c.isEmpty())
                .collect(Collectors.joining(" ")));
    }

    static SqlStatement empty() {
        return empty;
    }

    static SqlStatement create(String statement, Object... args) {
        return new SqlStatement() {
            @Override
            public String statement() {
                return statement;
            }

            @Override
            public Object[] args() {
                return args;
            }

            @Override
            public int hashCode() {
                return Objects.hash(statement, Objects.hash(args));
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof SqlStatement &&
                    Objects.equals(statement, ((SqlStatement) obj).statement()) &&
                    Arrays.equals(args, ((SqlStatement) obj).args());
            }
        };
    }
}

package com.slimgears.rxrepo.expressions.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.rxrepo.expressions.CollectionExpression;
import com.slimgears.rxrepo.expressions.ComposedExpression;
import com.slimgears.rxrepo.expressions.ObjectExpression;

import java.util.Collection;

@AutoValue
public abstract class CollectionComposedExpression<S, T, R, C extends Collection<R>>
    extends AbstractComposedExpression<S, T, C>
    implements CollectionExpression<S, R, C> {
    @JsonCreator
    public static <S, T, R, C extends Collection<R>> CollectionComposedExpression<S, T, R, C> create(
            @JsonProperty("type") Type type,
            @JsonProperty("source") ObjectExpression<S, T> source,
            @JsonProperty("expression") ObjectExpression<T, C> expression) {
        return new AutoValue_CollectionComposedExpression<>(type, source, expression);
    }

    @Override
    protected ObjectExpression<S, C> createConverted(ObjectExpression<S, T> newSource, ObjectExpression<T, C> newExpression) {
        return create(type(), newSource, newExpression);
    }
}

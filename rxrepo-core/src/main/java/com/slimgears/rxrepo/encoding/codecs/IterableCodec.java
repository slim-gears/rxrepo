package com.slimgears.rxrepo.encoding.codecs;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.slimgears.rxrepo.encoding.*;
import com.slimgears.rxrepo.expressions.internal.MoreTypeTokens;

import java.util.Optional;

public class IterableCodec<T> implements MetaCodec<Iterable<T>> {
    private final TypeToken<T> elementType;

    private IterableCodec(TypeToken<T> elementType) {
        this.elementType = elementType;
    }

    @Override
    public void encode(MetaContext.Writer context, Iterable<T> iterable) {
        MetaWriter writer = context.writer();
        MetaCodec<T> elementCodec = context.codecProvider().resolve(elementType);
        writer.writeBeginArray();
        iterable.forEach(val -> elementCodec.encode(context, val));
        writer.writeEndArray();
    }

    @Override
    public Iterable<T> decode(MetaContext.Reader context) {
        MetaReader reader = context.reader();
        MetaCodec<T> elementCodec = context.codecProvider().resolve(elementType);
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        reader.readBeginArray();
        while (!reader.isAt(MetaElementType.EndArray)) {
            builder.add(elementCodec.decode(context));
        }
        reader.readEndArray();
        return builder.build();
    }

    public static class Provider implements MetaCodecProvider {
        @SuppressWarnings("unchecked")
        @Override
        public <T> MetaCodec<T> tryResolve(TypeToken<T> type) {
            return type.isSubtypeOf(Iterable.class)
                    ? (MetaCodec<T>) Optional
                    .of((TypeToken<?>)MoreTypeTokens.elementType((TypeToken) type))
                    .filter(MoreTypeTokens::hasNoTypeVars)
                    .map(IterableCodec::new)
                    .orElse(null)
                    : null;
        }
    }
}

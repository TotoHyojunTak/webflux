package com.webflux.webflux.util;

import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

public interface HttpTraceWrapperRepository extends //
        Repository<HttpTraceWrapper, String> {

    Stream<HttpTraceWrapper> findAll(); // <1>

    void save(HttpTraceWrapper trace); // <2>
}

package io.hiusnef.sampleservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Minimal configuration so @WebMvcTest can run inside this module without the bootstrap module. In the root package so
 * the shared GlobalExceptionHandler (shared.web) is picked up too.
 */
@SpringBootApplication
class WebTestApplication {}

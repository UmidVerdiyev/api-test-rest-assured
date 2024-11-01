package interceptors;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestLoggingInterceptor implements Filter {
    private static final Logger logger = LogManager.getLogger(RequestLoggingInterceptor.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext context) {
        logger.info("---- API Request ----");
        logger.info("Method: {}", requestSpec.getMethod());
        logger.info("URI: {}", requestSpec.getURI());
        logger.info("Headers: {}", requestSpec.getHeaders());

        if (requestSpec.getBody() != null) {
            logger.info("Body: {}", requestSpec.getBody().toString());
        }

        return context.next(requestSpec, responseSpec);
    }
}

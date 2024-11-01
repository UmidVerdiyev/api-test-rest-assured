package interceptors;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResponseLoggingInterceptor implements Filter {
    private static final Logger logger = LogManager.getLogger(ResponseLoggingInterceptor.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext context) {
        Response response = context.next(requestSpec, responseSpec);

        logger.info("---- API Response ----");
        logger.info("Status Code: {}", response.getStatusCode());
        logger.info("Headers: {}", response.getHeaders());

        if (response.getBody() != null) {
            logger.info("Body: {}", response.getBody().asPrettyString());
        }

        return response;
    }
}

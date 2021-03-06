package com.serverless.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;
import com.serverless.model.ApiGatewayResponse;
import com.serverless.util.Constants;
import com.serverless.util.DynamoDBHelper;

import com.serverless.util.Util;
import org.apache.log4j.Logger;
import java.util.HashMap;

public class GetPostcodeHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = Logger.getLogger(GetPostcodeHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        String name = (String) input.get("value");
        LOG.info("name: " + name);
        Util.checkParameter(name,Constants.SUBURB);
        	
        int statusCode = 0;
        Object responseBody = null;  
		Map<String, String> headers = createHeader();
       
        try{
            //Search record from DynamoDB
            responseBody = DynamoDBHelper.getRecordforSuburb(name);
            statusCode = Constants.STATUS_CODE_SUCCESS;
        }catch(Exception e){
            LOG.error("Exception in getting record from DynamoDB "+e);
            responseBody = Constants.RESPONSE_ERROR;
            statusCode = Constants.STATUS_CODE_ERROR;
        }

        return ApiGatewayResponse.builder()
        .setStatusCode(statusCode)
        .setObjectBody(responseBody)
        .setHeaders(headers)
        .build();
    }

    private static Map<String, String> createHeader(){
		Map<String, String> headers = new HashMap<>();
		headers.put("X-Powered-By", "AWS Lambda & Serverless");
		headers.put("Content-Type", "application/json");
		return headers;
	}

}

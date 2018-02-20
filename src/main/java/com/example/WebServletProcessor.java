/*
 * Copyright 2012-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.example;

import com.amazonaws.services.sns.message.SnsMessage;
import com.amazonaws.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
//import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.util.Enumeration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import com.amazonaws.services.lambda.runtime.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import org.apache.log4j.*;
//import org.slf4j.LoggerFactory;
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.LambdaLogger;

/**
 * Displays up to 10 of the most recently received messages. Bound to 'GET /'.
 */
public class WebServletProcessor implements HttpServletProcessor {

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    //private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void process(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
        List<SnsMessage> recentMessages = SnsServletProcessor.getRecentMessages();
        /*String content = "Clarke Roche " +
        "clarkeroche@gmail.com";
        File file =new File("C://log.txt");
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);*/
        if (recentMessages.isEmpty()) {
          PrintWriter out = httpResponse.getWriter();
          out.printf("OK");
          //Logger LOGGER = LoggerFactory.getLogger(WebServletProcessor.class);
          //LOGGER.info("My message...");
          /*String req = httpRequest.getRequestURI();
          logger.debug("Requested Resource::"+req);*/

          /*Enumeration<String> enumeration = req.getParameterNames();

            while(enumeration.hasMoreElements()) {
                String parametername = enumeration.nextElement();
                logger.debug(parametername + " : " +req.getParameter(parametername));
            }
          //answer(httpResponse);
    	    //bw.write(content);
    	    //Closing BufferedWriter Stream
    	    //bw.close();


            /*httpResponse.getWriter().append("OK");
            httpResponse.getWriter().println("Clarke Roche");
            httpResponse.getWriter().println("clarkeroche@gmail.com");*/
        } else {
            recentMessages.forEach(m -> displayMessage(httpResponse, m));
        }
    }

    static final Logger logger = LogManager.getLogger(WebServletProcessor.class);

    public String myHandler(String name, Context context) {
        // System.out: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.out.println("log data from stdout \n this is continuation of system.out");

       // System.err: One log statement but with a line break (AWS Lambda writes two events to CloudWatch).
        System.err.println("log data from stderr. \n this is a continuation of system.err");

        // Use log4j to log the same thing as above and AWS Lambda will log only one event in CloudWatch.
        logger.debug("log data from log4j debug \n this is continuation of log4j debug");

        logger.error("log data from log4j err. \n this is a continuation of log4j.err");

        // Return will include the log stream name so you can look
        // up the log later.
        return String.format("Hello %s. log stream = %s", name, context.getLogStreamName());
    }

    private void displayMessage(HttpServletResponse httpServletResponse, SnsMessage m) {
        try {
            httpServletResponse.getWriter()
                               .append(m.getClass().getSimpleName())
                               .append(" received on ")
                               .append(DateUtils.formatISO8601Date(m.getTimestamp()))
                               .append(":\n")
                               .append(mapper.writeValueAsString(m))
                               .append("\n************************************************************************************************************************\n");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

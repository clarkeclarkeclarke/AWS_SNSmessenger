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
import org.apache.log4j.Logger;
import java.util.Enumeration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Displays up to 10 of the most recently received messages. Bound to 'GET /'.
 */
public class WebServletProcessor implements HttpServletProcessor {

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private Logger logger = Logger.getLogger(this.getClass());

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
          String req = httpRequest.getRequestURI();
          logger.debug("Requested Resource::"+req);

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

    public void answer(HttpServletResponse httpResponse) throws IOException {
      PrintWriter ans = httpResponse.getWriter();
      ans.printf("Clarke Roche");
      ans.printf("clarkeroche@gmail.com");
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

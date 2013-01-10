/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@DependsOn("dspaceServices")
public class IndexController {
    @Autowired
    MailSender mailSender;

    @RequestMapping(value = "/")
    public String indexAction() {

        return "index/index";
    }

    @RequestMapping(value = "/feedback")
    public String feedbackAction(HttpServletRequest req,
            HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("mail");
        String body = req.getParameter("message");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo("dspace@lyncode.com");
        message.setSubject("SpringUI Feedback : " + name);
        message.setText(body);
        mailSender.send(message);

        PrintWriter p = resp.getWriter();
        p.write("OK");
        p.flush();

        return null;
    }
}

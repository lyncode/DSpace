/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.controllers;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.Utils;
import org.dspace.services.ContextService;
import org.dspace.springui.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FileController {
    private static Logger log = LogManager.getLogger(FileController.class);

    @RequestMapping(value = "/logo/{id}", method = RequestMethod.GET)
    public String retrieveLogoAction(@PathVariable("id") String fileID,
            ModelMap model, HttpServletRequest request,
            HttpServletResponse response) {
        Context ctx = RequestUtils.getDSpaceContext(request);
        try {
            boolean isLogo = true;
            
            Bitstream bitstream = Bitstream.find(ctx, Integer.parseInt(fileID));

            Bundle bundle = bitstream.getBundles().length > 0 ? bitstream
                    .getBundles()[0] : null;

            if (bundle != null
                    && bundle.getName().equals(Constants.LICENSE_BUNDLE_NAME)
                    && bitstream.getName().equals(
                            Constants.LICENSE_BITSTREAM_NAME)) {
                isLogo = false;
            }

            if (isLogo) {

                // Pipe the bits
                InputStream is = bitstream.retrieve();

                // Set the response MIME type
                response.setContentType(bitstream.getFormat().getMIMEType());

                // Response length
                response.setHeader("Content-Length",
                        String.valueOf(bitstream.getSize()));

                Utils.bufferedCopy(is, response.getOutputStream());
                is.close();
                response.getOutputStream().flush();

                return null;
            } else {
                model.addAttribute("title", "error.title.filedownload.2");
                model.addAttribute("message", "error.message.filedownload.2");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            model.addAttribute("title", "error.title.filedownload.1");
            model.addAttribute("message", "error.message.filedownload.1");
        }
        return "error/error";
    }
}

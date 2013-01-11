/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.core.Constants;
import org.dspace.core.Utils;
import org.dspace.orm.dao.api.IBitstreamDao;
import org.dspace.orm.entity.Bitstream;
import org.dspace.orm.entity.Bundle;
import org.dspace.services.ContextService;
import org.dspace.services.auth.AuthorizationException;
import org.dspace.services.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@DependsOn("dspaceServices")
public class FileController {
    private static Logger log = LogManager.getLogger(FileController.class);

    @Autowired IBitstreamDao bitstreamDao;
    @Autowired ContextService contextService;
    
    @RequestMapping(value = "/logo/{id}", method = RequestMethod.GET)
    public String retrieveLogoAction(@PathVariable("id") String fileID,
            ModelMap model, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            boolean isLogo = true;
            
            Bitstream bitstream = bitstreamDao.selectById(Integer.parseInt(fileID));
            List<Bundle> bundles = bitstream.getBundles();
            Bundle bundle = bundles.isEmpty() ? null : bundles.get(0);
            
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
                response.setContentType(bitstream.getFormat().getMimetype());

                // Response length
                response.setHeader("Content-Length",
                        String.valueOf(bitstream.getSize()));

                Utils.bufferedCopy(is, response.getOutputStream());
                is.close();
                response.getOutputStream().flush();

                // Return null to disable the view resolution mechanism
                return null;
            } else {
                model.addAttribute("title", "Logo doesn't exist");
                model.addAttribute("message", "The logo you are looking for doesn't exist");
            }
        } catch (AuthorizationException e) {
            log.info(e.getMessage(), e);
            model.addAttribute("title", "Authorization denied");
            model.addAttribute("message", "You don't have access to this resource");
        } catch (StorageException e) {
        	log.error(e.getMessage(), e);
            model.addAttribute("title", "Logo not available");
            model.addAttribute("message", "The logo you are looking for isn't available");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
            model.addAttribute("title", "Something went wrong");
            model.addAttribute("message", "An error occurred, please, try again.");
		}
        return "error/error";
    }
}

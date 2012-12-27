/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.controllers;

import org.dspace.core.Constants;
import org.dspace.orm.dao.api.ICollectionDao;
import org.dspace.orm.dao.api.ICommunityDao;
import org.dspace.orm.dao.api.IHandleDao;
import org.dspace.orm.entity.Collection;
import org.dspace.orm.entity.Community;
import org.dspace.orm.entity.Handle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HandleController {

    @Autowired
    IHandleDao handleDao;
    @Autowired
    ICommunityDao communityDao;
    @Autowired
    ICollectionDao collectionDao;

    @RequestMapping(value = "/handle/{prefix}/{id}", method = RequestMethod.GET)
    public String handleAction(@PathVariable("prefix") String prefix,
            @PathVariable("id") String id, ModelMap model) {

        Handle hd = handleDao.selectByHandle(prefix + "/" + id);
        switch (hd.getResourceType()) {
            case Constants.COMMUNITY:
                Community com = communityDao.selectById(hd.getResourceId());
                model.addAttribute("community", com);
                return "community/show";
            case Constants.COLLECTION:
                Collection col = collectionDao.selectById(hd.getResourceId());
                model.addAttribute("collection", col);
                return "collection/show";
        }

        return null;
    }
}

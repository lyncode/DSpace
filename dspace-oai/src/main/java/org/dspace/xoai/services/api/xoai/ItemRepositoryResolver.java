package org.dspace.xoai.services.api.xoai;

import com.lyncode.xoai.dataprovider.services.api.ItemRepository;
import org.dspace.xoai.services.api.context.ContextServiceException;

public interface ItemRepositoryResolver {
    ItemRepository getItemRepository () throws ContextServiceException;
}
